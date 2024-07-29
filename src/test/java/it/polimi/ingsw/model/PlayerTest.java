package it.polimi.ingsw.model;

import it.polimi.ingsw.view.RemoteView;
import it.polimi.ingsw.view.RemoteViewDummy;
import org.junit.jupiter.api.Test;

import static it.polimi.ingsw.model.TileTest.tileTest;
import static it.polimi.ingsw.model.WorkerTest.workerTest;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    public static void playerTest(String nickname, Worker w0, Worker w1, RemoteView rv, Player p){
        assertEquals(nickname, p.getNickname());
        assertEquals(w0, p.getWorkers()[0]);
        assertEquals(w1, p.getWorkers()[1]);
        assertEquals(rv, p.getRemoteView());
    }

    @Test
    public void ConstructorValidRemoteViewTest() {

        RemoteViewDummy rv = new RemoteViewDummy("Marco");
        Player p = new Player(rv);
        Worker w0 = p.getWorkers()[0];
        Worker w1 = p.getWorkers()[1];
        Tile t0 = new Tile(0,1);
        Tile t1 = new Tile(3,4);
        t1.setLevel(ETileLevel.LEVEL3);
        w0.setTile(t0);
        w1.setTile(t1);

        tileTest(0,1, w0, false, ETileLevel.GROUND, t0);
        tileTest(3,4, w1, false, ETileLevel.LEVEL3, t1);
        workerTest(t0, p, 0, w0);
        workerTest(t1, p, 1, w1);
        playerTest("Marco", w0, w1, rv, p);

    }

    @Test
    public void ConstructorNullRemoteViewTest() {

        assertThrows(IllegalArgumentException.class, () -> {
            new Player(null);
        });

    }

}
