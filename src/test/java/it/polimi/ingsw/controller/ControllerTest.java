package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.ArtemisCard;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.ChronusCard;
import it.polimi.ingsw.network.events.ActionEvent;
import it.polimi.ingsw.network.events.CardEvent;
import it.polimi.ingsw.network.events.ChoiceEvent;
import it.polimi.ingsw.network.events.PlayerNicknameEvent;
import it.polimi.ingsw.utils.GameFactory;
import it.polimi.ingsw.view.RemoteView;
import it.polimi.ingsw.view.RemoteViewDummy;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ControllerTest {

    private static Game game;
    private static Board board;
    private static Controller controller;
    private static List<RemoteView> remoteViews;
    private static List<String> nicknamesList;
    private static String challengerNick;
    private static String otherNick;

    @BeforeAll
    public static void setup() {
        RemoteView[] remoteViewsArray = {   new RemoteViewDummy("Bot1"),
                                            new RemoteViewDummy("Bot2")
        };
        remoteViews = Arrays.asList(remoteViewsArray);
        game = GameFactory.buildEmptyGameFromRemoteViewsList(remoteViews);
        board = game.getBoard();
        controller = new Controller(remoteViews, game);
        nicknamesList = game.getPlayerNicknamesList();
        challengerNick = game.getChallenger().getNickname();
        otherNick = nicknamesList.get(0).equals(challengerNick) ? nicknamesList.get(1) : nicknamesList.get(0);
    }

    @Order(1)
    @Test
    public void initTest() {
        assertNotNull(game.getChallenger());
        assertSame(game.getCurrentPlayer(), game.getChallenger());
        assertEquals(EStartupPhase.PICKCARDS ,game.getStartupPhase());
        assertTrue(controller.isActive());
        assertTrue(game.getSelectedCards().isEmpty());
    }

    @Order(2)
    @Test
    public void pickCardsTest() {

        CardEvent cardEvent;

        // APOLLO not from the challenger
        cardEvent = new CardEvent(EGodPower.APOLLO);
        cardEvent.setSignature(otherNick);

        controller.update(cardEvent);

        assertTrue(controller.isActive());
        assertTrue(game.getSelectedCards().isEmpty());
        assertSame(game.getChallenger(), game.getCurrentPlayer());

        // ARTEMIS from the challenger
        cardEvent = new CardEvent(EGodPower.ARTEMIS);
        cardEvent.setSignature(challengerNick);

        controller.update(cardEvent);

        assertTrue(controller.isActive());
        assertEquals(1, game.getSelectedCards().size());
        assertEquals(EGodPower.ARTEMIS, game.getSelectedCards().get(0));
        assertSame(game.getChallenger(), game.getCurrentPlayer());

        // Try again with ARTEMIS
        cardEvent = new CardEvent(EGodPower.ARTEMIS);
        cardEvent.setSignature(challengerNick);

        controller.update(cardEvent);

        assertTrue(controller.isActive());
        assertEquals(1, game.getSelectedCards().size());
        assertEquals(EGodPower.ARTEMIS, game.getSelectedCards().get(0));
        assertSame(game.getChallenger(), game.getCurrentPlayer());

        // CHRONUS not from the challenger
        cardEvent = new CardEvent(EGodPower.CHRONUS);
        cardEvent.setSignature(otherNick);

        controller.update(cardEvent);

        assertTrue(controller.isActive());
        assertEquals(1, game.getSelectedCards().size());
        assertEquals(EGodPower.ARTEMIS, game.getSelectedCards().get(0));
        assertSame(game.getChallenger(), game.getCurrentPlayer());

        // CHRONUS from the challenger
        cardEvent = new CardEvent(EGodPower.CHRONUS);
        cardEvent.setSignature(challengerNick);

        controller.update(cardEvent);

        assertTrue(controller.isActive());
        assertEquals(2, game.getSelectedCards().size());
        assertEquals(EGodPower.ARTEMIS, game.getSelectedCards().get(0));
        assertEquals(EGodPower.CHRONUS, game.getSelectedCards().get(1));
        assertEquals(EStartupPhase.DEALCARDS, game.getStartupPhase());
        assertEquals(otherNick, game.getCurrentPlayer().getNickname());

        assertEquals(0, game.getPlayerCards().size());

    }

    @Order(3)
    @Test
    public void dealCardsTest() {

        CardEvent cardEvent;

        // Try a card not selected by the challenger from not the current player
        cardEvent = new CardEvent(EGodPower.PAN);
        cardEvent.setSignature(challengerNick);

        controller.update(cardEvent);

        assertTrue(controller.isActive());
        assertEquals(2, game.getSelectedCards().size());
        assertEquals(EGodPower.ARTEMIS, game.getSelectedCards().get(0));
        assertEquals(EGodPower.CHRONUS, game.getSelectedCards().get(1));
        assertEquals(EStartupPhase.DEALCARDS, game.getStartupPhase());
        assertEquals(otherNick, game.getCurrentPlayer().getNickname());
        assertEquals(0, game.getPlayerCards().size());

        // Try a card selected by the challenger from not the current player
        cardEvent = new CardEvent(EGodPower.ARTEMIS);
        cardEvent.setSignature(challengerNick);

        controller.update(cardEvent);

        assertTrue(controller.isActive());
        assertEquals(2, game.getSelectedCards().size());
        assertEquals(EGodPower.ARTEMIS, game.getSelectedCards().get(0));
        assertEquals(EGodPower.CHRONUS, game.getSelectedCards().get(1));
        assertEquals(EStartupPhase.DEALCARDS, game.getStartupPhase());
        assertEquals(otherNick, game.getCurrentPlayer().getNickname());
        assertEquals(0, game.getPlayerCards().size());

        // Current player selects ARTEMIS
        cardEvent = new CardEvent(EGodPower.ARTEMIS);
        cardEvent.setSignature(otherNick);

        controller.update(cardEvent);

        assertTrue(controller.isActive());
        assertEquals(0, game.getSelectedCards().size());
        assertEquals(EStartupPhase.PICKFIRSTPLAYER, game.getStartupPhase());
        assertEquals(challengerNick, game.getCurrentPlayer().getNickname());
        assertEquals(2, game.getPlayerCards().size());
        assertEquals(EGodPower.CHRONUS, game.getPlayerCards().get(nicknamesList.indexOf(challengerNick)));
        assertEquals(EGodPower.ARTEMIS, game.getPlayerCards().get(nicknamesList.indexOf(otherNick)));
        List<Player> players = game.getPlayers();
        Card challengerCard = players.get(nicknamesList.indexOf(challengerNick)).getCard();
        Card otherCard = players.get(nicknamesList.indexOf(otherNick)).getCard();

        assertTrue(challengerCard instanceof ChronusCard);
        assertTrue(otherCard instanceof ArtemisCard);

    }

    @Order(4)
    @Test
    public void pickFirstPlayerTest() {

        PlayerNicknameEvent nicknameEvent;

        // Try a valid nick from NOT the current player
        nicknameEvent = new PlayerNicknameEvent(challengerNick);
        nicknameEvent.setSignature(otherNick);

        controller.update(nicknameEvent);

        assertEquals(EStartupPhase.PICKFIRSTPLAYER, game.getStartupPhase());
        assertEquals(challengerNick, game.getCurrentPlayer().getNickname());

        // Try an invalid nick from the current player
        nicknameEvent = new PlayerNicknameEvent("Bot42");
        nicknameEvent.setSignature(challengerNick);

        controller.update(nicknameEvent);

        assertEquals(EStartupPhase.PICKFIRSTPLAYER, game.getStartupPhase());
        assertEquals(challengerNick, game.getCurrentPlayer().getNickname());

        // Set a valid first player
        nicknameEvent = new PlayerNicknameEvent(otherNick);
        nicknameEvent.setSignature(challengerNick);

        controller.update(nicknameEvent);

        assertEquals(EStartupPhase.PLACEFIRSTWORKER, game.getStartupPhase());
        assertEquals(otherNick, game.getCurrentPlayer().getNickname());

    }

    @Order(5)
    @Test
    public void firstPlayerplaceFirstWorkerSimpleTest() {

        ActionEvent actionEvent;
        Player currentPlayer = game.getCurrentPlayer();
        Player otherPlayer = game.getPlayers().get(0) == currentPlayer ? game.getPlayers().get(1) : game.getPlayers().get(0);

        actionEvent = new ActionEvent(new Position(0,0));
        actionEvent.setSignature(game.getCurrentPlayer().getNickname());

        controller.update(actionEvent);

        assertEquals(EStartupPhase.PLACESECONDWORKER, game.getStartupPhase());
        assertEquals(currentPlayer.getNickname(), game.getCurrentPlayer().getNickname());
        BoardTest.boardTest(board, "boardEmpty.txt");
        WorkerTest.workerTest(board.getTile(0,0), currentPlayer, 0, currentPlayer.getWorkers()[0]);
        WorkerTest.workerTest(null, currentPlayer, 1, currentPlayer.getWorkers()[1]);
        WorkerTest.workerTest(null, otherPlayer, 0, otherPlayer.getWorkers()[0]);
        WorkerTest.workerTest(null, otherPlayer, 1, otherPlayer.getWorkers()[1]);

    }

    @Order(6)
    @Test
    public void firstPlayerPlaceSecondWorkerSimpleTest() {

        ActionEvent actionEvent;
        Player oldCurrentPlayer = game.getCurrentPlayer();
        Player otherPlayer = game.getPlayers().get(0) == oldCurrentPlayer ? game.getPlayers().get(1) : game.getPlayers().get(0);

        // Try placing second worker on top of first worker
        actionEvent = new ActionEvent(new Position(0,0));
        actionEvent.setSignature(game.getCurrentPlayer().getNickname());

        controller.update(actionEvent);

        assertEquals(EStartupPhase.PLACESECONDWORKER, game.getStartupPhase());
        assertEquals(oldCurrentPlayer.getNickname(), game.getCurrentPlayer().getNickname());
        BoardTest.boardTest(board, "boardEmpty.txt");
        WorkerTest.workerTest(board.getTile(0,0), oldCurrentPlayer, 0, oldCurrentPlayer.getWorkers()[0]);
        WorkerTest.workerTest(null, oldCurrentPlayer, 1, oldCurrentPlayer.getWorkers()[1]);
        WorkerTest.workerTest(null, otherPlayer, 0, otherPlayer.getWorkers()[0]);
        WorkerTest.workerTest(null, otherPlayer, 1, otherPlayer.getWorkers()[1]);

        // Valid second worker placement
        actionEvent = new ActionEvent(new Position(0,1));
        actionEvent.setSignature(game.getCurrentPlayer().getNickname());

        controller.update(actionEvent);

        assertEquals(EStartupPhase.PLACEFIRSTWORKER, game.getStartupPhase());
        assertEquals(otherPlayer.getNickname(), game.getCurrentPlayer().getNickname());
        BoardTest.boardTest(board, "boardEmpty.txt");
        WorkerTest.workerTest(board.getTile(0,0), oldCurrentPlayer, 0, oldCurrentPlayer.getWorkers()[0]);
        WorkerTest.workerTest(board.getTile(0,1), oldCurrentPlayer, 1, oldCurrentPlayer.getWorkers()[1]);
        WorkerTest.workerTest(null, otherPlayer, 0, otherPlayer.getWorkers()[0]);
        WorkerTest.workerTest(null, otherPlayer, 1, otherPlayer.getWorkers()[1]);

    }

    @Order(7)
    @Test
    public void secondPlayerPlaceFirstWorkerSimpleTest() {

        ActionEvent actionEvent;
        Player currentPlayer = game.getCurrentPlayer();
        Player otherPlayer = game.getPlayers().get(0) == currentPlayer ? game.getPlayers().get(1) : game.getPlayers().get(0);

        // Try placing first worker on top of other worker
        actionEvent = new ActionEvent(new Position(0,0));
        actionEvent.setSignature(game.getCurrentPlayer().getNickname());

        controller.update(actionEvent);

        assertEquals(EStartupPhase.PLACEFIRSTWORKER, game.getStartupPhase());
        assertEquals(currentPlayer.getNickname(), game.getCurrentPlayer().getNickname());
        BoardTest.boardTest(board, "boardEmpty.txt");
        WorkerTest.workerTest(board.getTile(0,0), otherPlayer, 0, otherPlayer.getWorkers()[0]);
        WorkerTest.workerTest(board.getTile(0,1), otherPlayer, 1, otherPlayer.getWorkers()[1]);
        WorkerTest.workerTest(null, currentPlayer, 0, currentPlayer.getWorkers()[0]);
        WorkerTest.workerTest(null, currentPlayer, 1, currentPlayer.getWorkers()[1]);

        // Valid first worker placement
        actionEvent = new ActionEvent(new Position(3,0));
        actionEvent.setSignature(game.getCurrentPlayer().getNickname());

        controller.update(actionEvent);

        assertEquals(EStartupPhase.PLACESECONDWORKER, game.getStartupPhase());
        assertEquals(currentPlayer.getNickname(), game.getCurrentPlayer().getNickname());
        BoardTest.boardTest(board, "boardEmpty.txt");
        WorkerTest.workerTest(board.getTile(0,0), otherPlayer, 0, otherPlayer.getWorkers()[0]);
        WorkerTest.workerTest(board.getTile(0,1), otherPlayer, 1, otherPlayer.getWorkers()[1]);
        WorkerTest.workerTest(board.getTile(3,0), currentPlayer, 0, currentPlayer.getWorkers()[0]);
        WorkerTest.workerTest(null, currentPlayer, 1, currentPlayer.getWorkers()[1]);

    }

    @Order(8)
    @Test
    public void secondPlayerPlaceSecondWorkerSimpleTest() {

        ActionEvent actionEvent;
        Player oldCurrentPlayer = game.getCurrentPlayer();
        Player otherPlayer = game.getPlayers().get(0) == oldCurrentPlayer ? game.getPlayers().get(1) : game.getPlayers().get(0);

        // Try placing second worker on top of first worker
        actionEvent = new ActionEvent(new Position(3,0));
        actionEvent.setSignature(game.getCurrentPlayer().getNickname());

        controller.update(actionEvent);

        assertEquals(EStartupPhase.PLACESECONDWORKER, game.getStartupPhase());
        assertEquals(oldCurrentPlayer.getNickname(), game.getCurrentPlayer().getNickname());
        BoardTest.boardTest(board, "boardEmpty.txt");
        WorkerTest.workerTest(board.getTile(3,0), oldCurrentPlayer, 0, oldCurrentPlayer.getWorkers()[0]);
        WorkerTest.workerTest(null, oldCurrentPlayer, 1, oldCurrentPlayer.getWorkers()[1]);
        WorkerTest.workerTest(board.getTile(0,0), otherPlayer, 0, otherPlayer.getWorkers()[0]);
        WorkerTest.workerTest(board.getTile(0,1), otherPlayer, 1, otherPlayer.getWorkers()[1]);

        // Valid second worker placement
        actionEvent = new ActionEvent(new Position(4,4));
        actionEvent.setSignature(game.getCurrentPlayer().getNickname());

        controller.update(actionEvent);

        assertEquals(EStartupPhase.GAMESTARTED, game.getStartupPhase());
        assertEquals(otherPlayer.getNickname(), game.getCurrentPlayer().getNickname());
        BoardTest.boardTest(board, "boardEmpty.txt");
        WorkerTest.workerTest(board.getTile(3,0), oldCurrentPlayer, 0, oldCurrentPlayer.getWorkers()[0]);
        WorkerTest.workerTest(board.getTile(4,4), oldCurrentPlayer, 1, oldCurrentPlayer.getWorkers()[1]);
        WorkerTest.workerTest(board.getTile(0,0), otherPlayer, 0, otherPlayer.getWorkers()[0]);
        WorkerTest.workerTest(board.getTile(0,1), otherPlayer, 1, otherPlayer.getWorkers()[1]);
        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
        assertTrue(controller.isActive());

    }

    @Order(9)
    @Test
    public void simpleWorkerSelectionTest() {

        ActionEvent selectWorkerEvent;
        Player currentPlayer = game.getCurrentPlayer();
        Player otherPlayer = game.getPlayers().get(0) == currentPlayer ? game.getPlayers().get(1) : game.getPlayers().get(0);

        selectWorkerEvent = new ActionEvent(new Position(0,3));
        selectWorkerEvent.setSignature(game.getCurrentPlayer().getNickname());

        controller.update(selectWorkerEvent);

        assertEquals(EStartupPhase.GAMESTARTED, game.getStartupPhase());
        assertEquals(currentPlayer.getNickname(), game.getCurrentPlayer().getNickname());
        BoardTest.boardTest(board, "boardEmpty.txt");
        WorkerTest.workerTest(board.getTile(0,0), currentPlayer, 0, currentPlayer.getWorkers()[0]);
        WorkerTest.workerTest(board.getTile(0,1), currentPlayer, 1, currentPlayer.getWorkers()[1]);
        WorkerTest.workerTest(board.getTile(3,0), otherPlayer, 0, otherPlayer.getWorkers()[0]);
        WorkerTest.workerTest(board.getTile(4,4), otherPlayer, 1, otherPlayer.getWorkers()[1]);
        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
        assertTrue(controller.isActive());

        selectWorkerEvent = new ActionEvent(new Position(0,0));
        selectWorkerEvent.setSignature(game.getCurrentPlayer().getNickname());

        controller.update(selectWorkerEvent);

        assertEquals(EStartupPhase.GAMESTARTED, game.getStartupPhase());
        assertEquals(currentPlayer.getNickname(), game.getCurrentPlayer().getNickname());
        BoardTest.boardTest(board, "boardEmpty.txt");
        WorkerTest.workerTest(board.getTile(0,0), currentPlayer, 0, currentPlayer.getWorkers()[0]);
        WorkerTest.workerTest(board.getTile(0,1), currentPlayer, 1, currentPlayer.getWorkers()[1]);
        WorkerTest.workerTest(board.getTile(3,0), otherPlayer, 0, otherPlayer.getWorkers()[0]);
        WorkerTest.workerTest(board.getTile(4,4), otherPlayer, 1, otherPlayer.getWorkers()[1]);
        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());
        assertTrue(controller.isActive());

    }

    @Order(10)
    @Test
    public void simpleMoveTest() {

        ActionEvent moveEvent;
        Player currentPlayer = game.getCurrentPlayer();
        Player otherPlayer = game.getPlayers().get(0) == currentPlayer ? game.getPlayers().get(1) : game.getPlayers().get(0);

        // Invalid move
        moveEvent = new ActionEvent(new Position(0,3));
        moveEvent.setSignature(game.getCurrentPlayer().getNickname());

        controller.update(moveEvent);

        assertEquals(EStartupPhase.GAMESTARTED, game.getStartupPhase());
        assertEquals(currentPlayer.getNickname(), game.getCurrentPlayer().getNickname());
        BoardTest.boardTest(board, "boardEmpty.txt");
        WorkerTest.workerTest(board.getTile(0,0), currentPlayer, 0, currentPlayer.getWorkers()[0]);
        WorkerTest.workerTest(board.getTile(0,1), currentPlayer, 1, currentPlayer.getWorkers()[1]);
        WorkerTest.workerTest(board.getTile(3,0), otherPlayer, 0, otherPlayer.getWorkers()[0]);
        WorkerTest.workerTest(board.getTile(4,4), otherPlayer, 1, otherPlayer.getWorkers()[1]);
        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());
        assertTrue(controller.isActive());

        // Valid move by NOT the current player
        moveEvent = new ActionEvent(new Position(1,0));
        moveEvent.setSignature(otherPlayer.getNickname());

        controller.update(moveEvent);

        assertEquals(EStartupPhase.GAMESTARTED, game.getStartupPhase());
        assertEquals(currentPlayer.getNickname(), game.getCurrentPlayer().getNickname());
        BoardTest.boardTest(board, "boardEmpty.txt");
        WorkerTest.workerTest(board.getTile(0,0), currentPlayer, 0, currentPlayer.getWorkers()[0]);
        WorkerTest.workerTest(board.getTile(0,1), currentPlayer, 1, currentPlayer.getWorkers()[1]);
        WorkerTest.workerTest(board.getTile(3,0), otherPlayer, 0, otherPlayer.getWorkers()[0]);
        WorkerTest.workerTest(board.getTile(4,4), otherPlayer, 1, otherPlayer.getWorkers()[1]);
        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());
        assertTrue(controller.isActive());

        // Valid move to (1,0)
        moveEvent = new ActionEvent(new Position(1,0));
        moveEvent.setSignature(game.getCurrentPlayer().getNickname());

        controller.update(moveEvent);

        assertEquals(EStartupPhase.GAMESTARTED, game.getStartupPhase());
        assertEquals(currentPlayer.getNickname(), game.getCurrentPlayer().getNickname());
        BoardTest.boardTest(board, "boardEmpty.txt");
        WorkerTest.workerTest(board.getTile(1,0), currentPlayer, 0, currentPlayer.getWorkers()[0]);
        WorkerTest.workerTest(board.getTile(0,1), currentPlayer, 1, currentPlayer.getWorkers()[1]);
        WorkerTest.workerTest(board.getTile(3,0), otherPlayer, 0, otherPlayer.getWorkers()[0]);
        WorkerTest.workerTest(board.getTile(4,4), otherPlayer, 1, otherPlayer.getWorkers()[1]);
        assertEquals(ETurnPhase.GODPOWER, game.getTurnPhase());
        assertTrue(controller.isActive());

    }

    @Order(11)
    @Test
    public void simpleGodPowerChoiceTest() {

        ChoiceEvent godPowerChoiceEvent;
        ActionEvent actionEvent;
        Player currentPlayer = game.getCurrentPlayer();
        Player otherPlayer = game.getPlayers().get(0) == currentPlayer ? game.getPlayers().get(1) : game.getPlayers().get(0);

        // Choice not to use the godpower from NOT the current player
        godPowerChoiceEvent = new ChoiceEvent(false);
        godPowerChoiceEvent.setSignature(otherPlayer.getNickname());

        controller.update(godPowerChoiceEvent);

        assertEquals(EStartupPhase.GAMESTARTED, game.getStartupPhase());
        assertEquals(currentPlayer.getNickname(), game.getCurrentPlayer().getNickname());
        BoardTest.boardTest(board, "boardEmpty.txt");
        WorkerTest.workerTest(board.getTile(1,0), currentPlayer, 0, currentPlayer.getWorkers()[0]);
        WorkerTest.workerTest(board.getTile(0,1), currentPlayer, 1, currentPlayer.getWorkers()[1]);
        WorkerTest.workerTest(board.getTile(3,0), otherPlayer, 0, otherPlayer.getWorkers()[0]);
        WorkerTest.workerTest(board.getTile(4,4), otherPlayer, 1, otherPlayer.getWorkers()[1]);
        assertEquals(ETurnPhase.GODPOWER, game.getTurnPhase());
        assertTrue(controller.isActive());

        // Choice to use the godpower from the current player
        godPowerChoiceEvent = new ChoiceEvent(true);
        godPowerChoiceEvent.setSignature(game.getCurrentPlayer().getNickname());

        controller.update(godPowerChoiceEvent);

        assertEquals(EStartupPhase.GAMESTARTED, game.getStartupPhase());
        assertEquals(currentPlayer.getNickname(), game.getCurrentPlayer().getNickname());
        BoardTest.boardTest(board, "boardEmpty.txt");
        WorkerTest.workerTest(board.getTile(1,0), currentPlayer, 0, currentPlayer.getWorkers()[0]);
        WorkerTest.workerTest(board.getTile(0,1), currentPlayer, 1, currentPlayer.getWorkers()[1]);
        WorkerTest.workerTest(board.getTile(3,0), otherPlayer, 0, otherPlayer.getWorkers()[0]);
        WorkerTest.workerTest(board.getTile(4,4), otherPlayer, 1, otherPlayer.getWorkers()[1]);
        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());
        assertTrue(controller.isActive());

        // Valid move by NOT the current player
        actionEvent = new ActionEvent(new Position(1,1));
        actionEvent.setSignature(otherPlayer.getNickname());

        controller.update(actionEvent);

        assertEquals(EStartupPhase.GAMESTARTED, game.getStartupPhase());
        assertEquals(currentPlayer.getNickname(), game.getCurrentPlayer().getNickname());
        BoardTest.boardTest(board, "boardEmpty.txt");
        WorkerTest.workerTest(board.getTile(1,0), currentPlayer, 0, currentPlayer.getWorkers()[0]);
        WorkerTest.workerTest(board.getTile(0,1), currentPlayer, 1, currentPlayer.getWorkers()[1]);
        WorkerTest.workerTest(board.getTile(3,0), otherPlayer, 0, otherPlayer.getWorkers()[0]);
        WorkerTest.workerTest(board.getTile(4,4), otherPlayer, 1, otherPlayer.getWorkers()[1]);
        assertEquals(ETurnPhase.MOVE, game.getTurnPhase());
        assertTrue(controller.isActive());

        // Valid move by the current player
        actionEvent = new ActionEvent(new Position(2,0));
        actionEvent.setSignature(game.getCurrentPlayer().getNickname());

        controller.update(actionEvent);

        assertEquals(EStartupPhase.GAMESTARTED, game.getStartupPhase());
        assertEquals(currentPlayer.getNickname(), game.getCurrentPlayer().getNickname());
        BoardTest.boardTest(board, "boardEmpty.txt");
        WorkerTest.workerTest(board.getTile(2,0), currentPlayer, 0, currentPlayer.getWorkers()[0]);
        WorkerTest.workerTest(board.getTile(0,1), currentPlayer, 1, currentPlayer.getWorkers()[1]);
        WorkerTest.workerTest(board.getTile(3,0), otherPlayer, 0, otherPlayer.getWorkers()[0]);
        WorkerTest.workerTest(board.getTile(4,4), otherPlayer, 1, otherPlayer.getWorkers()[1]);
        assertEquals(ETurnPhase.BUILD, game.getTurnPhase());
        assertTrue(controller.isActive());

    }

    @Order(12)
    @Test
    public void simpleBuildTest() {

        ActionEvent buildEvent;
        Player currentPlayer = game.getCurrentPlayer();
        Player otherPlayer = game.getPlayers().get(0) == currentPlayer ? game.getPlayers().get(1) : game.getPlayers().get(0);

        // Invalid build by the current player to (4,4)
        buildEvent = new ActionEvent(new Position(4,4));
        buildEvent.setSignature(game.getCurrentPlayer().getNickname());

        controller.update(buildEvent);

        assertEquals(EStartupPhase.GAMESTARTED, game.getStartupPhase());
        assertEquals(currentPlayer.getNickname(), game.getCurrentPlayer().getNickname());
        BoardTest.boardTest(board, "boardEmpty.txt");
        WorkerTest.workerTest(board.getTile(2,0), currentPlayer, 0, currentPlayer.getWorkers()[0]);
        WorkerTest.workerTest(board.getTile(0,1), currentPlayer, 1, currentPlayer.getWorkers()[1]);
        WorkerTest.workerTest(board.getTile(3,0), otherPlayer, 0, otherPlayer.getWorkers()[0]);
        WorkerTest.workerTest(board.getTile(4,4), otherPlayer, 1, otherPlayer.getWorkers()[1]);
        assertEquals(ETurnPhase.BUILD, game.getTurnPhase());
        assertTrue(controller.isActive());

        // Valid build by the current player to (2,1)
        buildEvent = new ActionEvent(new Position(2,1));
        buildEvent.setSignature(game.getCurrentPlayer().getNickname());

        controller.update(buildEvent);

        assertEquals(EStartupPhase.GAMESTARTED, game.getStartupPhase());
        assertEquals(otherPlayer.getNickname(), game.getCurrentPlayer().getNickname());
        BoardTest.boardTest(board, "boards/boardControllerTest.txt");
        WorkerTest.workerTest(board.getTile(2,0), currentPlayer, 0, currentPlayer.getWorkers()[0]);
        WorkerTest.workerTest(board.getTile(0,1), currentPlayer, 1, currentPlayer.getWorkers()[1]);
        WorkerTest.workerTest(board.getTile(3,0), otherPlayer, 0, otherPlayer.getWorkers()[0]);
        WorkerTest.workerTest(board.getTile(4,4), otherPlayer, 1, otherPlayer.getWorkers()[1]);
        assertEquals(ETurnPhase.WORKERSELECTION, game.getTurnPhase());
        assertTrue(controller.isActive());

    }

}