package thao.matchle;

import java.util.Objects;

/**
 * WorstCaseStrategy class implements the MatchingStrategy interface and provides a method to find the best guess
 * based on the worst-case strategy, which is to choose the guess with the maximum score among all ngrams in the corpus.
 */
public class WorstCaseStrategy implements MatchingStrategy {
    private final Corpus corpus;

    private WorstCaseStrategy(Corpus corpus) {
        this.corpus = corpus;
    }

    /**
     * Factory method to create a new WorstCaseStrategy object from a given corpus.
     *
     * @param corpus the corpus to use for the strategy
     * @return a new WorstCaseStrategy object
     * @throws NullPointerException if the corpus is null
     */
    public static WorstCaseStrategy from(Corpus corpus) {
        Objects.requireNonNull(corpus);
        return new WorstCaseStrategy(corpus);
    }

    /**
     * Calculates the score of a guess in the corpus using a worst-case strategy, 
     * that is choosing the guess with maximum score among all ngrams in corpus.
     * @param guess the NGram representing the guess
     * @return the score of the guess in the corpus in the Worst Case strategy
     */
    long scoreWorstCase(NGram guess) {
        Objects.requireNonNull(guess);
        if (corpus.isEmpty()) {
            throw new IllegalStateException("Can not calculate the score of an empty corpus.");
        }
        return score(guess, stream -> stream.max().orElseThrow(() -> new IllegalStateException("No maximum value found")), corpus);
    }

    /**
     * Returns the best guess based on the worst-case strategy.
     * This method finds the NGram in the corpus that has the maximum score when compared to the given guess.
     *
     * @return The best guess as an NGram.
     */
    public NGram guess() {
        return bestGuess(ngram -> scoreWorstCase(ngram), corpus);
    }
}
