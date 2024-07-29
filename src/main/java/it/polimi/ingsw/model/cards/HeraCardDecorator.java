package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Tile;

/**
 * {@link it.polimi.ingsw.model.EGodPower#HERA}
 */
public class HeraCardDecorator extends CardDecorator {
    private Tile destinationTile;

    public HeraCardDecorator(Card wrappee) {
        super(wrappee);
    }

    /**
     * This method overrides CardDecorator#move
     * Wrappee behaviour is not modified; we just need to store destinationTile value for a possible this.checkWinCondition() call
     *
     * @param destinationTile The tile where the worker is going to be moved to
     */
    @Override
    public void move(Tile destinationTile) {
        this.destinationTile = destinationTile;

        wrappee.move(destinationTile);
    }

    /**
     * This method overrides CardDecorator#checkWinCondition
     * Hera godpower denies her opponents to win by moving in a perimetral tile.
     * This method check if this.destinationTile (saved during the previuos this.move() call) is in fact a perimetral tile;
     *  if so, it inhibits the wrappee call to checkWinCondition().
     *
     */
    @Override
    public void checkWinCondition() {
        if(!destinationTile.isPerimetralTile()) {
            wrappee.checkWinCondition();
        }
    }

}
