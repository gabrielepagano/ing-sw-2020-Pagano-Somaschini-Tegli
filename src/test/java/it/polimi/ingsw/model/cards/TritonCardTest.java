package it.polimi.ingsw.model.cards;


import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.events.Event;
import it.polimi.ingsw.utils.GameFactory;
import it.polimi.ingsw.view.RemoteViewDummy;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TritonCardTest {

    @Test
    public void getTilesToMoveSimpleTest() {

        Game game = GameFactory.buildGameFromFile("games/gameTriton+Athena.txt", "boards/boardTriton+Athena_0.txt");
        Board board = game.getBoard();
        // Player in 0 is BotTriton, player in 1 is BotAthena
        List<Player> players = game.getPlayers();

        // Athena (BotAthena) is first player
        RemoteViewDummy rvd = (RemoteViewDummy) game.getCurrentPlayer().getRemoteView();
        List<Event> actualReceivedEvents = rvd.getReceivedEvents();

        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
        assertFalse(game.isGameOver());
        List<String> expectedPlayerNicknamesList = new ArrayList<>();
        expectedPlayerNicknamesList.add("BotTriton");
        expectedPlayerNicknamesList.add("BotAthena");
        assertEquals(expectedPlayerNicknamesList, game.getPlayerNicknamesList());

        // Select worker on (2,2)
        game.getCurrentPlayerCard().getPlayable().selectWorker(game.getCurrentPlayer().getWorkers()[0]);

        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "boards/boardTriton+Athena_0.txt"));
        assertFalse(game.isGameOver());

        List<Tile> actualTilesToMove = game.getCurrentPlayerCard().getPlayable().getTilesToMove();
        assertTrue(actualTilesToMove.contains(board.getTile(1,2)));
        assertTrue(actualTilesToMove.contains(board.getTile(2,3)));
        assertTrue(actualTilesToMove.contains(board.getTile(3,2)));
        assertEquals(3, actualTilesToMove.size());

        // Change worker selection to (3,3)
        game.getCurrentPlayerCard().getPlayable().move(board.getTile(3,3));

        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "boards/boardTriton+Athena_0.txt"));
        assertFalse(game.isGameOver());

        actualTilesToMove = game.getCurrentPlayerCard().getPlayable().getTilesToMove();
        assertTrue(actualTilesToMove.contains(board.getTile(2,3)));
        assertTrue(actualTilesToMove.contains(board.getTile(2,4)));
        assertTrue(actualTilesToMove.contains(board.getTile(3,2)));
        assertTrue(actualTilesToMove.contains(board.getTile(3,4)));
        assertTrue(actualTilesToMove.contains(board.getTile(4,2)));
        assertTrue(actualTilesToMove.contains(board.getTile(4,3)));
        assertTrue(actualTilesToMove.contains(board.getTile(4,4)));
        assertEquals(7, actualTilesToMove.size());

    }

    @Test
    public void getTilesToMoveAfterAthenaMovesUpTest() {

        Game game = GameFactory.buildGameFromFile("games/gameTriton+Athena.txt", "boards/boardTriton+Athena_0.txt");
        Board board = game.getBoard();
        // Player in 0 is BotTriton, player in 1 is BotAthena
        List<Player> players = game.getPlayers();

        // Athena (BotAthena) is first player
        RemoteViewDummy rvd = (RemoteViewDummy) game.getCurrentPlayer().getRemoteView();
        List<Event> actualReceivedEvents = rvd.getReceivedEvents();

        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
        assertFalse(game.isGameOver());
        List<String> expectedPlayerNicknamesList = new ArrayList<>();
        expectedPlayerNicknamesList.add("BotTriton");
        expectedPlayerNicknamesList.add("BotAthena");
        assertEquals(expectedPlayerNicknamesList, game.getPlayerNicknamesList());

        // Select worker on (2,2)
        game.getCurrentPlayerCard().getPlayable().selectWorker(game.getCurrentPlayer().getWorkers()[0]);
        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "boards/boardTriton+Athena_0.txt"));

        // Move worker to (2,3)
        game.getCurrentPlayerCard().getPlayable().move(board.getTile(2,3));
        assertEquals(ETurnPhase.BUILD, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "boards/boardTriton+Athena_0.txt"));

        List<Tile> actualTilesToBuild = game.getCurrentPlayerCard().getPlayable().getTilesToBuild();
        assertTrue(actualTilesToBuild.contains(board.getTile(1,2)));
        assertTrue(actualTilesToBuild.contains(board.getTile(1,4)));
        assertTrue(actualTilesToBuild.contains(board.getTile(2,2)));
        assertTrue(actualTilesToBuild.contains(board.getTile(2,4)));
        assertTrue(actualTilesToBuild.contains(board.getTile(3,2)));
        assertTrue(actualTilesToBuild.contains(board.getTile(3,4)));
        assertEquals(6, actualTilesToBuild.size());

        // Build on (2,4)
        game.getCurrentPlayerCard().getPlayable().build(board.getTile(2,4));

        // Now it is Athena's (BotAthena) turn
        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
        assertEquals("BotAthena", game.getCurrentPlayer().getNickname());
        assertTrue(BoardTest.boardTest(board, "boards/boardTriton+Athena_1.txt"));
        assertFalse(game.isGameOver());

        // Select worker on (1,1)
        game.getCurrentPlayerCard().getPlayable().selectWorker(game.getCurrentPlayer().getWorkers()[0]);
        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "boards/boardTriton+Athena_1.txt"));
        assertFalse(game.isGameOver());

        // Move selected worker to (0,1)
        game.getCurrentPlayerCard().getPlayable().move(board.getTile(0,1));
        assertEquals(ETurnPhase.BUILD, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "boards/boardTriton+Athena_1.txt"));

        // Build on (0,0)
        game.getCurrentPlayerCard().getPlayable().build(board.getTile(0,0));

        // Now it is Triton's (BotTriton) turn
        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
        assertEquals("BotTriton", game.getCurrentPlayer().getNickname());
        assertTrue(BoardTest.boardTest(board, "boards/boardTriton+Athena_2.txt"));
        assertFalse(game.isGameOver());

        // Select worker on (2,3)
        game.getCurrentPlayerCard().getPlayable().selectWorker(game.getCurrentPlayer().getWorkers()[0]);

        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "boards/boardTriton+Athena_2.txt"));
        assertFalse(game.isGameOver());

        List<Tile> actualTilesToMove = game.getCurrentPlayerCard().getPlayable().getTilesToMove();
        assertTrue(actualTilesToMove.contains(board.getTile(1,2)));
        assertTrue(actualTilesToMove.contains(board.getTile(2,2)));
        assertTrue(actualTilesToMove.contains(board.getTile(3,4)));
        assertEquals(3, actualTilesToMove.size());

        // Change worker selection to (3,3)
        game.getCurrentPlayerCard().getPlayable().move(board.getTile(3,3));

        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "boards/boardTriton+Athena_2.txt"));
        assertFalse(game.isGameOver());

        actualTilesToMove = game.getCurrentPlayerCard().getPlayable().getTilesToMove();
        assertTrue(actualTilesToMove.contains(board.getTile(2,2)));
        assertTrue(actualTilesToMove.contains(board.getTile(3,4)));
        assertTrue(actualTilesToMove.contains(board.getTile(4,2)));
        assertTrue(actualTilesToMove.contains(board.getTile(4,3)));
        assertEquals(4, actualTilesToMove.size());

    }

    @Test
    public void moveRepeatedlyOnPerimeterSimpleTest() {

        Game game = GameFactory.buildGameFromFile("games/gameTriton+Athena.txt", "boards/boardTriton+Athena_0.txt");
        Board board = game.getBoard();
        // Player in 0 is BotTriton, player in 1 is BotAthena
        List<Player> players = game.getPlayers();

        // Athena (BotAthena) is first player
        RemoteViewDummy rvd = (RemoteViewDummy) game.getCurrentPlayer().getRemoteView();
        List<Event> actualReceivedEvents = rvd.getReceivedEvents();

        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
        assertFalse(game.isGameOver());
        List<String> expectedPlayerNicknamesList = new ArrayList<>();
        expectedPlayerNicknamesList.add("BotTriton");
        expectedPlayerNicknamesList.add("BotAthena");
        assertEquals(expectedPlayerNicknamesList, game.getPlayerNicknamesList());

        // Select worker on (3,3)
        game.getCurrentPlayerCard().getPlayable().selectWorker(game.getCurrentPlayer().getWorkers()[1]);

        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "boards/boardTriton+Athena_0.txt"));
        assertFalse(game.isGameOver());

        // Move to (4,4)
        game.getCurrentPlayerCard().getPlayable().move(board.getTile(4,4));

        assertEquals(ETurnPhase.GODPOWER, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "boards/boardTriton+Athena_0.txt"));
        assertFalse(game.isGameOver());

        // Triton decides to use his god power and move again from (4,4)
        game.getCurrentPlayerCard().getPlayable().useGodPower(true);

        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "boards/boardTriton+Athena_0.txt"));
        assertFalse(game.isGameOver());

        List<Tile> actualTilesToMove = game.getCurrentPlayerCard().getPlayable().getTilesToMove();
        assertTrue(actualTilesToMove.contains(board.getTile(3,3)));
        assertTrue(actualTilesToMove.contains(board.getTile(3,4)));
        assertTrue(actualTilesToMove.contains(board.getTile(4,3)));
        assertEquals(3, actualTilesToMove.size());

        // Move to (3,4)
        game.getCurrentPlayerCard().getPlayable().move(board.getTile(3,4));

        assertEquals(ETurnPhase.GODPOWER, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "boards/boardTriton+Athena_0.txt"));
        assertFalse(game.isGameOver());

        // Triton decides to use his god power and move again from (3,4)
        game.getCurrentPlayerCard().getPlayable().useGodPower(true);

        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "boards/boardTriton+Athena_0.txt"));
        assertFalse(game.isGameOver());

        actualTilesToMove = game.getCurrentPlayerCard().getPlayable().getTilesToMove();
        assertTrue(actualTilesToMove.contains(board.getTile(2,4)));
        assertTrue(actualTilesToMove.contains(board.getTile(3,3)));
        assertTrue(actualTilesToMove.contains(board.getTile(4,3)));
        assertTrue(actualTilesToMove.contains(board.getTile(4,3)));
        assertEquals(5, actualTilesToMove.size());

        // Move to (4,3)
        game.getCurrentPlayerCard().getPlayable().move(board.getTile(4,3));

        assertEquals(ETurnPhase.GODPOWER, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "boards/boardTriton+Athena_0.txt"));
        assertFalse(game.isGameOver());

        // Triton decides to use his god power and move again from (4,3)
        game.getCurrentPlayerCard().getPlayable().useGodPower(true);

        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "boards/boardTriton+Athena_0.txt"));
        assertFalse(game.isGameOver());

        actualTilesToMove = game.getCurrentPlayerCard().getPlayable().getTilesToMove();
        assertTrue(actualTilesToMove.contains(board.getTile(3,2)));
        assertTrue(actualTilesToMove.contains(board.getTile(3,3)));
        assertTrue(actualTilesToMove.contains(board.getTile(3,4)));
        assertTrue(actualTilesToMove.contains(board.getTile(4,2)));
        assertTrue(actualTilesToMove.contains(board.getTile(4,4)));
        assertEquals(5, actualTilesToMove.size());

        // Move to (3,2)
        game.getCurrentPlayerCard().getPlayable().move(board.getTile(3,2));

        assertEquals(ETurnPhase.BUILD, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "boards/boardTriton+Athena_0.txt"));
        assertFalse(game.isGameOver());

        List<Tile> actualTilesToBuild = game.getCurrentPlayerCard().getPlayable().getTilesToBuild();
        assertTrue(actualTilesToBuild.contains(board.getTile(2,3)));
        assertTrue(actualTilesToBuild.contains(board.getTile(3,1)));
        assertTrue(actualTilesToBuild.contains(board.getTile(3,3)));
        assertTrue(actualTilesToBuild.contains(board.getTile(4,1)));
        assertTrue(actualTilesToBuild.contains(board.getTile(4,2)));
        assertTrue(actualTilesToBuild.contains(board.getTile(4,3)));
        assertEquals(6, actualTilesToBuild.size());

    }

    @Test
    public void moveRepeatedlyOnPerimeterThenDeclineGodPowerTest() {

        Game game = GameFactory.buildGameFromFile("games/gameTriton+Athena.txt", "boards/boardTriton+Athena_0.txt");
        Board board = game.getBoard();
        // Player in 0 is BotTriton, player in 1 is BotAthena
        List<Player> players = game.getPlayers();

        // Athena (BotAthena) is first player
        RemoteViewDummy rvd = (RemoteViewDummy) game.getCurrentPlayer().getRemoteView();
        List<Event> actualReceivedEvents = rvd.getReceivedEvents();

        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
        assertFalse(game.isGameOver());
        List<String> expectedPlayerNicknamesList = new ArrayList<>();
        expectedPlayerNicknamesList.add("BotTriton");
        expectedPlayerNicknamesList.add("BotAthena");
        assertEquals(expectedPlayerNicknamesList, game.getPlayerNicknamesList());

        // Select worker on (3,3)
        game.getCurrentPlayerCard().getPlayable().selectWorker(game.getCurrentPlayer().getWorkers()[1]);

        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "boards/boardTriton+Athena_0.txt"));
        assertFalse(game.isGameOver());

        // Move to (4,4)
        game.getCurrentPlayerCard().getPlayable().move(board.getTile(4,4));

        assertEquals(ETurnPhase.GODPOWER, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "boards/boardTriton+Athena_0.txt"));
        assertFalse(game.isGameOver());

        // Triton decides to use his god power and move again from (4,4)
        game.getCurrentPlayerCard().getPlayable().useGodPower(true);

        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "boards/boardTriton+Athena_0.txt"));
        assertFalse(game.isGameOver());

        // Move to (3,4)
        game.getCurrentPlayerCard().getPlayable().move(board.getTile(3,4));

        assertEquals(ETurnPhase.GODPOWER, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "boards/boardTriton+Athena_0.txt"));
        assertFalse(game.isGameOver());

        // Triton decides NOT to use his god power
        game.getCurrentPlayerCard().getPlayable().useGodPower(false);

        assertEquals(ETurnPhase.BUILD, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "boards/boardTriton+Athena_0.txt"));
        assertFalse(game.isGameOver());

        List<Tile> actualTilesToBuild = game.getCurrentPlayerCard().getPlayable().getTilesToBuild();
        assertTrue(actualTilesToBuild.contains(board.getTile(2,3)));
        assertTrue(actualTilesToBuild.contains(board.getTile(2,4)));
        assertTrue(actualTilesToBuild.contains(board.getTile(3,3)));
        assertTrue(actualTilesToBuild.contains(board.getTile(4,3)));
        assertTrue(actualTilesToBuild.contains(board.getTile(4,4)));
        assertEquals(5, actualTilesToBuild.size());

        // Build on (2,4)
        game.getCurrentPlayerCard().getPlayable().build(board.getTile(2,4));

        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
        assertTrue(BoardTest.boardTest(board, "boards/boardTriton+Athena_1.txt"));
        assertFalse(game.isGameOver());
        assertEquals("BotAthena", game.getCurrentPlayer().getNickname());

    }

}