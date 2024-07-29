package it.polimi.ingsw.model;

/**
 * Represents the possible building levels for a Tile. Note that a building can be domed even if it is not fully built up to {@link #LEVEL3}
 */
public enum ETileLevel {

    GROUND, LEVEL1, LEVEL2, LEVEL3;

    private int height;

    private ETileLevel() {
        this.height = ordinal();
    }

    /**
     * @return An integer representing the height corresponding to this ETileLevel
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * @return The value of ETileLevel that follows this, in ascending height order. If this is the last value of ETileLevel, we simply return it (there is no next value)
     */
    public ETileLevel getNextValue() {

        ETileLevel[] values = values();

        if(height == values.length - 1) {
            return values[values.length - 1];
        }else {
            return values[height + 1];
        }

    }
}
