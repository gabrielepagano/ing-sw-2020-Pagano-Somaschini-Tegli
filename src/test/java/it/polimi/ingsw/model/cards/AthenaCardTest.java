package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.events.Event;
import it.polimi.ingsw.utils.GameFactory;
import it.polimi.ingsw.view.RemoteViewDummy;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AthenaCardTest {

    @Test
    public void athenaMinotaurApolloMinotaurGetTilesToMoveAthenaUpTest() {

        Game game = GameFactory.buildGameFromFile("games/gameAthena+Minotaur+Apollo.txt", "boards/boardAthena+Minotaur+Apollo_0.txt");
        Board board = game.getBoard();
        // Player in 0 is BotAthena, player in 1 is BotMinotaur, player in 2 is BotApollo
        List<Player> players = game.getPlayers();

        // Athena (BotAthena) is first player
        RemoteViewDummy rvd = (RemoteViewDummy) game.getCurrentPlayer().getRemoteView();
        List<Event> actualReceivedEvents = rvd.getReceivedEvents();

        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
        assertFalse(game.isGameOver());
        List<String> expectedPlayerNicknamesList = new ArrayList<>();
        expectedPlayerNicknamesList.add("BotAthena");
        expectedPlayerNicknamesList.add("BotMinotaur");
        expectedPlayerNicknamesList.add("BotApollo");
        assertEquals(expectedPlayerNicknamesList, game.getPlayerNicknamesList());

        // Select worker on (3,3)
        game.getCurrentPlayerCard().getPlayable().selectWorker(game.getCurrentPlayer().getWorkers()[1]);

        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "boards/boardAthena+Minotaur+Apollo_0.txt"));
        assertFalse(game.isGameOver());

        // Move worker on (4,3)
        game.getCurrentPlayerCard().getPlayable().move(board.getTile(4,3));

        assertEquals(ETurnPhase.BUILD, game.getTurnPhase());
        assertEquals("BotAthena", game.getCurrentPlayer().getNickname());
        assertTrue(BoardTest.boardTest(board, "boards/boardAthena+Minotaur+Apollo_0.txt"));
        assertFalse(game.isGameOver());

        // Build on (4,2)
        game.getCurrentPlayerCard().getPlayable().build(board.getTile(4,2));

        // Now it is Minotaur's (BotMinotaur) turn
        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
        assertEquals("BotMinotaur", game.getCurrentPlayer().getNickname());
        assertTrue(BoardTest.boardTest(board, "boards/boardAthena+Minotaur+Apollo_1.txt"));
        assertFalse(game.isGameOver());

        // Select worker on (2,2)
        game.getCurrentPlayerCard().getPlayable().selectWorker(game.getCurrentPlayer().getWorkers()[0]);

        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());
        assertEquals("BotMinotaur", game.getCurrentPlayer().getNickname());
        assertTrue(BoardTest.boardTest(board, "boards/boardAthena+Minotaur+Apollo_1.txt"));
        assertFalse(game.isGameOver());

        List<Tile> actualTilesToMove = game.getCurrentPlayerCard().getPlayable().getTilesToMove();
        assertTrue(actualTilesToMove.contains(board.getTile(1,1)));
        assertTrue(actualTilesToMove.contains(board.getTile(1,2)));
        assertTrue(actualTilesToMove.contains(board.getTile(1,3)));
        assertTrue(actualTilesToMove.contains(board.getTile(2,1)));
        assertTrue(actualTilesToMove.contains(board.getTile(2,3)));
        assertTrue(actualTilesToMove.contains(board.getTile(3,2)));
        assertTrue(actualTilesToMove.contains(board.getTile(3,3)));
        assertEquals(7, actualTilesToMove.size());

    }

    @Test
    public void athenaMinotaurApolloMinotaurGetTilesToMoveNoAthenaUpTest() {

        Game game = GameFactory.buildGameFromFile("games/gameAthena+Minotaur+Apollo.txt", "boards/boardAthena+Minotaur+Apollo_0.txt");
        Board board = game.getBoard();
        // Player in 0 is BotAthena, player in 1 is BotMinotaur, player in 2 is BotApollo
        List<Player> players = game.getPlayers();

        // Athena (BotAthena) is first player
        RemoteViewDummy rvd = (RemoteViewDummy) game.getCurrentPlayer().getRemoteView();
        List<Event> actualReceivedEvents = rvd.getReceivedEvents();

        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
        assertFalse(game.isGameOver());
        List<String> expectedPlayerNicknamesList = new ArrayList<>();
        expectedPlayerNicknamesList.add("BotAthena");
        expectedPlayerNicknamesList.add("BotMinotaur");
        expectedPlayerNicknamesList.add("BotApollo");
        assertEquals(expectedPlayerNicknamesList, game.getPlayerNicknamesList());

        // Select worker on (3,3)
        game.getCurrentPlayerCard().getPlayable().selectWorker(game.getCurrentPlayer().getWorkers()[1]);

        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "boards/boardAthena+Minotaur+Apollo_0.txt"));
        assertFalse(game.isGameOver());

        // Move worker on (3,2)
        game.getCurrentPlayerCard().getPlayable().move(board.getTile(3,2));

        assertEquals(ETurnPhase.BUILD, game.getTurnPhase());
        assertEquals("BotAthena", game.getCurrentPlayer().getNickname());
        assertTrue(BoardTest.boardTest(board, "boards/boardAthena+Minotaur+Apollo_0.txt"));
        assertFalse(game.isGameOver());

        // Build on (4,2)
        game.getCurrentPlayerCard().getPlayable().build(board.getTile(4,2));

        // Now it is Minotaur's (BotMinotaur) turn
        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
        assertEquals("BotMinotaur", game.getCurrentPlayer().getNickname());
        assertTrue(BoardTest.boardTest(board, "boards/boardAthena+Minotaur+Apollo_1.txt"));
        assertFalse(game.isGameOver());

        // Select worker on (2,2)
        game.getCurrentPlayerCard().getPlayable().selectWorker(game.getCurrentPlayer().getWorkers()[0]);

        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());
        assertEquals("BotMinotaur", game.getCurrentPlayer().getNickname());
        assertTrue(BoardTest.boardTest(board, "boards/boardAthena+Minotaur+Apollo_1.txt"));
        assertFalse(game.isGameOver());

        List<Tile> actualTilesToMove = game.getCurrentPlayerCard().getPlayable().getTilesToMove();
        assertTrue(actualTilesToMove.contains(board.getTile(1,1)));
        assertTrue(actualTilesToMove.contains(board.getTile(1,2)));
        assertTrue(actualTilesToMove.contains(board.getTile(1,3)));
        assertTrue(actualTilesToMove.contains(board.getTile(2,1)));
        assertTrue(actualTilesToMove.contains(board.getTile(2,3)));
        assertTrue(actualTilesToMove.contains(board.getTile(3,1)));
        assertTrue(actualTilesToMove.contains(board.getTile(3,2)));
        assertTrue(actualTilesToMove.contains(board.getTile(3,3)));
        assertEquals(8, actualTilesToMove.size());

        // Move on (3,2) pushing athena back onto (4,2)
        game.getCurrentPlayerCard().getPlayable().move(board.getTile(3,2));

        assertEquals(ETurnPhase.BUILD, game.getTurnPhase());
        assertEquals("BotMinotaur", game.getCurrentPlayer().getNickname());
        assertTrue(BoardTest.boardTest(board, "boards/boardAthena+Minotaur+Apollo_1.txt"));
        assertFalse(game.isGameOver());
        TileTest.tileTest(4,2, players.get(0).getWorkers()[1], false, ETileLevel.LEVEL1, board.getTile(4,2));
        TileTest.tileTest(3,2, game.getCurrentPlayer().getWorkers()[0], false, ETileLevel.GROUND, board.getTile(3,2));
        TileTest.tileTest(2,2, null, false, ETileLevel.LEVEL1, board.getTile(2,2));

        List<Tile> actualTilesToBuild = game.getCurrentPlayerCard().getPlayable().getTilesToBuild();
        assertTrue(actualTilesToBuild.contains(board.getTile(2,1)));
        assertTrue(actualTilesToBuild.contains(board.getTile(2,2)));
        assertTrue(actualTilesToBuild.contains(board.getTile(2,3)));
        assertTrue(actualTilesToBuild.contains(board.getTile(3,1)));
        assertTrue(actualTilesToBuild.contains(board.getTile(3,3)));
        assertTrue(actualTilesToBuild.contains(board.getTile(4,1)));
        assertTrue(actualTilesToBuild.contains(board.getTile(4,3)));
        assertFalse(actualTilesToBuild.contains(board.getTile(4,2)));
        assertFalse(actualTilesToBuild.contains(board.getTile(3,2)));
        assertEquals(7, actualTilesToBuild.size());

    }

}