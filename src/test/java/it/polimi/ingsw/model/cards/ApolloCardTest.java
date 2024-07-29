package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.events.ErrorEvent;
import it.polimi.ingsw.network.events.Event;
import it.polimi.ingsw.utils.GameFactory;
import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.view.RemoteViewDummy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ApolloCardTest {

    @Test
    public void apolloWithAthenaTest() {

        Game game = GameFactory.buildGameFromFile("games/gameApollo+Athena.txt", "boards/boardApollo+Athena_0.txt");
        Board board = game.getBoard();
        // Player in 0 is BotAthena, player in 1 is BotApollo
        List<Player> players = game.getPlayers();

        // Athena (BotAthena) is first player
        RemoteViewDummy rvd = (RemoteViewDummy) game.getCurrentPlayer().getRemoteView();
        List<Event> actualReceivedEvents = rvd.getReceivedEvents();

        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
        assertFalse(game.isGameOver());
        List<String> expectedPlayerNicknamesList = new ArrayList<>();
        expectedPlayerNicknamesList.add("BotAthena");
        expectedPlayerNicknamesList.add("BotApollo");
        assertEquals(expectedPlayerNicknamesList, game.getPlayerNicknamesList());

        // Select worker on (0,2)
        game.getCurrentPlayerCard().getPlayable().selectWorker(game.getCurrentPlayer().getWorkers()[0]);

        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "boards/boardApollo+Athena_0.txt"));

        List<Tile> actualTilesToMove = game.getCurrentPlayerCard().getPlayable().getTilesToMove();
        assertTrue(actualTilesToMove.contains(board.getTile(0,1)));
        assertTrue(actualTilesToMove.contains(board.getTile(0,3)));
        assertTrue(actualTilesToMove.contains(board.getTile(1,2)));
        assertTrue(actualTilesToMove.contains(board.getTile(1,3)));
        assertEquals(4, actualTilesToMove.size());
        assertFalse(game.isGameOver());

        // Move worker to (0,1)
        game.getCurrentPlayerCard().getPlayable().move(board.getTile(0,1));

        assertEquals(ETurnPhase.BUILD, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "boards/boardApollo+Athena_0.txt"));

        List<Tile> actualTilesToBuild = game.getCurrentPlayerCard().getPlayable().getTilesToBuild();
        assertTrue(actualTilesToBuild.contains(board.getTile(0,0)));
        assertTrue(actualTilesToBuild.contains(board.getTile(0,2)));
        assertTrue(actualTilesToBuild.contains(board.getTile(1,2)));
        assertEquals(3, actualTilesToBuild.size());
        assertFalse(game.isGameOver());

        // Build in (0,0)
        game.getCurrentPlayerCard().getPlayable().build(board.getTile(0,0));

        // Now it is Apollo's (BotApollo) turn
        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
        assertEquals("BotApollo", game.getCurrentPlayer().getNickname());
        assertTrue(BoardTest.boardTest(board, "boards/boardApollo+Athena_1.txt"));
        assertFalse(game.isGameOver());

        // Select worker on (1,1)
        game.getCurrentPlayerCard().getPlayable().selectWorker(game.getCurrentPlayer().getWorkers()[0]);

        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "boards/boardApollo+Athena_1.txt"));

        actualTilesToMove = game.getCurrentPlayerCard().getPlayable().getTilesToMove();
        assertTrue(actualTilesToMove.contains(board.getTile(0,2)));
        assertTrue(actualTilesToMove.contains(board.getTile(1,0)));
        assertTrue(actualTilesToMove.contains(board.getTile(1,2)));
        assertTrue(actualTilesToMove.contains(board.getTile(2,0)));
        assertTrue(actualTilesToMove.contains(board.getTile(2,1)));
        assertEquals(5, actualTilesToMove.size());
        assertFalse(game.isGameOver());

        // Change worker selection to (2,3)
        game.getCurrentPlayerCard().getPlayable().move(board.getTile(2,3));

        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "boards/boardApollo+Athena_1.txt"));

        actualTilesToMove = game.getCurrentPlayerCard().getPlayable().getTilesToMove();
        assertTrue(actualTilesToMove.contains(board.getTile(1,2)));
        assertTrue(actualTilesToMove.contains(board.getTile(1,3)));
        assertTrue(actualTilesToMove.contains(board.getTile(1,4)));
        assertTrue(actualTilesToMove.contains(board.getTile(2,4)));
        assertTrue(actualTilesToMove.contains(board.getTile(3,2)));
        assertTrue(actualTilesToMove.contains(board.getTile(3,3)));
        assertTrue(actualTilesToMove.contains(board.getTile(3,4)));
        assertEquals(7, actualTilesToMove.size());
        assertFalse(game.isGameOver());

        // Change again worker selection to (1,1)
        game.getCurrentPlayerCard().getPlayable().move(board.getTile(1,1));

        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "boards/boardApollo+Athena_1.txt"));

        actualTilesToMove = game.getCurrentPlayerCard().getPlayable().getTilesToMove();
        assertTrue(actualTilesToMove.contains(board.getTile(0,2)));
        assertTrue(actualTilesToMove.contains(board.getTile(1,0)));
        assertTrue(actualTilesToMove.contains(board.getTile(1,2)));
        assertTrue(actualTilesToMove.contains(board.getTile(2,0)));
        assertTrue(actualTilesToMove.contains(board.getTile(2,1)));
        assertEquals(5, actualTilesToMove.size());
        assertFalse(game.isGameOver());

        // Move worker to (1,0)
        game.getCurrentPlayerCard().getPlayable().move(board.getTile(1,0));

        assertEquals(ETurnPhase.BUILD, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "boards/boardApollo+Athena_1.txt"));
        WorkerTest.workerTest(board.getTile(1,1), players.get(0), 1, players.get(0).getWorkers()[1]);

        actualTilesToBuild = game.getCurrentPlayerCard().getPlayable().getTilesToBuild();
        assertTrue(actualTilesToBuild.contains(board.getTile(0,0)));
        assertTrue(actualTilesToBuild.contains(board.getTile(2,0)));
        assertTrue(actualTilesToBuild.contains(board.getTile(2,1)));
        assertEquals(3, actualTilesToBuild.size());
        assertFalse(game.isGameOver());

        // Build in (2,0)
        game.getCurrentPlayerCard().getPlayable().build(board.getTile(2,0));

        // Now it is Athena's (BotAthena) turn
        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "boards/boardApollo+Athena_2.txt"));
        assertEquals("BotAthena", game.getCurrentPlayer().getNickname());
        assertFalse(game.isGameOver());

        // Select worker on (0,1)
        game.getCurrentPlayerCard().getPlayable().selectWorker(game.getCurrentPlayer().getWorkers()[0]);

        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "boards/boardApollo+Athena_2.txt"));

        // Move worker to (0,0)
        game.getCurrentPlayerCard().getPlayable().move(board.getTile(0,0));

        assertEquals(ETurnPhase.BUILD, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "boards/boardApollo+Athena_2.txt"));

        actualTilesToBuild = game.getCurrentPlayerCard().getPlayable().getTilesToBuild();
        assertTrue(actualTilesToBuild.contains(board.getTile(0,1)));
        assertEquals(1, actualTilesToBuild.size());

        // Build in (0,1)
        game.getCurrentPlayerCard().getPlayable().build(board.getTile(0,1));

        // Now it is Apollo's (BotApollo) turn
        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
        assertEquals("BotApollo", game.getCurrentPlayer().getNickname());
        assertTrue(BoardTest.boardTest(board, "boards/boardApollo+Athena_3.txt"));
        assertFalse(game.isGameOver());

        // Select worker in (2,3)
        game.getCurrentPlayerCard().getPlayable().selectWorker(game.getCurrentPlayer().getWorkers()[1]);

        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "boards/boardApollo+Athena_3.txt"));

        actualTilesToMove = game.getCurrentPlayerCard().getPlayable().getTilesToMove();
        assertTrue(actualTilesToMove.contains(board.getTile(1,2)));
        assertTrue(actualTilesToMove.contains(board.getTile(1,3)));
        assertTrue(actualTilesToMove.contains(board.getTile(1,4)));
        assertTrue(actualTilesToMove.contains(board.getTile(2,2)));
        assertTrue(actualTilesToMove.contains(board.getTile(2,4)));
        assertTrue(actualTilesToMove.contains(board.getTile(3,2)));
        assertTrue(actualTilesToMove.contains(board.getTile(3,3)));
        assertTrue(actualTilesToMove.contains(board.getTile(3,4)));
        assertEquals(8, actualTilesToMove.size());
        assertFalse(game.isGameOver());

        // Change worker selection to (1,0)
        game.getCurrentPlayerCard().getPlayable().move(board.getTile(1,0));

        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "boards/boardApollo+Athena_3.txt"));

        actualTilesToMove = game.getCurrentPlayerCard().getPlayable().getTilesToMove();
        assertTrue(actualTilesToMove.contains(board.getTile(0,0)));
        assertTrue(actualTilesToMove.contains(board.getTile(1,1)));
        assertTrue(actualTilesToMove.contains(board.getTile(2,0)));
        assertTrue(actualTilesToMove.contains(board.getTile(2,1)));
        assertEquals(4, actualTilesToMove.size());
        assertFalse(game.isGameOver());

        // Move worker to (0,0)
        game.getCurrentPlayerCard().getPlayable().move(board.getTile(0,0));

        assertEquals(ETurnPhase.BUILD, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "boards/boardApollo+Athena_3.txt"));
        WorkerTest.workerTest(board.getTile(1,0), players.get(0), 0, players.get(0).getWorkers()[0]);

        actualTilesToBuild = game.getCurrentPlayerCard().getPlayable().getTilesToBuild();
        assertTrue(actualTilesToBuild.contains(board.getTile(0,1)));
        assertEquals(1, actualTilesToBuild.size());

    }

    @Test
    public void apolloWithAthenaCannotBuildAfterMoveTest() {

        Game game = GameFactory.buildGameFromFile("games/gameApollo+Athena1.txt", "boards/boardApollo+Athena1_0.txt");
        Board board = game.getBoard();
        // Player in 0 is BotApollo, player in 1 is BotAthena
        List<Player> players = game.getPlayers();

        // Apollo (BotApollo) is first player
        RemoteViewDummy rvd = (RemoteViewDummy) game.getCurrentPlayer().getRemoteView();
        List<Event> actualReceivedEvents = rvd.getReceivedEvents();

        // Select worker in (1,0)
        game.getCurrentPlayerCard().getPlayable().selectWorker(game.getCurrentPlayer().getWorkers()[1]);

        List<Tile> actualTilesToMove = game.getCurrentPlayerCard().getPlayable().getTilesToMove();
        assertTrue(actualTilesToMove.contains(board.getTile(0,0)));
        assertTrue(actualTilesToMove.contains(board.getTile(1,1)));
        assertTrue(actualTilesToMove.contains(board.getTile(2,0)));
        assertTrue(actualTilesToMove.contains(board.getTile(2,1)));
        assertEquals(4, actualTilesToMove.size());

        // Move worker to (0,0)
        game.getCurrentPlayerCard().getPlayable().move(board.getTile(0,0));

        assertEquals(ETurnPhase.BUILD, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "boards/boardApollo+Athena1_0.txt"));
        assertTrue(game.isGameOver());

    }

    @Test
    public void apolloWithAthenaCannotBuildAfterMoveTest2() {

        Game game = GameFactory.buildGameFromFile("games/gameApollo+Athena2.txt", "boards/boardApollo+Athena2_0.txt");
        Board board = game.getBoard();
        // Player in 0 is BotApollo, player in 1 is BotAthena
        List<Player> players = game.getPlayers();

        // Apollo (BotApollo) is first player
        RemoteViewDummy rvd = (RemoteViewDummy) game.getCurrentPlayer().getRemoteView();
        List<Event> actualReceivedEvents = rvd.getReceivedEvents();

        // Select worker in (1,2)
        game.getCurrentPlayerCard().getPlayable().selectWorker(game.getCurrentPlayer().getWorkers()[0]);

        List<Tile> actualTilesToMove = game.getCurrentPlayerCard().getPlayable().getTilesToMove();
        assertTrue(actualTilesToMove.contains(board.getTile(0,1)));
        assertTrue(actualTilesToMove.contains(board.getTile(0,2)));
        assertTrue(actualTilesToMove.contains(board.getTile(0,3)));
        assertTrue(actualTilesToMove.contains(board.getTile(2,1)));
        assertTrue(actualTilesToMove.contains(board.getTile(2,2)));
        assertEquals(5, actualTilesToMove.size());

        // Move worker to (2,2)
        game.getCurrentPlayerCard().getPlayable().move(board.getTile(2,2));

        assertEquals(ETurnPhase.BUILD, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "boards/boardApollo+Athena2_0.txt"));
        assertTrue(game.isGameOver());

    }

    @Test
    public void apolloWithAthenaCannotMoveTest() {

        Game game = GameFactory.buildGameFromFile("games/gameApollo+Athena3.txt", "boards/boardApollo+Athena3_0.txt");
        Board board = game.getBoard();
        // Player in 0 is BotAthena, player in 1 is BotApollo
        List<Player> players = game.getPlayers();

        // Athena (BotAthena) is first player
        RemoteViewDummy rvd = (RemoteViewDummy) game.getCurrentPlayer().getRemoteView();
        List<Event> actualReceivedEvents = rvd.getReceivedEvents();

        // Select worker on (3,3)
        game.getCurrentPlayerCard().getPlayable().selectWorker(game.getCurrentPlayer().getWorkers()[1]);

        List<Tile> actualTilesToMove = game.getCurrentPlayerCard().getPlayable().getTilesToMove();
        assertTrue(actualTilesToMove.contains(board.getTile(2,2)));
        assertTrue(actualTilesToMove.contains(board.getTile(2,3)));
        assertTrue(actualTilesToMove.contains(board.getTile(2,4)));
        assertTrue(actualTilesToMove.contains(board.getTile(3,4)));
        assertTrue(actualTilesToMove.contains(board.getTile(4,2)));
        assertTrue(actualTilesToMove.contains(board.getTile(4,3)));
        assertTrue(actualTilesToMove.contains(board.getTile(4,4)));
        assertEquals(7, actualTilesToMove.size());

        // Move worker to (2,3)
        game.getCurrentPlayerCard().getPlayable().move(board.getTile(2,3));

        assertEquals(ETurnPhase.BUILD, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "boards/boardApollo+Athena3_0.txt"));
        assertFalse(game.isGameOver());
        List<Tile> actualTilesToBuild = game.getCurrentPlayerCard().getPlayable().getTilesToBuild();
        assertTrue(actualTilesToBuild.contains(board.getTile(1,3)));
        assertTrue(actualTilesToBuild.contains(board.getTile(1,4)));
        assertTrue(actualTilesToBuild.contains(board.getTile(2,2)));
        assertTrue(actualTilesToBuild.contains(board.getTile(2,4)));
        assertTrue(actualTilesToBuild.contains(board.getTile(3,3)));
        assertTrue(actualTilesToBuild.contains(board.getTile(3,4)));
        assertEquals(6, actualTilesToBuild.size());

        // Build in (2,2)
        game.getCurrentPlayerCard().getPlayable().build(board.getTile(2,2));

        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "boards/boardApollo+Athena3_1.txt"));
        assertTrue(game.isGameOver());

    }

    @Test
    public void apolloWithAthenaCannotMoveTest2() {

        Game game = GameFactory.buildGameFromFile("games/gameApollo+Athena4.txt", "boards/boardApollo+Athena4_0.txt");
        Board board = game.getBoard();
        // Player in 0 is BotAthena, player in 1 is BotApollo
        List<Player> players = game.getPlayers();

        // Athena (BotAthena) is first player
        RemoteViewDummy rvd;
        List<Event> actualReceivedEvents;

        // Select worker in (3,3)
        game.getCurrentPlayerCard().getPlayable().selectWorker(game.getCurrentPlayer().getWorkers()[1]);

        // Move worker to (2,3)
        game.getCurrentPlayerCard().getPlayable().move(board.getTile(2,3));

        // Build in (2,4)
        game.getCurrentPlayerCard().getPlayable().build(board.getTile(2,4));

        // Now it is Apollo's (BotApollo) turn
        // BotApollo has one of his workers (the one on (1,2)) blocked, so the worker is automatically selected
        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());
        assertEquals("BotApollo", game.getCurrentPlayer().getNickname());
        assertTrue(BoardTest.boardTest(board, "boards/boardApollo+Athena4_1.txt"));
        assertFalse(game.isGameOver());

        List<Tile> actualTilesToMove = game.getCurrentPlayerCard().getPlayable().getTilesToMove();
        assertTrue(actualTilesToMove.contains(board.getTile(0,0)));
        assertEquals(1, actualTilesToMove.size());
        rvd = (RemoteViewDummy) game.getCurrentPlayer().getRemoteView();
        actualReceivedEvents = rvd.getReceivedEvents();
        rvd.clearReceivedEvents();

        // Try changing worker to the one on (1,2). Should produce an ErrorEvent
        game.getCurrentPlayerCard().getPlayable().move(board.getTile(1,2));
        assertEquals(1, actualReceivedEvents.size());
        assertTrue(actualReceivedEvents.get(0) instanceof ErrorEvent);
        rvd.clearReceivedEvents();

        // Move to (0,0)
        game.getCurrentPlayerCard().getPlayable().move(board.getTile(0,0));

        assertEquals(ETurnPhase.BUILD, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "boards/boardApollo+Athena4_1.txt"));
        assertFalse(game.isGameOver());
        List<Tile> actualTilesToBuild = game.getCurrentPlayerCard().getPlayable().getTilesToBuild();
        assertTrue(actualTilesToBuild.contains(board.getTile(0,1)));
        assertTrue(actualTilesToBuild.contains(board.getTile(1,0)));
        assertTrue(actualTilesToBuild.contains(board.getTile(1,1)));
        assertEquals(3, actualTilesToBuild.size());

        // Build on (1,1)
        game.getCurrentPlayerCard().getPlayable().build(board.getTile(1,1));

        // Now it is Athena's (BotAthena) turn
        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
        assertEquals("BotAthena", game.getCurrentPlayer().getNickname());
        assertTrue(BoardTest.boardTest(board, "boards/boardApollo+Athena4_2.txt"));
        assertFalse(game.isGameOver());

        // Select worker in (3,2)
        game.getCurrentPlayerCard().getPlayable().selectWorker(game.getCurrentPlayer().getWorkers()[0]);

        // Move worker to (2,2)
        game.getCurrentPlayerCard().getPlayable().move(board.getTile(2,2));

        // Build in (3,2)
        game.getCurrentPlayerCard().getPlayable().build(board.getTile(3,2));

        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
        TileTest.tileTest(3,2,null, false, ETileLevel.LEVEL1, board.getTile(3,2));
        assertTrue(game.isGameOver());

    }

}