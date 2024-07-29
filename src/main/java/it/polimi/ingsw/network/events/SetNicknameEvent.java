package it.polimi.ingsw.network.events;

/**
 * This {@code Event} is used by the server as an ack, to notify a client that the nickname that was
 * sent is valid and it is now assigned to it
 */
public class SetNicknameEvent extends Event {
    private String acceptedNickname;

    /**
     * Creates a new {@code SetNicknameEvent} object giving as input a {@code String} that represents the nickname of
     * the user (already accepted and different from the other players' ones).
     * @param acceptedNickname a string with the accepted nickname
     */
    public SetNicknameEvent(String acceptedNickname) {
        if(acceptedNickname == null) {
            throw new IllegalArgumentException("Nickname cannot be null");
        }
        this.acceptedNickname = acceptedNickname;
    }

    /**
     * Getter for the accepted player's nickname carried by this {@code Event}
     * @return the {@param acceptedNickname}
     */
    public String getAcceptedNickname() {
        return acceptedNickname;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "SetNicknameEvent: " + acceptedNickname;
    }
}
