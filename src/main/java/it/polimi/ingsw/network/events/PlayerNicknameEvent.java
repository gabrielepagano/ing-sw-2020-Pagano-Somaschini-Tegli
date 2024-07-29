package it.polimi.ingsw.network.events;

import it.polimi.ingsw.model.Player;

/**
 * This {@code Event} is sent by a client to the server and contains the nickname of one of
 * the players in the game to which that client belongs
 */
public class PlayerNicknameEvent extends Event {

    private String playerNickname;

    /**
     * Creates a new {@code PlayerNicknameEvent}, setting the nickname of the player
     * @param playerNickname a string representing the nickname of one of the players in a certain game
     */
    public PlayerNicknameEvent(String playerNickname) {
        this.playerNickname = playerNickname;
    }

    /**
     * Getter for the player nickname carried by this {@code Event}
     * @return
     */
    public String getPlayerNickname() {
        return playerNickname;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "PlayerNicknameEvent: " + playerNickname;
    }

}
