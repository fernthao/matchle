package thao.matchle;

import org.junit.Test;

import static org.junit.Assert.*;
import java.util.HashMap;
import java.util.Map;

public class GameTest {

    @Test
    public void testGameInitialization() {
        Game game = Game.from("wordlist.txt", 5, 6);
        assertNotNull(game.getHistory());
        assertTrue(game.getHistory().isEmpty());
    }

    @Test
    public void testIsOverWhenMaxAttemptsReached() {
        Game game = Game.from("wordlist.txt", 5, 1);
        Map<IndexedCharacter, GuessResult.MatchType> resultMap = new HashMap<>();
        resultMap.put(new IndexedCharacter(0, 'a'), GuessResult.MatchType.NONE);
        game.getHistory().add(GuessResult.of(NGram.from("apple"), resultMap)); // Simulate one guess
        assertTrue(game.isOver());
    }

    @Test
    public void testIsOverWhenWinConditionMet() {
        Game game = Game.from("wordlist.txt", 5, 6);
        Map<IndexedCharacter, GuessResult.MatchType> resultMap = new HashMap<>();
        resultMap.put(new IndexedCharacter(0, 'a'), GuessResult.MatchType.EXACT);
        resultMap.put(new IndexedCharacter(1, 'p'), GuessResult.MatchType.EXACT);
        resultMap.put(new IndexedCharacter(2, 'p'), GuessResult.MatchType.EXACT);
        resultMap.put(new IndexedCharacter(3, 'l'), GuessResult.MatchType.EXACT);
        resultMap.put(new IndexedCharacter(4, 'e'), GuessResult.MatchType.EXACT);
        game.getHistory().add(GuessResult.of(NGram.from("apple"), resultMap)); // Simulate a winning guess
        assertTrue(game.isOver());
    }

    @Test
    public void testIsWin() {
        Game game = Game.from("wordlist.txt", 5, 6);
        Map<IndexedCharacter, GuessResult.MatchType> resultMap = new HashMap<>();
        resultMap.put(new IndexedCharacter(0, 'a'), GuessResult.MatchType.EXACT);
        resultMap.put(new IndexedCharacter(1, 'p'), GuessResult.MatchType.EXACT);
        resultMap.put(new IndexedCharacter(2, 'p'), GuessResult.MatchType.EXACT);
        resultMap.put(new IndexedCharacter(3, 'l'), GuessResult.MatchType.EXACT);
        resultMap.put(new IndexedCharacter(4, 'e'), GuessResult.MatchType.EXACT);
        game.getHistory().add(GuessResult.of(NGram.from("apple"), resultMap)); // Simulate a winning guess
        assertTrue(game.isWin());
    }

    @Test
    public void testGetFeedback() {
        Game game = Game.from("wordlist.txt", 5, 6);
        Map<IndexedCharacter, GuessResult.MatchType> resultMap = new HashMap<>();
        resultMap.put(new IndexedCharacter(0, 'a'), GuessResult.MatchType.NONE);
        game.getHistory().add(GuessResult.of(NGram.from("apple"), resultMap)); // Simulate a guess
        game.getFeedback(); // Should not throw any exceptions
    }

    @Test
    public void testLoadCorpusWithInvalidFile() {
        try {
            Game.from("invalid_file.txt", 5, 6);
            fail("Expected RuntimeException due to invalid file path.");
        } catch (RuntimeException e) {
            assertTrue(e.getMessage().contains("Error loading corpus from file"));
        }
    }

    @Test
    public void testKeyGeneration() {
        Game game = Game.from("wordlist.txt", 5, 6);
        assertNotNull(game.getKey()); // Ensure key is generated
    }

    @Test
    public void testHistoryUpdatesAfterGuess() {
        Game game = Game.from("wordlist.txt", 5, 6);
        Map<IndexedCharacter, GuessResult.MatchType> resultMap = new HashMap<>();
        resultMap.put(new IndexedCharacter(0, 'a'), GuessResult.MatchType.NONE);
        game.getHistory().add(GuessResult.of(NGram.from("apple"), resultMap)); // Simulate a guess
        assertEquals(1, game.getHistory().size());
    }

    @Test
    public void testCorpusLoadedCorrectly() {
        Game game = Game.from("wordlist.txt", 5, 6);
        assertNotNull(game.getCorpus());
        assertFalse(game.getCorpus().isEmpty());
    }

    @Test
    public void testMaxAttemptsSetCorrectly() {
        Game game = Game.from("wordlist.txt", 5, 6);
        assertEquals(6, game.getMaxAttempts());
    }

    @Test
    public void testKeyIsFromCorpus() {
        Game game = Game.from("wordlist.txt", 5, 6);
        assertTrue(game.getCorpus().contains(game.getKey()));
    }

    @Test
    public void testBestWorstCaseGuess() {
        Game game = Game.from("wordlist.txt", 5, 6);
        NGram bestGuess = game.bestWorstCaseGuess();
        assertNotNull(bestGuess);
        assertTrue(game.getCorpus().contains(bestGuess));
    }
    @Test
    public void testBestAverageCaseGuess() {
        Game game = Game.from("wordlist.txt", 5, 6);
        NGram bestGuess = game.bestAverageCaseGuess();
        assertNotNull(bestGuess);
        assertTrue(game.getCorpus().contains(bestGuess));
    }
}