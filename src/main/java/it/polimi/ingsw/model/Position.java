package it.polimi.ingsw.model;

import java.io.Serializable;

/**
 * This class represents an immutable position on a 2d 5x5 grid, with 0-indexed rows and columns. Strictly related to {@code Board}.
 */
public final class  Position implements Serializable {

    private int row;
    private int col;

    /**
     * Constructs a new {@code Position} representing the specified couple of coordinates
     *
     * @param row The row index of this {@code Position} on the grid
     * @param col The column index of this {@code Position} on the grid
     */
    public Position(int row, int col) {
        if (row < 0 || row > 4 || col < 0 || col > 4){
            throw new IllegalArgumentException("Invalid index values");
        }
        this.row = row;
        this.col = col;
    }

    /**
     * @return The row index of this {@code Position}
     */
    public int getRow() {
        return row;
    }

    /**
     * @return The column index of this {@code Position}
     */
    public int getCol() {
        return col;
    }

    /**
     * Compares this {@code Position} to the specified object.  The result is {@code
     * true} if and only if the argument is not {@code null} and is a {@code
     * Position} object that represents the same couple of coordinates as this object
     *
     * @param o The object to compare this {@code Position} against
     * @return {@code true} if the given object represents a {@code Position} equivalent
     * to this position, {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if(o == null) {
            return false;
        }
        if(!(o instanceof Position)) {
            return false;
        }
        Position p = (Position)o;
        return this.getRow() == p.getRow() && this.getCol() == p.getCol();
    }

    /**
     * @return A unique number id for each position
     */
    @Override
    public int hashCode() {
        return row * 5 + col;
    }

    @Override
    public String toString() {
        return "(" + row + "," + col + ")";
    }

}
