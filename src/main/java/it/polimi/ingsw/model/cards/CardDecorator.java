package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Playable;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.model.Worker;

import java.util.List;

/**
 * This class is the model of a {@code Card} decorator. We does not modify any behaviour of wrappee.
 */
public abstract class CardDecorator implements Playable {
    protected Playable wrappee;

    public CardDecorator(Card component) {
        this.wrappee = attach(component);
    }

    /**
     * Attach this decorator instance to a {@code Card}.
     * This means wrapping {@code Card#decorator}, which could be another {@code CardDecorator}.
     * Substantially we are performing an addition to a circular linked list.
     *
     * @param component A {@code Card}
     * @return The {@code Playable} that will be wrapped by this instance
     */
    public Playable attach(Card component){
        Playable playable = component.getPlayable();
        component.setPlayable(this);

        return playable;
    }

    /**
     * Detach this decorator instance from a {@code Card}.
     * Substantially we are performing an subtraction to a circular linked list.
     */
    public void detach(){
        Playable decorator = this.wrappee.getPlayable();

        while(decorator.getPlayable() != this){
            decorator = decorator.getPlayable();
        }
        decorator.setPlayable(this.wrappee);
    }

    /**
     * {@inheritDoc}
     * @param worker The worker indicated for selection
     */
    @Override
    public void selectWorker(Worker worker) {
        wrappee.selectWorker(worker);
    }

    /**
     * {@inheritDoc}
     * @param destinationTile  The {@code Tile} where the worker is going to be moved to
     */
    @Override
    public void move(Tile destinationTile) {
        wrappee.move(destinationTile);
    }

    /**
     * Base decorator does not modify any behaviour of its wrappee.
     * {@inheritDoc}
     *
     * @param builtTile The {@code Tile} where the worker will build
     */
    @Override
    public void build(Tile builtTile) {
        wrappee.build(builtTile);
    }

    /**
     * {@inheritDoc}
     * @param isUsed Indicates if the godpower will be used this turn.
     */
    @Override
    public void useGodPower(boolean isUsed) {
        wrappee.useGodPower(isUsed);
    }

    /**
     * {@inheritDoc}
     * @param tile The tile where the worker is currently placed
     * @return
     */
    @Override
    public List<Tile> getTilesToMove(Tile tile) {
        return wrappee.getTilesToMove(tile);
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public List<Tile> getTilesToMove() {
        return wrappee.getTilesToMove();
    }

    /**
     * {@inheritDoc}
     * @param tile The tile where the worker is currently placed
     * @return
     */
    @Override
    public List<Tile> getTilesToBuild(Tile tile) {
        return wrappee.getTilesToBuild(tile);
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public List<Tile> getTilesToBuild() {
        return wrappee.getTilesToBuild();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void checkWinCondition() {
        wrappee.checkWinCondition();
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public Playable getPlayable() {
        return wrappee;
    }

    /**
     * {@inheritDoc}
     * @param decorator A {@code Playable}
     */
    @Override
    public void setPlayable(Playable decorator) {
        this.wrappee = decorator;
    }

    /**
     * {@inheritDoc}
     * @param canChangeWorkerSelection indicate if it is still possible to change selected worker
     */
    @Override
    public void setCanChangeWorkerSelection(boolean canChangeWorkerSelection) {
        this.wrappee.setCanChangeWorkerSelection(canChangeWorkerSelection);
    }

}
