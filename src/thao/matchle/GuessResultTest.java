package thao.matchle;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;

public class GuessResultTest {
    @Test
    public void testOfFactoryMethod() {
        NGram guess = NGram.from("test");
        Map<IndexedCharacter, GuessResult.MatchType> resultMap = Map.of(
            new IndexedCharacter(0, 't'), GuessResult.MatchType.EXACT,
            new IndexedCharacter(1, 'e'), GuessResult.MatchType.PARTIAL,
            new IndexedCharacter(2, 's'), GuessResult.MatchType.NONE,
            new IndexedCharacter(3, 't'), GuessResult.MatchType.EXACT
        );

        GuessResult guessResult = GuessResult.of(guess, resultMap);

        assertEquals(guess, guessResult.getGuess());
        assertEquals(resultMap, guessResult.getResultMap());
    }

    @Test
    public void testIsMatchTrue() {
        NGram guess = NGram.from("test");
        Map<IndexedCharacter, GuessResult.MatchType> resultMap = Map.of(
            new IndexedCharacter(0, 't'), GuessResult.MatchType.EXACT,
            new IndexedCharacter(1, 'e'), GuessResult.MatchType.EXACT,
            new IndexedCharacter(2, 's'), GuessResult.MatchType.EXACT,
            new IndexedCharacter(3, 't'), GuessResult.MatchType.EXACT
        );

        GuessResult guessResult = GuessResult.of(guess, resultMap);

        assertTrue(guessResult.isMatch());
    }

    @Test
    public void testIsMatchFalse() {
        NGram guess = NGram.from("test");
        Map<IndexedCharacter, GuessResult.MatchType> resultMap = Map.of(
            new IndexedCharacter(0, 't'), GuessResult.MatchType.EXACT,
            new IndexedCharacter(1, 'e'), GuessResult.MatchType.PARTIAL,
            new IndexedCharacter(2, 's'), GuessResult.MatchType.NONE,
            new IndexedCharacter(3, 't'), GuessResult.MatchType.EXACT
        );

        GuessResult guessResult = GuessResult.of(guess, resultMap);

        assertTrue(!guessResult.isMatch());
    }

    @Test
    public void testGetMatchType() {
        NGram guess = NGram.from("test");
        Map<IndexedCharacter, GuessResult.MatchType> resultMap = Map.of(
            new IndexedCharacter(0, 't'), GuessResult.MatchType.EXACT,
            new IndexedCharacter(1, 'e'), GuessResult.MatchType.PARTIAL,
            new IndexedCharacter(2, 's'), GuessResult.MatchType.NONE,
            new IndexedCharacter(3, 't'), GuessResult.MatchType.EXACT
        );

        GuessResult guessResult = GuessResult.of(guess, resultMap);

        assertEquals(GuessResult.MatchType.EXACT, guessResult.getMatchType(0));
        assertEquals(GuessResult.MatchType.PARTIAL, guessResult.getMatchType(1));
        assertEquals(GuessResult.MatchType.NONE, guessResult.getMatchType(2));
        assertEquals(GuessResult.MatchType.EXACT, guessResult.getMatchType(3));
    }

    @Test
    public void testToString() {
        NGram guess = NGram.from("test");
        Map<IndexedCharacter, GuessResult.MatchType> resultMap = Map.of(
            new IndexedCharacter(0, 't'), GuessResult.MatchType.EXACT,
            new IndexedCharacter(1, 'e'), GuessResult.MatchType.PARTIAL,
            new IndexedCharacter(2, 's'), GuessResult.MatchType.NONE,
            new IndexedCharacter(3, 't'), GuessResult.MatchType.EXACT
        );

        GuessResult guessResult = GuessResult.of(guess, resultMap);

        String expected = "Guess: test\nResult: 0-t: EXACT, 1-e: PARTIAL, 2-s: NONE, 3-t: EXACT, ";
        assertEquals(expected, guessResult.toString());
    }

    @Test
    public void testMerge() {
        NGram guess = NGram.from("test");
        Map<IndexedCharacter, GuessResult.MatchType> resultMap1 = Map.of(
            new IndexedCharacter(0, 't'), GuessResult.MatchType.EXACT,
            new IndexedCharacter(1, 'e'), GuessResult.MatchType.PARTIAL
        );
        Map<IndexedCharacter, GuessResult.MatchType> resultMap2 = Map.of(
            new IndexedCharacter(2, 's'), GuessResult.MatchType.NONE,
            new IndexedCharacter(3, 't'), GuessResult.MatchType.EXACT
        );

        GuessResult guessResult1 = GuessResult.of(guess, resultMap1);
        GuessResult guessResult2 = GuessResult.of(guess, resultMap2);

        GuessResult mergedResult = guessResult1.merge(guessResult2);

        Map<IndexedCharacter, GuessResult.MatchType> expectedMap = Map.of(
            new IndexedCharacter(0, 't'), GuessResult.MatchType.EXACT,
            new IndexedCharacter(1, 'e'), GuessResult.MatchType.PARTIAL,
            new IndexedCharacter(2, 's'), GuessResult.MatchType.NONE,
            new IndexedCharacter(3, 't'), GuessResult.MatchType.EXACT
        );

        assertEquals(expectedMap, mergedResult.getResultMap());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMergeWithDifferentGuesses() {
        NGram guess1 = NGram.from("test");
        NGram guess2 = NGram.from("fail");
        Map<IndexedCharacter, GuessResult.MatchType> resultMap1 = Map.of(
            new IndexedCharacter(0, 't'), GuessResult.MatchType.EXACT
        );
        Map<IndexedCharacter, GuessResult.MatchType> resultMap2 = Map.of(
            new IndexedCharacter(0, 'f'), GuessResult.MatchType.NONE
        );

        GuessResult guessResult1 = GuessResult.of(guess1, resultMap1);
        GuessResult guessResult2 = GuessResult.of(guess2, resultMap2);

        guessResult1.merge(guessResult2);
    }
}
