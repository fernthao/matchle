package thao.matchle;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IndexedCharacterTest {
    @Test
    public void testIndexedCharacter() {
        IndexedCharacter ic = new IndexedCharacter(1, 'a');
        assertEquals(1, ic.index());
        assertEquals((Character) 'a', ic.character());
    }
}

