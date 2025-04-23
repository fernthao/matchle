package thao.matchle;

import org.junit.Test;

import static org.junit.Assert.*;

public class NGramMatcherTest {

    @Test
    public void testOfCreatesNGramMatcher() {
        NGram key = NGram.from("apple");
        NGram guess = NGram.from("apric");
        
        assertNotNull(NGramMatcher.of(key, guess));
    }
    @Test
    public void testMatchIdentical() {
        NGram key = NGram.from("apple");
        NGram guess = NGram.from("apple");
        
        NGramMatcher matcher = NGramMatcher.of(key, guess);
        GuessResult result = matcher.match();
        
        assertEquals(GuessResult.MatchType.EXACT, result.getMatchType(0));
        assertEquals(key, result.getGuess());
    }
    @Test
    public void testMatchDifferent() {
        NGram key = NGram.from("apple");
        NGram guess = NGram.from("often");
        
        NGramMatcher matcher = NGramMatcher.of(key, guess);
        GuessResult result = matcher.match();
        
        assertEquals(GuessResult.MatchType.PARTIAL, result.getMatchType(3));
    }
    @Test
    public void testMatchAbsent() {
        NGram key = NGram.from("apple");
        NGram guess = NGram.from("grune");
        
        NGramMatcher matcher = NGramMatcher.of(key, guess);
        GuessResult result = matcher.match();
        
        assertEquals(GuessResult.MatchType.NONE, result.getMatchType(0));
    }
}
