package thao.matchle;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class WorstCaseStrategyTest {
    private static final String TEST_WORD = "apple";
    private static final Corpus TEST_CORPUS = Corpus.Builder.EMPTY
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
}