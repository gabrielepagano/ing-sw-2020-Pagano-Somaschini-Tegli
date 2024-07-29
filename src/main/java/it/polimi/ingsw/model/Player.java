package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.view.RemoteView;

/**
 * {@code Player} represents a player of the game.
 */
public class Player {

    private Worker[] workers;
    private Card card;
    private RemoteView remoteView;


    /**
     * Makes a {@code Player} object giving as input a {@code RemoteView} that represents
     * the server-side view component associated to this player.
     * @param remoteView  the server-side view component associated to this player
     */
    public Player(RemoteView remoteView) {
        if(remoteView == null) {
            throw new IllegalArgumentException("RemoteView cannot be null");
        }
        this.remoteView = remoteView;
        this.workers = new Worker[2];
        workers[0] = new Worker(this, 0);
        workers[1] = new Worker(this, 1);
    }

    public Worker[] getWorkers() {
        return workers;
    }

    public Card getCard() {
        return this.card;
    }

    public RemoteView getRemoteView() {
        return this.remoteView;
    }

    /**
     * The {@code Player} has a nickname, a {@code String} that represents his unique username.
     * @return the unique nickname of the player
     */
    public String getNickname() {
        return remoteView.getNickname();
    }

    public void setCard(Card card) {
        this.card = card;
    }

}
