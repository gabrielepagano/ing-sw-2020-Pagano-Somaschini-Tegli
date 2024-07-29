package it.polimi.ingsw.network.events;

/**
 * This {@code Event} is used to indicate that some kind of client disconnection has happened, either voluntarily or accidentally
 */
public class DisconnectedEvent extends Event{
    private final String description;

    /**
     * Creates a new {@code DisconnectedEvent} object, giving a {@code String} as input to describe it.
     * @param description  the {@code String} that describes the {@code DisconnectedEvent}
     */
    public DisconnectedEvent(String description) {
        this.description = description;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "DisconnectedEvent: " + description;
    }
}
