package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.EGodPower;
import it.polimi.ingsw.model.Tile;

import java.util.List;

/**
 * {@link EGodPower#ATHENA}
 */
public class AthenaCardDecorator extends CardDecorator {

    public AthenaCardDecorator(Card wrappee) {
        super(wrappee);
    }

    /**
     * This method overrides CardDecorator#getTilesToMove()
     * Since Athena godpower is active, we remove all tiles higher than the starting one from the list of possible moves
     *{@inheritDoc}
     *
     * @param tile The tile where the worker is currently placed
     * @return All possible moves considering Athena's malus
     */
    @Override
    public List<Tile> getTilesToMove(Tile tile) {
        List<Tile> tilesToMove = super.getTilesToMove(tile);

        tilesToMove.removeIf(possibleMove -> Tile.heightDifference(possibleMove, tile) > 0);

        return tilesToMove;
    }
}
