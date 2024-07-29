package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.events.ChoiceEvent;
import it.polimi.ingsw.network.events.HighlightEvent;
import it.polimi.ingsw.network.events.MessageEvent;
import it.polimi.ingsw.network.events.TurnPhaseEvent;

import java.util.List;

/**
 * {@link EGodPower#DEMETER}
 */
public class DemeterCard extends Card {
    private Tile builtTile;

    public DemeterCard(Player owner, Game game) {
        super(owner, game);
        godPower = EGodPower.DEMETER;
    }

    /**
     * {@inheritDoc}
     * @param builtTile The {@code Tile} where the worker will build
     */
    @Override
    public void build(Tile builtTile){
        if (tilesToBuild.contains(builtTile)) {
            this.builtTile = builtTile;
        }
        super.build(builtTile);
    }

    /**
     * {@inheritDoc}
     * @param isUsed Indicates if the godpower will be used this turn.
     */
    @Override
    public void useGodPower(boolean isUsed) {
        if(isUsed) {
            super.useGodPower(true);
        } else {
            this.usedGodPower = false;
            setTurnPhase();
            game.changeTurn();
        }
    }

    /**
     * Demeter can't build two times on the same tile.
     * {@inheritDoc}
     *
     * @param tile The tile where the worker is currently placed
     * @return
     */
    @Override
    public List<Tile> getTilesToBuild(Tile tile){
        List<Tile> result = super.getTilesToBuild(tile);
        if (usedGodPower) {
            result.remove(builtTile);
        }
        return result;
    }

    /**
     * This method overrides Card#setTurnPhase
     * {@inheritDoc}
     *                                                 -> BUILD -> end      if the player want to use Demeter's godpower
     *                                    -> GODPOWER -
     *                                   |             -> end               if the player doesn't want to use Demeter's godpower
     * WORKERSELECTION -> MOVE -> BUILD -
     *                                    -> end                            if the player can't use Demeter's godpower
     *
     */
    @Override
    protected void setTurnPhase(){
        if (game.getTurnPhase() == ETurnPhase.BUILD) {
            if (usedGodPower) {
                // Reset flag
                usedGodPower = false;
                game.setTurnPhase();
                game.changeTurn();
            }
            else {
                // Simula utilizzo god power
                usedGodPower = true;
                tilesToBuild = decorator.getTilesToBuild(selectedWorker.getTile());
                usedGodPower = false;

                if (tilesToBuild.isEmpty()) {
                    game.setTurnPhase();
                    game.changeTurn();
                }
                else {
                    game.setTurnPhase(ETurnPhase.GODPOWER);
                    setChanged();
                    notify(new HighlightEvent(selectedWorker.getPosition()));
                    setChanged();
                    notify(new ChoiceEvent("Do you want to build again?", "Yes", "No"));
                    setChanged();
                    notify(new TurnPhaseEvent(game.getTurnPhase()));
                }
            }
        } else if (game.getTurnPhase() == ETurnPhase.GODPOWER) {
            if (usedGodPower) {
                game.setTurnPhase(ETurnPhase.BUILD);

                setChanged();
                notify(new MessageEvent(owner.getNickname() + " decided to use his/her god power and will build again"));
            }
            else {
                game.setTurnPhase(ETurnPhase.WORKERSELECTION);

                setChanged();
                notify(new MessageEvent(owner.getNickname() + " decided NOT to use his/her god power"));
            }
        }
        else {
            game.setTurnPhase();
        }
    }

}
