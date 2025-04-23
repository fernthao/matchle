package thao.matchle;

/**
 * A record that represents a character with its index in a string.
 * Used to keep track of the position of each character in the string.
 * @param index the index of the character in the string
 * @param character the character at the given index
 */
public record IndexedCharacter(int index, Character character) {
    /**
     * @return the string representation of the IndexedCharacter.
     * The format is "index-character", where index is the position of the character in the string.
     * For example, if the character is 'a' at index 0, it will return "0-a".
     */
    @Override
    public String toString() {
        return String.format("%d-%s", index, character);
    }
}