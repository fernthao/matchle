package thao.matchle;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;;

public class CorpusTest {
    @Test
    public void testCorpusBuilder() {
        NGram n1 = NGram.from("abc");
        Corpus.Builder builder = Corpus.Builder.EMPTY.add(n1);
        Corpus corpus = builder.build();
        assertNotNull(corpus);
        assertEquals(1, corpus.size());
        assertTrue(corpus.contains(n1));
    }
    @Test
    public void testCorpusBuilderMultipleNGrams() {
        Corpus.Builder builder = Corpus.Builder.EMPTY.add(NGram.from("apples")).add(NGram.from("banana")).add(NGram.from("cherry"));
        Corpus testCorpus = builder.build();
        assertNotNull(testCorpus);
        assertEquals(3, testCorpus.size());
        assertTrue(testCorpus.contains(NGram.from("apples")));
        assertTrue(testCorpus.contains(NGram.from("banana")));
        assertTrue(testCorpus.contains(NGram.from("cherry")));
    }
}