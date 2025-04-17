package thao.matchle;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class WorstCaseStrategy implements MatchingStrategy {
    private final Corpus corpus;
    private final GuessResult result;

    private WorstCaseStrategy(Corpus corpus, GuessResult result) {
        this.corpus = corpus;
        this.result = result;
    }

    public static WorstCaseStrategy from(Corpus corpus, List<GuessResult> history) {
        Objects.requireNonNull(corpus);
        GuessResult mergedResult = null;
        for (GuessResult result : history) {
            mergedResult = mergedResult == null ? result : mergedResult.merge(result);
        }
        return new WorstCaseStrategy(corpus, mergedResult);
    }

    // Maximum score of guess among all corpus’ n-grams
    private long scoreWorstCase(NGram guess) {
        Objects.requireNonNull(guess);
        if (corpus.isEmpty()) {
            throw new IllegalStateException("Can not calculate the score of an empty corpus.");
        }
        return score(guess, stream -> stream.max().orElseThrow(() -> new IllegalStateException("No maximum value found")), corpus);
    }

    public NGram guess() {
        return bestGuess(ngram -> scoreWorstCase(ngram), corpus);
    }
}
