package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.EGodPower;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Tile;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link EGodPower#MINOTAUR}
 */
public class MinotaurCard extends Card {

    public MinotaurCard(Player owner, Game game) {
        super(owner, game);
        godPower = EGodPower.MINOTAUR;
    }

    /**
     * This method overrides Card#move
     * The movement mechanism of Minotaur is identical to the general case.
     * This method check if the selected destination contains a worker; if so Minotaur is charging him, therefore
     *  we also move that worker to the "straight backwards tile" relative to its current position
     * {@inheritDoc}
     *
     * @param destinationTile The tile where the worker is going to be moved to
     */
    @Override
    public void move(Tile destinationTile) {
        if(tilesToMove.contains(destinationTile) && destinationTile.hasWorker()) {
            Tile backwardsTile = game.getBoard().getStraightBackwardsTile(selectedWorker.getTile().getPosition(), destinationTile.getPosition());

            destinationTile.getWorker().moveWorker(backwardsTile);
        }
        super.move(destinationTile);
    }

    /**
     * This method overrides Card#getTilesToMove
     * This method generates all possible moves for Minotaur. He can move in the standard way but also in tiles
     *  occupied by an opponents worker he can charge. Those are determined by checking if:
     *  <ul>
     *      <li>the backwards tile exists</li>
     *      <li>the backwards tile is not occupied by a dome or another worker</li>
     *  </ul>
     * {@inheritDoc}
     *
     * @param startingTile The tile where the worker is currently placed
     * @return All possible moves also considering Minotaur's godpower
     */
    @Override
    public List<Tile> getTilesToMove(Tile startingTile) {
        List<Tile> result = game.getBoard().getSurroundingTiles(startingTile.getPosition());

        // Temporary list to avoid ConcurrentModificatonException and messy index logic on remove
        List<Tile> tilesToRemove = new ArrayList<>();
        for(Tile t : result){
            Tile backwardsTile = game.getBoard().getStraightBackwardsTile(startingTile.getPosition(), t.getPosition());

            //Tiles with a dome, occupied by a not-chargeable worker or too high have to be removed
            if(Tile.heightDifference(t, startingTile) > 1
                    || t.isDomed()
                    || (t.hasWorker() && t.getWorker().getOwner() == owner)
                    || (t.hasWorker() && backwardsTile == null)
                    || (t.hasWorker() && backwardsTile.getWorker() != null)
                    || (t.hasWorker() && backwardsTile.isDomed()) ){
                tilesToRemove.add(t);
            }
        }
        result.removeAll(tilesToRemove);

        return result;
    }

}
