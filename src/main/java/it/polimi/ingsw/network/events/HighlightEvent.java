package it.polimi.ingsw.network.events;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.utils.ListUtility;

import java.util.ArrayList;
import java.util.List;

/**
 * This {@code Event} contains a list of {@code Position}s that have to be highlighted client-side, to inform
 * a player about his possible next moves. An {@code HighlightEvent} is inherently reserved for the current player
 */
public class HighlightEvent extends Event{
    private final List<Position> positionsToHighlight;

    /**
     * Creates a new {@code HighlightEvent} object giving as input a list of {@code Position} to highlight.
     * @param positionsToHighlight  the list of positions to highlight
     */
    public HighlightEvent(List<Position> positionsToHighlight){
        this.currentPlayerReserved = true;
        this.positionsToHighlight = positionsToHighlight;
    }

    /**
     * Creates a new {@code HighlightEvent} with a single {@code Position} to highlight
     * @param toHighlight the position to highlight
     */
    public HighlightEvent(Position toHighlight){
        this.currentPlayerReserved = true;
        this.positionsToHighlight = new ArrayList<>();
        positionsToHighlight.add(toHighlight);
    }

    /**
     * Creates a new {@code HighlightEvent} with 2 {@code Position}s to highlight
     * @param toHighlight1 the first position to highlight
     * @param toHighlight2 the second position to highlight
     */
    public HighlightEvent(Position toHighlight1, Position toHighlight2){
        this.currentPlayerReserved = true;
        this.positionsToHighlight = new ArrayList<>();
        positionsToHighlight.add(toHighlight1);
        positionsToHighlight.add(toHighlight2);
    }


    /**
     * Getter for the list of {@code Position}s to highligh carried by this {@code Event}
     * @return the {@param positionsToHighlight}
     */
    public List<Position> getPositionsToHighlight() {
        return positionsToHighlight;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "HighlightEvent: " + ListUtility.listToString(positionsToHighlight);
    }
}
