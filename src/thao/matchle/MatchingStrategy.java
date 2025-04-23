package thao.matchle;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * Interface for matching strategies in the Matchle game.
 */
public interface MatchingStrategy {
    /**
     * Returns the best guess based on the current strategy.
     * 
     * @return The best guess as an NGram.
     */
    NGram guess();

    /**
     * Calculates the size of the corpus that is consistent with the given feedback (GuessResult).
     * 
     * @param result The feedback result to filter the corpus against.
     * @param corpus The corpus of NGrams to filter.
     * @return The number of NGrams in the corpus that match the feedback result.
     */
    default long size(GuessResult result, Corpus corpus) {
        List<NGram> matchResult = corpus.stream()
                                .filter(ngram -> {
                                    NGramMatcher matcher = NGramMatcher.of(ngram, result.getGuess());
                                    GuessResult match = matcher.match();
                                    return match.equals(result);
                                })
                                .collect(Collectors.toList());
        return (long) matchResult.size();
    }

    /**
     * Calculates the score of a pair of key and guess in a certain corpus.
     * The score is the number ngrams in the corpus that are consistent with the match result of the guess - key input pair.
     * 
     * @param key The NGram representing the key.
     * @param guess The NGram representing the guess.
     * @param corpus The corpus of NGrams to filter.
     * @return The number of NGrams in the corpus that match the given pair.
     */
    default long score(NGram key, NGram guess, Corpus corpus) {
        if (corpus.isEmpty()) {
            throw new IllegalStateException("Can not calculate the score of an empty corpus.");
        }
        Objects.requireNonNull(key);
        Objects.requireNonNull(guess);
        return size(NGramMatcher.of(key, guess).match(), corpus);
    }

    /**
     * Calculates the score of a guess in a certain corpus using an aggregator function.
     * 
     * @param guess The NGram representing the guess.
     * @param aggregator The function to aggregate the scores.
     * @param corpus The corpus of NGrams to filter.
     * @return The aggregated score of the guess in the corpus.
     */
    default long score(NGram guess, Function<LongStream, Long> aggregator, Corpus corpus) {
        assert guess != null;
        assert aggregator != null;

        return aggregator.apply(corpus.stream()
                         .mapToLong(key -> score(key, guess, corpus)));
    }

    /**
     * Finds the best guess based on a given criterion function.
     * 
     * @param criterion The function to evaluate the score of each NGram.
     * @param corpus The corpus of NGrams to filter.
     * @return The NGram with the best score according to the criterion.
     */
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
