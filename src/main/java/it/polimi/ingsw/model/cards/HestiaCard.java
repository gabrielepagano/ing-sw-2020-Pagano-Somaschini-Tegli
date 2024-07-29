package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.events.ChoiceEvent;
import it.polimi.ingsw.network.events.HighlightEvent;
import it.polimi.ingsw.network.events.MessageEvent;
import it.polimi.ingsw.network.events.TurnPhaseEvent;

import java.util.List;

/**
 * {@link EGodPower#HESTIA}
 */
public class HestiaCard extends Card {

    public HestiaCard(Player owner, Game game) {
        super(owner, game);
        godPower = EGodPower.HESTIA;
    }


    /**
     * This method overrides Card.getTilesToBuild()
     * Hestia's godpower allows her to build again but not on a perimetral tile.
     * If we are performing the godpower, we simply remove the perimetral tiles returned as buildable by the general method.
     *  This action is actually performed in this.setTurnPhase since we need to check if there are any before taking action.
     * {@inheritDoc}
     *
     * @param tile The tile where the worker is currently placed
     * @return All possible tiles where the worker can build
     */
    @Override
    public List<Tile> getTilesToBuild(Tile tile){
        List<Tile> result = super.getTilesToBuild(tile);
        if (usedGodPower) {
            result.removeIf(Tile::isPerimetralTile);
        }
        return result;
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
     * This method overrides Card#setTurnPhase
     * {@inheritDoc}
     *                                                 -> BUILD -> end      if the player want to use Hestia's godpower
     *                                    -> GODPOWER -
     *                                   |             -> end               if the player doesn't want to use Hestia's godpower
     * WORKERSELECTION -> MOVE -> BUILD -
     *                                    -> end                            if the player can't use Hestia's godpower
     *
     */
    @Override
    protected void setTurnPhase(){
        if (game.getTurnPhase() == ETurnPhase.BUILD) {
            if (usedGodPower) {
                usedGodPower = false;
                game.setTurnPhase();
                game.changeTurn();
            }
            else {
                // Simulate god power usage
                usedGodPower = true;
                tilesToBuild = decorator.getTilesToBuild(selectedWorker.getTile());
                usedGodPower = false;

                if (!(tilesToBuild.isEmpty())) {
                    game.setTurnPhase(ETurnPhase.GODPOWER);
                    setChanged();
                    notify(new HighlightEvent(selectedWorker.getPosition()));
                    setChanged();
                    notify(new ChoiceEvent("Do you want to build again?", "Yes", "No"));
                    setChanged();
                    notify(new TurnPhaseEvent(ETurnPhase.GODPOWER));
                }
                else {
                    game.setTurnPhase();
                    game.changeTurn();
                }
            }
        }
        else if (game.getTurnPhase() == ETurnPhase.GODPOWER) {
            if (usedGodPower) {
                game.setTurnPhase(ETurnPhase.BUILD);

                setChanged();
                notify(new MessageEvent(owner.getNickname() + " decided to use his/her god power and will build again from " + selectedWorker.getPosition().toString()));
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
