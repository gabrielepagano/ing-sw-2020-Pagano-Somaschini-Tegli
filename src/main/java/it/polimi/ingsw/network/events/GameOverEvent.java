package it.polimi.ingsw.network.events;

/**
 * This {@code Event} is used by the server to notify all the clients still connected to a game that
 * a game over has been reached. A gameOverEvent should close all active connections on his way from server
 * to client
 */
public class GameOverEvent extends Event {

    /**
     {@inheritDoc}
     */
    @Override
    public String toString() {
        return "GameOverEvent";
    }
}
