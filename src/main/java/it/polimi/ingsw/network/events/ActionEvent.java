package it.polimi.ingsw.network.events;

import it.polimi.ingsw.model.Position;

/**
 * This {@code Event} is used by the client to send to the server a {@code Position} on a board, on which it wants
 * to perform some operation: select a worker, move, build
 */
public class ActionEvent extends Event {
    private final Position clicked;

    /**
     * Creates a new {@code ActionEvent}, setting the {@code Position} property
     * @param clicked a position object
     */
    public ActionEvent(Position clicked){
        this.clicked = clicked;
    }


    /**
     * Getter for the {@code Position} associated with this event
     * @return the {@param clicked}
     */
    public Position getClicked(){
        return this.clicked;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "ActionEvent: " + clicked.toString();
    }
}
