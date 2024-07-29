package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Tile;

/**
 * {@link it.polimi.ingsw.model.EGodPower#CHRONUS}
 */
public class ChronusCardDecorator extends CardDecorator {
    private ChronusCard concreteChronus;

    public ChronusCardDecorator(Card component, ChronusCard concreteChronus) {
        super(component);

        this.concreteChronus = concreteChronus;
    }

    /**
     * This method overrides CardDecorator#build
     * After the wrappee has completed its build phase, we call ChronusCard#checkWinCondition to verify it
     *  on the new board state.
     *
     * {@inheritDoc}
     * @param buildTile The built tile
     */
    @Override
    public void build(Tile buildTile) {
        wrappee.build(buildTile);
        concreteChronus.checkWinCondition();
    }

}
