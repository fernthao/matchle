package thao.matchle;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CorpusTest {
    @Test
    void testCorpusBuilder() {
        NGram n1 = NGram.from("abc");
        Corpus.Builder builder = Corpus.Builder.EMPTY.add(n1);
        Corpus corpus = builder.build();
        assertNotNull(corpus);
        assertEquals(1, corpus.size());
        assertTrue(corpus.contains(n1));
    }
}