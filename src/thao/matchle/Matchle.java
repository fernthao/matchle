package thao.matchle;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Matchle {

    public static void main(String[] args) {
        // Load 5-letter words
        Corpus corpus = loadCorpus("wordlist.txt", 5); 

        // Key is picked arbitrarily
        Random random = new Random();
        List<NGram> ngrams = new ArrayList<>(corpus.getCorpus());
        NGram key = ngrams.get(random.nextInt(ngrams.size()));

        playMatchle(corpus, key, 2);
        System.out.println("Key: " + key.toString());
    }

    private static Corpus loadCorpus(String filename, int wordSize) {
        assert filename != null;
        assert wordSize > 0;

        try {
            List<String> words = Files.lines(Paths.get(filename))
                                      .map(String::trim)
                                      .filter(word -> word.length() == wordSize)
                                      .collect(Collectors.toList());

            Corpus.Builder builder = Corpus.Builder.EMPTY;
            words.forEach(word -> builder.add(NGram.from(word)));

            return builder.build();
        } catch (IOException e) {
            throw new RuntimeException("Error loading corpus from file: " + filename, e);
        }
    }

    private static void playMatchle(Corpus corpus, NGram key, int maxAttempts) {
        assert corpus != null;
        assert key != null;
        assert maxAttempts > 0;

        Scanner scanner = new Scanner(System.in);
        Set<Filter> filters = new HashSet<>();

        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            System.out.print("Enter your guess: ");
            String guessInput = scanner.nextLine().trim();

            if (guessInput.length() != key.size()) {
                System.out.println("Invalid guess length.");
                continue;
            }

            NGram guess = NGram.from(guessInput);

            NGramMatcher matcher = NGramMatcher.of(key, guess);
            Filter currentFilter = matcher.match();

            filters.add(currentFilter);

            long score = corpus.score(key, guess);
            System.out.println("Score: " + score);

            if (key.equals(guess)) {
                System.out.println("Congratulations! You found the key: " + key);
                break;
            }

            System.out.println("Best worst-case guess: " + corpus.bestWorstCaseGuess(guess));
            System.out.println("Best average-case guess: " + corpus.bestAverageCaseGuess(guess));
        }
        scanner.close();
    }
}
