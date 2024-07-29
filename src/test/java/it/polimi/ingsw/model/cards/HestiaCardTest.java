package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.utils.GameFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

class HestiaCardTest {

    static Game game;
    static Board board;
    //static List<RemoteView> remoteViews;
    //static List<Player> players;
    static Player currentPlayer;
    static Playable currentPlayerCard;

    @BeforeEach
    public void setUp(){
        game = GameFactory.buildGameFromFile("gameHestia.txt", "boardHestia.txt");
        board = game.getBoard();
        currentPlayer = game.getCurrentPlayer();
        currentPlayerCard = currentPlayer.getCard().getPlayable();
    }

    @Test
    void getTilesToBuild() {
        // Select worker in (3,2)
        currentPlayerCard.selectWorker(currentPlayer.getWorkers()[1]);

        // Move worker in (3,3)
        currentPlayerCard.move(board.getTile(3,3));

        // Build in (3,4)
        currentPlayerCard.build(board.getTile(3,4));

        // Use Prometheus GODPOWER to build before moving
        currentPlayerCard.useGodPower(true);

        // Check standard generation
        List<Tile> actualTilesToBuild =  currentPlayerCard.getTilesToBuild();
        List<Tile> expectedTilesToBuild = new ArrayList<>();
        expectedTilesToBuild.add(board.getTile(3,2));
        for (Tile t : expectedTilesToBuild) {
            assertTrue(actualTilesToBuild.contains(t));
        }
        assertFalse(actualTilesToBuild.contains(board.getTile(3,3)));

        //Perimetral tiles
        assertFalse(actualTilesToBuild.contains(board.getTile(2,4)));
        assertFalse(actualTilesToBuild.contains(board.getTile(4,4)));
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

        // Use Artemis GODPOWER to move again
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

        // Use Artemis GODPOWER to move again
        currentPlayerCard.useGodPower(true);
        assertEquals(ETurnPhase.BUILD, game.getTurnPhase());

        // Build in (1,2) again
        currentPlayerCard.build(board.getTile(1,2));
        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
    }

    /**
     * If the only tiles buildable around Hestia are perimetrals, it makes no sense to use her godpower
     */
    @Test
    void setTurnPhase_Forced() {
        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());

        // Select worker in (3,2)
        currentPlayerCard.selectWorker(currentPlayer.getWorkers()[1]);
        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());

        // Move worker in (3,3)
        currentPlayerCard.move(board.getTile(3,3));
        assertEquals(ETurnPhase.BUILD, game.getTurnPhase());

        // Build in (3,2)
        // Hestia can't use her godpower as there are no targetable tiles
        currentPlayerCard.build(board.getTile(3,2));
        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
    }

}