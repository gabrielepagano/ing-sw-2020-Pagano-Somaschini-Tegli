package it.polimi.ingsw.model;

import it.polimi.ingsw.view.RemoteView;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WorkerTest {

    public static void workerTest(Tile tile, Player owner, int id, Worker w) {
        assertEquals(id, w.getId());
        assertEquals(owner, w.getOwner());
        assertEquals(tile, w.getTile());
    }

    @Test
    public void ConstructorTest() {

        Player gabriele = new Player(new RemoteView("Gabriele"));

        assertThrows(IllegalArgumentException.class, () -> {
            Worker worker = new Worker(null, 3);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            Worker worker = new Worker(gabriele, -4);
        });

        Worker worker = new Worker(gabriele, 1);
        workerTest(null, gabriele, 1, worker);

    }

    @Test
    public void setTileToNotNullTest() {

        Player gabriele = new Player(new RemoteView("Gabriele"));
        Worker worker = new Worker(gabriele, 3);

        Tile t = new Tile(2,3);
        t.setLevel(ETileLevel.LEVEL1);

        workerTest(null, gabriele, 3, worker);
        TileTest.tileTest(2,3, null, false, ETileLevel.LEVEL1, t);

        worker.setTile(t);

        workerTest(t, gabriele, 3, worker);
        TileTest.tileTest(2, 3, worker, false, ETileLevel.LEVEL1, t);

    }

    @Test
    public void setTileToNullTest() {

        Player gabriele = new Player(new RemoteView("Gabriele"));
        Worker worker = new Worker(gabriele, 3);

        Tile t = new Tile(4,0);
        t.setLevel(ETileLevel.LEVEL3);

        worker.setTile(t);
        worker.setTile(null);

        workerTest(null, gabriele, 3, worker);
        TileTest.tileTest(4,0, null, false, ETileLevel.LEVEL3, t);

    }

    @Test
    public void getOwnerNicknameTest() {

        Player gabriele = new Player(new RemoteView("Gabriele"));
        Worker worker = new Worker(gabriele, 3);

        assertEquals("Gabriele", worker.getOwnerNickname());

    }

    @Test
    public void getPositionTest() {

        Player gabriele = new Player(new RemoteView("Gabriele"));
        Worker worker = new Worker(gabriele, 3);

        // First case
        Tile t1 = new Tile(0,3);
        worker.setTile(t1);
        assertEquals(new Position(0,3), worker.getPosition());

        // Second case
        Tile t2 = new Tile(4,4);
        worker.setTile(t2);
        assertEquals(new Position(4,4), worker.getPosition());

        // Third case
        Tile t3 = new Tile(2,0);
        worker.setTile(t3);
        assertEquals(new Position(2,0), worker.getPosition());

    }

    @Test
    public void moveWorkerOnEmptyTileTest() {

        Player gabriele = new Player(new RemoteView("Gabriele"));

        Tile origin = new Tile(3,4);
        Tile dest = new Tile(1,1);
        dest.setLevel(ETileLevel.LEVEL2);

        Worker worker = new Worker(gabriele, 0);
        worker.setTile(origin);

        workerTest(origin, gabriele, 0, worker);
        TileTest.tileTest(1,1, null, false, ETileLevel.LEVEL2, dest);
        TileTest.tileTest(3, 4, worker, false, ETileLevel.GROUND, origin);

        worker.moveWorker(dest);

        workerTest(dest, gabriele, 0, worker);
        TileTest.tileTest(1,1, worker, false, ETileLevel.LEVEL2, dest);
        TileTest.tileTest(3, 4, null, false, ETileLevel.GROUND, origin);

    }

    @Test
    public void moveWorkerOnOccupiedTileTest() {

        Player gabriele = new Player(new RemoteView("Gabriele"));
        Player marco = new Player(new RemoteView("Marco"));

        Tile origin = new Tile(2,2);
        origin.setLevel(ETileLevel.LEVEL1);
        Tile dest = new Tile(2,3);
        dest.setLevel(ETileLevel.LEVEL3);

        Worker workerG = new Worker(gabriele, 0);
        Worker workerM = new Worker(marco, 2);

        // Workers placing
        workerG.setTile(origin);
        workerM.setTile(dest);

        // Before movement
        workerTest(origin, gabriele, 0, workerG);
        workerTest(dest, marco, 2, workerM);
        TileTest.tileTest(2, 2, workerG, false, ETileLevel.LEVEL1, origin);
        TileTest.tileTest(2, 3, workerM, false, ETileLevel.LEVEL3, dest);

        workerG.moveWorker(dest);

        // After movement
        workerTest(dest, gabriele, 0, workerG);
        workerTest(origin, marco, 2, workerM);
        TileTest.tileTest(2, 2, workerM, false, ETileLevel.LEVEL1, origin);
        TileTest.tileTest(2, 3, workerG, false, ETileLevel.LEVEL3, dest);

    }

    @Test
    public void moveWorkerOnDomedTileTest() {

        Player gabriele = new Player(new RemoteView("Gabriele"));

        Tile origin = new Tile(0, 0);
        Tile dest = new Tile(1,1);
        dest.setLevel(ETileLevel.LEVEL2);
        dest.setDomed(true);

        Worker worker = new Worker(gabriele, 0);
        worker.setTile(origin);

        workerTest(origin, gabriele, 0, worker);
        TileTest.tileTest(0,0, worker, false, ETileLevel.GROUND, origin);
        TileTest.tileTest(1, 1, null, true, ETileLevel.LEVEL2, dest);

        worker.moveWorker(dest);

        // Nothing changed
        workerTest(origin, gabriele, 0, worker);
        TileTest.tileTest(0,0, worker, false, ETileLevel.GROUND, origin);
        TileTest.tileTest(1, 1, null, true, ETileLevel.LEVEL2, dest);

    }

    @Test
    public void moveWorkerOnNullTileTest() {

        Player gabriele = new Player(new RemoteView("Gabriele"));

        Tile origin = new Tile(4, 1);
        origin.setLevel(ETileLevel.LEVEL3);

        Worker worker = new Worker(gabriele, 0);
        worker.setTile(origin);

        workerTest(origin, gabriele, 0, worker);
        TileTest.tileTest(4,1, worker, false, ETileLevel.LEVEL3, origin);

        assertThrows(IllegalArgumentException.class, () -> worker.moveWorker(null));

        // Nothing changed
        workerTest(origin, gabriele, 0, worker);
        TileTest.tileTest(4,1, worker, false, ETileLevel.LEVEL3, origin);

    }

    @Test
    public void moveWorkerOnSameTileTest() {

        Player gabriele = new Player(new RemoteView("Gabriele"));

        Tile origin = new Tile(3,4);
        origin.setLevel(ETileLevel.LEVEL2);

        Worker worker = new Worker(gabriele, 0);
        worker.setTile(origin);

        workerTest(origin, gabriele, 0, worker);
        TileTest.tileTest(3, 4, worker, false, ETileLevel.LEVEL2, origin);

        worker.moveWorker(origin);

        workerTest(origin, gabriele, 0, worker);
        TileTest.tileTest(3, 4, worker, false, ETileLevel.LEVEL2, origin);

    }

}
