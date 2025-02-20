package thao.matchle;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

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
    
    public Filter match () {
        // Return false if key and guess have different lengths
        if (key.size() != guess.size()) {
            return  Filter.FALSE;
        }
        // Create a predicate that checks if the guess matches the key
        Predicate<NGram> matchPredicate = matchIdentical().and(matchDifferent().and(matchAbsent()));

        return Filter.from(matchPredicate);    
    }

    // Template matching algorithm for all three match cases
    private Predicate<NGram> match(Predicate<? super IndexedCharacter> matchType, BiFunction<NGram, IndexedCharacter, Boolean> matchTest, String message) {
        Objects.requireNonNull(matchType);
        Objects.requireNonNull(matchTest);
        Objects.requireNonNull(message);
        
        List<Predicate<NGram>> allMatches = 
           guess.stream()
                .filter(matchType)
                .map(guessedIndexedChar -> {
                    // Report to player
                    System.out.println(guessedIndexedChar.character() + message);

                    // Return a predicate
                    Predicate<NGram> currentMatch = ngram -> matchTest.apply(ngram, guessedIndexedChar);
                    return currentMatch;
                }).collect(Collectors.toList());
            
        // Anding all predicate together
        Predicate<NGram> result = ngram -> true;
        for (Predicate<NGram> match : allMatches) {
            result = result.and(match);
        }
        return result;
    }

    private Predicate<NGram> matchIdentical() {
        return match(key::matches, (ngram, indexChar) -> ngram.matches(indexChar), " is in the word and in the correct spot.");
    }

    private Predicate<NGram> matchDifferent() {
        return match(key::containsElseWhere, (ngram, indexChar) -> ngram.containsElseWhere(indexChar), " is in the word but in the wrong spot.");
    }

    private Predicate<NGram> matchAbsent() {
        return match(Predicate.not(key::contains), (ngram, indexChar) -> !ngram.contains(indexChar), " is not in the word in any spot.");
    }
}
