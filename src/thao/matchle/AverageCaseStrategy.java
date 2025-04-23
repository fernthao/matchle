package thao.matchle;

import java.util.Objects;

/**
 * This class implements the average case to find the best guess in a corpus.
 * The best guess is the one that minimizes the average number of remaining candidates
 * after each guess.
 */
public class AverageCaseStrategy implements MatchingStrategy {
    private final Corpus corpus;

    private AverageCaseStrategy(Corpus corpus) {
        assert corpus != null;
        this.corpus = corpus;
    }

    /**
     * Creates a new AverageCaseStrategy instance with the given corpus.
     *
     * @param corpus The corpus to be used for the strategy.
     * @return A new AverageCaseStrategy instance.
     */
    public static AverageCaseStrategy from(Corpus corpus) {
        Objects.requireNonNull(corpus);
        
        return new AverageCaseStrategy(corpus);
    }

    /**
     * Calculates the score of a guess in the average case, which is the sum of the scores
     * @param guess The NGram representing the guess.
     * @throws NullPointerException if guess is null.
     * @throws IllegalStateException if the corpus is empty.
     * @return The average case score of the guess.
     */
    long scoreAverageCase(NGram guess) {
        Objects.requireNonNull(guess);
        if (corpus.isEmpty()) {
            throw new IllegalStateException("Can not calculate the score of an empty corpus.");
        }
        return score(guess, stream -> stream.sum(), corpus);
    }

    /**
     * Returns the best guess based on the average case strategy.
     *
     * @return The best guess as an NGram.
     */
    public NGram guess() {
        return bestGuess(ngram -> scoreAverageCase(ngram), corpus);
    }
}
