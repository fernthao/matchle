package thao.matchle;

import org.junit.Test;
import static org.junit.Assert.*;;

public class AverageCaseStrategyTest {
    private static final String TEST_WORD = "apple";
    private static final Corpus TEST_CORPUS = Corpus.Builder.empty(5)
            .add(NGram.from("apple"))
            .add(NGram.from("grape"))
            .add(NGram.from("peach"))
            .add(NGram.from("berry"))
            .build();
    private static final AverageCaseStrategy TEST_STRATEGY = AverageCaseStrategy.from(TEST_CORPUS);
    
    @Test
    public void testFrom() {
        assertNotNull(TEST_STRATEGY);
    }

    @Test
    public void testScoreAverageCase() {
        System.out.println("Testing scoreAverageCase " + TEST_STRATEGY.scoreAverageCase(NGram.from(TEST_WORD)));
        assertNotNull(TEST_STRATEGY.scoreAverageCase(NGram.from(TEST_WORD)));
    }

    @Test
    public void testGuess() {
        System.out.println("Testing guess " + TEST_STRATEGY.guess().toString());
        assertNotNull(TEST_STRATEGY.guess().toString());
    }
}