package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.utils.GameFactory;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class ArtemisCardTest {

    static Game game;
    static Board board;
    //static List<RemoteView> remoteViews;
    //static List<Player> players;
    static Player currentPlayer;
    static Playable currentPlayerCard;

    @BeforeEach
    public void setup(){
        game = GameFactory.buildGameFromFile("gameArtemis.txt", "boardEmpty.txt");
        board = game.getBoard();
        currentPlayer = game.getCurrentPlayer();
        currentPlayerCard = currentPlayer.getCard().getPlayable();
    }

    @Test
    public void getTilesToMove() {

        // Select worker in (1,2)
        currentPlayerCard.selectWorker(currentPlayer.getWorkers()[0]);
        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());

        // Check moves generation (1° time)
        //  (1,2) & (2,3) are standard excluded tiles, being respectively the starting tile and a tile occupied by a worker
        List<Tile> actualTilesToMove = currentPlayerCard.getTilesToMove();
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
        assertFalse(actualTilesToMove.contains(board.getTile(2,3)));

        // Move worker in (1,3)
        currentPlayerCard.move(board.getTile(1,3));
        assertEquals(ETurnPhase.GODPOWER, game.getTurnPhase());

        // Use Artemis godpower to move again
        currentPlayerCard.useGodPower(true);
        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());

        // Check moves generation (2° time)
        actualTilesToMove = currentPlayerCard.getTilesToMove(board.getTile(1,3));
        expectedTilesToMove.clear();
        expectedTilesToMove.add(board.getTile(0,2));
        expectedTilesToMove.add(board.getTile(0,3));
        expectedTilesToMove.add(board.getTile(0,4));
        expectedTilesToMove.add(board.getTile(1,4));
        expectedTilesToMove.add(board.getTile(2,2));
        expectedTilesToMove.add(board.getTile(2,4));
        for (Tile t : expectedTilesToMove) {
            assertTrue(actualTilesToMove.contains(t));
        }

        assertFalse(actualTilesToMove.contains(board.getTile(1,3)));
        assertFalse(actualTilesToMove.contains(board.getTile(2,3)));
        //Check the old starting tile is excluded
        assertFalse(actualTilesToMove.contains(board.getTile(1,2)));

    }

    @Test
    public void setTurnPhaseNotGodPower(){

        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());

        // Select worker in (1,2)
        currentPlayerCard.selectWorker(currentPlayer.getWorkers()[0]);
        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());

        // Move worker in (1,3)
        currentPlayerCard.move(board.getTile(1,3));
        assertEquals(ETurnPhase.GODPOWER, game.getTurnPhase());

        // Dont use Artemis GODPOWER
        currentPlayerCard.useGodPower(false);
        assertEquals(ETurnPhase.BUILD, game.getTurnPhase());

        // Build in (1,2)
        currentPlayerCard.build(board.getTile(1,2));
        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
    }

    @Test
    public void setTurnPhaseGodPower(){

        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());

        // Select worker in (1,2)
        currentPlayerCard.selectWorker(currentPlayer.getWorkers()[0]);
        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());

        // Move worker in (1,3)
        currentPlayerCard.move(board.getTile(1,3));
        assertEquals(ETurnPhase.GODPOWER, game.getTurnPhase());

        // Use Artemis GODPOWER to move again
        currentPlayerCard.useGodPower(true);
        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());

        // Move worker in (1,4)
        currentPlayerCard.move(board.getTile(1,4));
        assertEquals(ETurnPhase.BUILD, game.getTurnPhase());

        // Build in (1,3)
        currentPlayerCard.build(board.getTile(1,3));
        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
    }
}