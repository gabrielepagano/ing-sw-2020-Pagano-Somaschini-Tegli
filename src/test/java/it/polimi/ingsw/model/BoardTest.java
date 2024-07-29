package it.polimi.ingsw.model;

import it.polimi.ingsw.utils.BoardFactory;
import it.polimi.ingsw.utils.GameFactory;
import it.polimi.ingsw.view.RemoteView;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import static it.polimi.ingsw.model.Constants.COLUMNS;
import static it.polimi.ingsw.model.Constants.ROWS;
import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    public static boolean boardTest(Board board, String boardresourcePath) {

        ClassLoader classLoader = GameFactory.class.getClassLoader();
        Scanner resourceScanner = new Scanner(classLoader.getResourceAsStream(boardresourcePath));

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLUMNS; c++) {

                Tile currentTile = board.getTile(r,c);
                int level = 0;
                boolean isDomed = false;

                if(resourceScanner.hasNextLine()) {
                    String line = resourceScanner.nextLine();
                    level = line.charAt(0) - '0';
                    isDomed = line.charAt(1) == 'D';
                    if(currentTile.getLevel().getHeight() != level || currentTile.isDomed() != isDomed) {
                        return false;
                    }
                }

            }
        }

        return true;
    }

    @Test
    public void Constructor0ArgumentsTest() {
        Board board = new Board();
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLUMNS; c++) {
                TileTest.tileTest(r, c, null, false, ETileLevel.GROUND, board.getTile(r, c));
            }
        }
    }

    @Test
    public void Constructor1ArgumentTest() {
        Board board = new Board(new ArrayList<RemoteView>());
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLUMNS; c++) {
                TileTest.tileTest(r, c, null, false, ETileLevel.GROUND, board.getTile(r, c));
            }
        }
    }

    @Test
    public void getTileSimpleTest() {

        Board board = BoardFactory.buildBoardWithoutWorkersFromFile("board002.txt");
        Tile t;

        t = board.getTile(0, 1);
        TileTest.tileTest(0, 1, null, true, ETileLevel.LEVEL1, t);

        t = board.getTile(0, 3);
        TileTest.tileTest(0, 3, null, false, ETileLevel.LEVEL2, t);

        t = board.getTile(new Position(1, 2));
        TileTest.tileTest(1, 2, null, false, ETileLevel.LEVEL3, t);

        t = board.getTile(new Position(1, 3));
        TileTest.tileTest(1, 3, null, false, ETileLevel.GROUND, t);

        t = board.getTile(new Position(2, 1));
        TileTest.tileTest(2, 1, null, true, ETileLevel.LEVEL2, t);

        t = board.getTile(new Position(2, 4));
        TileTest.tileTest(2, 4, null, true, ETileLevel.GROUND, t);

        t = board.getTile(new Position(3, 0));
        TileTest.tileTest(3, 0, null, false, ETileLevel.LEVEL1, t);

        t = board.getTile(new Position(3, 1));
        TileTest.tileTest(3, 1, null, false, ETileLevel.GROUND, t);

        t = board.getTile(new Position(3, 3));
        TileTest.tileTest(3, 3, null, false, ETileLevel.LEVEL1, t);

        t = board.getTile(new Position(3, 4));
        TileTest.tileTest(3, 4, null, false, ETileLevel.LEVEL1, t);

        t = board.getTile(new Position(4, 1));
        TileTest.tileTest(4, 1, null, false, ETileLevel.GROUND, t);

        t = board.getTile(new Position(4, 3));
        TileTest.tileTest(4, 3, null, false, ETileLevel.LEVEL1, t);

    }

    @Test
    public void getTileInvalidRowIndexTest() {

        Board board = new Board();

        assertThrows(IllegalArgumentException.class, () -> {
            board.getTile(-1, 3);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            board.getTile(5, 1);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            board.getTile(6, 2);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            board.getTile(-2, 4);
        });

    }

    @Test
    public void getTileInvalidColumnIndexTest() {

        Board board = new Board();

        assertThrows(IllegalArgumentException.class, () -> {
            board.getTile(1, 5);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            board.getTile(2, 10);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            board.getTile(1, -2);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            board.getTile(4, -3);
        });

    }

    @Test
    public void getTileInvalidRowAndColumnIndexesTest() {

        Board board = new Board();

        assertThrows(IllegalArgumentException.class, () -> {
           board.getTile(-1, 6);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            board.getTile(7, 10);
        });

    }

    @Test
    public void getTileNullPositionTest() {

        Board board = new Board();

        assertThrows(IllegalArgumentException.class, () -> {
           board.getTile(null);
        });

    }

    @Test
    public void setTileSimpleTest() {

        Board board = new Board();
        Tile tile1 = new Tile(1, 2);
        Tile tile2 = new Tile(3, 4);

        assertNotSame(tile1, board.getTile(1, 2));
        assertNotSame(tile2, board.getTile(3, 4));

        board.setTile(tile1);
        // Shouldn't do anything
        board.setTile(null);

        assertSame(tile1, board.getTile(1, 2));
        assertNotSame(tile2, board.getTile(3, 4));

        board.setTile(tile2);

        assertSame(tile1, board.getTile(1, 2));
        assertSame(tile2, board.getTile(3, 4));

    }

    @Test
    public void getSurroundingTilesValidPositionTest() {

        Board board = new Board();
        List<Tile> actualResult;
        List<Tile> expectedResult = new ArrayList<>();

        // Case (0,0)
        actualResult = board.getSurroundingTiles(0,0);
        expectedResult.add(board.getTile(0,1));
        expectedResult.add(board.getTile(1,0));
        expectedResult.add(board.getTile(1,1));
        assertTrue(actualResult.containsAll(expectedResult));
        assertEquals(3, actualResult.size());
        assertEquals(actualResult, board.getSurroundingTiles(new Position(0,0)));

        // Case (0,4)
        expectedResult.clear();
        actualResult = board.getSurroundingTiles(0,4);
        expectedResult.add(board.getTile(0,3));
        expectedResult.add(board.getTile(1,3));
        expectedResult.add(board.getTile(1,4));
        assertTrue(actualResult.containsAll(expectedResult));
        assertEquals(3, actualResult.size());
        assertEquals(actualResult, board.getSurroundingTiles(new Position(0,4)));

        // Case (4,0)
        expectedResult.clear();
        actualResult = board.getSurroundingTiles(4,0);
        expectedResult.add(board.getTile(3,0));
        expectedResult.add(board.getTile(3,1));
        expectedResult.add(board.getTile(4,1));
        assertTrue(actualResult.containsAll(expectedResult));
        assertEquals(3, actualResult.size());
        assertEquals(actualResult, board.getSurroundingTiles(new Position(4,0)));

        // Case (4,4)
        expectedResult.clear();
        actualResult = board.getSurroundingTiles(4,4);
        expectedResult.add(board.getTile(3,3));
        expectedResult.add(board.getTile(3,4));
        expectedResult.add(board.getTile(4,3));
        assertTrue(actualResult.containsAll(expectedResult));
        assertEquals(3, actualResult.size());
        assertEquals(actualResult, board.getSurroundingTiles(new Position(4,4)));

        // Case (2,0)
        expectedResult.clear();
        actualResult = board.getSurroundingTiles(2,0);
        expectedResult.add(board.getTile(1,0));
        expectedResult.add(board.getTile(1,1));
        expectedResult.add(board.getTile(2,1));
        expectedResult.add(board.getTile(3,0));
        expectedResult.add(board.getTile(3,1));
        assertTrue(actualResult.containsAll(expectedResult));
        assertEquals(5, actualResult.size());
        assertEquals(actualResult, board.getSurroundingTiles(new Position(2,0)));

        // Case (4,3)
        expectedResult.clear();
        actualResult = board.getSurroundingTiles(4,3);
        expectedResult.add(board.getTile(3,2));
        expectedResult.add(board.getTile(3,3));
        expectedResult.add(board.getTile(3,4));
        expectedResult.add(board.getTile(4,2));
        expectedResult.add(board.getTile(4,4));
        assertTrue(actualResult.containsAll(expectedResult));
        assertEquals(5, actualResult.size());
        assertEquals(actualResult, board.getSurroundingTiles(new Position(4,3)));

        // Case (1,4)
        expectedResult.clear();
        actualResult = board.getSurroundingTiles(1,4);
        expectedResult.add(board.getTile(0,3));
        expectedResult.add(board.getTile(0,4));
        expectedResult.add(board.getTile(1,3));
        expectedResult.add(board.getTile(2,3));
        expectedResult.add(board.getTile(2,4));
        assertTrue(actualResult.containsAll(expectedResult));
        assertEquals(5, actualResult.size());
        assertEquals(actualResult, board.getSurroundingTiles(new Position(1,4)));

        // Case (0,2)
        expectedResult.clear();
        actualResult = board.getSurroundingTiles(0,2);
        expectedResult.add(board.getTile(0,1));
        expectedResult.add(board.getTile(0,3));
        expectedResult.add(board.getTile(1,1));
        expectedResult.add(board.getTile(1,2));
        expectedResult.add(board.getTile(1,3));
        assertTrue(actualResult.containsAll(expectedResult));
        assertEquals(5, actualResult.size());
        assertEquals(actualResult, board.getSurroundingTiles(new Position(0,2)));

        // Case (2,2)
        expectedResult.clear();
        actualResult = board.getSurroundingTiles(2,2);
        expectedResult.add(board.getTile(1,1));
        expectedResult.add(board.getTile(1,2));
        expectedResult.add(board.getTile(1,3));
        expectedResult.add(board.getTile(2,1));
        expectedResult.add(board.getTile(2,3));
        expectedResult.add(board.getTile(3,1));
        expectedResult.add(board.getTile(3,2));
        expectedResult.add(board.getTile(3,3));
        assertTrue(actualResult.containsAll(expectedResult));
        assertEquals(8, actualResult.size());
        assertEquals(actualResult, board.getSurroundingTiles(new Position(2,2)));

        // Case (3,1)
        expectedResult.clear();
        actualResult = board.getSurroundingTiles(3,1);
        expectedResult.add(board.getTile(2,0));
        expectedResult.add(board.getTile(2,1));
        expectedResult.add(board.getTile(2,2));
        expectedResult.add(board.getTile(3,0));
        expectedResult.add(board.getTile(3,2));
        expectedResult.add(board.getTile(4,0));
        expectedResult.add(board.getTile(4,1));
        expectedResult.add(board.getTile(4,2));
        assertTrue(actualResult.containsAll(expectedResult));
        assertEquals(8, actualResult.size());
        assertEquals(actualResult, board.getSurroundingTiles(new Position(3,1)));

    }

    @Test
    public void getSurroundingTilesInvalidPositionTest() {

        Board board = new Board();
        List<Tile> actualResult;

        actualResult = board.getSurroundingTiles(-1, 4);
        assertTrue(actualResult.isEmpty());

        actualResult = board.getSurroundingTiles(-3, 7);
        assertTrue(actualResult.isEmpty());

        actualResult = board.getSurroundingTiles(2, 5);
        assertTrue(actualResult.isEmpty());

        actualResult = board.getSurroundingTiles(null);
        assertTrue(actualResult.isEmpty());

        actualResult = board.getSurroundingTiles(null);
        assertTrue(actualResult.isEmpty());

    }

    @Test
    public void getStraightBackwardsTileTest() {

        Board board = new Board();

        Position charger, target;
        Tile actualTile;

        charger = new Position(2, 2);
        target = new Position(1, 1);
        // Expected: (0, 0) North-West
        actualTile = board.getStraightBackwardsTile(charger, target);
        assertSame(board.getTile(new Position(0,0)), actualTile);

        charger = new Position(2, 2);
        target = new Position(3, 3);
        // Expected: (4, 4) South-East
        actualTile = board.getStraightBackwardsTile(charger, target);
        assertSame(board.getTile(new Position(4,4)), actualTile);

        charger = new Position(3, 2);
        target = new Position(2, 3);
        // Expected: (1, 4) North-East
        actualTile = board.getStraightBackwardsTile(charger, target);
        assertSame(board.getTile(new Position(1,4)), actualTile);

        charger = new Position(0, 4);
        target = new Position(1, 3);
        // Expected: (2, 2) South-West
        actualTile = board.getStraightBackwardsTile(charger, target);
        assertSame(board.getTile(new Position(2,2)), actualTile);

        charger = new Position(4, 2);
        target = new Position(4, 3);
        // Expected: (4, 4) East
        actualTile = board.getStraightBackwardsTile(charger, target);
        assertSame(board.getTile(new Position(4,4)), actualTile);

        charger = new Position(2, 3);
        target = new Position(1, 3);
        // Expected: (0, 3) North
        actualTile = board.getStraightBackwardsTile(charger, target);
        assertSame(board.getTile(new Position(0,3)), actualTile);

        charger = new Position(0, 1);
        target = new Position(1, 1);
        // Expected: (2, 1) South
        actualTile = board.getStraightBackwardsTile(charger, target);
        assertSame(board.getTile(new Position(2,1)), actualTile);

        charger = new Position(3, 4);
        target = new Position(3, 3);
        // Expected: (3, 2) West
        actualTile = board.getStraightBackwardsTile(charger, target);
        assertSame(board.getTile(new Position(3,2)), actualTile);

        charger = new Position(1, 1);
        target = new Position(0, 0);
        // Expected: null
        actualTile = board.getStraightBackwardsTile(charger, target);
        assertNull(actualTile);

        charger = new Position(1, 0);
        target = new Position(0, 0);
        // Expected: null
        actualTile = board.getStraightBackwardsTile(charger, target);
        assertNull(actualTile);

        charger = new Position(0, 1);
        target = new Position(0, 0);
        // Expected: null
        actualTile = board.getStraightBackwardsTile(charger, target);
        assertNull(actualTile);

        charger = new Position(3, 3);
        target = new Position(4, 3);
        // Expected: null
        actualTile = board.getStraightBackwardsTile(charger, target);
        assertNull(actualTile);

    }

    @Test
    public void getStraightBackwardsTileNullPositionTest() {

        Board board = new Board();
        Tile actualTile;

        actualTile = board.getStraightBackwardsTile(null, new Position(2,1));
        assertNull(actualTile);

        actualTile = board.getStraightBackwardsTile(new Position(1,1), null);
        assertNull(actualTile);

        actualTile = board.getStraightBackwardsTile(null, null);
        assertNull(actualTile);

    }

    @Test
    public void getStraightBackwardsTileInvalidIndexesTest() {

        Board board = new Board();
        Tile actualTile;

        actualTile = board.getStraightBackwardsTile(new Position(1,0), new Position(2,2));
        assertNull(actualTile);

        actualTile = board.getStraightBackwardsTile(new Position(1,0), new Position(1,0));
        assertNull(actualTile);

        actualTile = board.getStraightBackwardsTile(new Position(0,0), new Position(4,4));
        assertNull(actualTile);

        actualTile = board.getStraightBackwardsTile(new Position(4,1), new Position(2,1));
        assertNull(actualTile);

        actualTile = board.getStraightBackwardsTile(new Position(4,3), new Position(2,2));
        assertNull(actualTile);

        actualTile = board.getStraightBackwardsTile(new Position(2,0), new Position(4,1));
        assertNull(actualTile);

        actualTile = board.getStraightBackwardsTile(new Position(0,1), new Position(3,3));
        assertNull(actualTile);

        actualTile = board.getStraightBackwardsTile(new Position(2,3), new Position(2,3));
        assertNull(actualTile);

    }

    @Test
    public void getPerimetralNextValidTileTest() {

        Board board = new Board();
        Tile tile;

        tile = board.getTile(0,2);
        assertSame(board.getTile(0,3), board.getPerimetralNext(tile, true));
        assertSame(board.getTile(0,1), board.getPerimetralNext(tile, false));

        tile = board.getTile(1,0);
        assertSame(board.getTile(0,0), board.getPerimetralNext(tile, true));
        assertSame(board.getTile(2,0), board.getPerimetralNext(tile, false));

        tile = board.getTile(4,1);
        assertSame(board.getTile(4,0), board.getPerimetralNext(tile, true));
        assertSame(board.getTile(4,2), board.getPerimetralNext(tile, false));

        tile = board.getTile(2,4);
        assertSame(board.getTile(3,4), board.getPerimetralNext(tile, true));
        assertSame(board.getTile(1,4), board.getPerimetralNext(tile, false));

        // NW corner
        tile = board.getTile(0,0);
        assertSame(board.getTile(0,1), board.getPerimetralNext(tile, true));
        assertSame(board.getTile(1,0), board.getPerimetralNext(tile, false));

        // NE corner
        tile = board.getTile(0,4);
        assertSame(board.getTile(1,4), board.getPerimetralNext(tile, true));
        assertSame(board.getTile(0,3), board.getPerimetralNext(tile, false));

        // SE corner
        tile = board.getTile(4,4);
        assertSame(board.getTile(4,3), board.getPerimetralNext(tile, true));
        assertSame(board.getTile(3,4), board.getPerimetralNext(tile, false));

        // SW corner
        tile = board.getTile(4,0);
        assertSame(board.getTile(3,0), board.getPerimetralNext(tile, true));
        assertSame(board.getTile(4,1), board.getPerimetralNext(tile, false));

    }

    @Test
    public void getPerimetralNextInvalidTileTest() {

        Board board = new Board();

        assertThrows(IllegalArgumentException.class, () -> {
            board.getPerimetralNext(board.getTile(2,2), true);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            board.getPerimetralNext(board.getTile(2,2), false);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            board.getPerimetralNext(board.getTile(3,1), true);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            board.getPerimetralNext(board.getTile(3,1), false);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            board.getPerimetralNext(board.getTile(2,1), true);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            board.getPerimetralNext(board.getTile(2,1), false);
        });

    }

    @Test
    public void getPerimetralNextNullTileTest() {

        Board board = new Board();

        assertThrows(IllegalArgumentException.class, () -> {
           Tile tile = board.getPerimetralNext(null, true);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            Tile tile = board.getPerimetralNext(null, false);
        });

    }

    @Test
    public void getPerimetralPreviousNullTileTest() {

        Board board = new Board();

        assertThrows(IllegalArgumentException.class, () -> {
            Tile tile = board.getPerimetralPrevious(null, true);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            Tile tile = board.getPerimetralPrevious(null, false);
        });

    }

}
