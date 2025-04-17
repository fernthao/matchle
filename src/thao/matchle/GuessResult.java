package thao.matchle;

import java.util.Map;
import java.util.Objects;

class GuessResult {
    private NGram guess;
    Map<IndexedCharacter, MatchType> resultMap;

    public enum MatchType {
        EXACT, PARTIAL, NONE
    }

    private GuessResult(NGram guess, Map<IndexedCharacter, MatchType> resultMap) {
        assert guess != null;
        assert resultMap != null;
        this.guess = guess;
        this.resultMap = resultMap;
    }
    public static GuessResult of(NGram guess, Map<IndexedCharacter, MatchType> resultMap) {
        Objects.requireNonNull(guess);
        Objects.requireNonNull(resultMap);
        return new GuessResult(guess, resultMap);
    }

    public boolean isMatch() {
        for (MatchType matchType : resultMap.values()) {
            if (matchType != MatchType.EXACT) {
                return false;
            }
        }
        return true;
    }
    public NGram getGuess() {
        return guess;
    }
    public Map<IndexedCharacter, MatchType> getResultMap() {
        return resultMap;
    }
    public MatchType getMatchType(int index) {
        return resultMap.get(new IndexedCharacter(index, guess.get(index)));
    }
    public GuessResult merge(GuessResult other) {
        Objects.requireNonNull(other);
        if (this.guess.equals(other.guess)) {
            return this;
        }
        if (!this.guess.equals(other.guess)) {
            throw new IllegalArgumentException("Cannot merge GuessResults with different guesses");
        }
        Map<IndexedCharacter, MatchType> mergedMap = this.resultMap;
        for (Map.Entry<IndexedCharacter, MatchType> entry : other.resultMap.entrySet()) {
            mergedMap.put(entry.getKey(), entry.getValue());
        }
        return new GuessResult(this.guess, mergedMap);
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Guess: ").append(guess).append("\n");
        sb.append("Result: ");
        for (Map.Entry<IndexedCharacter, MatchType> entry : resultMap.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append(", ");
        }
        return sb.toString();
    }
}
