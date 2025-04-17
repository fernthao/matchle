package thao.matchle;

import java.util.*;
import java.util.function.ToLongFunction;
import java.util.stream.*;
import java.lang.IllegalStateException;
import java.util.function.Function;

// Corpus class
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

    // Methods
    public Set<NGram> getCorpus() {
        return this.corpus;
    }
    
    public int size() {
        return corpus.size();
    }

    public boolean contains(NGram n) {
        return corpus.contains(n);
    }

    public java.util.Iterator<NGram> iterator() {
        return corpus.iterator();
    }
    
    public Stream<NGram> stream() {
        return corpus.stream();
    }

    // Returns a copy of the private variable
    public Set<NGram> corpus() {
        return new HashSet<>(corpus);
    }

    public int wordSize() {
        return wordSize;
    }

    public boolean isEmpty() {
        return corpus.isEmpty();
    }

    // Builder class
    public static final class Builder {
        private Set<NGram> ngrams;
        public static final Builder EMPTY = new Builder(new HashSet<NGram>());

        // Constructor
        private Builder(Set<NGram> ngrams) {
            assert ngrams != null;
            this.ngrams = ngrams;
        }

        public static final Builder of(Corpus corpus) {
            Objects.requireNonNull(corpus);
            return new Builder(corpus.corpus());
        }

        // Methods
        public Builder add(NGram n) {
            Objects.requireNonNull(n);
            ngrams.add(n);
            return this;
        }
        
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

        public boolean isConsistent(Integer wordSize) {
            Objects.requireNonNull(wordSize);
            for (NGram n : ngrams) {
                    if (n.size() != wordSize) return false;
                }
                return true;
        }

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
