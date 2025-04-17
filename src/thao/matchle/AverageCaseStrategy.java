package thao.matchle;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class AverageCaseStrategy implements MatchingStrategy {

    // Sum of scores of guess among all corpus’ n-grams.
    public long scoreAverageCase(NGram guess) {
        Objects.requireNonNull(guess);
        if (corpus.isEmpty()) {
            throw new IllegalStateException("Can not calculate the score of an empty corpus.");
        }
        return score(guess, LongStream::sum);
    }

    public NGram guess(NGram guess) {
        Objects.requireNonNull(guess);
        return bestGuess(ngram -> scoreAverageCase(ngram));
    }
}
