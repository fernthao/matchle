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

    // Returns the number of n-grams consistent with the filter. 
    public long size(Filter filter) {
        Objects.requireNonNull(filter);
        return corpus.stream().filter(ngram -> filter.test(ngram)).count();
    }

    // Method to calculate corpus score
    public long score(NGram key, NGram guess) {
        if (corpus.isEmpty()) {
            throw new IllegalStateException("Can not calculate the score of an empty corpus.");
        }
        Objects.requireNonNull(key);
        Objects.requireNonNull(guess);
        return size(NGramMatcher.of(key, guess).match());
    }

    private long score(NGram guess, Function<LongStream, Long> aggregator) {
        assert guess != null;
        assert aggregator != null;

        return aggregator.apply(corpus.stream()
                         .mapToLong(key -> score(key, guess)));
    }

    // Maximum score of guess among all corpus’ n-grams
    public long scoreWorstCase(NGram guess) {
        Objects.requireNonNull(guess);
        if (corpus.isEmpty()) {
            throw new IllegalStateException("Can not calculate the score of an empty corpus.");
        }
        return score(guess, stream -> stream.max().orElseThrow(() -> new IllegalStateException("No maximum value found")));
    }

    // Sum of scores of guess among all corpus’ n-grams.
    public long scoreAverageCase(NGram guess) {
        Objects.requireNonNull(guess);
        if (corpus.isEmpty()) {
            throw new IllegalStateException("Can not calculate the score of an empty corpus.");
        }
        return score(guess, LongStream::sum);
    }

    public NGram bestGuess(ToLongFunction<NGram> criterion) {
        Objects.requireNonNull(criterion);
        long minScore = corpus.stream()
                              .mapToLong(key -> criterion.applyAsLong(key))
                              .min().getAsLong();
        
        // return an ngram with minScore                      
        return corpus.stream()
                     .filter(key -> criterion.applyAsLong(key) == minScore)
                     .collect(Collectors.toList())
                     .get(0);
    }

    public NGram bestWorstCaseGuess(NGram guess) {
        Objects.requireNonNull(guess);
        return bestGuess(ngram -> scoreWorstCase(ngram));
    }

    public NGram bestAverageCaseGuess(NGram guess) {
        Objects.requireNonNull(guess);
        return bestGuess(ngram -> scoreAverageCase(ngram));
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

        // returns a builder with the n-grams that are consistent with the filter
        public Builder filter(Filter filter) {
            Objects.requireNonNull(filter);
            ngrams = ngrams.stream().filter(ngram -> filter.test(ngram)).collect(Collectors.toSet());
            return this;
        }
    }
}
