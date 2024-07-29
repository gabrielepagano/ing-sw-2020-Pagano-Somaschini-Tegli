package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.EStartupPhase;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.DisconnectedException;
import it.polimi.ingsw.network.Observer;
import it.polimi.ingsw.network.events.*;
import it.polimi.ingsw.server.RemoteConnection;

import java.util.List;

/**
 * This class represents the user view element server-side.
 * It is connected to the Model & Controller classes and only redirects their interactions through the socket.
 */
public class RemoteView extends View {

    /**
     * This class is used to receive {@code Event} from the socket and manage them.
     * It's an {@code Observer} of {@code RemoteConnection}
     */
    private class MessageReceiver implements Observer {

        /**
         * Forward all notifications from the socket to the {@code Controller} through this {@code RemoteView}.
         * All {@code Event} gets marked so that the sender identity don't get lost.
         *
         * @param arg Event received
         */
        @Override
        public void update(Event arg) {
            arg.setSignature(nickname);
            setChanged();
            RemoteView.this.notify(arg);
        }

    }

    private RemoteConnection connection;
    private String nickname;


    public RemoteView(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Link up this class with an instance of {@code RemoteConnection} that will manage socket communication.
     *
     * @param connection Instance of socket wrapper
     */
    public void initializeConnection(RemoteConnection connection) {

        if (connection == null) {
            throw new IllegalArgumentException("Connection cannot be null");
        }
        if(this.connection == null) {
            this.connection = connection;
            this.connection.registerObserver(new MessageReceiver());
        }

    }

    /**
     * Returns the nickname of the {@code Player} relative to this view.
     *
     * @return Player's nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * {@inheritDoc}
     *
     * @return The event read from the input source
     * @throws DisconnectedException if the socket communication fails
     */
    @Override
    public Event readEvent() throws DisconnectedException {
        Event received = connection.receiveEvent(3 * 1000);
        if(received instanceof DisconnectedEvent) {
            connection.close();
        }
        return received;
    }

    /**
     * {@inheritDoc}
     * @param message The string representing the textual message to be printed
     * @throws DisconnectedException if the socket communication fails
     */
    @Override
    public void printMessage(String message) throws DisconnectedException {
        connection.sendEvent(new MessageEvent(message));
    }

    /**
     * {@inheritDoc}
     *
     * @param message The string representing the textual message to be printed
     * @param currentPlayerReserved Specify if the message is reserved for the current player
     * @throws DisconnectedException if the socket communication fails
     */
    @Override
    public void printMessage(String message, boolean currentPlayerReserved) throws DisconnectedException {
        MessageEvent messageEvent = new MessageEvent(message, currentPlayerReserved);
        connection.sendEvent(messageEvent);
    }

    /**
     * {@inheritDoc}
     *
     * @param message The string representing the textual message to be printed
     * @throws DisconnectedException if the socket communication fails
     */
    @Override
    public void logMessage(String message) throws DisconnectedException {
        MessageEvent messageEvent = new MessageEvent(message);
        connection.sendEvent(messageEvent);
    }

    /**
     * {@inheritDoc}
     *
     * @param message The string representing the textual message to be printed
     * @param currentPlayerReserved Specify if the message is reserved for the current player
     * @throws DisconnectedException if the socket communication fails
     */
    @Override
    public void logMessage(String message, boolean currentPlayerReserved) throws DisconnectedException {
        MessageEvent messageEvent = new MessageEvent(message, currentPlayerReserved);
        connection.sendEvent(messageEvent);
    }

    /**
     * {@inheritDoc}
     *
     * @param tilesToUpdate The list of {@code Tile} that have been updated
     * @throws DisconnectedException if the socket communication fails
     */
    @Override
    public void updateBoard(List<Tile> tilesToUpdate) throws DisconnectedException {
        BoardEvent update = new BoardEvent(tilesToUpdate);
        connection.sendEvent(update);
    }

    /**
     * {@inheritDoc}
     *
     * @param positionsToHighlight The list of {@code Position} that have to be highlighted
     * @throws DisconnectedException if the socket communication fails
     */
    @Override
    public void highlightTiles(List<Position> positionsToHighlight) throws DisconnectedException {
        connection.sendEvent(new HighlightEvent(positionsToHighlight));
    }

    /**
     * {@inheritDoc}
     *
     * @param question The string representing the question to be asked
     * @param opTrue The string representing the textual answer for {@code true}
     * @param opFalse The string representing the textual answer for {@code false}
     * @throws DisconnectedException if the socket communication fails
     */
    @Override
    public void askChoice(String question, String opTrue, String opFalse) throws DisconnectedException {
        connection.sendEvent(new ChoiceEvent(question, opTrue, opFalse));
    }

    /**
     * {@inheritDoc}
     *
     * @param warning The string representing the textual message to be printed
     * @param currentPlayerReserved Specify if the message is reserved for the current player
     * @throws DisconnectedException if the socket communication fails
     */
    @Override
    public void printError(String warning, boolean currentPlayerReserved) throws DisconnectedException {
        connection.sendEvent(new ErrorEvent(warning, currentPlayerReserved));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setGameOver() {
        try {
            connection.sendEvent(new GameOverEvent());
        } catch (DisconnectedException e) {
            // Ignore it since we are closing the connection anyway
        }
        // Send it back to the controller
        setChanged();
        notify(new GameOverEvent());

        connection.close();
    }

    /**
     * {@inheritDoc}
     *
     * @param nickname Current player's nickname
     * @throws DisconnectedException if the socket communication fails
     */
    @Override
    public void setCurrentPlayer(String nickname) throws DisconnectedException {
        connection.sendEvent(new SetCurrentPlayerEvent(nickname));
    }

    /**
     * {@inheritDoc}
     *
     * @param nickname Challenger's nickname
     * @throws DisconnectedException if the socket communication fails
     */
    @Override
    public void setChallenger(String nickname) throws DisconnectedException {
        connection.sendEvent(new SetChallengerEvent(nickname));
    }

    /**
     * {@inheritDoc}
     *
     * @param acceptedNickname Client's nickname as double check
     * @throws DisconnectedException if the socket communication fails
     */
    @Override
    public void setNickname(String acceptedNickname) throws DisconnectedException {
        connection.sendEvent(new SetNicknameEvent(acceptedNickname));
    }

    /**
     * {@inheritDoc}
     *
     * @param acceptedNPlayers Lobby size specified by the client as a double check
     * @throws DisconnectedException if the socket communication fails
     */
    @Override
    public void setNPlayers(int acceptedNPlayers) throws DisconnectedException {
        connection.sendEvent(new SetNPlayersEvent(acceptedNPlayers));
    }

    /**
     * {@inheritDoc}
     *
     * @param selectedCard God relative the chosen card
     * @throws DisconnectedException if the socket communication fails
     */
    @Override
    public void setSelectedCard(EGodPower selectedCard) throws DisconnectedException {
        connection.sendEvent(new CardEvent(selectedCard));
    }

    /**
     * {@inheritDoc}
     *
     * @param startupPhase Match initialization phase
     * @throws DisconnectedException if the socket communication fails
     */
    @Override
    public void setStartupPhase(EStartupPhase startupPhase) throws DisconnectedException {
        connection.sendEvent(new StartupPhaseEvent(startupPhase));
    }

    /**
     * {@inheritDoc}
     *
     * @param turnPhase Turn phase currently taking place
     * @throws DisconnectedException if the socket communication fails
     */
    @Override
    public void setTurnPhase(ETurnPhase turnPhase) throws  DisconnectedException {
        connection.sendEvent(new TurnPhaseEvent(turnPhase));
    }

    /**
     * {@inheritDoc}
     *
     * @param loserNickname Losing player's nickname
     * @throws DisconnectedException if the socket communication fails
     */
    @Override
    public void setLoser(String loserNickname) throws DisconnectedException {
        connection.sendEvent(new LoseEvent(loserNickname));
    }

    /**
     * {@inheritDoc}
     *
     * @param playersNicknames The list of {@code Player} nicknames
     * @throws DisconnectedException if the socket communication fails
     */
    @Override
    public void setPlayersNicknames(List<String> playersNicknames) throws DisconnectedException {
        connection.sendEvent(new StartGameEvent(playersNicknames));
    }

}
