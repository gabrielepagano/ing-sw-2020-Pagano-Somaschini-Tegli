package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.EStartupPhase;
import it.polimi.ingsw.model.EGodPower;
import it.polimi.ingsw.model.ETurnPhase;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.network.DisconnectedException;
import it.polimi.ingsw.network.Observable;
import it.polimi.ingsw.network.Observer;
import it.polimi.ingsw.network.events.*;

import java.util.List;

/**
 * The methods of the View class represents the operations common to both
 * the ClientView-s and the RemoteView, which however take on different meanings
 * depending on the side of the connection:
 * <ul>
 *     <li>server-side, the RemoteView reads objects (Events) from the socket and
 *     prints objects to the socket (forwarding Model's notifications to the
 *     ClientView)</li>
 *     <li>client-side, the ClientView reads from stdin and prints to stdout, with
 *     the most relevant inputs that are parsed and sent to the server through
 *     ClientConnection (so that view methods' invocation is forwarded to RemoteView)</li>
 * </ul>
 * Details on the Observer/Observable pattern are given in the subclasses' descriptions
 */
public abstract class View extends Observable implements Observer {

    /**
     * Reads an object (Event) from the main input of this view element
     * <ul>
     *     <li>stdin for {@code ClientView}</li>
     *     <li>inferred graphical element for {@code GUIClientView}</li>
     *     <li>socket input stream for {@code RemoteView}</li>
     * </ul>
     * @return The event read from the input source
     * @throws DisconnectedException if the socket communication fails
     */
    public abstract Event readEvent() throws DisconnectedException;

    /**
     * Prints a message to the main output of this view element
     * <ul>
     *     <li>stdout for {@code ClientView}</li>
     *     <li>relative graphical element for {@code GUIClientView}</li>
     *     <li>socket output stream for {@code RemoteView}</li>
     * </ul>
     * @param message The string representing the textual message to be printed
     * @throws DisconnectedException if the socket communication fails
     */
    public abstract void printMessage(String message) throws DisconnectedException;

    /**
     * Prints a message to the main output of this view element, but only to the current {@code Player} if specified
     * <ul>
     *     <li>stdout for {@code ClientView}</li>
     *     <li>relative graphical element for {@code GUIClientView}</li>
     *     <li>socket output stream for {@code RemoteView}</li>
     * </ul>
     * @param message The string representing the textual message to be printed
     * @param currentPlayerReserved Specify if the message is reserved for the current player
     * @throws DisconnectedException if the socket communication fails
     */
    public abstract void printMessage(String message, boolean currentPlayerReserved) throws DisconnectedException;

    /**
     * Prints an informative message about recent game actions to the main output of this view element
     * <ul>
     *     <li>stdout for {@code ClientView}</li>
     *     <li>relative graphical element for {@code GUIClientView}</li>
     *     <li>socket output stream for {@code RemoteView}</li>
     * </ul>
     * @param message The string representing the textual message to be printed
     * @throws DisconnectedException if the socket communication fails
     */
    public abstract void logMessage(String message) throws DisconnectedException;

    /**
     * Prints an informative message about recent game actions to the main output of this view element,
     * but only to the current {@code Player} if specified
     * <ul>
     *     <li>stdout for {@code ClientView}</li>
     *     <li>relative graphical element for {@code GUIClientView}</li>
     *     <li>socket output stream for {@code RemoteView}</li>
     * </ul>
     * @param message The string representing the textual message to be printed
     * @param currentPlayerReserved Specify if the message is reserved for the current player
     * @throws DisconnectedException if the socket communication fails
     */
    public abstract void logMessage(String message, boolean currentPlayerReserved) throws DisconnectedException;

    /**
     * Updates the copy of the current game's {@code Board} kept by this view element with the specified list of changes.
     * Immediately after a board update, its representation should be refreshed according to the methodology of this view element.
     *
     * @param tilesToUpdate The list of {@code Tile} that have been updated
     * @throws DisconnectedException if the socket communication fails
     */
    public abstract void updateBoard(List<Tile> tilesToUpdate) throws DisconnectedException;

    /**
     * Highlights the specified positions of the game's {@code Board} according to the methodology of this view element.
     *
     *
     * @param positionsToHighlight The list of {@code Position} that have to be highlighted
     * @throws DisconnectedException if the socket communication fails
     */
    public abstract void highlightTiles(List<Position> positionsToHighlight) throws DisconnectedException;

    /**
     * Prints a true/false question (according to the methodology of this view element) that accepts only 2 possible answers, specified as arguments.
     *
     * Ideally, until a valid answer to the question is given, this view element should filter and
     * block all playing inputs (treating them as invalid or simply ignoring them), while letting pass
     * the more general ones, like "quit".
     *
     * @param question The string representing the question to be asked
     * @param opTrue The string representing the textual answer for {@code true}
     * @param opFalse The string representing the textual answer for {@code false}
     * @throws DisconnectedException if the socket communication fails
     */
    public abstract void askChoice(String question, String opTrue, String opFalse) throws DisconnectedException;

    /**
     * Prints a warning (in the main output of this view element) in response of an illegal input forwarded by the user, that resulted in a suppressed action.
     * <ul>
     *     <li>stdout for {@code ClientView}</li>
     *     <li>relative graphical element for {@code GUIClientView}</li>
     *     <li>socket output stream for {@code RemoteView}</li>
     * </ul>
     * Note that this kinds of events are always reserved to the current player, with the exception of disconnection notification.
     *
     * @param warning The string representing the textual message to be printed
     * @param currentPlayerReserved Specify if the message is reserved for the current player
     * @throws DisconnectedException if the socket communication fails
     */
    public abstract void printError(String warning, boolean currentPlayerReserved) throws DisconnectedException;

    /**
     * Notifies that the game has resolved to a winner and it's about to be shutdown.
     * This is always the last message send during a match.
     *
     * @throws DisconnectedException if the socket communication fails
     */
    public abstract void setGameOver() throws DisconnectedException;

    /**
     * Notifies the new current player'identity, specified through its nickname.
     * Related to the start of a turn.
     *
     * @param nickname Current player's nickname
     * @throws DisconnectedException if the socket communication fails
     */
    public abstract void setCurrentPlayer(String nickname) throws DisconnectedException;

    /**
     * Notifies the challenger's identity to the players, specified through its nickname.
     * This is sent once during game initialization.
     *
     * @param nickname Challenger's nickname
     * @throws DisconnectedException if the socket communication fails
     */
    public abstract void setChallenger(String nickname) throws DisconnectedException;

    /**
     * Notifies the client that the nickname insert has been accepted by the {@code Server}.
     * More of an ack than actual "setting" communication.
     *
     * @param acceptedNickname Client's nickname as double check
     * @throws DisconnectedException if the socket communication fails
     */
    public abstract void setNickname(String acceptedNickname) throws DisconnectedException;

    /**
     * Notifies the client that the lobby size (2 or 3) chosen has been accepted by the {@code Server}.
     * More of an ack than actual "setting" communication.
     *
     * @param acceptedNPlayers Lobby size specified by the client as a double check
     * @throws DisconnectedException if the socket communication fails
     */
    public abstract void setNPlayers(int acceptedNPlayers) throws DisconnectedException;

    /**
     * Notifies the client a {@code Card}, identified through their specific god, that has been chosen and will be present during the game.
     *
     * @param selectedCard God relative the chosen card
     * @throws DisconnectedException if the socket communication fails
     */
    public abstract void setSelectedCard(EGodPower selectedCard) throws DisconnectedException;

    /**
     * Notifies the client about the progression of the match initialization
     *
     * @param startupPhase Match initialization phase
     * @throws DisconnectedException if the socket communication fails
     */
    public abstract void setStartupPhase(EStartupPhase startupPhase) throws DisconnectedException;

    /**
     * Notifies the client about the progression of the current turn
     *
     * @param turnPhase Turn phase currently taking place
     * @throws DisconnectedException if the socket communication fails
     */
    public abstract void setTurnPhase(ETurnPhase turnPhase) throws DisconnectedException;

    /**
     * Notifies the client about the defeat of one a {@code Player}
     *
     * @param loserNickname Losing player's nickname
     * @throws DisconnectedException if the socket communication fails
     */
    public abstract void setLoser(String loserNickname) throws DisconnectedException;

    /**
     * Send to all clients the full list of players of this match
     *
     * @param playersNicknames The list of {@code Player} nicknames
     * @throws DisconnectedException if the socket communication fails
     */
    public abstract void setPlayersNicknames(List<String> playersNicknames) throws DisconnectedException;

    /**
     * Receives events from an Observable observed by this view element and handles them,
     * based on their specific type.
     *
     * After determining the nature of the notification, {@code update} proceeds to unpack the
     * Event, extracts the relevant information (e.g., a list of changes, a message to print, ...)
     * and calls the most suitable handling method.
     *
     * The view shouldn't receive
     * <ul>
     *     <li>{@code ActionEvent}s</li>
     *     <li>{@code PingEvent}s</li>
     * </ul>
     *
     * @param arg The event representing details of the received notification
     */
    @Override
    public void update(Event arg){
        try {

            if (arg instanceof MessageEvent) {
                MessageEvent received = (MessageEvent) arg;

                logMessage(received.getMessage(), received.isReserved());
            }
            else if (arg instanceof BoardEvent) {
                BoardEvent received = (BoardEvent) arg;

                updateBoard(received.getTilesToUpdate());
            }
            else if (arg instanceof HighlightEvent) {
                HighlightEvent received = (HighlightEvent) arg;

                highlightTiles(received.getPositionsToHighlight());
            }
            else if (arg instanceof ChoiceEvent) {
                ChoiceEvent received = (ChoiceEvent) arg;

                askChoice(received.getQuestion(), received.getOptionTrue(), received.getOptionFalse());
            }
            else if (arg instanceof ErrorEvent){
                ErrorEvent received = (ErrorEvent) arg;

                printError(received.getWarning(), received.isReserved());
            }
            else if(arg instanceof SetCurrentPlayerEvent){
                SetCurrentPlayerEvent received = (SetCurrentPlayerEvent) arg;

                setCurrentPlayer(received.getCurrentPlayer());
            }
            else if(arg instanceof DisconnectedEvent) {
                printMessage("A disconnection took place");
            }
            else if (arg instanceof GameOverEvent) {
                // Close connection
                // Notify controller and make it inactive
                setGameOver();
            }
            else if (arg instanceof StartGameEvent) {
                StartGameEvent received = (StartGameEvent) arg;

                setPlayersNicknames(received.getPlayerNicknames());
            }
            else if (arg instanceof SetNicknameEvent) {
                SetNicknameEvent received = (SetNicknameEvent) arg;

                setNickname(received.getAcceptedNickname());
            }
            else if (arg instanceof SetNPlayersEvent) {
                SetNPlayersEvent received = (SetNPlayersEvent) arg;

                setNPlayers(received.getAcceptedNPlayers());
            }
            else if(arg instanceof SetChallengerEvent){
                SetChallengerEvent received = (SetChallengerEvent) arg;

                setChallenger(received.getChallengerNickname());
            }
            else if (arg instanceof CardEvent) {
                CardEvent received = (CardEvent) arg;

                setSelectedCard(received.getCard());
            }
            else if (arg instanceof StartupPhaseEvent) {
                StartupPhaseEvent received = (StartupPhaseEvent) arg;

                setStartupPhase(received.getNewStartupPhase());
            }
            else if (arg instanceof TurnPhaseEvent) {
                TurnPhaseEvent received = (TurnPhaseEvent) arg;

                setTurnPhase(received.getTurnPhase());
            }
            else if (arg instanceof LoseEvent) {
                LoseEvent received = (LoseEvent) arg;

                setLoser(received.getLoserNickname());
            }

        } catch(DisconnectedException e) {
            System.err.println(e.getMessage());
        }
    }

}
