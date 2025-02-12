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
    void testMatchReturnsFalseForDifferentSizeNGrams() {
        NGram key = NGram.from("apple");
        NGram guess = NGram.from("apples");
        NGramMatcher matcher = NGramMatcher.of(key, guess);
        
        assertFalse(matcher.match().test(key));
    }
    
    @Test
    void testMatchIdentical() {
        NGram key = NGram.from("apple");
        NGram guess = NGram.from("apple");
        NGramMatcher matcher = NGramMatcher.of(key, guess);
        
        assertTrue(matcher.match().test(key));
    }
    
    @Test
    void testMatchDifferentPositions() {
        NGram key = NGram.from("apple");
        NGram guess = NGram.from("ppale");
        NGramMatcher matcher = NGramMatcher.of(key, guess);
        
        assertTrue(matcher.match().test(key));
    }
    
    @Test
    void testMatchAbsentCharacters() {
        NGram key = NGram.from("apple");
        NGram guess = NGram.from("xxxxx");
        NGramMatcher matcher = NGramMatcher.of(key, guess);
        
        assertTrue(matcher.match().test(key));
    }

    @Test
    void testExample() {
        NGram key = NGram.from("rebus");
        NGram guess = NGram.from("route");
        NGramMatcher matcher = NGramMatcher.of(key, guess);

        assertTrue(matcher.match().test(key));
        assertFalse(matcher.match().test(NGram.from("regex")));
        assertTrue(matcher.match().test(NGram.from("redux")));
    }
}
