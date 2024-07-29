package it.polimi.ingsw.model;

import it.polimi.ingsw.network.Observer;
import it.polimi.ingsw.view.RemoteView;

import java.util.*;

import static it.polimi.ingsw.model.Constants.COLUMNS;
import static it.polimi.ingsw.model.Constants.ROWS;

/**
 * This class is a wrapper for a grid of Tiles (that is, a 2d array of Tiles) with some useful methods.
 */
public class Board {

    private Tile[][] tilesGrid;

    /**
     * Initializes a newly created {@code Board} so that it is empty
     */
    public Board() {
        tilesGrid = new Tile[ROWS][COLUMNS];
        for(int r = 0; r < ROWS; r++) {
            for(int c = 0; c < COLUMNS; c++) {
                tilesGrid[r][c] = new Tile(r, c);
            }
        }
    }

    /**
     * Initializes a newly created {@code Board} so that it it is empty
     * and registers the specified remoteViews as observers
     */
    public Board(List<RemoteView> remoteViews) {
        this();
        registerObserversOnTiles(remoteViews);
    }


    public void registerObserversOnTiles(List<? extends Observer> observersList) {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLUMNS; c++) {
                tilesGrid[r][c].registerObservers(observersList);
            }
        }
    }

    /**
     * Returns the {@code Tile} specified by the coordinates
     *
     * @param row The row index of the {@code Tile} to return
     * @param col The column index of the {@code Tile} to return
     * @return The {@code Tile} in position (row,col) on this {@code Board}, {@code null} if an invalid (i.e., out-of-bounds) row or column index is provided
     */
    public Tile getTile(int row, int col) {
        if(row < 0 || row >= ROWS) {
            throw new IllegalArgumentException("Invalid row index");
        }
        if(col < 0 || col >= COLUMNS) {
            throw new IllegalArgumentException("Invalid column index");
        }
        return tilesGrid[row][col];
    }

    /**
     * Returns the {@code Tile} specified by the coordinates
     *
     * @param pos The position on this {@code Board} of the {@code Tile} to return
     * @return The {@code Tile} at the specified {@code Position} on this {@code Board}, {@code null} if an invalid (i.e., out-of-bounds) or {@code null} position is provided
     */
    public Tile getTile(Position pos) {
        if(pos == null) {
            throw new IllegalArgumentException("The position cannot be null");
        }
        return getTile(pos.getRow(), pos.getCol());
    }

    /**
     * Substitute a tile of this board
     *
     * @param t The substitute {@code Tile}
     */
    public void setTile(Tile t) {
        if (t != null) {
            int row = t.getRow();
            int col = t.getColumn();
            tilesGrid[row][col] = t;
        }
    }

    /**
     * Gets a list of all the Tiles on this {@code Board} that surround the {@code Tile} at the specified row and column index. The list returned can contain up to 8 Tiles
     *
     * @param row The row index of the {@code Tile} whose surrounding Tiles have to be returned
     * @param col The column index of the {@code Tile} whose surrounding Tiles have to be returned
     * @return The list of Tiles surrounding the {@code Tile} at the specified row and column index on the {@code Board}, {@code null} if an invalid (i.e., out-of-bounds) row or column index is provided
     */
    public List<Tile> getSurroundingTiles(int row, int col) {

        List<Tile> result = new ArrayList<>();

        if(row >= 0 && row < ROWS && col >= 0 && col < COLUMNS) {

            for(int r = row - 1; r <= row + 1; r++) {
                for(int c = col - 1; c <= col + 1; c++) {
                    if(r >= 0 && r < ROWS && c >= 0 && c < COLUMNS && (r != row || c != col)) {
                        result.add(tilesGrid[r][c]);
                    }
                }
            }
            
        }

        return result;
    }

    /**
     * Gets a list of all the Tiles on this {@code Board} that surround the {@code Tile} at the specified {@code Position}. The list returned can contain up to 8 Tiles
     *
     * @param pos The position on this {@code Board} of the {@code Tile} whose surrounding Tiles have to be returned
     * @return The list of Tiles surrounding the {@code Tile} at the specified position on the {@code Board}, {@code null} if an invalid (i.e., out-of-bounds) or {@code null} position is provided
     */
    public List<Tile> getSurroundingTiles(Position pos) {
        if(pos == null) {
            return new ArrayList<>();
        }
        return getSurroundingTiles(pos.getRow(), pos.getCol());
    }

    /**
     * Gets the {@code Tile} on this {@code Board} that is immediately behind the specified target tile and opposite to the specified charger tile.
     * This method is particularly useful for managing the Minotaur GodPower
     *
     * @param chargerRow The row index of the charger tile
     * @param chargerCol The column index of the charger tile
     * @param targetRow The row index of the target tile
     * @param targetCol The column index of the target tile
     * @return  The {@code Tile} on this {@code Board} that is immediately behind the specified target tile and opposite to the specified charger tile,
     *          {@code null} if an invalid (i.e., out-of-bounds) row or column index is provided or if the specified charger and target tiles
     *          are not adjacent
     */
    public Tile getStraightBackwardsTile(int chargerRow, int chargerCol, int targetRow, int targetCol) {

        int dr;
        int dc;
        int resultRow;
        int resultCol;

        dr = targetRow - chargerRow;
        dc = targetCol - chargerCol;

        // Apparently, this is the style suggested in the Oracle/Sun guidelines. Like, zoinks scoob!
        if(chargerRow < 0 || chargerRow >= ROWS || chargerCol < 0 || chargerCol >= COLUMNS
                || targetRow < 0 || targetRow >= ROWS || targetCol < 0 || targetCol >= COLUMNS) {
            return null;
        }

        // target e charger non devono nè coincidere, nè essere troppo distanti
        if (dr > 1 || dr < -1 || dc > 1 || dc < -1 || (dr == 0 && dc == 0)) {
            return null;
        }

        resultRow = targetRow + dr;
        resultCol = targetCol + dc;

        if (resultRow < 0 || resultRow >= ROWS || resultCol < 0 || resultCol >= COLUMNS) {
            return null;
        }

        return tilesGrid[resultRow][resultCol];

    }

    /**
     * Gets the {@code Tile} on this {@code Board} that is immediately behind the specified target tile and opposite to the specified charger tile.
     * This method is particularly useful for managing the Minotaur GodPower
     *
     * @param chargerPos The position on this {@code Board} of the charger tile
     * @param targetPos The position on this {@code Board} of the target tile
     * @return  The {@code Tile} on this {@code Board} that is immediately behind the specified target tile and opposite to the specified charger tile,
     *          {@code null} if an invalid (i.e., out-of-bounds) or {@code null} position is provided or if the specified charger and target tiles
     *          are not adjacent
     */
    public Tile getStraightBackwardsTile(Position chargerPos, Position targetPos) {
        if(chargerPos == null || targetPos == null) {
            return null;
        }
        return getStraightBackwardsTile(chargerPos.getRow(), chargerPos.getCol(), targetPos.getRow(), targetPos.getCol());
    }

    /**
     * Given a perimetral {@code Tile} on this board, this method returns the perimetral {@code Tile} that follows it in CW or
     * CCW order, depending on the boolean passed as second argument
     * @param current The origin tile
     * @param clockwise The rotation sense
     * @return The next {@code Tile} on the perimeter following clockwise/counter-clockwise order
     */
    public Tile getPerimetralNext(Tile current, boolean clockwise){

        if(current == null || !(current.isPerimetralTile())) {
            throw new IllegalArgumentException();
        }

        int currentRowIndex = current.getRow();
        int currentColumnIndex = current.getColumn();

        if (clockwise){
            if(currentRowIndex == 0 && currentColumnIndex < 4) {
                currentColumnIndex++;
            }
            else if(currentColumnIndex == 4 && currentRowIndex < 4) {
                currentRowIndex++;
            }
            else if(currentRowIndex == 4 && currentColumnIndex > 0) {
                currentColumnIndex--;
            }
            else if(currentColumnIndex == 0 && currentRowIndex > 0) {
                currentRowIndex--;
            }
        }
        else {
            if(currentRowIndex < 4 && currentColumnIndex == 0) {
                currentRowIndex++;
            }
            else if(currentColumnIndex < 4 && currentRowIndex == 4) {
                currentColumnIndex++;
            }
            else if(currentRowIndex > 0 && currentColumnIndex == 4) {
                currentRowIndex--;
            }
            else if(currentColumnIndex > 0 && currentRowIndex == 0) {
                currentColumnIndex--;
            }
        }

        return getTile(currentRowIndex, currentColumnIndex);
    }

    /**
     * Given a perimetral {@code Tile} on this board, this method returns the perimetral {@code Tile} that precedes it in CW or
     * CCW order, depending on the boolean passed as second argument
     * @param current The origin tile
     * @param clockwise The rotation sense
     * @return The previous {@code Tile} on the perimeter following clockwise/anti-clockwise order
     */
    public Tile getPerimetralPrevious(Tile current, boolean clockwise){
        return this.getPerimetralNext(current, !clockwise);
    }

}
