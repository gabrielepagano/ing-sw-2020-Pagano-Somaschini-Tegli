package it.polimi.ingsw.model;

import it.polimi.ingsw.network.events.BoardEvent;
import it.polimi.ingsw.network.Observable;
import java.io.Serializable;

/**
 * The {@code Worker} class represents a worker, a simple pawn on the {@code Board}.
 * A {@code Worker} should be on one and only one {@code Board}, but does not know that {@code Board}.
 * A {@code Worker} only knows the {@code Tile} of the {@code Board} on which it is currently placed
 */
public class Worker extends Observable implements Serializable {

    private Tile tile;
    private transient Player owner;
    private String ownerNickname;
    private int id;

    public Worker(Player owner, int id) {
        if(owner == null || id < 0) {
            throw new IllegalArgumentException();
        }
        this.owner = owner;
        this.ownerNickname = owner.getNickname();
        this.id = id;

        //Is initialized during the "Placing phase" in Controller.updateInitialization()
        this.tile = null;
    }

    /**
     * @return The Tile on which this Worker is placed
     */
    public Tile getTile() {
        return this.tile;
    }

    /**
     * It is strongly recommended to use {@link #moveWorker(Tile)} to move this Worker to a given destination Tile.
     * @param tile The Tile on which to place this Worker
     */
    public void setTile(Tile tile) {
        if(tile == null) {
            this.getTile().setWorker(null);
        } else {
            tile.setWorker(this);
        }
        this.tile = tile;
    }

    /**
     * @return The position of this {@code Worker} on the {@code Board}
     */
    public Position getPosition() {
        return new Position(tile.getRow(), tile.getColumn());
    }

    /**
     * @return The Player that owns this Worker
     */
    public Player getOwner() {
        return this.owner;
    }

    /**
     *
     * @return The nickname of the Player that owns this worker
     */
    public String getOwnerNickname() {
        return this.ownerNickname;
    }

    /**
     * @return The id of this worker
     */
    public int getId() {
        return this.id;
    }

    /**
     * Moves this Worker on the specified destination tile. If another Worker is already on the destination Tile, the 2 Workers are swapped.
     * The origin and destination {@code Tile} are updated correspondingly.
     * If the destination tile is already domed, no movement takes place and the method doesn't do anything. No other check is performed on the destination Tile.
     * @param destTile The destination Tile where to move this Worker
     * @throws IllegalArgumentException if the destination Tile is {@code null}
     */
    public void moveWorker(Tile destTile) {
        Tile originTile = this.getTile();

        if(destTile == null) {
            throw new IllegalArgumentException("The destination tile is null, please choose a valid one");
        }
        if(!destTile.isDomed() && originTile != destTile) {
            originTile.setWorker(destTile.getWorker());
            if(originTile.hasWorker()) {
                //si attiva nel caso di Apollo
                originTile.getWorker().setTile(originTile);
            }

            this.setTile(destTile);
            this.getTile().setWorker(this);

            setChanged();
            notify(new BoardEvent((Tile) originTile.clone(),(Tile) destTile.clone()));
        }

    }

}