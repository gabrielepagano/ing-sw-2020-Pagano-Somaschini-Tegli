package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.events.ChoiceEvent;
import it.polimi.ingsw.network.events.HighlightEvent;
import it.polimi.ingsw.network.events.MessageEvent;
import it.polimi.ingsw.network.events.TurnPhaseEvent;
import java.util.List;

/**
 * {@link EGodPower#ARTEMIS}
 */
public class ArtemisCard extends Card {

    private Tile workerSelectionTile;

    public ArtemisCard(Player owner, Game game){
        super(owner, game);
        godPower = EGodPower.ARTEMIS;
        workerSelectionTile = selectedWorker.getTile();
    }

    /**
     * {@inheritDoc}
     * @param worker The worker indicated for selection
     */
    @Override
    public void selectWorker(Worker worker) {
        if(worker != null && worker.getOwner() == owner) {
            workerSelectionTile = worker.getTile();
        }
        super.selectWorker(worker);
    }

    /**
     * If Artemis is moving for the second time, she can't move back to her original position.
     *
     * {@inheritDoc}
     * @param tile The tile where the worker is currently placed
     * @return
     */
    @Override
    public List<Tile> getTilesToMove(Tile tile){
        List<Tile> result = super.getTilesToMove(tile);
        if(usedGodPower) {
            result.remove(this.workerSelectionTile);
        }
        return result;
    }

    /**
     * This method overrides Card#setTurnPhase().
     * {@inheritDoc}
     *
     *                          -> MOVE -> BUILD -> end     if the player want to use Artemis' godpower
     * WORKERSELECTION -> MOVE -
     *                          -> BUILD -> end             if the player doesnt want to use Artemis' godpower
     *
     */
    @Override
    protected void setTurnPhase() {
        switch (game.getTurnPhase()) {
            // At the end of Card#move()
            case MOVE:
                if (!usedGodPower) {
                    // Simulate god power usage
                    usedGodPower = true;
                    tilesToMove = decorator.getTilesToMove(selectedWorker.getTile());
                    usedGodPower = false;
                    if(!(tilesToMove.isEmpty())) {

                        game.setTurnPhase(ETurnPhase.GODPOWER);

                        setChanged();
                        notify(new HighlightEvent(selectedWorker.getPosition()));

                        setChanged();
                        notify(new ChoiceEvent("Choose your next action", "Move", "Build"));

                        setChanged();
                        notify(new TurnPhaseEvent(game.getTurnPhase()));

                    } else {
                        game.setTurnPhase();

                        // Reset not necessary, usedGodPower is already null
                    }
                } else {
                    game.setTurnPhase();

                    // Reset for next turn
                    usedGodPower = false;
                }
                break;
            case GODPOWER:
                if (usedGodPower) {
                    game.setTurnPhase(ETurnPhase.MOVE);
                    startingTile = selectedWorker.getTile();

                    setChanged();
                    notify(new MessageEvent(owner.getNickname() + " decided to use his/her god power and will move again from " + selectedWorker.getPosition().toString()));
                } else {
                    game.setTurnPhase(ETurnPhase.BUILD);

                    setChanged();
                    notify(new MessageEvent(owner.getNickname() + " decided NOT to use his/her god power"));
                }
                break;
            default:
                super.setTurnPhase();
                break;
        }
    }

}