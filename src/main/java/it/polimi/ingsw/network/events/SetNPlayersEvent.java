package it.polimi.ingsw.network.events;

/**
 * This {@code Event} is used by the server as an ack, to notify a client that is choice
 * on the number of players was valid and accepted
 */
public class SetNPlayersEvent extends Event {
    private int acceptedNPlayers;

    /**
     * Creates a new {@code SetNPlayersEvent}, setting the accepted number of players in the game
     * @param acceptedNPlayers  the accepted number of players in the game
     */
    public SetNPlayersEvent(int acceptedNPlayers) {
        this.acceptedNPlayers = acceptedNPlayers;
    }

    /**
     * Getter for the accepted number of players carried by this {@code Event}
     * @return the {@param acceptedNPlayers}
     */
    public int getAcceptedNPlayers() {
        return acceptedNPlayers;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "SetNPlayersEvent: " + acceptedNPlayers;
    }
}
