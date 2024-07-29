package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.*;

import java.util.List;

public class ZeusCard extends Card {

    public ZeusCard(Player owner, Game game) {
        super(owner, game);
        godPower = EGodPower.ZEUS;
    }

    /**
     * This method overrides Card#getTilesToBuild
     * Zeus godpower allows his workers to build in the tile where they are standing.
     * {@inheritDoc}
     *
     * @param tile The tile where the worker is currently placed
     * @return All possible tiles where the worker can build
     */
    @Override
    public List<Tile> getTilesToBuild(Tile tile) {
        List<Tile> result = super.getTilesToBuild(tile);
        if (tile.getLevel() != ETileLevel.LEVEL3) {
            result.add(tile);
        }

        return result;
    }
}
