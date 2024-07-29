package it.polimi.ingsw.network.events;

/**
 * This {@code Event} is used by the server to notify a client of a generic error
 */
public class ErrorEvent extends Event{
    private final String warning;

    /**
     * Creates a new {@code ErrorEvent} object, setting the specified warning message
     * @param warning a string representing a warning message associated with this errorEvent
     */
    public ErrorEvent(String warning) {
        this.warning = warning;
        this.currentPlayerReserved = true;
    }

    /**
     * Creates a new {@code ErrorEvent} object, setting the specified warning message and the value for the
     * currentPlayerReserved flag
     * @param warning a string representing a warning message associated with this errorEvent
     * @param currentPlayerReserved a boolean used to indicate that this event is reserved for the current player
     */
    public ErrorEvent(String warning, boolean currentPlayerReserved) {
        this.warning = warning;
        this.currentPlayerReserved = currentPlayerReserved;
    }

    /**
     * Getter for the warning message carried by this {@code ErrorEvent}
     * @return the {@param warning}
     */
    public String getWarning() {
        return warning;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return  "ErrorEvent: " + warning;
    }
}
