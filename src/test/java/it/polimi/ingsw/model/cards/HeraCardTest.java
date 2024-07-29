package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.utils.GameFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

class HeraCardTest {

    static Game game;
    static Board board;
    //static List<RemoteView> remoteViews;
    //static List<Player> players;
    static Player currentPlayer;
    static Playable currentPlayerCard;

    @BeforeEach
    void setUp() {
        game = GameFactory.buildGameFromFile("games/gameHera.txt", "boards/boardHera.txt");
        board = game.getBoard();
        currentPlayer = game.getCurrentPlayer();
        currentPlayerCard = currentPlayer.getCard().getPlayable();
    }

    @Test
    void decorator_checkWinCondition_denyStandard(){
        // Pan: Select worker in (4,2)
        currentPlayerCard.selectWorker(currentPlayer.getWorkers()[0]);

        assertSame(ETileLevel.LEVEL2, board.getTile(4,2).getLevel());
        assertSame(ETileLevel.LEVEL3, board.getTile(4,1).getLevel());
        assertTrue(board.getTile(4,1).isPerimetralTile());

        // Pan: Move worker in (4,1)
        currentPlayerCard.move(board.getTile(4,1));
        // Check Pan worker has been correctly moved
        assertEquals(board.getTile(4,1).getWorker(), currentPlayer.getWorkers()[0]);
        assertEquals(currentPlayer.getWorkers()[0].getTile(), board.getTile(4,1));

        //Game is still going (no winner)
        assertFalse(game.isGameOver());
        assertEquals(ETurnPhase.BUILD, game.getTurnPhase());
        assertEquals(currentPlayer, game.getCurrentPlayer());
    }

    @Test
    void decorator_checkWinCondition_denyPan(){
        // Pan: Select worker in (4,2)
        currentPlayerCard.selectWorker(currentPlayer.getWorkers()[0]);

        assertSame(ETileLevel.LEVEL2, board.getTile(4,2).getLevel());
        assertSame(ETileLevel.GROUND, board.getTile(4,3).getLevel());
        assertTrue(board.getTile(4,3).isPerimetralTile());

        // Pan: Move worker in (4,3)
        currentPlayerCard.move(board.getTile(4,3));
        // Check Pan worker has been correctly moved
        assertEquals(board.getTile(4,3).getWorker(), currentPlayer.getWorkers()[0]);
        assertEquals(currentPlayer.getWorkers()[0].getTile(), board.getTile(4,3));

        //Game is still going (no winner)
        assertFalse(game.isGameOver());
        assertEquals(ETurnPhase.BUILD, game.getTurnPhase());
        assertEquals(currentPlayer, game.getCurrentPlayer());
    }

    @Test
    void destroy(){
        // Pan: Select worker in (2,3)
        currentPlayerCard.selectWorker(currentPlayer.getWorkers()[1]);

        // Pan: Move worker in (2,2)
        currentPlayerCard.move(board.getTile(2,2));

        // Pan: Build in (1,3), this completely block Hera -> Hera LOSE
        currentPlayerCard.build(board.getTile(1,3));

        assertFalse(game.isGameOver());
        assertFalse(board.getTile(0,0).hasWorker());
        assertFalse(board.getTile(0,4).hasWorker());
        assertEquals(2, game.getPlayers().size());
        assertEquals("BotApollo", game.getCurrentPlayer().getNickname());
        assertTrue(game.getCurrentPlayerCard() instanceof ApolloCard);

        currentPlayer = game.getCurrentPlayer();
        currentPlayerCard = currentPlayer.getCard().getPlayable();

        // Apollo: Select worker in (1,0)
        currentPlayerCard.selectWorker(currentPlayer.getWorkers()[0]);

        assertSame(ETileLevel.LEVEL2, board.getTile(1,0).getLevel());
        assertSame(ETileLevel.LEVEL3, board.getTile(2,0).getLevel());
        assertTrue(board.getTile(2,0).isPerimetralTile());

        // Apollo: Move worker in (2,0)
        currentPlayerCard.move(board.getTile(2,0));
        // Check Apollo worker has been correctly moved
        assertEquals(board.getTile(2,0).getWorker(), currentPlayer.getWorkers()[0]);
        assertEquals(currentPlayer.getWorkers()[0].getTile(), board.getTile(2,0));

        //Hera has lost, is now again possible to win in a perimatral tile
        assertTrue(game.isGameOver());
    }

}