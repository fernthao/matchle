package thao.matchle;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

public final class Filter {
    private final Predicate<NGram> predicate;
    public static final Filter FALSE = new Filter (ngram -> false);

    // Constructors
    private Filter (Predicate<NGram> predicate) {
        assert predicate != null;
        
        this.predicate = predicate;
    }

    public static Filter from (Predicate<NGram> predicate) {
        Objects.requireNonNull(predicate);
        return new Filter(predicate);
    }

    // Methods
    public boolean test(NGram n) {
        Objects.requireNonNull(n);
        return predicate.test(n);
    }

    public Filter and (Optional<Filter> other) {
        return  other.map(otherFilter -> from(this.predicate.and(otherFilter.predicate)))
                .orElse(this);
    }
}
