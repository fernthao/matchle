package thao.matchle;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public interface MatchingStrategy {
    // Returns a guess based on a certain strategy.
    NGram guess();

    // Return the size of corpus consistent with the filter/feedback
    default long size(GuessResult result, Corpus corpus) {
        List<NGram> matchResult = corpus.stream()
                                .filter(ngram -> {
                                    // Check if the guess matches the ngram the same way
                                    NGramMatcher matcher = NGramMatcher.of(ngram, result.getGuess());
                                    GuessResult match = matcher.match();
                                    return match.equals(result);
                                })
                                .collect(Collectors.toList());
        return (long) matchResult.size();
    }

    default long score(NGram key, NGram guess, Corpus corpus) {
        if (corpus.isEmpty()) {
            throw new IllegalStateException("Can not calculate the score of an empty corpus.");
        }
        Objects.requireNonNull(key);
        Objects.requireNonNull(guess);
        return size(NGramMatcher.of(key, guess).match(), corpus);
    }
    default long score(NGram guess, Function<LongStream, Long> aggregator, Corpus corpus) {
        assert guess != null;
        assert aggregator != null;

        return aggregator.apply(corpus.stream()
                         .mapToLong(key -> score(key, guess, corpus)));
    }

    default NGram bestGuess(ToLongFunction<NGram> criterion, Corpus corpus) {
        Objects.requireNonNull(criterion);
        long minScore = corpus.stream()
                                .mapToLong(key -> criterion.applyAsLong(key))
                                .min().getAsLong();
        
        // return an ngram with minScore                      
        return corpus.stream()
                        .filter(key -> criterion.applyAsLong(key) == minScore)
                        .collect(Collectors.toList())
                        .get(0);
    }
}
