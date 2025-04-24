package thao.matchle;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BarricadeTest {
    private Corpus testCorpus;
    private NGram testKey;
    private List<GuessResult> history;

    @Before
    public void setUp() {
        NGram n1 = NGram.from("apple");
        NGram n2 = NGram.from("guava");
        NGram n3 = NGram.from("mango");
        testCorpus = Corpus.Builder.empty(5).add(n1).add(n2).add(n3).build();
        testKey = NGram.from("apple");
        history = new ArrayList<>();
    }

    // TODO figure out why this fails
    @Test
    public void testValidatedGuessWithValidInput() {
        Optional<NGram> result = Barricade.validatedGuess("apple", testCorpus, 5);
        assertTrue("Validated guess should be present", result.isPresent());
        assertEquals("Validated guess should match input", NGram.from("apple"), result.get());
    }

    @Test
    public void testValidatedGuessWithNullInput() {
        try {
            Barricade.validatedGuess(null, testCorpus, 5);
            fail("Expected NullPointerException for null guess");
        } catch (NullPointerException e) {
            assertEquals("Guess cannot be null", e.getMessage());
        }
    }

    @Test
    public void testValidatedGuessWithBlankInput() {
        Optional<NGram> result = Barricade.validatedGuess("   ", testCorpus, 5);
        assertTrue("Validated guess should be empty for blank input", result.isEmpty());
    }

    @Test
    public void testValidatedGuessWithIncorrectLength() {
        Optional<NGram> result = Barricade.validatedGuess("apples", testCorpus, 5);
        assertTrue("Validated guess should be empty for incorrect length", result.isEmpty());
    }

    @Test
    public void testValidatedGuessWithNonExistentWord() {
            Optional<NGram> result = Barricade.validatedGuess("grape", testCorpus, 5);
            assertTrue("Validated guess should be empty for non-existent word", result.isEmpty());
    }

    @Test
    public void testMakeValidGuessWithValidInput() {
        boolean result = Barricade.makeValidGuess(history, testKey, "apple", testCorpus, 5);
        assertTrue("Make valid guess should return true for valid input", result);
        assertEquals("History should contain one result", 1, history.size());
    }

    @Test
    public void testMakeValidGuessWithInvalidInput() {
        boolean result = Barricade.makeValidGuess(history, testKey, "grape", testCorpus, 5);
        assertTrue("Make valid guess should return false for invalid input", !result);
        assertEquals("History should remain empty for invalid input", 0, history.size());
    }

    @Test
    public void testMakeValidGuessWithBlankInput() {
        boolean result = Barricade.makeValidGuess(history, testKey, "   ", testCorpus, 5);
        assertTrue("Make valid guess should return false for blank input", !result);
        assertEquals("History should remain empty for blank input", 0, history.size());
    }

    @Test
    public void testMakeValidGuessWithIncorrectLength() {
        boolean result = Barricade.makeValidGuess(history, testKey, "apples", testCorpus, 5);
        assertTrue("Make valid guess should return false for incorrect length", !result);
        assertEquals("History should remain empty for incorrect length", 0, history.size());
    }
    @Test(expected = NullPointerException.class)
    public void testMakeValidGuessWithNullKey() {
        Barricade.makeValidGuess(history, null, "apple", testCorpus, 5);
    }
    
    @Test(expected = NullPointerException.class)
    public void testMakeValidGuessWithNullGuessInputString() {
        Barricade.makeValidGuess(history, testKey, null, testCorpus, 5);
    }
    
    @Test(expected = NullPointerException.class)
    public void testMakeValidGuessWithNullCorpus() {
        Barricade.makeValidGuess(history, testKey, "apple", null, 5);
    }
    
    @Test(expected = NullPointerException.class)
    public void testValidatedGuessWithNullCorpus() {
        Barricade.validatedGuess("apple", null, 5);
    }
}
