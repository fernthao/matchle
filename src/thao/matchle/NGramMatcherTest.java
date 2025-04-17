package thao.matchle;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NGramMatcherTest {

    @Test
    void testOfCreatesNGramMatcher() {
        NGram key = NGram.from("apple");
        NGram guess = NGram.from("apric");
        
        assertNotNull(NGramMatcher.of(key, guess));
    }
    @Test
    void testMatchIdentical() {
        NGram key = NGram.from("apple");
        NGram guess = NGram.from("apple");
        
        NGramMatcher matcher = NGramMatcher.of(key, guess);
        GuessResult result = matcher.match();
        
        assertEquals(GuessResult.MatchType.EXACT, result.getMatchType(0));
        assertEquals(key, result.getGuess());
    }
    @Test
    void testMatchDifferent() {
        NGram key = NGram.from("apple");
        NGram guess = NGram.from("often");
        
        NGramMatcher matcher = NGramMatcher.of(key, guess);
        GuessResult result = matcher.match();
        
        assertEquals(GuessResult.MatchType.PARTIAL, result.getMatchType(3));
        assertEquals(key, result.getGuess());
    }
    @Test
    void testMatchAbsent() {
        NGram key = NGram.from("apple");
        NGram guess = NGram.from("banana");
        
        NGramMatcher matcher = NGramMatcher.of(key, guess);
        GuessResult result = matcher.match();
        
        assertEquals(GuessResult.MatchType.NONE, result.getMatchType(0));
        assertEquals(key, result.getGuess());
    }
}
