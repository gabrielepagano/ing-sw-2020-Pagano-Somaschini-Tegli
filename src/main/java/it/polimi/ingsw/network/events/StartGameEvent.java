package it.polimi.ingsw.network.events;

import it.polimi.ingsw.utils.ListUtility;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code StartGameEvent} is an {@code Event} that is invoked when a new game has started, to notify
 * all the connected players that the new game is ready to begin and tell them about the nicknames
 * of their opponents
 */
public class StartGameEvent extends Event {
    private List<String> playerNicknames;

    /**
     * Creates a new {@code StartGameEvent} object giving as input a list of {@code String} which
     * contains all the nicknames of the players that are going to play.
     * @param playerNicknames  the list of all the nicknames of the players that
     *                         are going to play
     */
    public StartGameEvent(List<String> playerNicknames) {
        this.playerNicknames = new ArrayList<>();
        for(String s : playerNicknames) {
            this.playerNicknames.add(s);
        }
    }

    /**
     * Getter for the list of players' nicknames carried by this {@code Event}
     * @return the {@param playerNicknames}
     */
    public List<String> getPlayerNicknames() {
        return this.playerNicknames;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "StartGameEvent: " + ListUtility.listToString(this.playerNicknames);
    }

}
