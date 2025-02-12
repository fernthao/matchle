package thao.matchle;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class IndexedCharacterTest {
    @Test
    void testIndexedCharacter() {
        IndexedCharacter ic = new IndexedCharacter(1, 'a');
        assertEquals(1, ic.index());
        assertEquals('a', ic.character());
    }
}

