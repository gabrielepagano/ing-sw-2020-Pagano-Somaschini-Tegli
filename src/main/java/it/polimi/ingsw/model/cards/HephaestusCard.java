package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.events.ChoiceEvent;
import it.polimi.ingsw.network.events.HighlightEvent;
import it.polimi.ingsw.network.events.MessageEvent;
import it.polimi.ingsw.network.events.TurnPhaseEvent;

/**
 * {@link EGodPower#HEPHAESTUS}
 */
public class HephaestusCard extends Card {
    private Tile builtTile;

    public HephaestusCard(Player owner, Game game) {
        super(owner, game);
        godPower = EGodPower.HEPHAESTUS;
    }

    /**
     * {@inheritDoc}
     * @param builtTile
     */
    @Override
    public void build(Tile builtTile) {
        if(tilesToBuild.contains(builtTile)) {
            this.builtTile = builtTile;
        }
        super.build(builtTile);
    }

    /**
     * This method overrides Card#useGodPower
     * Hephaestus godpower allows him to build the same tiles twice (but not a dome).
     * If the godpower is used, we build again and start turn-change routine.
     * {@inheritDoc}
     *
     * @param isUsed Current player's choice of using the relative godpower
     */
    @Override
    public void useGodPower(boolean isUsed) {
        if (isUsed) {
            builtTile.buildOneLevel();

            setChanged();
            notify(new MessageEvent(owner.getNickname() + " decided to use his/her god power and built again on " + builtTile.getPosition().toString()));
        } else {
            setChanged();
            notify(new MessageEvent(owner.getNickname() + " decided NOT to use his/her god power"));
        }

        super.useGodPower(isUsed);
        game.changeTurn();
    }

    /**
     * This method overrides Card#setTurnPhase
     * {@inheritDoc}
     *
     *                                    -> GODPOWER -> end    if the player can use Hephaestus's godpower
     * WORKERSELECTION -> MOVE -> BUILD -
     *                                    -> end                if the player can't use Hephaestus's godpower
     *
     */
    @Override
    protected void setTurnPhase() {
        if (game.getTurnPhase() == ETurnPhase.BUILD) {
            if (builtTile.getLevel().getHeight() <= ETileLevel.LEVEL2.getHeight()) {
                game.setTurnPhase(ETurnPhase.GODPOWER);
                setChanged();
                notify(new HighlightEvent(builtTile.getPosition()));
                setChanged();
                notify(new ChoiceEvent("Do you want to build another level in this tile?", "Yes", "No"));
                setChanged();
                notify(new TurnPhaseEvent(ETurnPhase.GODPOWER));
            }
            else {
                game.setTurnPhase();
                game.changeTurn();
            }
        }
        else if (game.getTurnPhase() == ETurnPhase.GODPOWER) {
            game.setTurnPhase(ETurnPhase.WORKERSELECTION);
        }
        else {
            game.setTurnPhase();
        }
    }

}
