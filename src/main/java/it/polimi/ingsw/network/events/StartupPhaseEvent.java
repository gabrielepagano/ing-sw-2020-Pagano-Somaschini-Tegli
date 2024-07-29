package it.polimi.ingsw.network.events;

import it.polimi.ingsw.controller.EStartupPhase;

/**
 * {@code StartupPhaseEvent} is an {@code Event} that is invoked when we have to set a new {@code EStartupPhase}.
 */
public class StartupPhaseEvent extends Event {

    private EStartupPhase newStartupPhase;

    /**
     * Creates a new {@code StartupPhaseEvent} object passing as input an {@code EstartupPhase}
     * that represents the phase to set.
     * @param newStartupPhase  the phase to set
     */
    public StartupPhaseEvent(EStartupPhase newStartupPhase) {
        this.newStartupPhase = newStartupPhase;
    }

    /**
     * Getter for the {@code EStartupPhase} carried by this {@code Event}
     * @return the {@param newStartupPhase}
     */
    public EStartupPhase getNewStartupPhase() {
        return newStartupPhase;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "StartupPhaseEvent: " + newStartupPhase;
    }
}
