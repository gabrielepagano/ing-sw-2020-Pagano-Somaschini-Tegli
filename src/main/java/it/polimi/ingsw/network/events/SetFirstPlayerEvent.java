package it.polimi.ingsw.network.events;

/**
 * This {@code Event} is used by the server to notify the clients of a game about the first
 * player, set by the challenger during the initialization
 */
public class SetFirstPlayerEvent extends Event {

    private String firstPlayerNickname;

    /**
     * Creates a new {@code SetFirstPlayerEvent} giving a {@code String} as input that represents the nickname of who
     * will be the first player.
     * @param firstPlayerNickname  the nickname of who will be the first player
     */
    public SetFirstPlayerEvent(String firstPlayerNickname) {
        this.firstPlayerNickname = firstPlayerNickname;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "SetFirstPlayerEvent: " + firstPlayerNickname;
    }
}
