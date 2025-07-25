package thao.matchle;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Game class represents the main game logic for the Matchle game.
 * It handles user input, manages the game state, and provides feedback to the player.
 */
public class Game {
    private Corpus corpus;
    private NGram key;
    private int maxAttempts;
    private List<GuessResult> history;
    
    /**
     * Main method to start the game with the default settings and corpus.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        Game game = Game.from("wordlist.txt", 5, 6);
        game.play();
    }

    /**
     * Constructor for Game.
     * @param corpusFilePath the path to the corpus file
     * @param wordSize the size of the words in the corpus
     * @param maxAttempts the maximum number of attempts allowed
     */
    private Game (Corpus corpus, NGram key, int wordSize, int maxAttempts) {
        assert corpus != null;
        assert key != null;
        assert wordSize > 0;
        assert maxAttempts > 0;

        this.corpus = corpus;
        this.key = key;
        this.maxAttempts = maxAttempts;
        this.history = new ArrayList<GuessResult>();
    }

    /**
     * Factory method to create a new Game instance.
     * @param corpusFilePath the relative path to the corpus file
     * @param wordSize the size of the words in the corpus
     * @param maxAttempts the maximum attempts allowed
     * @return the new Game instance
     * @throws NullPointerException if corpusFilePath is null
     * @throws IllegalArgumentException if wordSize or maxAttempts is less than or equal to 0
     * @throws RuntimeException if there is an error loading the corpus from the file
     */
    public static Game from(String corpusFilePath, int wordSize, int maxAttempts) {
        Objects.requireNonNull(corpusFilePath);
        if (wordSize <= 0) {
            throw new IllegalArgumentException("Word size must be greater than 0");
        }
        if (maxAttempts <= 0) {
            throw new IllegalArgumentException("Max attempts must be greater than 0");
        }
        Corpus corpus = corpusFromFile(corpusFilePath, wordSize);
        NGram key = keyGen(corpus);
        return new Game(corpus, key, wordSize, maxAttempts);
    }

    /**
     * Loads the corpus from a file.
     * @param corpusFilePath the path to the corpus file
     * @param wordSize the size of the words in the corpus
     * @return the loaded Corpus
     * @throws NullPointerException if corpusFilePath is null
     * @throws RuntimeException if there is an error loading the corpus from the file
     * @throws IllegalArgumentException if wordSize is less than or equal to 0
     */
    static Corpus corpusFromFile (String corpusFilePath, int wordSize) {
        Objects.requireNonNull(corpusFilePath);
        if (wordSize <= 0) {
            throw new IllegalArgumentException("Word size must be greater than 0");
        }

        try {
            List<String> words = Files.lines(Paths.get(corpusFilePath))
                                      .map(String::trim)
                                      .filter(word -> word.length() == wordSize)
                                      .collect(Collectors.toList());

            Corpus.Builder builder = Corpus.Builder.empty(wordSize);
            words.forEach(word -> builder.add(NGram.from(word)));

            return builder.build();
        } catch (IOException e) {
            throw new RuntimeException("Error loading corpus from file: " + corpusFilePath, e);
        }
    }

    /**
     * Generates a random key from the corpus.
     * @param corpus the corpus to generate the key from
     * @return a random NGram from the corpus
     * @throws NullPointerException if corpus is null
     * @throws IllegalArgumentException if the corpus is empty
     */
    static NGram keyGen(Corpus corpus) {
        Objects.requireNonNull(corpus);
        if (corpus.isEmpty()) {
            throw new IllegalArgumentException("Corpus is empty");
        }
        // Key is picked arbitrarily
        Random random = new Random();
        List<NGram> ngrams = new ArrayList<>(corpus.corpus());
        return ngrams.get(random.nextInt(ngrams.size()));
    }

    /**
     * Start the game and take user input.
     */
    private void play() {
        assert key != null;
        assert corpus != null;
        assert maxAttempts > 0;
        assert history != null;
        System.out.println("******** Welcome to the Matchle game! ********");
        System.out.println("You have " + maxAttempts + " attempts to guess the key.");
        System.out.println("Guess a " + corpus.wordSize() + "-letter word.");
        Scanner scanner = new Scanner(System.in);
        while (!isOver()) {
            System.out.println("Attempts left: " + (maxAttempts - history.size()));
            System.out.print("Enter your guess: ");
            String guessInput = scanner.nextLine().trim();

            boolean success = makeGuess(guessInput);

            if (!success) {
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
        }
        scanner.close();
        System.out.println("Key: " + key.toString());
    }

    /**
     * Makes a guess
     * @param guessInput the guessed word
     * @return true if the guess was made and its result added to history, false otherwise
     */
    boolean makeGuess(String guessInput) {
        return Barricade.makeValidGuess(history, key, guessInput, corpus, corpus.wordSize());
    }

    /**
     * Checks if the game is over.
     * @return true if the game is over, false otherwise
     */
    boolean isOver() {
        return history.size() >= maxAttempts || isWin();
    }

    /**
     * Checks if the last guess was a win.
     * @return true if the last guess was a win, false otherwise
     */
    boolean isWin() {
        return history.size() > 0 && history.get(history.size() - 1).isMatch();
    }

    /**
     * Gets the guess history
     * @return the list of GuessResult objects representing the history of guesses
     */
    public List<GuessResult> getHistory() {
        return history;
    }

    /**
     * Print the feedback for the last guess in history.
     * If no guesses have been made, print a message indicating that.
     * @throws NullPointerException if history is null
     * @throws IllegalStateException if history is empty
     */
    public void getFeedback() {
        Objects.requireNonNull(history);
        if (history.isEmpty()) {
            throw new IllegalStateException("No guesses have been made yet.");
        }
        // Display the last guess result
        GuessResult lastGuess = history.get(history.size() - 1);
        System.out.println(lastGuess.toString());
    }

    /**
     * Get the corpus.
     * @return the Corpus object
     */
    public Corpus getCorpus() {
        return corpus;
    }

    /**
     * Get the maximum attempts allowed
     * @return the maximum attempts
     */
    public int getMaxAttempts() {
        return maxAttempts;
    }

    /**
     * Get the key.
     * @return the NGram representing the key
     */
    public NGram getKey() {
        return key;
    }

    // /**
    //  * Get the best guess according to the Worst Case Strategy.
    //  * @return the NGram representing the guess
    //  */
    // NGram bestWorstCaseGuess() {
    //     assert history != null;
    //     WorstCaseStrategy worstCaseStrategy = WorstCaseStrategy.from(corpus);
    //     return worstCaseStrategy.guess();
    // }

    // /**
    //  * Get the best guess according to the Average Case Strategy.
    //  * @return the NGram representing the guess
    //  */
    // NGram bestAverageCaseGuess() {
    //     assert history != null;
    //     AverageCaseStrategy averageCaseStrategy = AverageCaseStrategy.from(corpus);
    //     return averageCaseStrategy.guess();}
}
