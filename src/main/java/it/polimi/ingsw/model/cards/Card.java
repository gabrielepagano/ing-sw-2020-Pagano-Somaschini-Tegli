package it.polimi.ingsw.model.cards;


import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.Observable;
import it.polimi.ingsw.network.events.ErrorEvent;
import it.polimi.ingsw.network.events.HighlightEvent;
import it.polimi.ingsw.network.events.MessageEvent;
import it.polimi.ingsw.network.events.TurnPhaseEvent;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.model.Constants.*;


/**
 *
 * This class represents the "base 2-player game". The methods describe the default procedure of completing their tasks (without any godpower).
 * All godcards extends this class and redefine some of the basic behaviours.
 *
 */
public abstract class Card extends Observable implements Playable {

    protected static EGodPower godPower;
    protected Player owner;

    protected Game game;
    protected Playable decorator;

    protected Worker selectedWorker;
    protected List<Tile> tilesToMove;
    protected List<Tile> tilesToBuild;
    protected Tile startingTile;
    protected boolean usedGodPower;
    protected boolean canChangeWorkerSelection;

    public Card(Player owner, Game game) {
        this.owner = owner;
        this.game = game;
        this.decorator = this;
        this.selectedWorker = this.owner.getWorkers()[0];
        this.tilesToMove = new ArrayList<>();
        this.tilesToBuild = new ArrayList<>();
        this.startingTile = selectedWorker.getTile();
        this.usedGodPower = false;
        this.canChangeWorkerSelection = true;

        registerObservers(this.game.getRemoteViews());
    }

    /**
     * This method operates some actions on the environment to allow the card to work properly. It is called during the game
     *  initialization, right after the cards are selected and created.
     * In the general case, a card does not need to setup anything to function. This method is needed to generalize the procedure
     *  and allow some subclasses to define their own routine.
     *
     */
    public void setup() {
        // Base card, without decorator, doesn't do anything in setup
    }

    /**
     * This method operates some actions on the environment to allow the card to be removed without problems. It is called when the card
     *  owner loses.
     * In the general case, a card does not need to do anything to be removed with success. This method is needed to generalize the procedure
     *  and allow some subclasses to define their own routine.
     *
     */
    public void destroy(){
        // Base card, without decorator, doesn't do anything in destroy
    }

    /**
     * {@inheritDoc}
     * @param worker The worker indicated for selection
     */
    @Override
    public void selectWorker(Worker worker) {
        if (worker != null && worker.getOwner() == owner) {
            this.selectedWorker = worker;
            this.startingTile = selectedWorker.getTile();
            setCanChangeWorkerSelection(true);

            setChanged();
            notify(new MessageEvent(owner.getNickname() + " selected worker on " + selectedWorker.getPosition().toString()));

            setTurnPhase();
            render();
        } else {
            setChanged();
            notify(new ErrorEvent(NOT_VALID_WORKER_SELECTION_WARNING));
        }
    }

    /**
     * {@inheritDoc}
     * @param destinationTile  The {@code Tile} where the worker is going to be moved to
     */
    @Override
    public void move(Tile destinationTile) {
        if (tilesToMove.contains(destinationTile)) {
            selectedWorker.moveWorker(destinationTile);

            setChanged();
            notify(new MessageEvent(owner.getNickname() + " moved to " + selectedWorker.getPosition().toString()));

            // Hera can suppress the win condition
            decorator.checkWinCondition();
            setCanChangeWorkerSelection(false);

            if(!(game.isGameOver())) {
                setTurnPhase();
                render();
            }

        } else if (canChangeWorkerSelection() && destinationTile.hasWorker()
                && destinationTile.getWorker().getOwner() == game.getCurrentPlayer()
                && destinationTile.getWorker() != selectedWorker) {

            game.setTurnPhase(ETurnPhase.WORKERSELECTION);
            selectWorker(destinationTile.getWorker());

        } else {
            setChanged();
            notify(new ErrorEvent(NOT_VALID_MOVE_SELECTION_WARNING));
        }
    }

    /**
     * {@inheritDoc}
     * @param builtTile The {@code Tile} where the worker will build
     */
    @Override
    public void build(Tile builtTile) {
        if (tilesToBuild.contains(builtTile)) {
            builtTile.buildOneLevel();

            setChanged();
            notify(new MessageEvent(owner.getNickname() + " built on " + builtTile.getPosition().toString()));

            setCanChangeWorkerSelection(false);
            if (builtTile.getLevel() == ETileLevel.LEVEL3 && builtTile.isDomed()) {
                game.buildCompleteTower();
            }

            setTurnPhase();
        } else if (canChangeWorkerSelection() && builtTile.hasWorker()
                && builtTile.getWorker().getOwner() == game.getCurrentPlayer()
                && builtTile.getWorker() != selectedWorker) {

            game.setTurnPhase(ETurnPhase.WORKERSELECTION);
            selectWorker(builtTile.getWorker());

        } else {
            setChanged();
            notify(new ErrorEvent(NOT_VALID_BUILD_SELECTION_WARNING));
        }
    }

    /**
     * {@inheritDoc}
     * @param isUsed Indicates if the godpower will be used this turn.
     */
    @Override
    public void useGodPower(boolean isUsed) {
        this.usedGodPower = isUsed;

        setTurnPhase();
        render();
    }

    /**
     * {@inheritDoc}
     * @param tile The tile where the worker is currently placed
     * @return All possible tiles where the worker can move
     */
    @Override
    public List<Tile> getTilesToMove(Tile tile) {
        if(tile == null) {
            throw new IllegalArgumentException("Tile cannot be null");
        }
        List<Tile> result = game.getBoard().getSurroundingTiles(tile.getPosition());

        //Eliminiamo le tile con la cupola, occupate da un altro Worker o troppo in alto
        result.removeIf(t -> Tile.heightDifference(t, tile) > 1
                || t.getWorker() != null
                || t.isDomed());

        return result;
    }

    public List<Tile> getTilesToMove() {
        return this.tilesToMove;
    }

    /**
     * {@inheritDoc}
     * @param tile The tile where the worker is currently placed
     * @return All possible tiles where the worker can build
     */
    @Override
    public List<Tile> getTilesToBuild(Tile tile) {
        if(tile == null) {
            throw new IllegalArgumentException("Tile cannot be null");
        }
        List<Tile> result = game.getBoard().getSurroundingTiles(tile.getPosition());

        //Eliminiamo le tile con la cupola o occupate da un altro Worker
        result.removeIf(t -> t.getWorker() != null
                || t.isDomed());

        return result;
    }

    /**
     * {@inheritDoc}
     * @return
     */
    public List<Tile> getTilesToBuild() {
        return this.tilesToBuild;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void checkWinCondition() {
        // startingTile can be null only on the first turn of games with cards like Chronus, which
        // call checkWinCondition() even when they're not playing
        if(startingTile != null) {
            if (startingTile.getLevel() == ETileLevel.LEVEL2 && selectedWorker.getTile().getLevel() == ETileLevel.LEVEL3) {
                game.setWinner(owner);
            }
        }
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public Playable getPlayable(){
        return this.decorator;
    }

    /**
     * {@inheritDoc}
     * @param decorator A {@code Playable}
     */
    @Override
    public void setPlayable(Playable decorator){
        this.decorator = decorator;
    }

    /**
     * Computes and sets the next turn phase. This is called at the end of every phase-specific method.
     */
    protected void setTurnPhase() {
        game.setTurnPhase();
        if (game.getTurnPhase() == ETurnPhase.WORKERSELECTION) {
            game.changeTurn();
        }
    }

    /**
     * {@inheritDoc}
     * @param canChangeWorkerSelection indicate if it is still possible to change selected worker
     */
    public void setCanChangeWorkerSelection(boolean canChangeWorkerSelection) {
        this.canChangeWorkerSelection = canChangeWorkerSelection;
    }

    public boolean canChangeWorkerSelection() {
        return canChangeWorkerSelection;
    }


    /**
     * Computes the set of feasible next moves, depending on the current turn phase.
     * Dynamically calls {@code Card#getTilesToMove} or {@code Card#getTilesToBuild}
      */
    protected void render() {
        if (game.getTurnPhase() == ETurnPhase.MOVE) {
            tilesToMove = decorator.getTilesToMove(selectedWorker.getTile());

            if (!(tilesToMove.isEmpty())) {
                List<Position> positionsToHighlight = new ArrayList<>();
                for (Tile t : tilesToMove) {
                    positionsToHighlight.add(t.getPosition());
                }

                setChanged();
                notify(new HighlightEvent(positionsToHighlight));

                setChanged();
                notify(new TurnPhaseEvent(game.getTurnPhase()));

            }
        }
        else if (game.getTurnPhase() == ETurnPhase.BUILD) {
            tilesToBuild = decorator.getTilesToBuild(selectedWorker.getTile());

            if (!(tilesToBuild.isEmpty())) {
                List<Position> positionsToHighlight = new ArrayList<>();
                for (Tile t : tilesToBuild) {
                    positionsToHighlight.add(t.getPosition());
                }

                setChanged();
                notify(new HighlightEvent(positionsToHighlight));

                setChanged();
                notify(new TurnPhaseEvent(game.getTurnPhase()));

            } else {
                setChanged();
                notify(new ErrorEvent("You cannot build, you lose"));
                game.setLoser(game.getCurrentPlayer());
            }
        }
    }

}

