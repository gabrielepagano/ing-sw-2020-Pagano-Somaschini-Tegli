package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.utils.GameFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HephaestusCardTest {

    static Game game;
    static Board board;
    //static List<RemoteView> remoteViews;
    //static List<Player> players;
    static Player currentPlayer;
    static Playable currentPlayerCard;

    @BeforeEach
    void setUp() {
        game = GameFactory.buildGameFromFile("games/gameHephaestus.txt", "boards/boardHephaestus.txt");
        board = game.getBoard();
        currentPlayer = game.getCurrentPlayer();
        currentPlayerCard = currentPlayer.getCard().getPlayable();
    }

    @Test
    void useGodPower() {
        // Select worker in (1,2)
        currentPlayerCard.selectWorker(currentPlayer.getWorkers()[0]);

        // Move worker in (1,3)
        currentPlayerCard.move(board.getTile(1,3));

        assertSame(ETileLevel.GROUND, board.getTile(1, 2).getLevel());

        // Build in (1,2)
        currentPlayerCard.build(board.getTile(1,2));
        assertSame(ETileLevel.LEVEL1, board.getTile(1, 2).getLevel());

        // Use Hephaestus GODPOWER to build again
        currentPlayerCard.useGodPower(true);
        assertSame(ETileLevel.LEVEL2, board.getTile(1, 2).getLevel());
    }

    @Test
    void setTurnPhase_NotGodPower() {
        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());

        // Select worker in (1,2)
        currentPlayerCard.selectWorker(currentPlayer.getWorkers()[0]);
        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());

        // Move worker in (1,3)
        currentPlayerCard.move(board.getTile(1,3));
        assertEquals(ETurnPhase.BUILD, game.getTurnPhase());

        // Build in (1,2)
        currentPlayerCard.build(board.getTile(1,2));
        assertEquals(ETurnPhase.GODPOWER, game.getTurnPhase());

        // Don't use Hephaestus GODPOWER to build again
        currentPlayerCard.useGodPower(false);
        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
    }

    @Test
    void setTurnPhase_GodPower() {
        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());

        // Select worker in (1,2)
        currentPlayerCard.selectWorker(currentPlayer.getWorkers()[0]);
        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());

        // Move worker in (1,3)
        currentPlayerCard.move(board.getTile(1,3));
        assertEquals(ETurnPhase.BUILD, game.getTurnPhase());

        // Build in (1,2)
        currentPlayerCard.build(board.getTile(1,2));
        assertEquals(ETurnPhase.GODPOWER, game.getTurnPhase());

        // Don't use Hephaestus GODPOWER to build again
        currentPlayerCard.useGodPower(true);
        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
    }

    @Test
    void setTurnPhase_SuppressLevel2() {
        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());

        // Select worker in (1,2)
        currentPlayerCard.selectWorker(currentPlayer.getWorkers()[0]);
        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());

        // Move worker in (1,3)
        currentPlayerCard.move(board.getTile(1,3));
        assertEquals(ETurnPhase.BUILD, game.getTurnPhase());

        assertSame(ETileLevel.LEVEL2, board.getTile(0, 3).getLevel());

        // Build in (0,3), building again would mean building a dome -> supress godpower
        currentPlayerCard.build(board.getTile(0,3));
        assertSame(ETileLevel.LEVEL3, board.getTile(0, 3).getLevel());
        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
    }

    @Test
    void setTurnPhase_SuppressLevel3() {
        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());

        // Select worker in (1,2)
        currentPlayerCard.selectWorker(currentPlayer.getWorkers()[0]);
        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());

        // Move worker in (1,3)
        currentPlayerCard.move(board.getTile(1,3));
        assertEquals(ETurnPhase.BUILD, game.getTurnPhase());

        assertSame(ETileLevel.LEVEL3, board.getTile(1, 4).getLevel());
        assertFalse(board.getTile(1, 4).isDomed());

        // Build in (1,4), impossible to build again -> supress godpower
        currentPlayerCard.build(board.getTile(1,4));
        assertSame(ETileLevel.LEVEL3, board.getTile(1, 4).getLevel());
        assertTrue(board.getTile(1, 4).isDomed());
        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
    }

}