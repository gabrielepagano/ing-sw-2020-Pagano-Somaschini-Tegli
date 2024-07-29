package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.utils.GameFactory;
import org.junit.After;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PrometheusCardTest {
    //NOTE: there is no possible game state where a worker CAN'T build but CAN move;
    // That said, there is no meaningful test case around the "supress GODPOWER because of lack of building moves" situation

    static Game game;
    static Board board;
    //static List<RemoteView> remoteViews;
    //static List<Player> players;
    static Player currentPlayer;
    static Playable currentPlayerCard;

    @BeforeEach
    public void setup(){
        game = GameFactory.buildGameFromFile("gamePrometheus.txt", "boardPrometheus01.txt");
        board = game.getBoard();
        currentPlayer = game.getCurrentPlayer();
        currentPlayerCard = currentPlayer.getCard().getPlayable();
    }

    @Test
    public void build() {
        // Select worker in (1,2)
        currentPlayerCard.selectWorker(currentPlayer.getWorkers()[0]);

        // Use Prometheus GODPOWER
        currentPlayerCard.useGodPower(true);

        assertSame(board.getTile(1, 1).getLevel(), ETileLevel.GROUND);
        //Build tile in (0,1) - Before moving
        currentPlayerCard.build(board.getTile(1,1));
        assertSame(board.getTile(1, 1).getLevel(), ETileLevel.LEVEL1);

        // Move worker in (1,3)
        currentPlayerCard.move(board.getTile(1,3));

        assertSame(board.getTile(1, 2).getLevel(), ETileLevel.GROUND);
        //Build tile in (0,1) - After moving
        currentPlayerCard.build(board.getTile(1,2));
        assertSame(board.getTile(1, 2).getLevel(), ETileLevel.LEVEL1);
    }

    @Test
    public void getTilesToMove() {
        // Select worker in (1,2)
        currentPlayerCard.selectWorker(currentPlayer.getWorkers()[0]);

        // Don't Prometheus GODPOWER
        currentPlayerCard.useGodPower(false);

        // Check moves generation
        //  (1,2) & (2,3) are standard excluded tiles, being respectively the starting tile and a tile occupied by a worker
        List<Tile> actualTilesToMove = currentPlayerCard.getTilesToMove(board.getTile(1,2));
        List<Tile> expectedTilesToMove = new ArrayList<>();
        expectedTilesToMove.add(board.getTile(0,1));
        expectedTilesToMove.add(board.getTile(0,2));
        expectedTilesToMove.add(board.getTile(0,3));
        expectedTilesToMove.add(board.getTile(1,1));
        expectedTilesToMove.add(board.getTile(1,3));
        expectedTilesToMove.add(board.getTile(2,1));
        expectedTilesToMove.add(board.getTile(2,2));
        for (Tile t : expectedTilesToMove) {
            assertTrue(actualTilesToMove.contains(t));
        }
        assertFalse(actualTilesToMove.contains(board.getTile(1,2)));

        //Prometheus CAN move up since it did not use its GODPOWER
        assertTrue(actualTilesToMove.contains(board.getTile(2,3)));
    }

    @Test
    public void getTilesToMove_GodPower() {
        // Select worker in (1,2)
        currentPlayerCard.selectWorker(currentPlayer.getWorkers()[0]);

        // Use Prometheus GODPOWER to build before moving
        currentPlayerCard.useGodPower(true);

        // Build in (1,3)
        currentPlayerCard.build(board.getTile(1,3));

        // Check moves generation
        //  (1,2) & (2,3) are standard excluded tiles, being respectively the starting tile and a tile occupied by a worker
        List<Tile> actualTilesToMove = currentPlayerCard.getTilesToMove();
        List<Tile> expectedTilesToMove = new ArrayList<>();
        expectedTilesToMove.add(board.getTile(0,1));
        expectedTilesToMove.add(board.getTile(0,2));
        expectedTilesToMove.add(board.getTile(0,3));
        expectedTilesToMove.add(board.getTile(1,1));
        expectedTilesToMove.add(board.getTile(2,1));
        expectedTilesToMove.add(board.getTile(2,2));
        for (Tile t : expectedTilesToMove) {
            assertTrue(actualTilesToMove.contains(t));
        }
        assertFalse(actualTilesToMove.contains(board.getTile(1,2)));

        //Prometheus CAN'T move up this turn
        assertFalse("Prometheus was allowed to move upwards after building", actualTilesToMove.contains(board.getTile(1,3)));
        assertFalse("Prometheus was allowed to move upwards after building", actualTilesToMove.contains(board.getTile(2,3)));
    }

    @Test
    public void getTilesToBuild_NotBuild(){
        game = GameFactory.buildGameFromFile("gamePrometheus.txt", "boardPrometheus02.txt");
        board = game.getBoard();
        currentPlayer = game.getCurrentPlayer();
        currentPlayerCard = currentPlayer.getCard().getPlayable();

        // Select worker in (4,4)
        currentPlayerCard.selectWorker(currentPlayer.getWorkers()[1]);

        // Use Prometheus GODPOWER to build before moving
        currentPlayerCard.useGodPower(true);

        // Check standard generation
        List<Tile> actualTilesToBuild =  currentPlayerCard.getTilesToBuild(board.getTile(4,4));
        List<Tile> expectedTilesToBuild = new ArrayList<>();
        expectedTilesToBuild.add(board.getTile(3,3));
        expectedTilesToBuild.add(board.getTile(4,3));
        for (Tile t : expectedTilesToBuild) {
            assertTrue(actualTilesToBuild.contains(t));
        }

        //Check the marked tile has been removed
        assertFalse(actualTilesToBuild.contains(board.getTile(3,4)));
    }

    @Test
    public void setTurnPhase_NotGodPower() {
        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());

        // Select worker in (1,2)
        currentPlayerCard.selectWorker(currentPlayer.getWorkers()[0]);
        assertEquals(ETurnPhase.GODPOWER, game.getTurnPhase());

        // Dont use Prometheus GODPOWER
        currentPlayerCard.useGodPower(false);
        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());

        // Move worker in (1,3)
        currentPlayerCard.move(board.getTile(1,3));
        assertEquals(ETurnPhase.BUILD, game.getTurnPhase());

        // Build in (1,2)
        currentPlayerCard.build(board.getTile(1,2));
        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
    }

    @Test
    public void setTurnPhase_GodPower() {
        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());

        // Select worker in (1,2)
        currentPlayerCard.selectWorker(currentPlayer.getWorkers()[0]);
        assertEquals(ETurnPhase.GODPOWER, game.getTurnPhase());

        // Use Prometheus GODPOWER to build before moving
        currentPlayerCard.useGodPower(true);
        assertEquals(ETurnPhase.BUILD, game.getTurnPhase());

        // Build in (1,1)
        currentPlayerCard.build(board.getTile(1,1));
        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());

        // Move worker in (1,3)
        currentPlayerCard.move(board.getTile(1,3));
        assertEquals(ETurnPhase.BUILD, game.getTurnPhase());

        // Build in (1,2)
        currentPlayerCard.build(board.getTile(1,2));
        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
    }

    /**
     * All upwards
     * -> return true
     *
     * 1 1
     * 1 W
     */
    @Test
    public void canLockSelf_AllUp() {
        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());

        // Select worker in (4,4)
        currentPlayerCard.selectWorker(currentPlayer.getWorkers()[1]);
        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());

        //The worker will lock himself by building before moving
        assertNotEquals(ETurnPhase.GODPOWER, game.getTurnPhase());
    }

    /**
     * 1 way movement (not upwards) & >1 possible builds
     * -> return false + "illegalToBuild" tile
     *
     * 2 0
     * 2 W
     *
     */
    @Test
    public void canLockSelf_1WayFalse(){
        game = GameFactory.buildGameFromFile("gamePrometheus.txt", "boardPrometheus02.txt");
        board = game.getBoard();
        currentPlayer = game.getCurrentPlayer();
        currentPlayerCard = currentPlayer.getCard().getPlayable();


        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());

        // Select worker in (4,4)
        currentPlayerCard.selectWorker(currentPlayer.getWorkers()[1]);
        assertEquals(ETurnPhase.GODPOWER, game.getTurnPhase());

        //The worker will lock himself by building in this tile
        assertNotNull(
                ((PrometheusCard) currentPlayerCard).notBuild
        );
    }

    /**
     * 1 way movement (not upwards) & 1 possible build
     * -> return true
     *
     * D D D
     * D W D
     * D 0 D
     */
    @Test
    public void canLockSelf_1WayTrue(){
        game = GameFactory.buildGameFromFile("gamePrometheus.txt", "boardPrometheus02.txt");
        board = game.getBoard();
        currentPlayer = game.getCurrentPlayer();
        currentPlayerCard = currentPlayer.getCard().getPlayable();


        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());

        // Select worker in (1,2)
        currentPlayerCard.selectWorker(currentPlayer.getWorkers()[0]);
        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());
    }

    /*
    >1 way (is a test case?)
    Easy false
    D 0
    0 W

    Downwards tile (is a test case?)
    Easy false
    D 0
    D 1W
    */

}