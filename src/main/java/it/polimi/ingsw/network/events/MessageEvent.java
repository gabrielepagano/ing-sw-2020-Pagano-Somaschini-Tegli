package it.polimi.ingsw.network.events;

/**
 * {@code MessageEvent} represents an {@code Event} for a simple text message. Unlike an {@code ErrorEvent},
 * which should be immediately shown to the client, a messageEvent is logged and printed only at the right moment
 */
public class MessageEvent extends Event {
    private final String message;

    /**
     * Creates a new {@code MessageEvent} giving as input a {@code String} that represents a text message.
     * @param message a string representing a text message
     */
    public MessageEvent(String message) {
        this.message = message;
    }

    /**
     * Creates a new {@code MessageEvent}, setting the message and the fact that it is reserved for the
     * current player
     * @param message the text message
     * @param currentPlayerReserved a boolean representing the fact that the message carried by this {@code Event}
     *                              is reserved for the current player
     */
    public MessageEvent(String message, boolean currentPlayerReserved) {
        this.message = message;
        this.currentPlayerReserved = currentPlayerReserved;
    }

    /**
     * Getter for the message carried by this {@code Event}
     * @return the {@param message}
     */
    public String getMessage() {
        return message;
    }

    /**
     * {@inheritDoc}
     * @return the message concatenated with the {@code String} "CAN'T REPLY" if the message is reserved
     */
    @Override
    public String toString() {
        if(currentPlayerReserved) {
            return "MessageEvent: " + message + "; CAN'T REPLY";
        }
        else {
            return "MessageEvent: " + message;
        }
    }

}
