package it.polimi.ingsw.model;

import it.polimi.ingsw.network.events.BoardEvent;
import it.polimi.ingsw.view.RemoteViewDummy;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TileTest {

    public static void tileTest(int indexRow, int indexColumn, Worker worker, boolean domed, ETileLevel level, Tile t) {
        assertEquals(indexRow, t.getRow());
        assertEquals(indexColumn, t.getColumn());
        assertEquals(worker, t.getWorker());
        assertEquals(domed, t.isDomed());
        assertEquals(level, t.getLevel());
    }

    @Test
    public void Constructor2ArgumentsValidCoordsTest () {

        Tile tile;

        // First case: (2,4)
        tile = new Tile(2, 4);
        tileTest(2, 4, null, false, ETileLevel.GROUND, tile);

        // Second case: (1,3)
        tile = new Tile(1,3);
        tileTest(1,3, null, false, ETileLevel.GROUND, tile);

        // Third case: (4,1)
        tile = new Tile(4,1);
        tileTest(4,1, null, false, ETileLevel.GROUND, tile);

    }

    @Test
    public void Constructor2ArgumentsInvalidCoordsTest () {

        // First case: try (-1,4)
        assertThrows(IllegalArgumentException.class, () -> {
            Tile tile = new Tile(-1, 4);
        });

        // Second case: try (2,5)
        assertThrows(IllegalArgumentException.class, () -> {
            Tile tile = new Tile(2, 5);
        });

        // Third case: try (5,13)
        assertThrows(IllegalArgumentException.class, () -> {
            Tile tile = new Tile(5, 13);
        });

    }

    @Test
    public void Constructor1ArgumentValidPositionTest() {

        Tile tile;

        // First case: (1,4)
        tile = new Tile(new Position(1, 4));
        tileTest(1, 4, null, false, ETileLevel.GROUND, tile);

        // Second case: (3,2)
        tile = new Tile(new Position(3, 2));
        tileTest(3, 2, null, false, ETileLevel.GROUND, tile);

        // Third case: (4,4)
        tile = new Tile(new Position(4, 4));
        tileTest(4, 4, null, false, ETileLevel.GROUND, tile);

    }

    @Test
    public void Constructor1ArgumentNullPositionTest() {

        assertThrows(IllegalArgumentException.class, () -> {
           new Tile(null);
        });

    }

    @Test
    public void getPositionTest() {

        Tile tile;

        // First case: (0,0)
        tile = new Tile(0,0);
        assertEquals(new Position(0,0), tile.getPosition());

        // Second case: (1,4)
        tile = new Tile(1,4);
        assertEquals(new Position(1,4), tile.getPosition());

        // Third case: (3,3)
        tile = new Tile(3,3);
        assertEquals(new Position(3,3), tile.getPosition());

        // Fourth case: (4,2)
        tile = new Tile(4,2);
        assertEquals(new Position(4,2), tile.getPosition());

    }

    @Test
    public void heightDifferenceValidArgumentsTest() {

        Tile t1, t2;

        t1 = new Tile(2,2);
        t2 = new Tile(2,3);

        t1.setLevel(ETileLevel.GROUND);
        t2.setLevel(ETileLevel.GROUND);
        assertEquals(0, Tile.heightDifference(t1, t2));
        assertEquals(0, Tile.heightDifference(t2, t1));

        t1.setLevel(ETileLevel.LEVEL2);
        t2.setLevel(ETileLevel.LEVEL1);
        assertEquals(1, Tile.heightDifference(t1, t2));
        assertEquals(-1, Tile.heightDifference(t2, t1));

        t1.setLevel(ETileLevel.LEVEL1);
        t2.setLevel(ETileLevel.LEVEL3);
        assertEquals(-2, Tile.heightDifference(t1, t2));
        assertEquals(2, Tile.heightDifference(t2, t1));

        t1.setLevel(ETileLevel.GROUND);
        t2.setLevel(ETileLevel.LEVEL3);
        assertEquals(-3, Tile.heightDifference(t1, t2));
        assertEquals(3, Tile.heightDifference(t2, t1));

        t1.setLevel(ETileLevel.LEVEL2);
        t2.setLevel(ETileLevel.LEVEL2);
        assertEquals(0, Tile.heightDifference(t1, t2));
        assertEquals(0, Tile.heightDifference(t2, t1));

        // Let's change positions, just because
        t1 = new Tile(0,2);
        t2 = new Tile(4,3);

        t1.setLevel(ETileLevel.LEVEL3);
        t2.setLevel(ETileLevel.LEVEL2);
        assertEquals(1, Tile.heightDifference(t1, t2));
        assertEquals(-1, Tile.heightDifference(t2, t1));

        t1.setLevel(ETileLevel.LEVEL1);
        t2.setLevel(ETileLevel.LEVEL1);
        assertEquals(0, Tile.heightDifference(t1, t2));
        assertEquals(0, Tile.heightDifference(t2, t1));

    }

    @Test
    public void heightDifferenceNullArgumentsTest() {

        Tile t1, t2;

        t1 = new Tile(0,1);
        t1.setLevel(ETileLevel.LEVEL2);

        assertThrows(IllegalArgumentException.class, () -> {
           Tile.heightDifference(t1, null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            Tile.heightDifference(null, t1);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            Tile.heightDifference(null, null);
        });

    }

    @Test
    public void heightDifferenceSameTileTest() {

        Tile t;

        t = new Tile(2,3);

        t.setLevel(ETileLevel.GROUND);
        assertEquals(0, Tile.heightDifference(t, t));

        t.setLevel(ETileLevel.LEVEL1);
        assertEquals(0, Tile.heightDifference(t, t));

        t.setLevel(ETileLevel.LEVEL2);
        assertEquals(0, Tile.heightDifference(t, t));

        t.setLevel(ETileLevel.LEVEL3);
        assertEquals(0, Tile.heightDifference(t, t));

    }

    @Test
    public void isPerimetralTileTest() {

        Tile tile;

        tile = new Tile(0,0);
        assertTrue(tile.isPerimetralTile());

        tile = new Tile(1,1);
        assertFalse(tile.isPerimetralTile());

        tile = new Tile(0,3);
        assertTrue(tile.isPerimetralTile());

        tile = new Tile(2,0);
        assertTrue(tile.isPerimetralTile());

        tile = new Tile(4,4);
        assertTrue(tile.isPerimetralTile());

        tile = new Tile(1,4);
        assertTrue(tile.isPerimetralTile());

        tile = new Tile(0,2);
        assertTrue(tile.isPerimetralTile());

        tile = new Tile(3,3);
        assertFalse(tile.isPerimetralTile());

        tile = new Tile(2,3);
        assertFalse(tile.isPerimetralTile());

        tile = new Tile(4,0);
        assertTrue(tile.isPerimetralTile());

        tile = new Tile(2,2);
        assertFalse(tile.isPerimetralTile());

        tile = new Tile(4,2);
        assertTrue(tile.isPerimetralTile());

        tile = new Tile(3,1);
        assertFalse(tile.isPerimetralTile());

    }

    @Test
    public void isCornerTileTest() {

        Tile tile;

        // Check the corners
        tile = new Tile(0,0);
        assertTrue(tile.isCornerTile());

        tile = new Tile(0,4);
        assertTrue(tile.isCornerTile());

        tile = new Tile(4,0);
        assertTrue(tile.isCornerTile());

        tile = new Tile(4,4);
        assertTrue(tile.isCornerTile());

        // Check other tiles
        tile = new Tile(1,1);
        assertFalse(tile.isCornerTile());

        tile = new Tile(0,3);
        assertFalse(tile.isCornerTile());

        tile = new Tile(2,4);
        assertFalse(tile.isCornerTile());

        tile = new Tile(4,1);
        assertFalse(tile.isCornerTile());

        tile = new Tile(3,3);
        assertFalse(tile.isCornerTile());

        tile = new Tile(2,0);
        assertFalse(tile.isCornerTile());

    }

    @Test
    public void buildDomeTest() {

        Player gabriele = new Player(new RemoteViewDummy("Gabriele"));
        RemoteViewDummy rvd = (RemoteViewDummy) gabriele.getRemoteView();
        Worker worker = new Worker(gabriele, 0);
        Tile tile;

        tile = new Tile(3,1);
        tile.registerObserver(rvd);
        worker.setTile(tile);

        tileTest(3, 1, worker, false, ETileLevel.GROUND, tile);
        WorkerTest.workerTest(tile, gabriele, 0, worker);

        tile.buildDome();

        tileTest(3, 1, worker, true, ETileLevel.GROUND, tile);
        WorkerTest.workerTest(tile, gabriele, 0, worker);
        assertTrue(rvd.getLastReceivedEvent() instanceof BoardEvent);
        assertEquals(1, rvd.getReceivedEvents().size());

        tile.buildDome();

        tileTest(3, 1, worker, true, ETileLevel.GROUND, tile);
        WorkerTest.workerTest(tile, gabriele, 0, worker);
        assertTrue(rvd.getLastReceivedEvent() instanceof BoardEvent);
        assertEquals(1, rvd.getReceivedEvents().size());

        tile.buildDome();

        tileTest(3, 1, worker, true, ETileLevel.GROUND, tile);
        WorkerTest.workerTest(tile, gabriele, 0, worker);
        assertTrue(rvd.getLastReceivedEvent() instanceof BoardEvent);
        assertEquals(1, rvd.getReceivedEvents().size());

        // Let's change position, just because

        rvd.clearReceivedEvents();
        tile = new Tile(0,0);
        tile.registerObserver(rvd);
        tile.setLevel(ETileLevel.LEVEL2);
        worker.setTile(tile);

        tileTest(0, 0, worker, false, ETileLevel.LEVEL2, tile);
        WorkerTest.workerTest(tile, gabriele, 0, worker);

        tile.buildDome();

        tileTest(0, 0, worker, true, ETileLevel.LEVEL2, tile);
        WorkerTest.workerTest(tile, gabriele, 0, worker);
        assertTrue(rvd.getLastReceivedEvent() instanceof BoardEvent);
        assertEquals(1, rvd.getReceivedEvents().size());

        tile.buildDome();

        tileTest(0, 0, worker, true, ETileLevel.LEVEL2, tile);
        WorkerTest.workerTest(tile, gabriele, 0, worker);
        assertTrue(rvd.getLastReceivedEvent() instanceof BoardEvent);
        assertEquals(1, rvd.getReceivedEvents().size());

        tile.buildDome();

        tileTest(0, 0, worker, true, ETileLevel.LEVEL2, tile);
        WorkerTest.workerTest(tile, gabriele, 0, worker);
        assertTrue(rvd.getLastReceivedEvent() instanceof BoardEvent);
        assertEquals(1, rvd.getReceivedEvents().size());

    }

    @Test
    public void buildOneLevelTest() {

        Player gabriele = new Player(new RemoteViewDummy("Gabriele"));
        RemoteViewDummy rvd = (RemoteViewDummy) gabriele.getRemoteView();
        Worker worker = new Worker(gabriele, 0);
        Tile tile;

        tile = new Tile(2,3);
        worker.setTile(tile);
        tile.registerObserver(rvd);

        tileTest(2, 3, worker, false, ETileLevel.GROUND, tile);
        WorkerTest.workerTest(tile, gabriele, 0, worker);

        tile.buildOneLevel();

        tileTest(2, 3, worker, false, ETileLevel.LEVEL1, tile);
        WorkerTest.workerTest(tile, gabriele, 0, worker);
        assertTrue(rvd.getLastReceivedEvent() instanceof BoardEvent);
        assertEquals(1, rvd.getReceivedEvents().size());

        tile.buildOneLevel();

        tileTest(2, 3, worker, false, ETileLevel.LEVEL2, tile);
        WorkerTest.workerTest(tile, gabriele, 0, worker);
        assertTrue(rvd.getLastReceivedEvent() instanceof BoardEvent);
        assertEquals(2, rvd.getReceivedEvents().size());

        tile.buildOneLevel();

        tileTest(2, 3, worker, false, ETileLevel.LEVEL3, tile);
        WorkerTest.workerTest(tile, gabriele, 0, worker);
        assertTrue(rvd.getLastReceivedEvent() instanceof BoardEvent);
        assertEquals(3, rvd.getReceivedEvents().size());

        tile.buildOneLevel();

        tileTest(2, 3, worker, true, ETileLevel.LEVEL3, tile);
        WorkerTest.workerTest(tile, gabriele, 0, worker);
        assertTrue(rvd.getLastReceivedEvent() instanceof BoardEvent);
        assertEquals(4, rvd.getReceivedEvents().size());

        tile.buildOneLevel();

        tileTest(2, 3, worker, true, ETileLevel.LEVEL3, tile);
        WorkerTest.workerTest(tile, gabriele, 0, worker);
        assertTrue(rvd.getLastReceivedEvent() instanceof BoardEvent);
        assertEquals(4, rvd.getReceivedEvents().size());

        tile.buildOneLevel();

        tileTest(2, 3, worker, true, ETileLevel.LEVEL3, tile);
        WorkerTest.workerTest(tile, gabriele, 0, worker);
        assertTrue(rvd.getLastReceivedEvent() instanceof BoardEvent);
        assertEquals(4, rvd.getReceivedEvents().size());

        // Let's change position, just because

        tile = new Tile(0,0);
        worker.setTile(tile);

        tileTest(0, 0, worker, false, ETileLevel.GROUND, tile);
        WorkerTest.workerTest(tile, gabriele, 0, worker);

        tile.buildOneLevel();

        tileTest(0, 0, worker, false, ETileLevel.LEVEL1, tile);
        WorkerTest.workerTest(tile, gabriele, 0, worker);

        tile.buildOneLevel();

        tileTest(0, 0, worker, false, ETileLevel.LEVEL2, tile);
        WorkerTest.workerTest(tile, gabriele, 0, worker);

        tile.buildOneLevel();

        tileTest(0, 0, worker, false, ETileLevel.LEVEL3, tile);
        WorkerTest.workerTest(tile, gabriele, 0, worker);

        tile.buildOneLevel();

        tileTest(0, 0, worker, true, ETileLevel.LEVEL3, tile);
        WorkerTest.workerTest(tile, gabriele, 0, worker);

    }

}
