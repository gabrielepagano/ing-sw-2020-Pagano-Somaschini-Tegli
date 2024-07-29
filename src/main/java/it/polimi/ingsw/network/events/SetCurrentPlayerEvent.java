package it.polimi.ingsw.network.events;

import it.polimi.ingsw.model.Player;

/**
 * This {@code Event} is used by the server to notify the clients of a game about the current player,
 * setting a new value for it
 */
public class SetCurrentPlayerEvent extends Event {
    public final String currentPlayer;

    /**
     * Creates a new {@code SetCurrentPlayerObject}, giving as input the {@code Player} which is the
     * new current player of the game
     * @param currentPlayer a player representing the new current player of a game
     */
    public SetCurrentPlayerEvent(Player currentPlayer){
        this.currentPlayer = currentPlayer.getNickname();
    }

    /**
     * Creates a new {@code SetCurrentPlayerObject}, giving as input a {@code String} representing
     * nickname of the new current player of the game
     * @param currentPlayerNick a string representing the nickname of the new current player of the game
     */
    public SetCurrentPlayerEvent(String currentPlayerNick){
        this.currentPlayer = currentPlayerNick;
    }


    /**
     * Getter for the new current player nickname carried by this {@code Event}
     * @return the {@param currentPlayerNick}
     */
    public String getCurrentPlayer() {
        return currentPlayer;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "SetCurrentPlayerEvent: " + currentPlayer;
    }

}
