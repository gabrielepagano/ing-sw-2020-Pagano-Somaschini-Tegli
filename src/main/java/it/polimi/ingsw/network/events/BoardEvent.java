package it.polimi.ingsw.network.events;

import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.utils.ListUtility;

import java.util.ArrayList;
import java.util.List;

/**
 * This {@code} event is used by the server to notify the clients about some changes happened to a game board
 */
public class BoardEvent extends Event {
    private final List<Tile> tilesToUpdate;

    /**
     * Creates a new {@code BoardEvent}, passing a list of {@code Tile} objects that represent the changes happened
     * to the board
     * @param tilesToUpdate a list of {@code Tile}s representing the changes happened to the board
     */
    public BoardEvent(List<Tile> tilesToUpdate){
        this.tilesToUpdate = tilesToUpdate;
    }

    /**
     * Creates a new {@code BoardEvent}, passing 2 {@code Tile} objects that represent the changes happened
     * to the board
     * @param t1 a first tile of a board that somehow changed
     * @param t2 a second tile of a board that somehow changed
     */
    public BoardEvent(Tile t1, Tile t2){
        this.tilesToUpdate = new ArrayList<>();
        tilesToUpdate.add(t1);
        tilesToUpdate.add(t2);
    }

    /**
     * Creates a new {@code BoardEvent}, passing 1 {@code Tile} object that represent the changes happened
     * to the board
     * @param t a tile of a board that somehow changed
     */
    public BoardEvent(Tile t){
        this.tilesToUpdate = new ArrayList<>();
        tilesToUpdate.add(t);
    }

    /**
     * Getter for the list of tiles carried by this event, representing a list of changes happened to a game board
     * @return the {@param tilesToUpdate}
     */
    public List<Tile> getTilesToUpdate(){
        return this.tilesToUpdate;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "BoardEvent: " + ListUtility.listToString(tilesToUpdate);
    }
}
