package thao.matchle;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.function.Predicate;

class FilterTest {

    @Test
    void testFalseFilter() {
        Filter falseFilter = Filter.FALSE;
        NGram ngram = NGram.from("test");
        
        assertFalse(falseFilter.test(ngram));
    }
    
    @Test
    void testFilterFrom() {
        Predicate<NGram> predicate = ngram -> ngram.size() > 3;
        Filter filter = Filter.from(predicate);
        NGram ngram = NGram.from("test");
        
        assertTrue(filter.test(ngram));
    }
    
    @Test
    void testAnd() {
        Predicate<NGram> predicate1 = ngram -> ngram.size() > 3;
        Predicate<NGram> predicate2 = ngram -> ngram.size() < 10;
        
        Filter filter1 = Filter.from(predicate1);
        Filter filter2 = Filter.from(predicate2);
        
        Filter combinedFilter = filter1.and(Optional.of(filter2));
        NGram validNGram = NGram.from("hello");
        NGram invalidNGram = NGram.from("hi");
        
        assertTrue(combinedFilter.test(validNGram));
        assertFalse(combinedFilter.test(invalidNGram));
    }
    
    @Test
    void testAndWithEmptyOptional() {
        Predicate<NGram> predicate = ngram -> ngram.size() > 3;
        Filter filter = Filter.from(predicate);
        
        Filter resultFilter = filter.and(Optional.empty());
        NGram ngram = NGram.from("test");
        
        assertTrue(resultFilter.test(ngram));
    }
}
