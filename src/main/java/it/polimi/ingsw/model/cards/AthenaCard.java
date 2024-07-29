package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.EGodPower;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Tile;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link EGodPower#ATHENA}
 */
public class AthenaCard extends Card {
    private List<AthenaCardDecorator> activeDecorators = new ArrayList<>();

    public AthenaCard(Player owner, Game game) {
        super(owner, game);
        godPower = EGodPower.ATHENA;
    }

    /**
     * This method overrides Card#move()
     * Athena moves her worker normally, so the actual worker re-allocation logic is the same as in the general case.
     * Here we check if her godpower activates or deactivates and attach and detach AthenaCardDecorator-s to the other players accordingly.
     * {@inheritDoc}
     *
     * @param destinationTile The tile where the worker is going to be moved to
     */
    @Override
    public void move(Tile destinationTile) {
        super.move(destinationTile);
        if(tilesToMove.contains(destinationTile)) {

            if(Tile.heightDifference(destinationTile, startingTile) > 0 && !usedGodPower) {
                for (Player p : game.getPlayers()) {
                    if (p != this.owner) {
                        AthenaCardDecorator malus = new AthenaCardDecorator(p.getCard());

                        activeDecorators.add(malus);
                        usedGodPower = true;
                    }
                }
            }
            else if (Tile.heightDifference(destinationTile, startingTile) <= 0 && usedGodPower){
                for (AthenaCardDecorator malus : activeDecorators) {
                    malus.detach();
                }
                activeDecorators.clear();
                usedGodPower = false;
            }

        }
    }

    /**
     * This method overrides Card#destroy()
     * When Athena's owner loses, we have to detach all decorators so that her malus isn't applied anymore.
     * {@inheritDoc}
     *
     */
    @Override
    public void destroy() {
        for (AthenaCardDecorator malus : activeDecorators) {
            malus.detach();
        }
        activeDecorators.clear();
    }

}
