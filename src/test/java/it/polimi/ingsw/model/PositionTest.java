package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

public class PositionTest {

    public static void positionTest(int expIndexRow, int expIndexColumn, Position p) {
        assertEquals(expIndexRow, p.getRow());
        assertEquals(expIndexColumn, p.getCol());
    }

    @Test
    public void validPositionConstructorTest() {

        // First valid case: (2,4)
        int indexRow = 2;
        int indexColumn = 4;
        Position position = new Position(indexRow, indexColumn);
        positionTest(indexRow, indexColumn, position);

        // Second valid case: (0,0)
        indexRow = 0;
        indexColumn = 0;
        position = new Position(indexRow, indexColumn);
        positionTest(indexRow, indexColumn, position);

        // Third valid case: (4,4)
        indexRow = 4;
        indexColumn = 4;
        position = new Position(indexRow, indexColumn);
        positionTest(indexRow, indexColumn, position);

        // Fourth valid case: (0,2)
        indexRow = 0;
        indexColumn = 2;
        position = new Position(indexRow, indexColumn);
        positionTest(indexRow, indexColumn, position);

    }

    @Test
    public void invalidColumnConstructorTest() {

        // Column index is > 4
        assertThrows(IllegalArgumentException.class, () -> new Position(0, 7));

        // Column index is > 4
        assertThrows(IllegalArgumentException.class, () -> new Position(1, 5));

        // Column index is < 0
        assertThrows(IllegalArgumentException.class, () -> new Position(2, -3));

        // Column index is < 0
        assertThrows(IllegalArgumentException.class, () -> new Position(2, -1));

    }

    @Test
    public void invalidRowConstructorTest() {

        // Row index is > 4
        assertThrows(IllegalArgumentException.class, () -> new Position(10, 2));

        // Row index is > 4
        assertThrows(IllegalArgumentException.class, () -> new Position(5, 0));

        // Row index is < 0
        assertThrows(IllegalArgumentException.class, () -> new Position(-5, 4));

        // Row index is < 0
        assertThrows(IllegalArgumentException.class, () -> new Position(-1, 2));

    }

    @Test
    public void invalidRowAndColumnConstructorTest() {

        // Row index is < 0 and column index is < 0
        assertThrows(IllegalArgumentException.class, () -> new Position(-5, -4));

        // Row index is < 0 and column index is > 4
        assertThrows(IllegalArgumentException.class, () -> new Position(-1, 7));

        // Row index is > 4 and column index is < 0
        assertThrows(IllegalArgumentException.class, () -> new Position(6, -10));

        // Row index is > 4 and column index is > 4
        assertThrows(IllegalArgumentException.class, () -> new Position(8, 5));

    }

    @Test
    public void toStringTest() {

        Position p;

        // (3,2)
        p = new Position(3,2);
        assertEquals("(3,2)", p.toString());

        // (0,0)
        p = new Position(0,0);
        assertEquals("(0,0)", p.toString());

        // (1,4)
        p = new Position(1,4);
        assertEquals("(1,4)", p.toString());

        // (4,4)
        p = new Position(4,4);
        assertEquals("(4,4)", p.toString());

    }

    @Test
    public void equalsTest() {

        Position p1;
        Position p2;

        // equals() should return true
        p1 = new Position(1,2);
        p2 = new Position(1, 2);
        assertEquals(p1, p2);
        assertEquals(p2, p1);

        // equals() should return false
        p1 = new Position(3,2);
        p2 = new Position(2,3);
        assertNotEquals(p1, p2);
        assertNotEquals(p2, p1);

        // equals() should return false
        p1 = new Position(4,1);
        p2 = new Position(0,2);
        assertNotEquals(p1, p2);
        assertNotEquals(p2, p1);

        // equals() should return false
        p1 = new Position(3,2);
        p2 = null;
        assertNotEquals(p1, p2);

        // equals() should return false
        p1 = new Position(3,2);
        assertNotEquals(p1, new BigInteger("2"));

    }

    @Test
    public void hashCodeTest() {

        Position p;

        // First case
        p = new Position(0,0);
        assertEquals(0, p.hashCode());

        // Second case
        p = new Position(4,3);
        assertEquals(23, p.hashCode());

        // Third case
        p = new Position(0,2);
        assertEquals(2, p.hashCode());

        // Fourth case
        p = new Position(2,3);
        assertEquals(13, p.hashCode());

        // Fifth case
        p = new Position(3,1);
        assertEquals(16, p.hashCode());

    }

}

