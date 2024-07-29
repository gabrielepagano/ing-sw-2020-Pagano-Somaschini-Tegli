package it.polimi.ingsw.network.events;

/**
 * This {@code Event} is used by the server to notify the clients of a game about the chosen challenger for
 * a game
 */
public class SetChallengerEvent extends Event {
    private String challengerNickname;

    /**
     * Creates a new {@code SetChallengerEvent} passing as input a {@code String}
     * representing the nickname of the chosen challenger for a game
     * @param challengerNickname a string representing the nickname of the chosen challenger for a game
     */
    public SetChallengerEvent(String challengerNickname) {
        this.challengerNickname = challengerNickname;
    }

    /**
     * Getter for the chosen challenger nickname carried by this {@code Event}
     * @return the {@param challengerNickname}
     */
    public String getChallengerNickname() {
        return challengerNickname;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "SetChallengerEvent: " + challengerNickname;
    }
}
