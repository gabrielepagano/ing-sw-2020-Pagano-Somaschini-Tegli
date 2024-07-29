package it.polimi.ingsw.network;

/**
 * {@code DisconnectedException} is a type of {@code Exception} that is called after a network problem.
 */
public class DisconnectedException extends Exception {

    /**
     * Creates the {@code DisconnectedException} object.
     * @param message  the input {@code String} that we want to show to the users after the disconnection
     */
    public DisconnectedException(String message) {
        super(message);
    }

}
