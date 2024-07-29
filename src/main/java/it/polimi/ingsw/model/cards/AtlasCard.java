package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.events.*;

import static it.polimi.ingsw.model.Constants.NOT_VALID_BUILD_SELECTION_WARNING;

/**
 * {@link EGodPower#ATLAS}
 */
public class AtlasCard extends Card {

    private Tile builtTile;

    public AtlasCard(Player owner, Game game) {
        super(owner, game);
        godPower = EGodPower.ATLAS;
    }


    /**
     * Atlas doesn't actually build with this method, since we need to know if its godpower gets activated first.
     * {@inheritDoc}
     * @param builtTile The {@code Tile} where the worker will build
     */
    @Override
    public void build(Tile builtTile) {
        if(tilesToBuild.contains(builtTile)) {
            this.builtTile = builtTile;
            if(builtTile.getLevel() == ETileLevel.LEVEL3){
                // We invoke super to increase complete towers count
                super.build(builtTile);
            } else {
                setChanged();
                notify(new MessageEvent(owner.getNickname() + " decided to build on " + builtTile.getPosition().toString()));

                setTurnPhase();
            }
        }
        else {
            setChanged();
            notify(new ErrorEvent(NOT_VALID_BUILD_SELECTION_WARNING));
        }
    }

    /**
     * Atlas redefine this method to complete its building phase.
     * {@inheritDoc}
     * @param isUsed Indicates if the godpower will be used this turn.
     */
    @Override
    public void useGodPower(boolean isUsed) {
        setCanChangeWorkerSelection(false);
        if (isUsed) {
            builtTile.buildDome();

            setChanged();
            notify(new MessageEvent(owner.getNickname() + " decided to use his/her god power and built a dome on " + builtTile.getPosition().toString()));
        }
        else {
            builtTile.buildOneLevel();

            setChanged();
            notify(new MessageEvent(owner.getNickname() + " decided NOT to use his/her god power and built a level on " + builtTile.getPosition().toString()));
        }

        super.useGodPower(isUsed);
        game.changeTurn();
    }

    /**
     * This method overrides Card#setTurnPhase().
     * {@inheritDoc}
     *
     *                                   -> BUILD -> GODPOWER -> end    if the player can use Atlas' godpower
     * WORKERSELECTION -> MOVE -> BUILD -
     *                                   -> BUILD -> end                if the player can't use Atlas' godpower
     *
     */
    @Override
    protected void setTurnPhase() {
        if (game.getTurnPhase() == ETurnPhase.BUILD) {
            if (builtTile.getLevel() == ETileLevel.LEVEL3) {
                game.setTurnPhase();
                game.changeTurn();
            }
            else {
                game.setTurnPhase(ETurnPhase.GODPOWER);
                setChanged();
                notify(new HighlightEvent(builtTile.getPosition()));
                setChanged();
                notify(new ChoiceEvent("Do you want to build a dome in this tile?", "Yes", "No"));
                setChanged();
                notify(new TurnPhaseEvent(game.getTurnPhase()));
            }
        } else if (game.getTurnPhase() == ETurnPhase.GODPOWER) {
            // Avoid render() action in Card#useGodPower()
            game.setTurnPhase(ETurnPhase.WORKERSELECTION);
        }
        else {
            game.setTurnPhase();
        }
    }

}
