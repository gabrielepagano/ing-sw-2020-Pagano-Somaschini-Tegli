package it.polimi.ingsw.network.events;

import it.polimi.ingsw.model.ETurnPhase;

/**
 * {@code TurnPhaseEvent} is an {@code Event} that is invoked when a new {@code ETurnPhase} has to be set
 */
public class TurnPhaseEvent extends Event {

    private ETurnPhase turnPhase;

    /**
     * Creates a new {@code TurnPhaseEvent} object passing the {@code ETurnPhase} that is to be set as input.
     * @param turnPhase  the {@code ETurnPhase} that has to be set
     */
    public TurnPhaseEvent(ETurnPhase turnPhase) {
        this.turnPhase = turnPhase;
    }

    /**
     * Getter for the {@code ETurnPhase} carried by this {@code Event}
     * @return the {@param ETurnPhase}
     */
    public ETurnPhase getTurnPhase() {
        return turnPhase;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "TurnPhaseEvent: " + turnPhase;
    }
}
