package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.EGodPower;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Tile;

/**
 * {@link EGodPower#PAN}
 */
public class PanCard extends Card {

    public PanCard(Player owner, Game game) {
        super(owner, game);
        godPower = EGodPower.PAN;
    }

    /**
     * Pan's godpower allows him to win by moving down 2 or more levels.
     * {@inheritDoc}
     */
    @Override
    public void checkWinCondition() {
        super.checkWinCondition();

        if (Tile.heightDifference(selectedWorker.getTile(), startingTile) <= -2) {
            game.setWinner(selectedWorker.getOwner());
        }
    }

}
