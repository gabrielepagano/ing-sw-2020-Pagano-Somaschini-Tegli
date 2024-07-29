package it.polimi.ingsw.model;

/**
 * Represent a phase of the game turn.
 * The cards alternate between these phases in different ways. However {@link #WORKERSELECTION}, {@link #MOVE} and {@link #BUILD} must be always executed.
 * {@link #GODPOWER} is used to achieve specific behaviours in some cards related to theirs godpower
 *
 */
public enum ETurnPhase {

    WORKERSELECTION, MOVE, BUILD, GODPOWER;

    public ETurnPhase nextCircular() {
        switch(this) {
            case WORKERSELECTION:
                return MOVE;
            case MOVE:
                return BUILD;
            case BUILD:
                return WORKERSELECTION;
            default:
                return this;
        }
    }

}
