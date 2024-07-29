package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.EGodPower;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link EGodPower#HERA}
 */
public class HeraCard extends Card {
    private List<HeraCardDecorator> activeDecorators = new ArrayList<>();

    public HeraCard(Player owner, Game game) {
        super(owner, game);
        godPower = EGodPower.HERA;
    }

    /**
     * This method overrides Card#setup
     * Since Hera's godpower is always active, we need to perform the decoration of opponents cards before the start of the game.
     * {@inheritDoc}
     */
    @Override
    public void setup() {
        for (Player p : game.getPlayers()) {
            if (p != this.owner) {
                HeraCardDecorator malus = new HeraCardDecorator(p.getCard());

                activeDecorators.add(malus);
            }
        }
    }

    /**
     * This method overrides Card#destroy
     * When Hera's owner loses, we have to detach all decorators so that her malus isn't applied anymore.
     * {@inheritDoc}
     */
    @Override
    public void destroy() {
        for (HeraCardDecorator malus : activeDecorators) {
            malus.detach();
        }
        activeDecorators.clear();
    }

}
