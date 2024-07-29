package it.polimi.ingsw.network.events;

/**
 * This {@code Event} represents a ping message, a simple and light heartbeat bounced back and forth by client and
 * server and used by both to check that the other is still reachable through the network
 */
public class PingEvent extends Event {

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Ping!";
    }
}
