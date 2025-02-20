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

    @Test
    void testFilter() {
        NGram n1 = NGram.from("abc");
        NGram n2 = NGram.from("efg");
        Corpus.Builder builder = Corpus.Builder.EMPTY.add(n1).add(n2);
        builder = builder.filter(Filter.from(ngram -> ngram.contains(new IndexedCharacter(0, 'a'))));
        assertEquals(Corpus.Builder.EMPTY.add(n1), builder);
    }

    @Test
    void testScore() {
        NGram key = NGram.from("hello");
        NGram guess = NGram.from("hilly");
        Corpus.Builder builder = Corpus.Builder.EMPTY.add(key);
        Corpus corpus = builder.build();

        assertDoesNotThrow(() -> corpus.score(key, guess));
        assertTrue(corpus.score(key, guess) >= 0);
    }


    @Test
    void testScoreAverageCase() {
        NGram n1 = NGram.from("apple");
        NGram n2 = NGram.from("apply");
        Corpus.Builder builder = Corpus.Builder.EMPTY.add(n1).add(n2);
        Corpus corpus = builder.build();
        assertTrue(corpus.scoreAverageCase(n1) >= 0);
    }

    @Test
    void testBestWorstCaseGuess() {
        NGram n1 = NGram.from("apple");
        NGram n2 = NGram.from("apply");
        Corpus.Builder builder = Corpus.Builder.EMPTY.add(n1).add(n2);
        Corpus corpus = builder.build();
        NGram guess = NGram.from("apple");

        assertNotNull(corpus.bestWorstCaseGuess(guess));
    }

}