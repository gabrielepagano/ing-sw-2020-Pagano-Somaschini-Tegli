package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.utils.GameFactory;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

class MinotaurCardTest {

    static Game game;
    static Board board;
    //static List<RemoteView> remoteViews;
    //static List<Player> players;
    static Player currentPlayer;
    static Playable currentPlayerCard;

    @Test
    void move() {
        game = GameFactory.buildGameFromFile("gameMinotaur.txt", "boardEmpty.txt");
        board = game.getBoard();
        currentPlayer = game.getCurrentPlayer();
        currentPlayerCard = currentPlayer.getCard().getPlayable();

        // Select worker in (1,2)
        currentPlayerCard.selectWorker(currentPlayer.getWorkers()[0]);

        Worker charged = board.getTile(2,3).getWorker();

        // Move worker in (2,3), charging "Bot2#1" worker
        currentPlayerCard.move(board.getTile(2,3));

        // Check movement
        assertEquals(currentPlayer.getWorkers()[0].getTile(), board.getTile(2,3));

        // Check charged worker has been correctly moved
        assertEquals(board.getTile(3,4).getWorker(), charged);
        assertEquals(charged.getTile(), board.getTile(3,4));
    }

    @Test
    void getTilesToMove_Chargeable(){
        game = GameFactory.buildGameFromFile("gameMinotaur.txt", "boardEmpty.txt");
        board = game.getBoard();
        currentPlayer = game.getCurrentPlayer();
        currentPlayerCard = currentPlayer.getCard().getPlayable();

        // Select worker in (1,2)
        currentPlayerCard.selectWorker(currentPlayer.getWorkers()[0]);

        /*
         Check moves generation
            - - e - -
            - a W - -
            - b - c -
            d - - - -
        */
        List<Tile> actualTilesToMove = currentPlayerCard.getTilesToMove(board.getTile(1,2));
        List<Tile> expectedTilesToMove = new ArrayList<>();
        expectedTilesToMove.add(board.getTile(0,1));
        expectedTilesToMove.add(board.getTile(0,3));
        expectedTilesToMove.add(board.getTile(1,3));
        expectedTilesToMove.add(board.getTile(2,2));
        for (Tile t : expectedTilesToMove) {
            assertTrue(actualTilesToMove.contains(t));
        }
        assertFalse(actualTilesToMove.contains(board.getTile(1,2)));

        //"c" has a free backwards tile, Chargable
        assertTrue(actualTilesToMove.contains(board.getTile(2,3)));
    }

    @Test
    void getTilesToMove_NotChargeable() {
        game = GameFactory.buildGameFromFile("gameMinotaur.txt", "boardMinotaur.txt");
        board = game.getBoard();
        currentPlayer = game.getCurrentPlayer();
        currentPlayerCard = currentPlayer.getCard().getPlayable();

        // Select worker in (1,2)
        currentPlayerCard.selectWorker(currentPlayer.getWorkers()[0]);

        /*
         Check moves generation
            - - e - -
            - a W - -
            - b - c -
            d - - - D
        */
        List<Tile> actualTilesToMove = currentPlayerCard.getTilesToMove(board.getTile(1,2));
        List<Tile> expectedTilesToMove = new ArrayList<>();
        expectedTilesToMove.add(board.getTile(0,1));
        expectedTilesToMove.add(board.getTile(0,3));
        expectedTilesToMove.add(board.getTile(1,3));
        expectedTilesToMove.add(board.getTile(2,2));
        for (Tile t : expectedTilesToMove) {
            assertTrue(actualTilesToMove.contains(t));
        }
        assertFalse(actualTilesToMove.contains(board.getTile(1,2)));

        //"a" is Minotaur second worker, NOT Chargable
        assertFalse(actualTilesToMove.contains(board.getTile(1,1)));
        //"b" has another worker ("d") behind, NOT Chargable
        assertFalse(actualTilesToMove.contains(board.getTile(2,1)));
        //"c" has a dome behind, NOT Chargable
        assertFalse(actualTilesToMove.contains(board.getTile(2,3)));
        //"e" is on a perimetral tile, NOT Chargable
        assertFalse(actualTilesToMove.contains(board.getTile(0,2)));
    }

}