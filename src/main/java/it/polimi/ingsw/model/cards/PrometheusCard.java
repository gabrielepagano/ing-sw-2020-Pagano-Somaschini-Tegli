package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.events.ChoiceEvent;
import it.polimi.ingsw.network.events.HighlightEvent;
import it.polimi.ingsw.network.events.MessageEvent;
import it.polimi.ingsw.network.events.TurnPhaseEvent;

import java.util.List;

import static it.polimi.ingsw.model.Constants.*;

/**
 * {@link EGodPower#PROMETHEUS}
 */
public class PrometheusCard extends Card {
    Tile notBuild = null;

    public PrometheusCard(Player owner, Game game) {
        super(owner, game);
        godPower = EGodPower.PROMETHEUS;
    }

    /**
     * This method overrides Card#build
     * If Prometheus godpower has been used, we call Card#render so that the player can see all possible moves he can take.
     * {@inheritDoc}
     *
     * @param builtTile
     */
    @Override
    public void build(Tile builtTile) {
        super.build(builtTile);
        if(tilesToBuild.contains(builtTile) && usedGodPower) {
            render();
            usedGodPower = false;
        }
    }

    /**
     * This method overrides Card#getTilesToMove
     * If Prometheus godpower has been used, we remove tiles higher then the current worker level.
     * {@inheritDoc}
     *
     * @param tile The tile where the worker is currently placed
     * @return All possible moves
     */
    @Override
    public List<Tile> getTilesToMove(Tile tile) {
        List<Tile> result = super.getTilesToMove(tile);
        if (usedGodPower) {
            //Eliminiamo le tile più in alto di quella di partenza
            result.removeIf(t -> Tile.heightDifference(t, tile) > 0);
        }
        return result;
    }

    /**
     * This method overrides Card#getTilesToBuild
     * The logic is the same as in Card. We just remove the marked tile to avoid locks.
     * {@inheritDoc}
     *
     * @param tile The tile where the worker is currently placed
     * @return All possible tiles where the worker can build
     */
    @Override
    public List<Tile> getTilesToBuild(Tile tile) {
        List<Tile> result = super.getTilesToBuild(tile);
        if (usedGodPower && notBuild != null) {
            //Removing the only tile suitable for moving to avoid locks
            result.remove(notBuild);
        }
        return result;
    }

    /**
     * This method overrides Card#setTurnPhase
     * {@inheritDoc}
     *
     *                                 -> BUILD -> MOVE -> BUILD -> end     if the player want to use Prometheus's godpower
     *                    -> GODPOWER -
     *                   |             -> MOVE -> BUILD -> end              if the player doesn't want to use Prometheus's godpower
     * WORKERSELECTION -
     *                   -> MOVE -> BUILD -> end                            if the player can't use Prometheus's godpower
     *
     */
    @Override
    protected void setTurnPhase() {
        if (game.getTurnPhase() == ETurnPhase.WORKERSELECTION) {
            if(!canLockSelf()){
                game.setTurnPhase(ETurnPhase.GODPOWER);
                setChanged();
                notify(new HighlightEvent(selectedWorker.getPosition()));
                setChanged();
                notify(new ChoiceEvent("Choose your next action", "Build", "Move"));
                setChanged();
                notify(new TurnPhaseEvent(ETurnPhase.GODPOWER));
            }
            else {
                game.setTurnPhase(ETurnPhase.MOVE);
            }
        }
        else if (game.getTurnPhase() == ETurnPhase.BUILD) {
            if (usedGodPower) {
                game.setTurnPhase(ETurnPhase.MOVE);
            }
            else {
                game.setTurnPhase();
                game.changeTurn();
            }
        }
        else if (game.getTurnPhase() == ETurnPhase.GODPOWER) {
            if (usedGodPower) {
                game.setTurnPhase(ETurnPhase.BUILD);

                setChanged();
                notify(new MessageEvent(owner.getNickname() + " decided to use his/her god power and will build from " + selectedWorker.getPosition().toString() + " before moving"));
            }
            else {
                notBuild = null;

                game.setTurnPhase(ETurnPhase.MOVE);

                setChanged();
                notify(new MessageEvent(owner.getNickname() + " decided NOT to use his/her god power"));
            }
        }
        else {
            game.setTurnPhase();
        }
    }

    /**
     * In some corner cases, building before moving could cause the worker to lock himself.
     * We want to foresee this and suppress Prometheus godpower or modify the list of buildable tiles.
     *
     * For such a situation to occur, all possible moves must either be above the worker or unreachable, with the exception of at most one tile being at the same level.
     * The conditions checked here follows inverse logic.
     *
     * @return Is possible to activate Prometheus godpower
     */
    private boolean canLockSelf(){
        List<Tile> possibleMoves = decorator.getTilesToMove(selectedWorker.getTile());
        boolean sameHeight = false;

        for(Tile t: possibleMoves){
            int hrel = Tile.heightDifference(t, selectedWorker.getTile());

            if (hrel < 0) {
                this.notBuild = null;

                return false;
            }
            else if (hrel == 0) {
                if (sameHeight) {
                    this.notBuild = null;

                    return false;
                }
                else {
                    this.notBuild = t;

                    sameHeight = true;
                }
            }
        }

        if (sameHeight) {
            if (decorator.getTilesToBuild(selectedWorker.getTile()).size() > 1){
                return false;
            }
            //if the "notBuild" tile is the only buildable tile, the godpower can't target any tile
            else {
                this.notBuild = null;

                return true;
            }
        }

        return true;
    }

}
