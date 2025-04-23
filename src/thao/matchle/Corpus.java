package thao.matchle;

import java.util.*;
import java.util.stream.*;

/**
 * Corpus class represents a collection of NGrams and provides methods to manipulate and query the corpus.
 * It implements Iterable interface on type NGram to allow iteration over the NGrams in the corpus.
 */
public final class Corpus implements Iterable<NGram> {
    private final Set<NGram> corpus;
    private int wordSize;

    // Constructor
    private Corpus(Set<NGram> corpus) {
        assert corpus != null;
        this.corpus = Set.copyOf(corpus);
        assert corpus.iterator().hasNext(); 
        this.wordSize = corpus.iterator().next().size();
    }
    
    /**
     * Getter for wordSize
     * @return the size of the words in the corpus
     */
    public int size() {
        return corpus.size();
    }

    /**
     * Checks if the corpus contains a specific NGram.
     * @param n the NGram to check
     * @return true if the corpus contains the NGram, false otherwise
     */
    public boolean contains(NGram n) {
        return corpus.contains(n);
    }

    /**
     * Iterator object to traverse the corpus.
     * @return an iterator over the NGrams in the corpus
     */
    public java.util.Iterator<NGram> iterator() {
        return corpus.iterator();
    }
    
    /**
     * Allows for traversal of the corpus using a stream.
     * @return a stream of NGrams in the corpus
     */
    public Stream<NGram> stream() {
        return corpus.stream();
    }

    /**
     * Getter for the corpus.
     * @return a copy of the private variable corpus.
     */
    public Set<NGram> corpus() {
        return new HashSet<>(corpus);
    }

    /**
     * Getter for wordSize.
     * @return the size of the words in the corpus.
     */
    public int wordSize() {
        return wordSize;
    }

    /**
     * Check if the corpus is empty.
     * @return true if the corpus is empty, false otherwise.
     */
    public boolean isEmpty() {
        return corpus.isEmpty();
    }

    /**
     * Builder class for Corpus.
     * A companion class that provides methods to build a Corpus object.
     */
    public static final class Builder {
        /**
         * Set of NGrams in the builder.
         */
        private Set<NGram> ngrams;

        /**
         * Empty builder object.
         */
        public static final Builder EMPTY = new Builder(new HashSet<NGram>());

        // Constructor
        private Builder(Set<NGram> ngrams) {
            assert ngrams != null;
            this.ngrams = ngrams;
        }

        /**
         * Creates a new Builder object from an existing Corpus.
         * @param corpus the Corpus to copy
         * @return a new Builder object
         */
        public static final Builder of(Corpus corpus) {
            Objects.requireNonNull(corpus);
            return new Builder(corpus.corpus());
        }

        /**
         * Add a new NGram to the list of ngrams inside the builder.
         * @param n the NGram to add
         * @return the Builder object
         * @throws NullPointerException if n is null
         */
        public Builder add(NGram n) {
            Objects.requireNonNull(n);
            ngrams.add(n);
            return this;
        }
        
        /**
         * Add a collection of NGrams to the list of ngrams inside the builder.
         * @param nCollection the collection of NGrams to add
         * @return the Builder object
         * @throws NullPointerException if nCollection is null
         */
        public Builder addAll(Collection<NGram> nCollection) {
            Objects.requireNonNull(nCollection);
            for (NGram n : nCollection) {
                if (n != null) ngrams.add(n);
                else 
                // null NGram are not added to ngrams
                ;
            }
            return this;
        }

        /**
         * Check if the NGrams in the builder are consistent with a given word size.
         * @param wordSize the word size to check against
         * @return true if all NGrams in the builder are consistent with the given word size, false otherwise
         * @throws NullPointerException if wordSize is null
         */
        public boolean isConsistent(Integer wordSize) {
            Objects.requireNonNull(wordSize);
            for (NGram n : ngrams) {
                    if (n.size() != wordSize) return false;
                }
                return true;
        }

        /**
         * Build a new Corpus object from the NGrams in the builder.
         * @return a new Corpus object
         * @throws IllegalStateException if the NGrams in the builder are not consistent with the given word size
        */
        public Corpus build() {
            Corpus result = null;

            assert ngrams.iterator().hasNext(); 
            int wordSize = ngrams.iterator().next().size();

            if (this.isConsistent(wordSize)) {
                result = new Corpus(ngrams);
            }
            return result;
        } 
    }
}
