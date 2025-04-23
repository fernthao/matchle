package thao.matchle;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import thao.matchle.GuessResult.MatchType;

/**
 * NGramMatcher class encapsulate the matching algorithm to match a guessed n-gram against a target key n-gram.
*/
class NGramMatcher {
    private NGram key;
    private NGram guess;

    private NGramMatcher (NGram key, NGram guess) {
        assert key != null;
        assert guess != null;
        this.key = key;
        this.guess = guess;
    }

    /**
     * Factory method to create a NGramMatcher.
     * @param key the target n-gram
     * @param guess the guessed n-gram
     * @return a new NGramMatcher instance
     */
    public static final NGramMatcher of (NGram key, NGram guess) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(guess);
        return new NGramMatcher(key, guess);
    }
     
    /**
     * The central matching algorithm.
     * Matches the guess against the key by combining three match cases.
     * @return a GuessResult representing the match result
     */
    public GuessResult match () {
        // Create a resultMap that shows how the guess matches the key
        GuessResult result = matchIdentical().merge(matchDifferent().merge(matchAbsent()));

        return result;    
    }

    /**
     * Template matching algorithm for all three match cases
     * @param matchTest the predicate to test for each indexed character in the guess
     * @param matchType the type of match (EXACT, PARTIAL, NONE) being mapped to the character that pass the matchTest
     * @return a GuessResult representing the match result
     */
    private GuessResult match(Predicate<? super IndexedCharacter> matchTest, MatchType matchType) {
        Objects.requireNonNull(matchType);
        Objects.requireNonNull(matchTest);
        
        Map<IndexedCharacter, MatchType> resultMap =  
           guess.stream()
                .filter(matchTest)
                .map(guessedIndexedChar -> {
                    return new AbstractMap.SimpleEntry<IndexedCharacter, MatchType>(guessedIndexedChar, matchType);
                }).collect(Collectors.toMap(Map.Entry<IndexedCharacter, MatchType>::getKey, Map.Entry<IndexedCharacter, MatchType>::getValue));
        return GuessResult.of(guess, resultMap);
    }

    /**
     * Matches the guess against the key for identical characters.
     * This is the first match case.
     * @return a GuessResult representing the match result
    */
    private GuessResult matchIdentical() {
        return match(key::matches, MatchType.EXACT);
    }

    /**
     * Matches the guess against the key for characters that are present but not in the same position.
     * This is the second match case.
     * @return a GuessResult representing the match result
    */
    private GuessResult matchDifferent() {
        return match(key::containsElseWhere, MatchType.PARTIAL);
    }

    /**
     * Matches the guess against the key for characters that are not present at all.
     * This is the third match case.
     * @return a GuessResult representing the match result
    */
    private GuessResult matchAbsent() {
        return match(Predicate.not(key::contains), MatchType.NONE);
    }
}
