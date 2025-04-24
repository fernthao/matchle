package thao.matchle;

import org.junit.Test;

import static org.junit.Assert.*;

public class WorstCaseStrategyTest {
    private static final String TEST_WORD = "apple";
    private static final Corpus TEST_CORPUS = Corpus.Builder.empty(5)
            .add(NGram.from("apple"))
            .add(NGram.from("grape"))
            .add(NGram.from("peach"))
            .add(NGram.from("berry"))
            .build();
    private static final WorstCaseStrategy TEST_STRATEGY = WorstCaseStrategy.from(TEST_CORPUS);
    
    @Test
    public void testFrom() {
        assertNotNull(TEST_STRATEGY);
    }

    @Test
    public void testScoreWorstCase() {
        System.out.println("Testing scoreAverageCase " + TEST_STRATEGY.scoreWorstCase(NGram.from(TEST_WORD)));
        assertNotNull(TEST_STRATEGY.scoreWorstCase(NGram.from(TEST_WORD)));
    }

    @Test
    public void testGuess() {
        System.out.println("Testing guess " + TEST_STRATEGY.guess().toString());
        assertNotNull(TEST_STRATEGY.guess().toString());
    }

    @Test(expected = NullPointerException.class)
    public void testSizeWithNullResult() {
        TEST_STRATEGY.size(null, TEST_CORPUS); // Should throw NullPointerException
    }
    
    @Test(expected = NullPointerException.class)
    public void testSizeWithNullCorpus() {
        NGramMatcher matcher = NGramMatcher.of(NGram.from(TEST_WORD),  NGram.from("apple"));
        GuessResult result = matcher.match();
        TEST_STRATEGY.size(result, null); // Should throw NullPointerException
    }
    
    @Test
    public void testSizeWithValidInputs() {
        NGramMatcher matcher = NGramMatcher.of(NGram.from(TEST_WORD),  NGram.from("apple"));
        GuessResult result = matcher.match();
        long size = TEST_STRATEGY.size(result, TEST_CORPUS);
        assertTrue("Size should be non-negative", size >= 0);
    }
}