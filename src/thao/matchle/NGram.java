package thao.matchle;

import java.util.*;
import java.util.stream.*;

// NGram
public class NGram implements Iterable<IndexedCharacter>{
    private final List<Character> ngram;
    private final Set<Character> charset;

    // Constructors
    private NGram(ArrayList<Character> ngram, HashSet<Character> charset) {
        assert ngram != null;
        this.ngram = ngram;
        this.charset = charset;
    }

    // returns a new NGram from a copy of the argument
    public static final NGram from(List<Character> word) {
        NullCharacterException.validate(word);
        
        // Copy word into ngram, charset
        ArrayList<Character> ngram = new ArrayList<>(word);
        HashSet<Character> charset = new HashSet<>(word);

        return new NGram(ngram, charset);
    }

    // returns a new NGram from the characters in the argument
    public static final NGram from(String word) {
        // Convert String into List
        List<Character> charList = word.chars().mapToObj(c -> Character.valueOf((char) c)).collect(Collectors.toList());
        NullCharacterException.validate(charList);

        return from(charList);
    }

    // Methods
    public Character get(int i) {
        return ngram.get(i); 
    }

    public int size() {
        return ngram.size();
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() == NGram.class) {
            NGram n = NGram.class.cast(o);
            return (this.ngram.equals(n.ngram));
        }
        else return false;
    }

    @Override
    public int hashCode() {
        return ngram.hashCode();
    }

    // returns whether c’s character appears at the c’s index
    public boolean matches(IndexedCharacter c)  {
        Objects.requireNonNull(c);
        return c.character().equals(ngram.get(c.index()));
    }    

    // returns whether c appears anywhere in the n-gram
    public boolean contains(IndexedCharacter c) {
        Objects.requireNonNull(c);
        return ngram.contains(c.character());
    }

    // returns whether c’s character appears in the n-gram at an index different than c’s
    public boolean containsElseWhere(IndexedCharacter c) {
        Objects.requireNonNull(c);
        return this.contains(c) && !this.matches(c);
    }

    // Traversal
    public Stream<IndexedCharacter> stream() {
        return  IntStream
                .range(0, size())
                .mapToObj(i -> new IndexedCharacter(i, get(i))); 
    }

    public  java.util.Iterator<IndexedCharacter> iterator() {
        return new Iterator(this);
    }

    public final class Iterator implements java.util.Iterator<IndexedCharacter> {
        private int index;

        public Iterator (NGram ngramObj) {
            index = 0;
        }

        public boolean hasNext() {
            return index < ngram.size();
        }

        public IndexedCharacter next() {
            IndexedCharacter indexedCharacter = new IndexedCharacter(index, ngram.get(index));
            index++;
            return indexedCharacter;
        }
    }

    // Error handling
    public static final class NullCharacterException extends Exception {
        // Location of problematic character
        private final int index;

        // TOASK: Why Serialization?
        public static final long serialVersionUID = 42L;

        public int getIndex() {
            return index;
        } 

        // Constructor
        public NullCharacterException(int index) {
            if (index < 0) {
                throw new IllegalArgumentException("Index must be non-negative");
            }
            this.index = index;
        }

        // Methods
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

