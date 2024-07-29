package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.events.*;
import it.polimi.ingsw.utils.GameFactory;
import it.polimi.ingsw.view.RemoteViewDummy;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AtlasCardTest {

    @Test
    public void getTilesToMoveSimpleTest() {

        Game game = GameFactory.buildGameFromFile("games/gameAtlas+Pan.txt", "boards/boardAtlas+Pan_0.txt");
        Board board = game.getBoard();
        // Player in 0 is BotAtlas, player in 1 is BotPan
        List<Player> players = game.getPlayers();

        // Atlas (BotAtlas) is first player
        RemoteViewDummy rvd = (RemoteViewDummy) game.getCurrentPlayer().getRemoteView();
        List<Event> actualReceivedEvents = rvd.getReceivedEvents();

        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
        assertFalse(game.isGameOver());
        List<String> expectedPlayerNicknamesList = new ArrayList<>();
        expectedPlayerNicknamesList.add("BotAtlas");
        expectedPlayerNicknamesList.add("BotPan");
        assertEquals(expectedPlayerNicknamesList, game.getPlayerNicknamesList());

        // Select worker on (1,2)
        game.getCurrentPlayerCard().getPlayable().selectWorker(game.getCurrentPlayer().getWorkers()[1]);
        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "boards/boardAtlas+Pan_0.txt"));

        List<Tile> actualTilesToMove = game.getCurrentPlayerCard().getPlayable().getTilesToMove();
        assertTrue(actualTilesToMove.contains(board.getTile(0,1)));
        assertTrue(actualTilesToMove.contains(board.getTile(0,3)));
        assertTrue(actualTilesToMove.contains(board.getTile(2,1)));
        assertTrue(actualTilesToMove.contains(board.getTile(2,2)));
        assertTrue(actualTilesToMove.contains(board.getTile(2,3)));
        assertEquals(5, actualTilesToMove.size());

        // Change worker selection to (0,4)
        game.getCurrentPlayerCard().getPlayable().move(board.getTile(0,4));
        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "boards/boardAtlas+Pan_0.txt"));

        actualTilesToMove = game.getCurrentPlayerCard().getPlayable().getTilesToMove();
        assertTrue(actualTilesToMove.contains(board.getTile(0,3)));
        assertTrue(actualTilesToMove.contains(board.getTile(1,4)));
        assertEquals(2, actualTilesToMove.size());

    }

    @Test
    public void getTilesToBuildSimpleTest() {

        Game game = GameFactory.buildGameFromFile("games/gameAtlas+Pan.txt", "boards/boardAtlas+Pan_0.txt");
        Board board = game.getBoard();
        // Player in 0 is BotAtlas, player in 1 is BotPan
        List<Player> players = game.getPlayers();

        // Atlas (BotAtlas) is first player
        RemoteViewDummy rvd = (RemoteViewDummy) game.getCurrentPlayer().getRemoteView();
        List<Event> actualReceivedEvents = rvd.getReceivedEvents();

        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
        assertFalse(game.isGameOver());
        List<String> expectedPlayerNicknamesList = new ArrayList<>();
        expectedPlayerNicknamesList.add("BotAtlas");
        expectedPlayerNicknamesList.add("BotPan");
        assertEquals(expectedPlayerNicknamesList, game.getPlayerNicknamesList());

        // Select worker on (0,4)
        game.getCurrentPlayerCard().getPlayable().selectWorker(game.getCurrentPlayer().getWorkers()[0]);

        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "boards/boardAtlas+Pan_0.txt"));

        // Move worker to (1,4)
        game.getCurrentPlayerCard().getPlayable().move(board.getTile(1,4));
        rvd.clearReceivedEvents();

        assertEquals(ETurnPhase.BUILD, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "boards/boardAtlas+Pan_0.txt"));
        assertFalse(game.isGameOver());

        List<Tile> actualTilesToBuild = game.getCurrentPlayerCard().getPlayable().getTilesToBuild();
        assertTrue(actualTilesToBuild.contains(board.getTile(0,3)));
        assertTrue(actualTilesToBuild.contains(board.getTile(0,4)));
        assertTrue(actualTilesToBuild.contains(board.getTile(1,3)));
        assertTrue(actualTilesToBuild.contains(board.getTile(2,3)));
        assertTrue(actualTilesToBuild.contains(board.getTile(2,4)));
        assertEquals(5, actualTilesToBuild.size());

        // Try changing worker selection to (1,2)
        game.getCurrentPlayerCard().getPlayable().build(board.getTile(1,2));

        assertEquals(ETurnPhase.BUILD, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "boards/boardAtlas+Pan_0.txt"));
        assertFalse(game.isGameOver());

        actualTilesToBuild = game.getCurrentPlayerCard().getPlayable().getTilesToBuild();
        assertTrue(actualTilesToBuild.contains(board.getTile(0,3)));
        assertTrue(actualTilesToBuild.contains(board.getTile(0,4)));
        assertTrue(actualTilesToBuild.contains(board.getTile(1,3)));
        assertTrue(actualTilesToBuild.contains(board.getTile(2,3)));
        assertTrue(actualTilesToBuild.contains(board.getTile(2,4)));
        assertEquals(5, actualTilesToBuild.size());

    }

    @Test
    public void buildWithoutNeedForGodPowerTest() {

        Game game = GameFactory.buildGameFromFile("games/gameAtlas+Pan.txt", "boards/boardAtlas+Pan_0.txt");
        Board board = game.getBoard();
        // Player in 0 is BotAtlas, player in 1 is BotAtlas
        List<Player> players = game.getPlayers();

        // Atlas (BotAtlas) is first player
        RemoteViewDummy rvd = (RemoteViewDummy) game.getCurrentPlayer().getRemoteView();
        List<Event> actualReceivedEvents = rvd.getReceivedEvents();

        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
        assertFalse(game.isGameOver());
        List<String> expectedPlayerNicknamesList = new ArrayList<>();
        expectedPlayerNicknamesList.add("BotAtlas");
        expectedPlayerNicknamesList.add("BotPan");
        assertEquals(expectedPlayerNicknamesList, game.getPlayerNicknamesList());

        // Select worker on (0,4)
        game.getCurrentPlayerCard().getPlayable().selectWorker(game.getCurrentPlayer().getWorkers()[0]);

        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());

        // Move worker to (1,4)
        game.getCurrentPlayerCard().getPlayable().move(board.getTile(1,4));
        rvd.clearReceivedEvents();

        assertEquals(ETurnPhase.BUILD, game.getTurnPhase());
        assertFalse(game.isGameOver());

        // Build on (1,3)
        game.getCurrentPlayerCard().getPlayable().build(board.getTile(1,3));

        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "boards/boardAtlas+Pan_1.txt"));
        assertFalse(game.isGameOver());

    }

    @Test
    public void buildWithGodPowerTest() {

        Game game = GameFactory.buildGameFromFile("games/gameAtlas+Pan.txt", "boards/boardAtlas+Pan_0.txt");
        Board board = game.getBoard();
        // Player in 0 is BotAtlas, player in 1 is BotPan
        List<Player> players = game.getPlayers();

        // Atlas (BotAtlas) is first player
        RemoteViewDummy rvd = (RemoteViewDummy) game.getCurrentPlayer().getRemoteView();
        List<Event> actualReceivedEvents = rvd.getReceivedEvents();

        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
        assertFalse(game.isGameOver());

        // Select worker on (1,2)
        game.getCurrentPlayerCard().getPlayable().selectWorker(game.getCurrentPlayer().getWorkers()[1]);

        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());

        // Move worker to (2,2)
        game.getCurrentPlayerCard().getPlayable().move(board.getTile(2,2));

        assertEquals(ETurnPhase.BUILD, game.getTurnPhase());
        assertFalse(game.isGameOver());
        rvd.clearReceivedEvents();

        // Build on (1,1)
        game.getCurrentPlayerCard().getPlayable().build(board.getTile(1,1));

        assertEquals(ETurnPhase.GODPOWER, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "boards/boardAtlas+Pan_0.txt"));
        assertFalse(game.isGameOver());
        rvd.clearReceivedEvents();

        // User chooses to use Atlas god power
        game.getCurrentPlayerCard().getPlayable().useGodPower(true);

        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "boards/boardAtlas+Pan_2.txt"));
        assertEquals("BotPan", game.getCurrentPlayer().getNickname());
        assertFalse(game.isGameOver());

    }

    @Test
    public void buildWithoutGodPowerTest() {

        Game game = GameFactory.buildGameFromFile("games/gameAtlas+Pan.txt", "boards/boardAtlas+Pan_0.txt");
        Board board = game.getBoard();
        // Player in 0 is BotAtlas, player in 1 is BotPan
        List<Player> players = game.getPlayers();

        // Atlas (BotAtlas) is first player
        RemoteViewDummy rvd = (RemoteViewDummy) game.getCurrentPlayer().getRemoteView();
        List<Event> actualReceivedEvents = rvd.getReceivedEvents();

        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
        assertFalse(game.isGameOver());

        // Select worker on (1,2)
        game.getCurrentPlayerCard().getPlayable().selectWorker(game.getCurrentPlayer().getWorkers()[1]);

        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());

        // Move worker to (2,2)
        game.getCurrentPlayerCard().getPlayable().move(board.getTile(2,2));

        assertEquals(ETurnPhase.BUILD, game.getTurnPhase());
        assertFalse(game.isGameOver());
        rvd.clearReceivedEvents();

        // Build on (3,3)
        game.getCurrentPlayerCard().getPlayable().build(board.getTile(3,3));

        assertEquals(ETurnPhase.GODPOWER, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "boards/boardAtlas+Pan_0.txt"));
        assertFalse(game.isGameOver());
        rvd.clearReceivedEvents();

        // User chooses NOT to use Atlas god power
        game.getCurrentPlayerCard().getPlayable().useGodPower(false);

        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "boards/boardAtlas+Pan_3.txt"));
        assertEquals("BotPan", game.getCurrentPlayer().getNickname());
        assertFalse(game.isGameOver());

    }

}