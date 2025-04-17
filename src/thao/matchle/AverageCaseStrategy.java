package thao.matchle;

import java.util.List;
import java.util.Objects;

public class AverageCaseStrategy implements MatchingStrategy {
    private final Corpus corpus;
    private final GuessResult result;

    private AverageCaseStrategy(Corpus corpus, GuessResult result) {
        this.corpus = corpus;
        this.result = result;
    }

    public static AverageCaseStrategy from(Corpus corpus, List<GuessResult> history) {
        Objects.requireNonNull(corpus);
        GuessResult mergedResult = null;
        for (GuessResult result : history) {
            mergedResult = mergedResult == null ? result : mergedResult.merge(result);
        }
        return new AverageCaseStrategy(corpus, mergedResult);
    }

    // Maximum score of guess among all corpus’ n-grams
    private long scoreAverageCase(NGram guess) {
        Objects.requireNonNull(guess);
        if (corpus.isEmpty()) {
            throw new IllegalStateException("Can not calculate the score of an empty corpus.");
        }
        return score(guess, stream -> stream.sum(), corpus);
    }

    public NGram guess() {
        return bestGuess(ngram -> scoreAverageCase(ngram), corpus);
    }
}
