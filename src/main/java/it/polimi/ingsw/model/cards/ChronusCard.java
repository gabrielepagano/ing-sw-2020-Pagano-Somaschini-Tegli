package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.EGodPower;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Tile;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link EGodPower#CHRONUS}
 */
public class ChronusCard extends Card {
    private List<ChronusCardDecorator> activeDecorators = new ArrayList<>();

    public ChronusCard(Player owner, Game game) {
        super(owner, game);
        godPower = EGodPower.CHRONUS;
    }

    /**
     * This method overrides Card#setup
     * Since Chronus godpower is always active, we need to perform the decoration of opponents cards before the start of the game.
     * {@inheritDoc}
     */
    @Override
    public void setup() {
        for (Player p : game.getPlayers()) {
            if (p != this.owner) {
                ChronusCardDecorator malus = new ChronusCardDecorator(p.getCard(), this);

                activeDecorators.add(malus);
            }
        }
    }

    /**
     * This method overrides Card#destroy
     * When Chronus owner loses, we have to detach all decorators so that his win condition doesn't get check anymore.
     * {@inheritDoc}
     */
    @Override
    public void destroy() {
        for (ChronusCardDecorator malus : activeDecorators) {
            malus.detach();
        }
        activeDecorators.clear();
    }


    /**
     * This method overrides Card#build
     * Its more efficient to override this method rather than attach to Chronus his own decorator.
     * {@inheritDoc}
     *
     * @param builtTile The tile where the worker is going to build
     */
    @Override
    public void build(Tile builtTile) {
        super.build(builtTile);
        if(tilesToBuild.contains(builtTile)) {
            checkWinCondition();
        }
    }

    /**
     * This method overrides Card#checkWinCondition
     * Chronus godpower enables him to also win when there are 5 complete towers on the board.
     * This method is called every time build() is executed thanks to ChronusCardDecorator.
     * This is needed since Chronus can also win during opponents turns.
     *
     * {@inheritDoc}
     */
    @Override
    public void checkWinCondition() {
        super.checkWinCondition();
        if(game.getCompleteTowers() >= 5) {
            game.setWinner(owner);
        }
    }

}
