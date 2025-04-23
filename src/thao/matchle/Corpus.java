package thao.matchle;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.*;

/**
 * Corpus class represents a collection of NGrams and provides methods to manipulate and query the corpus.
 * It implements Iterable interface on type NGram to allow iteration over the NGrams in the corpus.
 */
public final class Corpus implements Iterable<NGram> {
    /**
     * Logger object to log messages.
     */
    private static final Logger logger = Logger.getLogger(Corpus.class.getName());
    /**
     * Set of NGrams in the corpus.
     */
    private final Set<NGram> corpus;
    /**
     * The size of the words in the corpus.
     */
    private int wordSize;

    // Constructor
    private Corpus(Set<NGram> corpus) {
        assert corpus != null;
        this.corpus = Set.copyOf(corpus);
        Iterator<NGram> it = corpus.iterator();
        assert it.hasNext(); 
        this.wordSize = it.next().size();; 
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
     * Return a string representation of the corpus
     * @return a string representation of the corpus
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (NGram n : corpus) {
            sb.append(n.toString()).append(", ");
        }
        return sb.toString();
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
         * The size of the words in the corpus.
         */
        private int wordSize;

        /**
         * Empty builder object.
         * @param wordSize the size of the words in the corpus
         * @return a new empty Builder object with the set wordSize
         * @throws IllegalArgumentException if wordSize is negative
         */
        public static final Builder empty(int wordSize) {
            if (wordSize < 0) {
                throw new IllegalArgumentException("Word size must be non-negative");
            }
            return new Builder(new HashSet<NGram>(), wordSize);
        }

        // Constructor
        private Builder(Set<NGram> ngrams, int wordSize) {
            assert ngrams != null;
            this.ngrams = ngrams;
            this.wordSize = wordSize;
        }

        /**
         * Creates a new Builder object from an existing Corpus.
         * @param corpus the Corpus to copy
         * @return a new Builder object
         */
        public static final Builder of(Corpus corpus) {
            Objects.requireNonNull(corpus);
            return new Builder(corpus.corpus(), corpus.wordSize());
        }

        /**
         * Add a new NGram to the list of ngrams inside the builder.
         * @param n the NGram to add
         * @return the Builder object
         * @throws NullPointerException if n is null
         * @throws IllegalStateException if n is not consistent with the word size
         */
        public Builder add(NGram n) {
            if (isValid(n)) {
                ngrams.add(n);
            } else {
                // empty since logging is done in isValid
            }
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
                if (!isValid(n)) {
                    // Log a warning if an invalid NGram is found and continue
                    // Favor logging over throwing an exception (robustness over correctness)
                    continue;
                }
                ngrams.add(n);
            }
            return this;
        }

        /**
         * Get the word size for the builder.
         * @return the word size
         */
        public int wordSize() {
            return wordSize;
        }

        /**
         * Check if the NGram is valid and ready to be added to the list of ngrams.
         * @param n the NGram to check
         * @return true if the NGram is valid, false otherwise
         */
        public boolean isValid(NGram n) {
            // Log a warning if an invalid NGram is found
            // Favor logging over throwing an exception (robustness over correctness)
            if (n == null) {
                logger.log(Level.WARNING, "Null NGram found in collection");
                return false;
            }
            if (n.size() != wordSize) {
                logger.log(Level.WARNING, "NGram size is not consistent with the word size");
                return false;
            }
            return true;
        }

        /**
         * Build a new Corpus object from the NGrams in the builder.
         * @return a new Corpus object
         * @throws IllegalStateException if the builder is empty
        */
        public Corpus build() {
            Objects.requireNonNull(ngrams);
            if (ngrams.isEmpty()) {
                throw new IllegalStateException("Cannot build an empty corpus");
            }
            return new Corpus(ngrams);
        } 
    }
}
