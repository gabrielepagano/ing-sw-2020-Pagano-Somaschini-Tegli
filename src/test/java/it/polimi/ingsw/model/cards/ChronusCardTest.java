package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.events.Event;
import it.polimi.ingsw.utils.GameFactory;
import it.polimi.ingsw.view.RemoteViewDummy;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ChronusCardTest {

    @Test
    public void chronusGodPowerSimpleTest() {

        Game game = GameFactory.buildGameFromFile("games/gameChronus.txt", "boards/boardChronus.txt");
        Board board = game.getBoard();
        // Player in 0 is BotChronus
        List<Player> players = game.getPlayers();

        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
        assertFalse(game.isGameOver());
        List<String> expectedPlayerNicknamesList = new ArrayList<>();
        expectedPlayerNicknamesList.add("BotChronus");
        assertEquals(expectedPlayerNicknamesList, game.getPlayerNicknamesList());

        // Chronus (BotChronus) is first player
        RemoteViewDummy rvd = (RemoteViewDummy) game.getCurrentPlayer().getRemoteView();
        List<Event> actualReceivedEvents = rvd.getReceivedEvents();

        // Select worker in (3,0)
        game.getCurrentPlayerCard().getPlayable().selectWorker(game.getCurrentPlayer().getWorkers()[0]);

        List<Tile> actualTilesToMove = game.getCurrentPlayerCard().getPlayable().getTilesToMove();
        assertTrue(actualTilesToMove.contains(board.getTile(2,1)));
        assertTrue(actualTilesToMove.contains(board.getTile(4,0)));
        assertTrue(actualTilesToMove.contains(board.getTile(4,1)));
        assertEquals(3, actualTilesToMove.size());

        // Move worker to (2,1)
        game.getCurrentPlayerCard().getPlayable().move(board.getTile(2,1));
        assertFalse(game.isGameOver());
        assertEquals(0, game.getCompleteTowers());

        // Build on (2,0)
        game.getCurrentPlayerCard().getPlayable().build(board.getTile(2,0));
        assertFalse(game.isGameOver());
        assertEquals(1, game.getCompleteTowers());
        TileTest.tileTest(2, 0, null, true, ETileLevel.LEVEL3, board.getTile(2,0));

        // Select worker in (2,1)
        game.getCurrentPlayerCard().getPlayable().selectWorker(game.getCurrentPlayer().getWorkers()[0]);

        // Move worker to (2,2)
        game.getCurrentPlayerCard().getPlayable().move(board.getTile(2,2));
        assertFalse(game.isGameOver());

        // Build on (3,1)
        game.getCurrentPlayerCard().getPlayable().build(board.getTile(3,1));
        assertFalse(game.isGameOver());
        assertEquals(2, game.getCompleteTowers());
        TileTest.tileTest(3, 1, null, true, ETileLevel.LEVEL3, board.getTile(3,1));

        // Select worker in (2,2)
        game.getCurrentPlayerCard().getPlayable().selectWorker(game.getCurrentPlayer().getWorkers()[0]);

        // Move worker to (2,3)
        game.getCurrentPlayerCard().getPlayable().move(board.getTile(2,3));
        assertFalse(game.isGameOver());

        // Build on (3,2)
        game.getCurrentPlayerCard().getPlayable().build(board.getTile(3,2));
        assertFalse(game.isGameOver());
        assertEquals(3, game.getCompleteTowers());
        TileTest.tileTest(3, 2, null, true, ETileLevel.LEVEL3, board.getTile(3,2));

        // Select worker in (2,3)
        game.getCurrentPlayerCard().getPlayable().selectWorker(game.getCurrentPlayer().getWorkers()[0]);

        // Move worker to (1,4)
        game.getCurrentPlayerCard().getPlayable().move(board.getTile(1,4));
        assertFalse(game.isGameOver());

        // Build on (2,4)
        game.getCurrentPlayerCard().getPlayable().build(board.getTile(2,4));
        assertFalse(game.isGameOver());
        assertEquals(4, game.getCompleteTowers());
        TileTest.tileTest(2, 4, null, true, ETileLevel.LEVEL3, board.getTile(2,4));

        // Select worker in (3,4)
        game.getCurrentPlayerCard().getPlayable().selectWorker(game.getCurrentPlayer().getWorkers()[1]);

        // Move worker to (2,3)
        game.getCurrentPlayerCard().getPlayable().move(board.getTile(2,3));
        assertFalse(game.isGameOver());

        // Build on (1,3)
        game.getCurrentPlayerCard().getPlayable().build(board.getTile(1,3));
        assertFalse(game.isGameOver());
        assertEquals(4, game.getCompleteTowers());
        TileTest.tileTest(1, 3, null, false, ETileLevel.LEVEL3, board.getTile(1,3));

        // Select worker in (2,3)
        game.getCurrentPlayerCard().getPlayable().selectWorker(game.getCurrentPlayer().getWorkers()[1]);

        // Move worker to (2,2)
        game.getCurrentPlayerCard().getPlayable().move(board.getTile(2,2));
        assertFalse(game.isGameOver());

        // Build on (3,3)
        game.getCurrentPlayerCard().getPlayable().build(board.getTile(3,3));
        assertTrue(game.isGameOver());
        assertEquals(5, game.getCompleteTowers());
        TileTest.tileTest(3, 3, null, true, ETileLevel.LEVEL3, board.getTile(3,3));

    }

    @Test
    public void chronusWithApolloSimpleTest() {

        Game game = GameFactory.buildGameFromFile("games/gameChronus+Apollo.txt", "boards/boardChronus+Apollo_0.txt");
        Board board = game.getBoard();
        // Player in 0 is BotChronus, player in 1 is BotApollo
        List<Player> players = game.getPlayers();

        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
        assertFalse(game.isGameOver());
        List<String> expectedPlayerNicknamesList = new ArrayList<>();
        expectedPlayerNicknamesList.add("BotChronus");
        expectedPlayerNicknamesList.add("BotApollo");
        assertEquals(expectedPlayerNicknamesList, game.getPlayerNicknamesList());

        // Chronus (BotChronus) is first player
        RemoteViewDummy rvd = (RemoteViewDummy) game.getCurrentPlayer().getRemoteView();
        List<Event> actualReceivedEvents = rvd.getReceivedEvents();

        // Select worker in (4,4)
        game.getCurrentPlayerCard().getPlayable().selectWorker(game.getCurrentPlayer().getWorkers()[1]);

        // Move worker to (3,4)
        game.getCurrentPlayerCard().getPlayable().move(board.getTile(3,4));

        assertFalse(game.isGameOver());
        assertEquals(0, game.getCompleteTowers());

        // Build on (3,3)
        game.getCurrentPlayerCard().getPlayable().build(board.getTile(3,3));

        // Now it is Apollo's (BotApollo) turn
        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
        assertEquals("BotApollo", game.getCurrentPlayer().getNickname());
        assertFalse(game.isGameOver());
        assertEquals(1, game.getCompleteTowers());

        // Select worker in (0,0)
        game.getCurrentPlayerCard().getPlayable().selectWorker(game.getCurrentPlayer().getWorkers()[0]);

        // Move worker to (1,0)
        game.getCurrentPlayerCard().getPlayable().move(board.getTile(1,0));

        assertFalse(game.isGameOver());
        assertEquals(1, game.getCompleteTowers());

        // Build on (1,1)
        game.getCurrentPlayerCard().getPlayable().build(board.getTile(1,1));

        // Now it is Chronus' (BotChronus) turn
        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
        assertEquals("BotChronus", game.getCurrentPlayer().getNickname());
        assertFalse(game.isGameOver());
        assertEquals(2, game.getCompleteTowers());

        // Select worker in (3,4)
        game.getCurrentPlayerCard().getPlayable().selectWorker(game.getCurrentPlayer().getWorkers()[1]);

        // Move worker to (2,4)
        game.getCurrentPlayerCard().getPlayable().move(board.getTile(2,4));

        assertFalse(game.isGameOver());
        assertEquals(2, game.getCompleteTowers());

        // Build on (2,3)
        game.getCurrentPlayerCard().getPlayable().build(board.getTile(2,3));

        // Now it is Apollo's (BotApollo) turn
        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
        assertEquals("BotApollo", game.getCurrentPlayer().getNickname());
        assertFalse(game.isGameOver());
        assertEquals(3, game.getCompleteTowers());

        // Select worker in (1,0)
        game.getCurrentPlayerCard().getPlayable().selectWorker(game.getCurrentPlayer().getWorkers()[0]);

        // Move worker to (2,0)
        game.getCurrentPlayerCard().getPlayable().move(board.getTile(2,0));

        assertFalse(game.isGameOver());
        assertEquals(3, game.getCompleteTowers());

        // Build on (2,1)
        game.getCurrentPlayerCard().getPlayable().build(board.getTile(2,1));

        // Now it is Chronus' (BotChronus) turn
        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
        assertEquals("BotChronus", game.getCurrentPlayer().getNickname());
        assertFalse(game.isGameOver());
        assertEquals(4, game.getCompleteTowers());

        // Select worker in (2,4)
        game.getCurrentPlayerCard().getPlayable().selectWorker(game.getCurrentPlayer().getWorkers()[1]);

        // Move worker to (1,4)
        game.getCurrentPlayerCard().getPlayable().move(board.getTile(1,4));

        assertFalse(game.isGameOver());
        assertEquals(4, game.getCompleteTowers());

        // Build on (0,4)
        game.getCurrentPlayerCard().getPlayable().build(board.getTile(0,4));

        // Now it is Apollo's (BotApollo) turn
        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
        assertEquals("BotApollo", game.getCurrentPlayer().getNickname());
        TileTest.tileTest(0,4,null, false, ETileLevel.LEVEL1, board.getTile(0,4));
        assertFalse(game.isGameOver());
        assertEquals(4, game.getCompleteTowers());

        // Select worker in (2,0)
        game.getCurrentPlayerCard().getPlayable().selectWorker(game.getCurrentPlayer().getWorkers()[0]);

        // Move worker to (3,0)
        game.getCurrentPlayerCard().getPlayable().move(board.getTile(3,0));

        assertFalse(game.isGameOver());
        assertEquals(4, game.getCompleteTowers());

        // Build on (3,1)
        game.getCurrentPlayerCard().getPlayable().build(board.getTile(3,1));

        // Now it is Chronus' (BotChronus) turn
        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
        assertEquals("BotChronus", game.getCurrentPlayer().getNickname());
        assertTrue(game.isGameOver());
        assertEquals(5, game.getCompleteTowers());

    }

}