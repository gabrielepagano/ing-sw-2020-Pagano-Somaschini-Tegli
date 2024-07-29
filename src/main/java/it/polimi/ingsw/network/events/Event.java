package it.polimi.ingsw.network.events;

import java.io.Serializable;

/**
 * {@code Event} is the generic interface for events (that is, sophisticated messages exchanged by client and server
 * It implements the {@code Serializable} interface, so that an event can be sent through the network using an object stream
 * <p> It has: </p>
 * <ul>
 *     <li> A signature, a {@code String} that indicates the nickname of the {@code Player} associated
 *     with the connection that this {@code Event} is going through</li>
 *     <li> A currentPlayerReserved flag, a boolean that indicates if an {@code Event} is reserved to
 *     the current player or not. During the "handshaking" between client and server (when the client has
 *     to indicate a nickname and the number of players), the player associated with the connection
 *     that this event is going through is by default the current one</li>
 * </ul>
 */
public abstract class Event implements Serializable {
    private static final long serialVersionUID = 69L;
    protected transient String signature;
    protected boolean currentPlayerReserved = false;

    /**
     * Getter for the signature of this event (that is, the nickname of the {@code Player} associated
     * with the connection that this {@code Event} is going through
     * @return a string representing the nickname of the {@code Player} associated with the connection that
     * this {@code Event} is going through
     */
    public String getSignature() {
        return signature;
    }

    /**
     * Setter for the signature of this event
     * @param nickname a string representing the signature to which set this event
     */
    public void setSignature(String nickname){
        this.signature = nickname;
    }

    /**
     * Getter for the flag currentPlayerReserved of this event
     * @return true if this event is reserved for the current player
     */
    public boolean isReserved() {
        return currentPlayerReserved;
    }

    /**
     * @return A string representation of this {@code Event}, containing the name of the event and some
     * details on the possible extra information that it carries
     */
    @Override
    public abstract String toString();
}
