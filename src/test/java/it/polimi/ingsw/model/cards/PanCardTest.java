package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.events.GameOverEvent;
import it.polimi.ingsw.utils.GameFactory;
import it.polimi.ingsw.view.RemoteView;
import it.polimi.ingsw.view.RemoteViewDummy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

class PanCardTest {

    static Game game;
    static Board board;
    static List<RemoteView> remoteViews;
    //static List<Player> players;
    static Player currentPlayer;
    static Playable currentPlayerCard;

    @BeforeEach
    void setUp() {
        game = GameFactory.buildGameFromFile("gamePan.txt", "boardPan.txt");
        board = game.getBoard();
        remoteViews = game.getRemoteViews();
        currentPlayer = game.getCurrentPlayer();
        currentPlayerCard = currentPlayer.getCard().getPlayable();
    }

    @Test
    void checkWinCondition() {
        RemoteViewDummy view = (RemoteViewDummy) remoteViews.get(0);

        // Select worker in (1,2)
        currentPlayerCard.selectWorker(currentPlayer.getWorkers()[0]);

        view.clearReceivedEvents();
        // Move worker in (1,3), this activate Pan alternate win-con
        currentPlayerCard.move(board.getTile(1,3));

        //Check firing of GameOverEvent
        assertTrue(view.getLastReceivedEvent() instanceof GameOverEvent);
        assertTrue(game.isGameOver());

        //System.out.println(ListUtility.listToString(view.getEventLog()));
    }

}