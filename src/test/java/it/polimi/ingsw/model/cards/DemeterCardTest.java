package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.events.Event;
import it.polimi.ingsw.utils.GameFactory;
import it.polimi.ingsw.view.RemoteViewDummy;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DemeterCardTest {

    @Test
    public void cantUseGodPowerTest() {

        Game game = GameFactory.buildGameFromFile("games/gameDemeter+Pan.txt", "boards/boardDemeter+Pan_0.txt");
        Board board = game.getBoard();
        // Player in 0 is BotDemeter, player in 1 is BotPan
        List<Player> players = game.getPlayers();

        // Demeter (BotDemeter) is first player
        RemoteViewDummy rvd = (RemoteViewDummy) game.getCurrentPlayer().getRemoteView();
        List<Event> actualReceivedEvents = rvd.getReceivedEvents();

        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
        assertFalse(game.isGameOver());
        List<String> expectedPlayerNicknamesList = new ArrayList<>();
        expectedPlayerNicknamesList.add("BotDemeter");
        expectedPlayerNicknamesList.add("BotPan");
        assertEquals(expectedPlayerNicknamesList, game.getPlayerNicknamesList());

        // Select worker on (3,4)
        game.getCurrentPlayerCard().getPlayable().selectWorker(game.getCurrentPlayer().getWorkers()[1]);

        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "boards/boardDemeter+Pan_0.txt"));
        assertFalse(game.isGameOver());

        // Move worker to (4,4)
        game.getCurrentPlayerCard().getPlayable().move(board.getTile(4,4));

        assertEquals(ETurnPhase.BUILD, game.getTurnPhase());
        assertEquals("BotDemeter", game.getCurrentPlayer().getNickname());
        assertTrue(BoardTest.boardTest(board, "boards/boardDemeter+Pan_0.txt"));
        assertFalse(game.isGameOver());

        List<Tile> actualTilesToBuild = game.getCurrentPlayerCard().getPlayable().getTilesToBuild();
        assertTrue(actualTilesToBuild.contains(board.getTile(3,4)));
        assertEquals(1, actualTilesToBuild.size());

        // Build on (3,4)
        game.getCurrentPlayerCard().getPlayable().build(board.getTile(3,4));

        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
        assertEquals("BotPan", game.getCurrentPlayer().getNickname());
        assertTrue(BoardTest.boardTest(board, "boards/boardDemeter+Pan_1.txt"));
        assertFalse(game.isGameOver());

    }

    @Test
    public void useGodPowerSimpleTest() {

        Game game = GameFactory.buildGameFromFile("games/gameDemeter+Pan.txt", "boards/boardDemeter+Pan_0.txt");
        Board board = game.getBoard();
        // Player in 0 is BotDemeter, player in 1 is BotPan
        List<Player> players = game.getPlayers();

        // Demeter (BotDemeter) is first player
        RemoteViewDummy rvd = (RemoteViewDummy) game.getCurrentPlayer().getRemoteView();
        List<Event> actualReceivedEvents = rvd.getReceivedEvents();

        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
        assertFalse(game.isGameOver());

        // Select worker on (1,2)
        game.getCurrentPlayerCard().getPlayable().selectWorker(game.getCurrentPlayer().getWorkers()[0]);

        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "boards/boardDemeter+Pan_0.txt"));
        assertFalse(game.isGameOver());

        // Move worker to (2,2)
        game.getCurrentPlayerCard().getPlayable().move(board.getTile(2,2));

        assertEquals(ETurnPhase.BUILD, game.getTurnPhase());
        assertEquals("BotDemeter", game.getCurrentPlayer().getNickname());
        assertTrue(BoardTest.boardTest(board, "boards/boardDemeter+Pan_0.txt"));
        assertFalse(game.isGameOver());

        List<Tile> actualTilesToBuild = game.getCurrentPlayerCard().getPlayable().getTilesToBuild();
        assertTrue(actualTilesToBuild.contains(board.getTile(1,1)));
        assertTrue(actualTilesToBuild.contains(board.getTile(1,2)));
        assertTrue(actualTilesToBuild.contains(board.getTile(1,3)));
        assertTrue(actualTilesToBuild.contains(board.getTile(2,1)));
        assertTrue(actualTilesToBuild.contains(board.getTile(2,3)));
        assertTrue(actualTilesToBuild.contains(board.getTile(3,1)));
        assertTrue(actualTilesToBuild.contains(board.getTile(3,2)));
        assertEquals(7, actualTilesToBuild.size());

        // Build on (2,3)
        game.getCurrentPlayerCard().getPlayable().build(board.getTile(2,3));

        assertEquals(ETurnPhase.GODPOWER, game.getTurnPhase());
        assertEquals("BotDemeter", game.getCurrentPlayer().getNickname());
        assertTrue(BoardTest.boardTest(board, "boards/boardDemeter+Pan_2.txt"));
        assertFalse(game.isGameOver());

        // User chooses to use god power
        game.getCurrentPlayerCard().getPlayable().useGodPower(true);

        assertEquals(ETurnPhase.BUILD, game.getTurnPhase());
        assertEquals("BotDemeter", game.getCurrentPlayer().getNickname());
        assertTrue(BoardTest.boardTest(board, "boards/boardDemeter+Pan_2.txt"));
        assertFalse(game.isGameOver());

        actualTilesToBuild = game.getCurrentPlayerCard().getPlayable().getTilesToBuild();
        assertTrue(actualTilesToBuild.contains(board.getTile(1,1)));
        assertTrue(actualTilesToBuild.contains(board.getTile(1,2)));
        assertTrue(actualTilesToBuild.contains(board.getTile(1,3)));
        assertTrue(actualTilesToBuild.contains(board.getTile(2,1)));
        assertTrue(actualTilesToBuild.contains(board.getTile(3,1)));
        assertTrue(actualTilesToBuild.contains(board.getTile(3,2)));
        assertEquals(6, actualTilesToBuild.size());

        // Build (dome) on (1,1)
        game.getCurrentPlayerCard().getPlayable().build(board.getTile(1,1));

        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
        assertEquals("BotPan", game.getCurrentPlayer().getNickname());
        assertTrue(BoardTest.boardTest(board, "boards/boardDemeter+Pan_3.txt"));
        assertFalse(game.isGameOver());

    }

    @Test
    public void useGodPowerBuild2DomesTest() {

        Game game = GameFactory.buildGameFromFile("games/gameDemeter+Pan.txt", "boards/boardDemeter+Pan_0.txt");
        Board board = game.getBoard();
        // Player in 0 is BotDemeter, player in 1 is BotPan
        List<Player> players = game.getPlayers();

        // Demeter (BotDemeter) is first player
        RemoteViewDummy rvd = (RemoteViewDummy) game.getCurrentPlayer().getRemoteView();
        List<Event> actualReceivedEvents = rvd.getReceivedEvents();

        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
        assertFalse(game.isGameOver());

        // Select worker on (1,2)
        game.getCurrentPlayerCard().getPlayable().selectWorker(game.getCurrentPlayer().getWorkers()[0]);

        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "boards/boardDemeter+Pan_0.txt"));
        assertFalse(game.isGameOver());

        // Move worker to (2,2)
        game.getCurrentPlayerCard().getPlayable().move(board.getTile(2,2));

        assertEquals(ETurnPhase.BUILD, game.getTurnPhase());
        assertEquals("BotDemeter", game.getCurrentPlayer().getNickname());
        assertTrue(BoardTest.boardTest(board, "boards/boardDemeter+Pan_0.txt"));
        assertFalse(game.isGameOver());

        // Build on (1,1)
        game.getCurrentPlayerCard().getPlayable().build(board.getTile(1,1));

        assertEquals(ETurnPhase.GODPOWER, game.getTurnPhase());
        assertEquals("BotDemeter", game.getCurrentPlayer().getNickname());
        assertTrue(BoardTest.boardTest(board, "boards/boardDemeter+Pan_4.txt"));
        assertFalse(game.isGameOver());

        // User chooses to use god power
        game.getCurrentPlayerCard().getPlayable().useGodPower(true);

        assertEquals(ETurnPhase.BUILD, game.getTurnPhase());
        assertEquals("BotDemeter", game.getCurrentPlayer().getNickname());
        assertTrue(BoardTest.boardTest(board, "boards/boardDemeter+Pan_4.txt"));
        assertFalse(game.isGameOver());

        List<Tile> actualTilesToBuild = game.getCurrentPlayerCard().getPlayable().getTilesToBuild();
        assertTrue(actualTilesToBuild.contains(board.getTile(1,2)));
        assertTrue(actualTilesToBuild.contains(board.getTile(1,3)));
        assertTrue(actualTilesToBuild.contains(board.getTile(2,1)));
        assertTrue(actualTilesToBuild.contains(board.getTile(2,3)));
        assertTrue(actualTilesToBuild.contains(board.getTile(3,1)));
        assertTrue(actualTilesToBuild.contains(board.getTile(3,2)));
        assertEquals(6, actualTilesToBuild.size());

        // Build (dome) on (2,1)
        game.getCurrentPlayerCard().getPlayable().build(board.getTile(2,1));

        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
        assertEquals("BotPan", game.getCurrentPlayer().getNickname());
        assertTrue(BoardTest.boardTest(board, "boards/boardDemeter+Pan_5.txt"));
        assertFalse(game.isGameOver());

    }

    @Test
    public void denyGodPowerTest() {

        Game game = GameFactory.buildGameFromFile("games/gameDemeter+Pan.txt", "boards/boardDemeter+Pan_0.txt");
        Board board = game.getBoard();
        // Player in 0 is BotDemeter, player in 1 is BotPan
        List<Player> players = game.getPlayers();

        // Demeter (BotDemeter) is first player
        RemoteViewDummy rvd = (RemoteViewDummy) game.getCurrentPlayer().getRemoteView();
        List<Event> actualReceivedEvents = rvd.getReceivedEvents();

        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
        assertFalse(game.isGameOver());

        // Select worker on (1,2)
        game.getCurrentPlayerCard().getPlayable().selectWorker(game.getCurrentPlayer().getWorkers()[0]);

        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "boards/boardDemeter+Pan_0.txt"));
        assertFalse(game.isGameOver());

        // Move worker to (2,2)
        game.getCurrentPlayerCard().getPlayable().move(board.getTile(2,2));

        assertEquals(ETurnPhase.BUILD, game.getTurnPhase());
        assertEquals("BotDemeter", game.getCurrentPlayer().getNickname());
        assertTrue(BoardTest.boardTest(board, "boards/boardDemeter+Pan_0.txt"));
        assertFalse(game.isGameOver());

        // Build on (1,1)
        game.getCurrentPlayerCard().getPlayable().build(board.getTile(1,1));

        assertEquals(ETurnPhase.GODPOWER, game.getTurnPhase());
        assertEquals("BotDemeter", game.getCurrentPlayer().getNickname());
        assertTrue(BoardTest.boardTest(board, "boards/boardDemeter+Pan_4.txt"));
        assertFalse(game.isGameOver());

        // User chooses NOT to use god power
        game.getCurrentPlayerCard().getPlayable().useGodPower(false);

        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
        assertEquals("BotPan", game.getCurrentPlayer().getNickname());
        assertTrue(BoardTest.boardTest(board, "boards/boardDemeter+Pan_4.txt"));
        assertFalse(game.isGameOver());

    }

}