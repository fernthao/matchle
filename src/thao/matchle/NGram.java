package thao.matchle;

import java.util.*;
import java.util.stream.*;

/**
 * NGram class represents a sequence of characters (n-gram) and provides methods to manipulate and query the n-gram.
 * It implements Iterable interface on IndexedCharacter type to allow iteration over the characters in the n-gram.
 * 
 */
public class NGram implements Iterable<IndexedCharacter>{
    private final List<Character> ngram;

    /**
     * Constructor for NGram.
     * @param ngram
     * @param charset
     */
    private NGram(ArrayList<Character> ngram) {
        assert ngram != null;
        this.ngram = ngram;
    }

    /**
     * Creates a new NGram from a copy of a list of characters.
     * @param word the list of characters.
     * @return a new NGram object created from a copy of the list of characters.
     * @throws NullPointerException if word is null.
     */
    public static final NGram from(List<Character> word) {
        NullCharacterException.validate(word);
        
        // Copy word into ngram
        ArrayList<Character> ngram = new ArrayList<>(word);
        return new NGram(ngram);
    }

    /**
     * Creates a new NGram from a string.
     * @param word the string to convert into an n-gram.
     * @return a new NGram object created from the string.
     */
    public static final NGram from(String word) {
        // Convert String into List
        List<Character> charList = word.chars().mapToObj(c -> Character.valueOf((char) c)).collect(Collectors.toList());
        NullCharacterException.validate(charList);

        return from(charList);
    }

    /**
     * Getter method to retrieve a character in n-gram.
     * @param i the index of the character to retrieve.
     * @return the character at the specified index.
     */
    public Character get(int i) {
        return ngram.get(i); 
    }

    /**
     * Getter method to retrieve the number of characters in an n-gram.
     * @return the length of the n-gram.
     */
    public int size() {
        return ngram.size();
    }

    /**
     * @return the n-gram as a string.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (char c : ngram) {
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * @param o the other NGram to compare with.
     * @return true if the ngram is the same as the other ngram, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NGram n = (NGram) o;
        return this.ngram.equals(n.ngram);
    }
    
    // The hashSet uses the hashCode method to determine if two objects are the same.
    // If two objects are the same, they must have the same hashCode.
    @Override
    public int hashCode() {
        return Objects.hash(ngram);
    }
    
    /**
     * Checks if the character at the specified index matches the character in the n-gram at the same index.
     * @param c the IndexedCharacter to check.
     * @throws NullPointerException if c is null.
     * @return true if the character at the specified index matches the character in the n-gram at the same index, false otherwise.
     */
    public boolean matches(IndexedCharacter c)  {
        Objects.requireNonNull(c);
        return c.character().equals(ngram.get(c.index()));
    }    

    /**
     * Check whether an IndexedCharacter appears anywhere in the n-gram
     * @param c the IndexedCharacter to check.
     * @throws NullPointerException if c is null.
     * @return true if the character appears in the n-gram, false otherwise.
     */
    public boolean contains(IndexedCharacter c) {
        Objects.requireNonNull(c);
        return ngram.contains(c.character());
    }

    /**
     * Check whether a character appears anywhere in the n-gram
     * @param c the Character to check.
     * @throws NullPointerException if c is null.
     * @return true if the character appears in the n-gram, false otherwise.
     */
    public boolean contains(Character c) {
        Objects.requireNonNull(c);
        return ngram.contains(c);
    }

    /**
     * Check whether an IndexedCharacter appears in the n-gram at an index different than its own.
     * @param c the IndexedCharacter to check.
     * @throws NullPointerException if c is null.
     * @return true if the character appears in the n-gram at an index different than its own, false otherwise.
     */
    public boolean containsElseWhere(IndexedCharacter c) {
        Objects.requireNonNull(c);
        return this.contains(c) && !this.matches(c);
    }

    /**
     * Allow for traversal of the n-gram using a stream.
     * @return a stream of IndexedCharacter objects.
     */
    public Stream<IndexedCharacter> stream() {
        return  IntStream
                .range(0, size())
                .mapToObj(i -> new IndexedCharacter(i, get(i))); 
    }

    /**
     * Allow for traversal of the n-gram using an iterator.
     * @return an iterator over the IndexedCharacter objects in the n-gram.
     */
    public java.util.Iterator<IndexedCharacter> iterator() {
        return new Iterator(this);
    }

    /**
     * Iterator class for NGram.
     * Allows for traversal of the n-gram using an iterator.
     */
    public final class Iterator implements java.util.Iterator<IndexedCharacter> {
        private int index;

        /**
         * Constructor for Iterator, which start the index at 0.
         * @param ngramObj the NGram object to iterate over.
         */
        Iterator (NGram ngramObj) {
            assert ngramObj != null;
            index = 0;
        }

        /**
         * Checks if there are more characters in the n-gram.
         * @return true if there are more characters, false otherwise.
         * @throws NullPointerException if ngram is null.
         * @throws IllegalStateException if index is negative or out of bounds.
         */
        public boolean hasNext() {
            Objects.requireNonNull(ngram);
            if (index < 0) {
                throw new IllegalStateException("Index must be non-negative");
            }
            if (index >= ngram.size()) {
                throw new IllegalStateException("Index out of bounds");
            }
            return index < ngram.size();
        }

        /**
         * Returns the next IndexedCharacter in the n-gram.
         * @return the next IndexedCharacter.
         * @throws NoSuchElementException if there are no more characters.
         * @throws NullPointerException if ngram is null.
         */
        public IndexedCharacter next() {
            Objects.requireNonNull(ngram);
            if (!hasNext()) {
                throw new NoSuchElementException("No more elements in the iterator");
            }
            IndexedCharacter indexedCharacter = new IndexedCharacter(index, ngram.get(index));
            index++;
            return indexedCharacter;
        }
    }

    /**
     * Exception class for handling null characters in the n-gram.
     * This exception is thrown when a null character is found in the n-gram.
     */
    public static final class NullCharacterException extends Exception {
        /**
         * Location of the problematic character
         */
        private final int index;

        /**
         * Serial version UID for serialization.
         */
        public static final long serialVersionUID = 42L;

        /**
         * Getter method to retrieve the index of the null character.
         * @return the index of the null character.
         */
        public int getIndex() {
            return index;
        } 

        /**
         * Constructor for NullCharacterException.
         * @param index the index of the null character.
         * @throws IllegalArgumentException if index is negative.
         */
        public NullCharacterException(int index) {
            if (index < 0) {
                throw new IllegalArgumentException("Index must be non-negative");
            }
            this.index = index;
        }

        /**
         * Validates the input list of characters.
         * @param ngram the list of characters to validate.
         * @throws NullPointerException if ngram is null.
         * @throws IllegalArgumentException if ngram contains null characters.
         * @return the valid list of characters.
         */
        public static final List<Character> validate(List<Character> ngram) {
            Objects.requireNonNull(ngram);
            int index = -1;
            for (int i = 0; i < ngram.size(); i++) {
                if (ngram.get(i) == null) {
                    index = i;
                    throw new IllegalArgumentException("Input can not contain null characters.", new NullCharacterException(index) );
                }
            }
            return ngram;
        }
    }
}

