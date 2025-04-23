package thao.matchle;

import java.util.Map;
import java.util.Objects;

/**
 * GuessResult class represents the result of a guess in a word game.
 * It contains the guessed n-gram and a map of indexed characters to their match types.
 */
class GuessResult {
    private NGram guess;
    private Map<IndexedCharacter, MatchType> resultMap;

    /**
     * MatchType enum represents the type of match for a character in the guess.
     * It can be EXACT, PARTIAL, or NONE.
     */
    public enum MatchType {
        /**
         * Exact match: the character is in the key at the same index.
         */
        EXACT, 
        /**
         * Partial match: the character appears in the key but at a different index.
         */
        PARTIAL, 
        /**
         * No match: the character does not appear in the key.
         */
        NONE
    }

    /**
     * Constructor for GuessResult.
     * @param guess the guessed n-gram
     * @param resultMap a map of indexed characters to their match types
     */
    private GuessResult(NGram guess, Map<IndexedCharacter, MatchType> resultMap) {
        assert guess != null;
        assert resultMap != null;
        this.guess = guess;
        this.resultMap = resultMap;
    }

    /**
     * Factory method to create a GuessResult.
     * @param guess the guessed n-gram
     * @param resultMap a map of indexed characters to their match types
     * @return a new GuessResult instance
     */
    public static GuessResult of(NGram guess, Map<IndexedCharacter, MatchType> resultMap) {
        Objects.requireNonNull(guess);
        Objects.requireNonNull(resultMap);
        return new GuessResult(guess, resultMap);
    }

    /**
     * Checks if the all the characters in the guess exactly matches the target n-gram.
     * @return true if all characters match exactly, false otherwise
     */
    public boolean isMatch() {
        for (MatchType matchType : resultMap.values()) {
            if (matchType != MatchType.EXACT) {
                return false;
            }
        }
        return true;
    }

    /**
     * Getter for the guessed n-gram.
     * @return the guessed n-gram
     */
    public NGram getGuess() {
        return guess;
    }

    /**
     * Getter for the result map.
     * @return the map of indexed characters to their match types
     */
    public Map<IndexedCharacter, MatchType> getResultMap() {
        return resultMap;
    }

    /**
     * Gets the match type for a specific index in the guess.
     * @param index the index of the character in the guess
     * @return the match type for the character at the specified index
     */
    public MatchType getMatchType(int index) {
        return resultMap.get(new IndexedCharacter(index, guess.get(index)));
    }
    
    /**
     * Returns a string representation of the GuessResult, showing each character and its match type.
     * @return a string representation of the GuessResult
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Guess: ").append(guess).append("\n");
        sb.append("Result: ");
        // for (Map.Entry<IndexedCharacter, MatchType> entry : resultMap.entrySet()) {
        //     sb.append(entry.getKey()).append(": ").append(entry.getValue()).append(", ");
        // }
        
        for (int i = 0; i < guess.size(); i++) {
            IndexedCharacter key = new IndexedCharacter(i, guess.get(i)); // or however it's constructed
            MatchType value = resultMap.get(key);
            sb.append(key).append(": ").append(value).append(", ");
        }
        return sb.toString();
    }

    /**
     * Merges this GuessResult with another GuessResult.
     * @param other the other GuessResult to merge with
     * @return a new GuessResult that is the result of merging this and the other GuessResult
     */
    public GuessResult merge(GuessResult other) {
        Objects.requireNonNull(other);
        if (!this.guess.equals(other.guess)) {
            throw new IllegalArgumentException("Cannot merge GuessResults with different guesses");
        }
        
        Map<IndexedCharacter, MatchType> mergedMap = new java.util.HashMap<>(this.resultMap);

        for (Map.Entry<IndexedCharacter, MatchType> entry : other.resultMap.entrySet()) {
            mergedMap.put(entry.getKey(), entry.getValue());
        }
        return new GuessResult(this.guess, mergedMap);
    }
}
