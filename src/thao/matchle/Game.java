package thao.matchle;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import thao.matchle.GuessResult.MatchType;

public class Game {
    private Corpus corpus;
    private NGram key;
    private int maxAttempts;
    private List<GuessResult> history;
    private boolean isOver;
    public static void main(String[] args) {
        Game game = new Game("wordlist.txt", 5, 6);
        game.play();
    }

    private Game (String corpusFilePath, int wordSize, int maxAttempts) {
        assert corpusFilePath != null;
        assert wordSize > 0;
        assert maxAttempts > 0;

        loadCorpus(corpusFilePath, wordSize);
        keyGen();
        this.maxAttempts = maxAttempts;
        this.history = new ArrayList<GuessResult>();
        this.isOver = false;
    }

    private void loadCorpus(String corpusFilePath, int wordSize) {
        assert corpusFilePath != null;
        assert wordSize > 0;

        try {
            List<String> words = Files.lines(Paths.get(corpusFilePath))
                                      .map(String::trim)
                                      .filter(word -> word.length() == wordSize)
                                      .collect(Collectors.toList());

            Corpus.Builder builder = Corpus.Builder.EMPTY;
            words.forEach(word -> builder.add(NGram.from(word)));

            corpus = builder.build();
        } catch (IOException e) {
            throw new RuntimeException("Error loading corpus from file: " + corpusFilePath, e);
        }
    }
    private void keyGen() {
        assert corpus != null;
        // Key is picked arbitrarily
        Random random = new Random();
        List<NGram> ngrams = new ArrayList<>(corpus.getCorpus());
        key = ngrams.get(random.nextInt(ngrams.size()));
    }

    private void play() {
        assert key != null;

        System.out.println("Guess a " + corpus.wordSize() + "-letter word.");
        Scanner scanner = new Scanner(System.in);
        while (!isOver()) {
            System.out.println("Attempts left: " + (maxAttempts - history.size()));
            System.out.print("Enter your guess: ");
            String guessInput = scanner.nextLine().trim();

            boolean successs = Barricade.makeValidGuess(history, key, guessInput, corpus, corpus.wordSize());

            if (!successs) {
                continue;
            }

            if (isWin()) {
                System.out.println("Congratulations! You found the key: " + key);
                break;
            }
            else {
                System.out.println("Incorrect guess. Match result:");
                getFeedback();
            }
            // System.out.println("Best worst case guess based on history: " + Barricade.bestWorstCaseGuess(history).toString());
            // System.out.println("Best average case guess based on history: " + Barricade.bestAverageCaseGuess(history).toString());
        }
        scanner.close();
        System.out.println("Key: " + key.toString());
    }
    private void makeGuess(NGram guess) {
        assert guess != null;
        assert key != null;

        NGramMatcher matcher = NGramMatcher.of(key, guess);
        GuessResult result = matcher.match();
        history.add(result);
    }

    private boolean isOver() {
        return history.size() >= maxAttempts;
    }

    private boolean isWin() {
        return history.get(history.size() - 1).isMatch();
    }

    public List<GuessResult> getHistory() {
        return history;
    }

    public void getFeedback() {
        assert history != null;
        assert !history.isEmpty();
        // Display the last guess result
        GuessResult lastGuess = history.get(history.size() - 1);
        for (Map.Entry<IndexedCharacter, MatchType> entry : lastGuess.getResultMap().entrySet()) {
            Character character = entry.getKey().character();
            MatchType matchType = entry.getValue();
            System.out.println("Character: " + character + ", Match Type: " + matchType);
        }
    }
}
