package thao.matchle;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import java.util.List;
import java.util.Iterator;

public class NGramTest {
    @Test
    public void testFromList() {
        List<Character> chars = List.of('a', 'b', 'c');
        NGram ngram = NGram.from(chars);
        assertEquals(3, ngram.size());
        assertEquals((Character) 'a', ngram.get(0));
        assertEquals((Character) 'b', ngram.get(1));
        assertEquals((Character) 'c', ngram.get(2));
    }

    @Test
    public void testFromString() {
        NGram ngram = NGram.from("abc");
        assertEquals(3, ngram.size());
        assertEquals((Character) 'a', ngram.get(0));
        assertEquals((Character) 'b', ngram.get(1));
        assertEquals((Character) 'c', ngram.get(2));
    }

    @Test
    public void testEquals() {
        NGram n1 = NGram.from("abc");
        NGram n2 = NGram.from("abc");
        NGram n3 = NGram.from("def");
        assertEquals(n1, n2);
        assertNotEquals(n1, n3);
    }

    @Test
    public void testMatches() {
        NGram ngram = NGram.from("abc");
        assertTrue(ngram.matches(new IndexedCharacter(1, 'b')));
        assertFalse(ngram.matches(new IndexedCharacter(1, 'c')));
    }

    @Test
    public void testContains() {
        NGram ngram = NGram.from("abc");
        assertTrue(ngram.contains(new IndexedCharacter(0, 'b')));
        assertFalse(ngram.contains(new IndexedCharacter(0, 'd')));
    }

    @Test
    public void testContainsElseWhere() {
        NGram ngram = NGram.from("aba");
        assertTrue(ngram.containsElseWhere(new IndexedCharacter(0, 'b')));
        assertFalse(ngram.containsElseWhere(new IndexedCharacter(1, 'b')));
    }

    @Test
    public void testIterator() {
        NGram ngram = NGram.from("abc");
        Iterator<IndexedCharacter> iterator = ngram.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(new IndexedCharacter(0, 'a'), iterator.next());
        assertEquals(new IndexedCharacter(1, 'b'), iterator.next());
        assertEquals(new IndexedCharacter(2, 'c'), iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test (expected = NullPointerException.class)
    public void testNullCharacterExceptionValidation() {
        // Test with a valid list
        List<Character> validList = List.of('a', 'b', 'c');
        try {
            NGram.NullCharacterException.validate(validList);
        } catch (Exception e) {
            fail("Exception should not be thrown for a valid list");
        }

        // Test with a list containing null
        List<Character> invalidList = List.of('a', null, 'c');
        try {
            NGram.NullCharacterException.validate(invalidList);
            fail("Exception should be thrown for a list containing null");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getCause() instanceof NGram.NullCharacterException);
            NGram.NullCharacterException cause = (NGram.NullCharacterException) e.getCause();
            assertEquals(1, cause.getIndex());
        }
    }

    @Test
    public void testNullCharacterExceptionConstructor() {
        // Test valid index
        NGram.NullCharacterException exception = new NGram.NullCharacterException(2);
        assertEquals(2, exception.getIndex());

        // Test invalid index
        try {
            new NGram.NullCharacterException(-1);
            fail("Exception should be thrown for a negative index");
        } catch (IllegalArgumentException e) {
            assertEquals("Index must be non-negative", e.getMessage());
        }
    }
}