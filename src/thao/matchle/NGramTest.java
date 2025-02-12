package thao.matchle;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.util.*;

class NGramTest {
    @Test
    void testFromList() {
        List<Character> chars = List.of('a', 'b', 'c');
        NGram ngram = NGram.from(chars);
        assertEquals(3, ngram.size());
        assertEquals('a', ngram.get(0));
        assertEquals('b', ngram.get(1));
        assertEquals('c', ngram.get(2));
    }

    @Test
    void testFromString() {
        NGram ngram = NGram.from("abc");
        assertEquals(3, ngram.size());
        assertEquals('a', ngram.get(0));
        assertEquals('b', ngram.get(1));
        assertEquals('c', ngram.get(2));
    }

    @Test
    void testEquals() {
        NGram n1 = NGram.from("abc");
        NGram n2 = NGram.from("abc");
        NGram n3 = NGram.from("def");
        assertEquals(n1, n2);
        assertNotEquals(n1, n3);
    }

    @Test
    void testMatches() {
        NGram ngram = NGram.from("abc");
        assertTrue(ngram.matches(new IndexedCharacter(1, 'b')));
        assertFalse(ngram.matches(new IndexedCharacter(1, 'c')));
    }

    @Test
    void testContains() {
        NGram ngram = NGram.from("abc");
        assertTrue(ngram.contains(new IndexedCharacter(0, 'b')));
        assertFalse(ngram.contains(new IndexedCharacter(0, 'd')));
    }

    @Test
    void testContainsElseWhere() {
        NGram ngram = NGram.from("aba");
        assertTrue(ngram.containsElseWhere(new IndexedCharacter(0, 'b')));
        assertFalse(ngram.containsElseWhere(new IndexedCharacter(1, 'b')));
    }

    @Test
    void testIterator() {
        NGram ngram = NGram.from("abc");
        Iterator<IndexedCharacter> iterator = ngram.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(new IndexedCharacter(0, 'a'), iterator.next());
        assertEquals(new IndexedCharacter(1, 'b'), iterator.next());
        assertEquals(new IndexedCharacter(2, 'c'), iterator.next());
        assertFalse(iterator.hasNext());
    }
}