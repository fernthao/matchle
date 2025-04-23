package thao.matchle;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;
import java.util.stream.Collectors;

public class CorpusTest {
    @Test
    public void testAdd() {
        Corpus.Builder builder = Corpus.Builder.empty(4);
        NGram ngram = NGram.from("test");
        builder.add(ngram);
        Corpus corpus = builder.build();
        assertNotNull(corpus);
        assertTrue(corpus.contains(ngram));
    }

    @Test
    public void testAddAll() {
        Corpus.Builder builder = Corpus.Builder.empty(5);
        NGram ngram1 = NGram.from("test1");
        NGram ngram2 = NGram.from("test2");
        Collection<NGram> ngrams = Arrays.asList(ngram1, ngram2);
        builder.addAll(ngrams);
        Corpus corpus = builder.build();
        assertNotNull(corpus);
        assertTrue(corpus.contains(ngram1));
        assertTrue(corpus.contains(ngram2));
    }
    @Test
    public void testAddInvalidNGram() {
        Corpus.Builder builder = Corpus.Builder.empty(5);
        NGram validNGram = NGram.from("valid");
        NGram invalidNGram1 = NGram.from("invalid");
        NGram invalidNGram2 = NGram.from("invalid");
        NGram invalidNGram3 = null;
        builder.add(invalidNGram1); // Should log a warning but not throw an exception
        Collection<NGram> ngrams = Arrays.asList(validNGram, invalidNGram1, invalidNGram2, invalidNGram3);
        builder.addAll(ngrams); // Should log a warning but not throw an exception
        Corpus corpus = builder.build();
        assertNotNull(corpus);
        assertFalse(corpus.contains(invalidNGram1));
        assertFalse(corpus.contains(invalidNGram2));
    }

    @Test (expected = IllegalStateException.class)
    public void testEmptyBuilder() {
        Corpus.Builder builder = Corpus.Builder.empty(3);
        builder.build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyBuilderNegativeWordSize() {
        Corpus.Builder.empty(-1);
    }

    @Test
    public void testIsValid() {
        Corpus.Builder builder = Corpus.Builder.empty(4);
        NGram validNGram = NGram.from("test");
        NGram invalidNGram = NGram.from("invalid");
        assertTrue(builder.isValid(validNGram));
        assertFalse(builder.isValid(invalidNGram));
    }

    @Test
    public void testBuildWithInvalidNGram() {
        Corpus.Builder builder = Corpus.Builder.empty(4);
        NGram validNGram = NGram.from("test");
        NGram invalidNGram = NGram.from("invalid");
        builder.add(validNGram);
        builder.add(invalidNGram); // Should log a warning but not throw an exception
        Corpus corpus = builder.build();
        assertNotNull(corpus);
        assertTrue(corpus.contains(validNGram));
        assertFalse(corpus.contains(invalidNGram));
    }

    @Test
    public void testCorpusSize() {
        Corpus.Builder builder = Corpus.Builder.empty(4);
        builder.add(NGram.from("test"));
        builder.add(NGram.from("abcd"));
        Corpus corpus = builder.build();
        assertEquals(2, corpus.size());
    }

    @Test
    public void testCorpusIterator() {
        Corpus.Builder builder = Corpus.Builder.empty(4);
        NGram ngram1 = NGram.from("test");
        NGram ngram2 = NGram.from("abcd");
        builder.add(ngram1);
        builder.add(ngram2);
        Corpus corpus = builder.build();
        Iterator<NGram> iterator = corpus.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(ngram1, iterator.next());
        assertEquals(ngram2, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testCorpusStream() {
        Corpus.Builder builder = Corpus.Builder.empty(4);
        NGram ngram1 = NGram.from("test");
        NGram ngram2 = NGram.from("abcd");
        builder.add(ngram1);
        builder.add(ngram2);
        Corpus corpus = builder.build();
        List<NGram> ngrams = corpus.stream().collect(Collectors.toList());
        assertEquals(2, ngrams.size());
        assertTrue(ngrams.contains(ngram1));
        assertTrue(ngrams.contains(ngram2));
    }

    @Test
    public void testCorpusToString() {
        Corpus.Builder builder = Corpus.Builder.empty(4);
        NGram ngram1 = NGram.from("test");
        NGram ngram2 = NGram.from("abcd");
        builder.add(ngram1);
        builder.add(ngram2);
        Corpus corpus = builder.build();
        String corpusString = corpus.toString();
        assertTrue(corpusString.contains("test"));
        assertTrue(corpusString.contains("abcd"));
    }

    @Test
    public void testBuilderOf() {
        Corpus.Builder builder = Corpus.Builder.empty(4);
        NGram ngram = NGram.from("test");
        builder.add(ngram);
        Corpus corpus = builder.build();
        Corpus.Builder newBuilder = Corpus.Builder.of(corpus);
        assertNotNull(newBuilder);
        assertEquals(4, newBuilder.wordSize());
    }
}
