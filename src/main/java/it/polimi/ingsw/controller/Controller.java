package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.Observer;
import it.polimi.ingsw.network.events.*;
import it.polimi.ingsw.view.RemoteView;

import java.util.List;


/**
 * {@code Controller} represents the controller class made following the MVC pattern.
 * It implements the {@code Observer} interface since the fact that observe the changes of
 * the {@code RemoteView} and could do an update after them. In some ways, a controller represents a game,
 * receiving notifications from the {@code RemoteView}s bound to the players, filtering them and passing their
 * content to the model classes
 */
public class Controller implements Observer {

    private Game game;
    private List<Player> players;
    private boolean active;


    /**
     * <p> Build a {@code Controller} registering it on each {@code RemoteView} getting some information such as
     * the ArrayList of the players and the {@code Game} which is bound to. </p>
     * <p> It also let the game start with the first {@code EStartupPhase} phase: the picking of the challenger. </p>
     * @param remoteViews  the ArrayList of the server-side views of the different players
     * @param game  the game that has to be started
     */
    public Controller(List<RemoteView> remoteViews, Game game) {
        for (RemoteView v : remoteViews) {
            v.registerObserver(this);
        }
        this.game = game;
        this.players = game.getPlayers();
        this.active = true;
        init();
        this.game.pickChallenger();
    }

    /**
     * Initializes the game that has to be initialized.
     */
    public void init() {
        game.startGame();
    }

    /**
     * Tells if this controller is active
     * @return a boolean that indicates if the the controller is active or not
     */
    public boolean isActive() {
        return active;
    }


    /**
     * As usual with the Observer&Observable pattern, this is the entry point for the notifications (objects of
     * type {@code Event}) coming from the {@code RemoteView}s associated with this controller/game
     * @param arg the event that the {@code Observable} object that invoked the {@code notify} method passed as an argument
     */
    // TODO: refactor this method to reduce its complexity. We could enclose the switch
    //      statement on line 90 into a new method, like updateDuringInitialization(Event arg)
    @Override
    public synchronized void update(Event arg) {
        System.out.println(arg.toString());
        // If this controller is inactive, ignore all notifications. None should come anyway,
        // because the controller is declared inactive only after a GameOverEvent, which
        // should close all the connections (remote and client)
        if(isActive()) {
            // Accept DisconnectedEvent from anyone
            if (arg instanceof DisconnectedEvent) {
                // If still initializing the game
                if (game.getStartupPhase() != EStartupPhase.GAMESTARTED) {
                    // Notify everyone of the disconnection
                    // Close every connection (remote and client) with GameOverEvent
                    game.abort();
                    active = false;
                } else if (players.size() > 1){
                    Player loser = null;
                    for (Player p : players) {
                        if (p.getNickname().equals(arg.getSignature())) {
                            loser = p;
                        }
                    }
                    if(loser != null) {
                        game.setLoser(loser);
                    }
                }
            }
            else if (arg instanceof GameOverEvent) {
                active = false;
            }
            else if (arg.getSignature() != null && arg.getSignature().equals(game.getCurrentPlayer().getNickname())) {
                if (game.getStartupPhase() != EStartupPhase.GAMESTARTED) {
                    switch (game.getStartupPhase()) {

                        case PICKCARDS:

                            if (arg instanceof CardEvent) {
                                CardEvent received = (CardEvent) arg;
                                game.pickCards(received.getCard());
                            }
                            break;

                        case DEALCARDS:

                            if (arg instanceof CardEvent) {
                                CardEvent received = (CardEvent) arg;
                                game.dealCards(received.getCard());
                            }
                            break;

                        case PICKFIRSTPLAYER:

                            if (arg instanceof PlayerNicknameEvent) {
                                PlayerNicknameEvent received = (PlayerNicknameEvent) arg;
                                game.pickFirstPlayer(received.getPlayerNickname());
                            }
                            break;

                        case PLACEFIRSTWORKER:

                            if (arg instanceof ActionEvent) {
                                ActionEvent received = (ActionEvent) arg;
                                game.placeFirstWorker(received.getClicked());
                            }
                            break;

                        case PLACESECONDWORKER:

                            if (arg instanceof ActionEvent) {
                                ActionEvent received = (ActionEvent) arg;
                                game.placeSecondWorker(received.getClicked());
                            }
                            break;

                    }
                }
                else {

                    Position tilePos;
                    Tile t;

                    switch (game.getTurnPhase()) {
                        case WORKERSELECTION:
                            if (arg instanceof ActionEvent) {
                                tilePos = ((ActionEvent) arg).getClicked();
                                t = game.getBoard().getTile(tilePos);
                                game.getCurrentPlayerCard().getPlayable().selectWorker(t.getWorker());
                            }

                            break;

                        case MOVE:
                            if (arg instanceof ActionEvent) {
                                tilePos = ((ActionEvent) arg).getClicked();
                                t = game.getBoard().getTile(tilePos);
                                game.getCurrentPlayerCard().getPlayable().move(t);
                            }
                            break;

                        case BUILD:
                            if (arg instanceof ActionEvent) {
                                tilePos = ((ActionEvent) arg).getClicked();
                                t = game.getBoard().getTile(tilePos);
                                game.getCurrentPlayerCard().getPlayable().build(t);
                            }
                            break;

                        case GODPOWER:
                            if (arg instanceof ChoiceEvent) {
                                game.getCurrentPlayerCard().getPlayable().useGodPower(((ChoiceEvent) arg).getChoice());
                            }
                            break;
                    }

                }
            }
        }
    }

}



