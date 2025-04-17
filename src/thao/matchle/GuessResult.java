package thao.matchle;

import java.util.Map;

class GuessResult {
    private NGram guess;
    Map<IndexedCharacter, MatchType> resultMap;

    public enum MatchType {
        EXACT, PARTIAL, NONE
    }

    // TODO: make this constructor private, create public of method
    public GuessResult(NGram guess, Map<IndexedCharacter, MatchType> resultMap) {
        assert guess != null;
        assert resultMap != null;
        this.guess = guess;
        this.resultMap = resultMap;
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
}
