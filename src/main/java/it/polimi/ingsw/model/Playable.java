package it.polimi.ingsw.model;

import java.util.List;

/**
 * This interface defines classes that can perform all game-related actions, allowing to play a turn completely.
 * All {@code Card} implement this, and all {@code CardDecorator} too. This allows the decorator to dynamically change
 * cards behaviours when they are attached to them.
 */
public interface Playable {

    /**
     * Select a {@code Worker} between those owned by the current player. This worker will be the one
     * to move and build in the next phases.
     * @param worker The worker indicated for selection
     */
    void selectWorker(Worker worker);

    /**
     * Move the selected {@code Worker} to the specified tile. This tile must be included in the
     * result set of {@code Playable#getTilesToMove}.
     * @param destinationTile  The {@code Tile} where the worker is going to be moved to
     */
    void move(Tile destinationTile);

    /**
     * Build the specified tile one {@code ETileLevel} higher or create a dome. This tile must be included in the
     * result set of {@code Playable#getTilesToBuild}.
     * @param builtTile The {@code Tile} where the worker will build
     */
    void build(Tile builtTile);

    /**
     * Reacts to the player's manifest choice of using/not using this {@code Card} godpower.
     * The specific actions done vary from card to card. Not all cards actually use this method.
     * @param isUsed Indicates if the godpower will be used this turn.
     */
    void useGodPower(boolean isUsed);

    /**
     * Generate all possible moves from a starting {@code Tile} taking into account this {@code Card} rules
     * of movement.
     * @param tile The tile where the worker is currently placed
     * @return All possible tiles where the worker can move
     */
    List<Tile> getTilesToMove(Tile tile);

    /**
     * @return The already generated set of possible moves
     */
    List<Tile> getTilesToMove();

    /**
     * Generate all possible builds from a starting {@code Tile} taking into account this {@code Card} rules
     * of building.
     * @param tile The tile where the worker is currently placed
     * @return All possible tiles where the worker can build
     */
    List<Tile> getTilesToBuild(Tile tile);

    /**
     * @return The already generated set of possible build
     */
    List<Tile> getTilesToBuild();

    /**
     * Check for winning conditions met in this turn. Generally called in {@code Card#move}.
     */
    void checkWinCondition();

    /**
     * @return This {@code Card} decorator or this {@code CardDecorator} wrappee.
     */
    Playable getPlayable();

    /**
     * Set this {@code Card} decorator or this {@code CardDecorator} wrappee.
     * @param decorator A {@code Playable}
     */
    void setPlayable(Playable decorator);

    /**
     * Set if it is still possible to change selected worker.
     * @param canChangeWorkerSelection indicate if it is still possible to change selected worker
     */
    void setCanChangeWorkerSelection(boolean canChangeWorkerSelection);

}
