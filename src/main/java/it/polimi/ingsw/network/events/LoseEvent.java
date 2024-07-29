package it.polimi.ingsw.network.events;

/**
 * This {@code Event} is used by the server to notify the clients of a game that someone lost
 */
public class LoseEvent extends Event {

    private String loserNickname;

    /**
     * Creates a new {@code LoseEvent} object, giving as input a {@code String} that represents the nickname of
     * the loser {@code Player}.
     * @param loserNickname  the nickname of the loser {@code Player}
     */
    public LoseEvent(String loserNickname) {
        this.loserNickname = loserNickname;
    }

    /**
     * Getter for the loser player nickname carried by this {@code Event}
     * @return the {@param loserNickname}
     */
    public String getLoserNickname() {
        return loserNickname;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "LoseEvent: " + getLoserNickname();
    }
}
