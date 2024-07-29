package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.EGodPower;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Tile;

import java.util.List;

/**
 * {@link EGodPower#APOLLO}
 */
public class ApolloCard extends Card {

    public ApolloCard(Player owner, Game game) {
        super(owner, game);
        godPower = EGodPower.APOLLO;

    }

    /**
     * Apollo can move in tile already occupied by an opponent's worker. When this happen, the workers gets swapped.
     *
     * {@inheritDoc}
     * @param tile The tile where the worker is currently placed
     * @return
     */
    @Override
    public List<Tile> getTilesToMove(Tile tile) {
        List<Tile> result = game.getBoard().getSurroundingTiles(tile.getPosition());

        //Eliminiamo le tile con la cupola o troppo in alto
        result.removeIf(t -> Tile.heightDifference(t, tile) > 1
                || t.isDomed()
                || (t.getWorker() != null && t.getWorker().getOwner() == this.owner));

        return result;
    }

}
