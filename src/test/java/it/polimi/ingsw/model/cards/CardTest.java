package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.events.*;
import it.polimi.ingsw.utils.GameFactory;
import it.polimi.ingsw.view.RemoteViewDummy;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CardTest {

    static Game game;
    static Board board;

    @BeforeEach
    public void setup(){
        game = GameFactory.buildGameFromFile("gameApollo.txt", "board3.0.txt");
        board = game.getBoard();
    }

    @Test
    public void getTilesToMoveFirstWorkerTest() {

        RemoteViewDummy rvd = (RemoteViewDummy) game.getCurrentPlayer().getRemoteView();
        List<Event> actualReceivedEvents = rvd.getReceivedEvents();

        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
        assertFalse(game.isGameOver());
        List<String> expectedPlayerNicknamesList = new ArrayList<>();
        expectedPlayerNicknamesList.add("Bot1");
        assertEquals(expectedPlayerNicknamesList, game.getPlayerNicknamesList());

        // Select worker in (1,2)
        game.getCurrentPlayerCard().getPlayable().selectWorker(game.getCurrentPlayer().getWorkers()[0]);
        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "board3.0.txt"));

        List<Tile> actualTilesToMove = game.getCurrentPlayerCard().getPlayable().getTilesToMove();
        assertTrue(actualTilesToMove.contains(board.getTile(0,1)));
        assertTrue(actualTilesToMove.contains(board.getTile(0,2)));
        assertTrue(actualTilesToMove.contains(board.getTile(0,3)));
        assertTrue(actualTilesToMove.contains(board.getTile(1,1)));
        assertTrue(actualTilesToMove.contains(board.getTile(1,3)));
        assertTrue(actualTilesToMove.contains(board.getTile(2,1)));
        assertTrue(actualTilesToMove.contains(board.getTile(2,2)));
        assertEquals(7, actualTilesToMove.size());
        // Explicit check (not necessary)
        assertFalse(actualTilesToMove.contains(board.getTile(1,2)));
        assertFalse(actualTilesToMove.contains(board.getTile(2,3)));
        rvd.clearReceivedEvents();

        assertFalse(game.isGameOver());
        assertEquals(expectedPlayerNicknamesList, game.getPlayerNicknamesList());

    }

    @Test
    public void getTilesToMoveSecondWorkerTest() {

        RemoteViewDummy rvd = (RemoteViewDummy) game.getCurrentPlayer().getRemoteView();
        List<Event> actualReceivedEvents = rvd.getReceivedEvents();

        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
        assertFalse(game.isGameOver());
        List<String> expectedPlayerNicknamesList = new ArrayList<>();
        expectedPlayerNicknamesList.add("Bot1");
        assertEquals(expectedPlayerNicknamesList, game.getPlayerNicknamesList());

        // Select worker in (2,3)
        game.getCurrentPlayerCard().getPlayable().selectWorker(game.getCurrentPlayer().getWorkers()[1]);
        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "board3.0.txt"));

        List<Tile> actualTilesToMove = game.getCurrentPlayerCard().getPlayable().getTilesToMove();
        assertTrue(actualTilesToMove.contains(board.getTile(1,3)));
        assertTrue(actualTilesToMove.contains(board.getTile(1,4)));
        assertTrue(actualTilesToMove.contains(board.getTile(2,2)));
        assertTrue(actualTilesToMove.contains(board.getTile(2,4)));
        assertTrue(actualTilesToMove.contains(board.getTile(3,2)));
        assertTrue(actualTilesToMove.contains(board.getTile(3,3)));
        assertTrue(actualTilesToMove.contains(board.getTile(3,4)));
        assertEquals(7, actualTilesToMove.size());
        // Explicit check (not necessary)
        assertFalse(actualTilesToMove.contains(board.getTile(1,2)));
        assertFalse(actualTilesToMove.contains(board.getTile(2,3)));

        rvd.clearReceivedEvents();

        assertFalse(game.isGameOver());
        assertEquals(expectedPlayerNicknamesList, game.getPlayerNicknamesList());

    }

    @Test
    public void changeWorkerSelectionSimpleTest() {

        RemoteViewDummy rvd = (RemoteViewDummy) game.getCurrentPlayer().getRemoteView();
        List<Event> actualReceivedEvents = rvd.getReceivedEvents();

        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
        assertFalse(game.isGameOver());
        List<String> expectedPlayerNicknamesList = new ArrayList<>();
        expectedPlayerNicknamesList.add("Bot1");
        assertEquals(expectedPlayerNicknamesList, game.getPlayerNicknamesList());

        // Select worker in (1,2)
        game.getCurrentPlayerCard().getPlayable().selectWorker(game.getCurrentPlayer().getWorkers()[0]);
        rvd.clearReceivedEvents();
        assertTrue(BoardTest.boardTest(board, "board3.0.txt"));

        // Change worker selection to (2,3)
        game.getCurrentPlayerCard().getPlayable().move(board.getTile(2,3));
        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "board3.0.txt"));

        List<Tile> actualTilesToMove = game.getCurrentPlayerCard().getPlayable().getTilesToMove();
        assertTrue(actualTilesToMove.contains(board.getTile(1,3)));
        assertTrue(actualTilesToMove.contains(board.getTile(1,4)));
        assertTrue(actualTilesToMove.contains(board.getTile(2,2)));
        assertTrue(actualTilesToMove.contains(board.getTile(2,4)));
        assertTrue(actualTilesToMove.contains(board.getTile(3,2)));
        assertTrue(actualTilesToMove.contains(board.getTile(3,3)));
        assertTrue(actualTilesToMove.contains(board.getTile(3,4)));
        assertEquals(7, actualTilesToMove.size());
        // Explicit check (not necessary)
        assertFalse(actualTilesToMove.contains(board.getTile(1,2)));
        assertFalse(actualTilesToMove.contains(board.getTile(2,3)));

        rvd.clearReceivedEvents();

        assertFalse(game.isGameOver());
        assertEquals(expectedPlayerNicknamesList, game.getPlayerNicknamesList());

    }

    @Test
    public void changeWorkerSelectionAfterRepeatedErrorsTest() {

        RemoteViewDummy rvd = (RemoteViewDummy) game.getCurrentPlayer().getRemoteView();
        List<Event> actualReceivedEvents = rvd.getReceivedEvents();

        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
        assertFalse(game.isGameOver());
        List<String> expectedPlayerNicknamesList = new ArrayList<>();
        expectedPlayerNicknamesList.add("Bot1");
        assertEquals(expectedPlayerNicknamesList, game.getPlayerNicknamesList());

        // Select worker in (1,2)
        game.getCurrentPlayerCard().getPlayable().selectWorker(game.getCurrentPlayer().getWorkers()[0]);
        rvd.clearReceivedEvents();
        assertTrue(BoardTest.boardTest(board, "board3.0.txt"));

        // Try moving to (4,1)
        game.getCurrentPlayerCard().getPlayable().move(board.getTile(4,1));
        assertSame(board.getTile(1,2), game.getCurrentPlayer().getWorkers()[0].getTile());
        assertSame(board.getTile(2,3), game.getCurrentPlayer().getWorkers()[1].getTile());
        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "board3.0.txt"));
        assertFalse(game.isGameOver());
        rvd.clearReceivedEvents();

        // Try moving to (0,0)
        game.getCurrentPlayerCard().getPlayable().move(board.getTile(0,0));
        assertSame(board.getTile(1,2), game.getCurrentPlayer().getWorkers()[0].getTile());
        assertSame(board.getTile(2,3), game.getCurrentPlayer().getWorkers()[1].getTile());
        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "board3.0.txt"));
        assertFalse(game.isGameOver());
        rvd.clearReceivedEvents();

        // Try moving to (2,0)
        game.getCurrentPlayerCard().getPlayable().move(board.getTile(2,0));
        assertSame(board.getTile(1,2), game.getCurrentPlayer().getWorkers()[0].getTile());
        assertSame(board.getTile(2,3), game.getCurrentPlayer().getWorkers()[1].getTile());
        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "board3.0.txt"));
        assertFalse(game.isGameOver());
        rvd.clearReceivedEvents();

        // Change worker selection to (2,3)
        game.getCurrentPlayerCard().getPlayable().move(board.getTile(2,3));

        List<Tile> actualTilesToMove = game.getCurrentPlayerCard().getPlayable().getTilesToMove();
        assertTrue(actualTilesToMove.contains(board.getTile(1,3)));
        assertTrue(actualTilesToMove.contains(board.getTile(1,4)));
        assertTrue(actualTilesToMove.contains(board.getTile(2,2)));
        assertTrue(actualTilesToMove.contains(board.getTile(2,4)));
        assertTrue(actualTilesToMove.contains(board.getTile(3,2)));
        assertTrue(actualTilesToMove.contains(board.getTile(3,3)));
        assertTrue(actualTilesToMove.contains(board.getTile(3,4)));
        assertEquals(7, actualTilesToMove.size());
        // Explicit check (not necessary)
        assertFalse(actualTilesToMove.contains(board.getTile(1,2)));
        assertFalse(actualTilesToMove.contains(board.getTile(2,3)));

        assertSame(board.getTile(1,2), game.getCurrentPlayer().getWorkers()[0].getTile());
        assertSame(board.getTile(2,3), game.getCurrentPlayer().getWorkers()[1].getTile());
        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "board3.0.txt"));
        assertFalse(game.isGameOver());

    }

    @Test
    public void moveSimpleTest() {

        RemoteViewDummy rvd = (RemoteViewDummy) game.getCurrentPlayer().getRemoteView();
        List<Event> actualReceivedEvents = rvd.getReceivedEvents();

        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
        assertFalse(game.isGameOver());
        List<String> expectedPlayerNicknamesList = new ArrayList<>();
        expectedPlayerNicknamesList.add("Bot1");
        assertEquals(expectedPlayerNicknamesList, game.getPlayerNicknamesList());

        // Select worker in (1,2)
        game.getCurrentPlayerCard().getPlayable().selectWorker(game.getCurrentPlayer().getWorkers()[0]);
        rvd.clearReceivedEvents();

        // Move worker to (1,3)
        game.getCurrentPlayerCard().getPlayable().move(board.getTile(1,3));
        assertSame(board.getTile(1,3), game.getCurrentPlayer().getWorkers()[0].getTile());
        assertSame(board.getTile(2,3), game.getCurrentPlayer().getWorkers()[1].getTile());
        assertEquals(ETurnPhase.BUILD, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "board3.0.txt"));
        assertFalse(game.isGameOver());

    }

    @Test
    public void tryChangeWorkerSelectionAfterMoveTest() {

        RemoteViewDummy rvd = (RemoteViewDummy) game.getCurrentPlayer().getRemoteView();
        List<Event> actualReceivedEvents = rvd.getReceivedEvents();

        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
        assertFalse(game.isGameOver());
        List<String> expectedPlayerNicknamesList = new ArrayList<>();
        expectedPlayerNicknamesList.add("Bot1");
        assertEquals(expectedPlayerNicknamesList, game.getPlayerNicknamesList());

        // Select worker in (2,3)
        game.getCurrentPlayerCard().getPlayable().selectWorker(game.getCurrentPlayer().getWorkers()[1]);
        rvd.clearReceivedEvents();

        // Move worker to (2,4)
        game.getCurrentPlayerCard().getPlayable().move(board.getTile(2,4));
        assertSame(board.getTile(1,2), game.getCurrentPlayer().getWorkers()[0].getTile());
        assertSame(board.getTile(2,4), game.getCurrentPlayer().getWorkers()[1].getTile());
        assertEquals(ETurnPhase.BUILD, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "board3.0.txt"));
        assertFalse(game.isGameOver());
        rvd.clearReceivedEvents();

        // Try changing worker selection to (1,2)
        game.getCurrentPlayerCard().getPlayable().build(board.getTile(1,2));
        assertSame(board.getTile(1,2), game.getCurrentPlayer().getWorkers()[0].getTile());
        assertSame(board.getTile(2,4), game.getCurrentPlayer().getWorkers()[1].getTile());
        assertEquals(ETurnPhase.BUILD, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "board3.0.txt"));
        assertFalse(game.isGameOver());

    }

    @Test
    public void tryMoveToInvalidTileTest() {

        RemoteViewDummy rvd = (RemoteViewDummy) game.getCurrentPlayer().getRemoteView();
        List<Event> actualReceivedEvents = rvd.getReceivedEvents();

        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
        assertFalse(game.isGameOver());
        List<String> expectedPlayerNicknamesList = new ArrayList<>();
        expectedPlayerNicknamesList.add("Bot1");
        assertEquals(expectedPlayerNicknamesList, game.getPlayerNicknamesList());

        // Select worker in (2,3)
        game.getCurrentPlayerCard().getPlayable().selectWorker(game.getCurrentPlayer().getWorkers()[1]);
        rvd.clearReceivedEvents();

        // Try moving worker to (2,1)
        game.getCurrentPlayerCard().getPlayable().move(board.getTile(2,1));
        assertSame(board.getTile(1,2), game.getCurrentPlayer().getWorkers()[0].getTile());
        assertSame(board.getTile(2,3), game.getCurrentPlayer().getWorkers()[1].getTile());
        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "board3.0.txt"));
        assertFalse(game.isGameOver());
        rvd.clearReceivedEvents();

        // Try moving worker to (4,4)
        game.getCurrentPlayerCard().getPlayable().move(board.getTile(4,4));
        assertSame(board.getTile(1,2), game.getCurrentPlayer().getWorkers()[0].getTile());
        assertSame(board.getTile(2,3), game.getCurrentPlayer().getWorkers()[1].getTile());
        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "board3.0.txt"));
        assertFalse(game.isGameOver());

    }

    @Test
    public void buildSimpleTest() {

        RemoteViewDummy rvd = (RemoteViewDummy) game.getCurrentPlayer().getRemoteView();
        List<Event> actualReceivedEvents = rvd.getReceivedEvents();

        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
        assertFalse(game.isGameOver());
        List<String> expectedPlayerNicknamesList = new ArrayList<>();
        expectedPlayerNicknamesList.add("Bot1");
        assertEquals(expectedPlayerNicknamesList, game.getPlayerNicknamesList());

        // Select worker in (1,2)
        game.getCurrentPlayerCard().getPlayable().selectWorker(game.getCurrentPlayer().getWorkers()[0]);
        rvd.clearReceivedEvents();

        // Move worker to (1,3)
        game.getCurrentPlayerCard().getPlayable().move(board.getTile(1,3));

        assertSame(board.getTile(1,3), game.getCurrentPlayer().getWorkers()[0].getTile());
        assertSame(board.getTile(2,3), game.getCurrentPlayer().getWorkers()[1].getTile());
        assertEquals(ETurnPhase.BUILD, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "board3.0.txt"));
        assertFalse(game.isGameOver());
        rvd.clearReceivedEvents();

        // Build in (0,4)
        game.getCurrentPlayerCard().getPlayable().build(board.getTile(0,4));
        assertSame(board.getTile(1,3), game.getCurrentPlayer().getWorkers()[0].getTile());
        assertSame(board.getTile(2,3), game.getCurrentPlayer().getWorkers()[1].getTile());
        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "board3.1.txt"));
        assertFalse(game.isGameOver());

    }

    @Test
    public void getTilesToBuildFirstWorkerTest() {

        RemoteViewDummy rvd = (RemoteViewDummy) game.getCurrentPlayer().getRemoteView();
        List<Event> actualReceivedEvents = rvd.getReceivedEvents();

        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
        assertFalse(game.isGameOver());
        List<String> expectedPlayerNicknamesList = new ArrayList<>();
        expectedPlayerNicknamesList.add("Bot1");
        assertEquals(expectedPlayerNicknamesList, game.getPlayerNicknamesList());

        // Select worker in (1,2)
        game.getCurrentPlayerCard().getPlayable().selectWorker(game.getCurrentPlayer().getWorkers()[0]);
        rvd.clearReceivedEvents();

        // Move worker to (1,3)
        game.getCurrentPlayerCard().getPlayable().move(board.getTile(1,3));

        List<Tile> actualTilesToBuild = game.getCurrentPlayerCard().getPlayable().getTilesToBuild();
        assertTrue(actualTilesToBuild.contains(board.getTile(0,2)));
        assertTrue(actualTilesToBuild.contains(board.getTile(0,3)));
        assertTrue(actualTilesToBuild.contains(board.getTile(0,4)));
        assertTrue(actualTilesToBuild.contains(board.getTile(1,2)));
        assertTrue(actualTilesToBuild.contains(board.getTile(1,4)));
        assertTrue(actualTilesToBuild.contains(board.getTile(2,2)));
        assertTrue(actualTilesToBuild.contains(board.getTile(2,4)));
        assertEquals(7, actualTilesToBuild.size());
        // Explicit check (not necessary)
        assertFalse(actualTilesToBuild.contains(board.getTile(1,3)));
        assertFalse(actualTilesToBuild.contains(board.getTile(2,3)));
        assertEquals(ETurnPhase.BUILD, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "board3.0.txt"));
        assertFalse(game.isGameOver());

    }

    @Test
    public void getTilesToBuildSecondWorkerTest() {

        RemoteViewDummy rvd = (RemoteViewDummy) game.getCurrentPlayer().getRemoteView();
        List<Event> actualReceivedEvents = rvd.getReceivedEvents();

        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
        assertFalse(game.isGameOver());
        List<String> expectedPlayerNicknamesList = new ArrayList<>();
        expectedPlayerNicknamesList.add("Bot1");
        assertEquals(expectedPlayerNicknamesList, game.getPlayerNicknamesList());

        // Select worker in (2,3)
        game.getCurrentPlayerCard().getPlayable().selectWorker(game.getCurrentPlayer().getWorkers()[1]);
        rvd.clearReceivedEvents();

        // Move worker to (3,4)
        game.getCurrentPlayerCard().getPlayable().move(board.getTile(3,4));

        List<Tile> actualTilesToBuild = game.getCurrentPlayerCard().getPlayable().getTilesToBuild();
        assertTrue(actualTilesToBuild.contains(board.getTile(2,3)));
        assertTrue(actualTilesToBuild.contains(board.getTile(2,4)));
        assertTrue(actualTilesToBuild.contains(board.getTile(3,3)));
        assertTrue(actualTilesToBuild.contains(board.getTile(4,3)));
        assertTrue(actualTilesToBuild.contains(board.getTile(4,4)));
        assertEquals(5, actualTilesToBuild.size());
        // Explicit check (not necessary)
        assertFalse(actualTilesToBuild.contains(board.getTile(3,4)));
        assertEquals(ETurnPhase.BUILD, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "board3.0.txt"));
        assertFalse(game.isGameOver());

    }

}