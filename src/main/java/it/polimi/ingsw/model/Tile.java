package it.polimi.ingsw.model;

import it.polimi.ingsw.network.events.BoardEvent;
import it.polimi.ingsw.network.Observable;
import java.io.Serializable;

/**
 * This class represents a cell of a {@code Board}. The position (row, col) of a {@code Tile} on the board is immutable.
 *
 * A {@code Tile} should belong to one and only one {@code Board}, but does not know which {@code Board}.
 * A {@code Tile} may have one {@code Worker} standing on itself (no more than one) and has a building, whose height is represented by a {@link ETileLevel} value.
 * A dome may be built on the {@code Tile}. The presence of a dome prevents any further building on the {@code Tile}.
 */
public class Tile extends Observable implements Serializable {

    private int row;
    private int column;
    private Worker worker;
    private boolean domed;
    private ETileLevel level;

    /**
     * Initializes a newly created {@code Tile}, so that it is at the specified row and column, has no {@code Worker}
     * and no building or dome built on itself
     *
     * @param row The row index on which to place this {@code Tile}
     * @param col The column index on which to place this {@code Tile}
     */
    public Tile(int row, int col) {

        if (row > 4 || row < 0 || col < 0 || col > 4) {
            throw new IllegalArgumentException("Invalid index values");
        }
        else {
            this.row = row;
            this.column = col;
            worker = null;
            domed = false;
            level = ETileLevel.GROUND;
        }

    }

    /**
     * Initializes a newly created {@code Tile}, so that it is at the specified {@code Position}, has no {@code Worker}
     * and no building or dome built on itself
     *
     * @param position The position on the {@code Board} on which to place this {@code Tile}
     */
    public Tile(Position position) {

        if(position == null) {
            throw new IllegalArgumentException("Position cannot be null");
        }

        this.row = position.getRow();
        this.column = position.getCol();
        worker = null;
        domed = false;
        level = ETileLevel.GROUND;

    }

    /**
     * @return The {@code Worker} that's on this {@code Tile}, {@code null} if no {@code Worker} occupies this {@code Tile}
     */
    public Worker getWorker() {
        return this.worker;
    }

    /**
     * @return The row index of this {@code Tile} on the {@code Board}
     */
    public int getRow() {
        return this.row;
    }

    /**
     * @return The column index of this {@code Tile} on the {@code Board}
     */
    public int getColumn() {
        return this.column;
    }

    /**
     * @param worker The worker that moves onto this {@code Tile}
     */
    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    /**
     * @return {@code true} if a dome has been built on this {@code Tile}, {@code false} otherwise
     */
    public boolean isDomed() {
        return this.domed;
    }

    /**
     * @return The {@link ETileLevel} value representing the height of the building on this {@code Tile}
     */
    public ETileLevel getLevel() {
        return this.level;
    }

    /**
     * @param level The new {@link ETileLevel} value of the building on this {@code Tile}
     */
    public void setLevel(ETileLevel level) {
        this.level = level;
    }

    /**
     * @return The position of this {@code Tile} on the {@code Board}
     */
    public Position getPosition() {
        return new Position(row, column);
    }

    /**
     * Builds one building level on this {@code Tile}.
     *
     * If the building level is already {@link ETileLevel#LEVEL3} and there is no dome, a dome is built.
     * No action is performed if a dome is already built on this {@code Tile}, no matter what the building level is
     */
    public void buildOneLevel() {
        if(isDomed()) {
            return;
        }

        ETileLevel currentLevel = getLevel();

        if(currentLevel == ETileLevel.LEVEL3) {
            buildDome();
        }
        else {
            setLevel(currentLevel.getNextValue());

            setChanged();
            notify(new BoardEvent((Tile) this.clone()));
        }
    }

    /**
     * Builds a dome on this {@code Tile}
     */
    public void buildDome() {
        if(isDomed()) {
            return;
        }
        this.domed = true;
        setChanged();
        notify(new BoardEvent((Tile) this.clone()));
    }

    public void setDomed(boolean b) {
        this.domed = b;
    }

    /**
     * @return Indicates if this tile is on the perimeter of the {@code Board}
     */
    public boolean isPerimetralTile() {
        return (row == 0 || row == 4 || column == 0 || column == 4);
    }

    /**
     * @return Indicates if this tile is a corner of the {@code Board}
     */
    public boolean isCornerTile() {
        return (   row == 0 && column == 0
                || row == 0 && column == 4
                || row == 4 && column == 0
                || row == 4 && column == 4);
    }

    /**
     * Computes the height difference between the buildings on the specified tiles
     * @param t1 The first {@code Tile} to compare
     * @param t2 The second {@code Tile} to compare
     * @return The height difference between the buildings on the given tiles
     */
    public static int heightDifference(Tile t1, Tile t2) {
        if(t1 == null) {
            throw new IllegalArgumentException("First tile to compare is null");
        }
        if(t2 == null) {
            throw new IllegalArgumentException("Second tile to compare is null");
        }
        return t1.getLevel().getHeight() - t2.getLevel().getHeight();
    }

    public boolean hasWorker() {
        return this.worker != null;
    }

    @Override
    public Object clone(){
        Tile clone = new Tile(this.row, this.column);
        clone.setWorker(this.worker);
        clone.setDomed(isDomed());
        clone.setLevel(this.level);

        return clone;
    }

    @Override
    public String toString() {
        return this.getPosition().toString();
    }
}
