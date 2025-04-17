package thao.matchle;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import thao.matchle.GuessResult.MatchType;

class NGramMatcher {
    private NGram key;
    private NGram guess;

    private NGramMatcher (NGram key, NGram guess) {
        assert key != null;
        assert guess != null;
        this.key = key;
        this.guess = guess;
    }

    public static final NGramMatcher of (NGram key, NGram guess) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(guess);
        return new NGramMatcher(key, guess);
    }
     
    public GuessResult match () {
        assert key.size() != guess.size();

        // Create a resultMap that shows how the guess matches the key
        GuessResult result = matchIdentical().merge(matchDifferent().merge(matchAbsent()));

        return result;    
    }

    // Template matching algorithm for all three match cases
    private GuessResult match(Predicate<? super IndexedCharacter> matchTest, MatchType matchType) {
        Objects.requireNonNull(matchType);
        Objects.requireNonNull(matchTest);
        
        Map<IndexedCharacter, MatchType> resultMap =  
           guess.stream()
                .filter(matchTest)
                .map(guessedIndexedChar -> {
                    // Return a mapping from the character to the match type
                    return new AbstractMap.SimpleEntry<IndexedCharacter, MatchType>(guessedIndexedChar, matchType);
                }).collect(Collectors.toMap(Map.Entry<IndexedCharacter, MatchType>::getKey, Map.Entry<IndexedCharacter, MatchType>::getValue));
        return GuessResult.of(guess, resultMap);
    }

    private GuessResult matchIdentical() {
        return match(key::matches, MatchType.EXACT);
    }

    private GuessResult matchDifferent() {
        return match(key::containsElseWhere, MatchType.PARTIAL);
    }

    private GuessResult matchAbsent() {
        return match(Predicate.not(key::contains), MatchType.NONE);
    }
}
