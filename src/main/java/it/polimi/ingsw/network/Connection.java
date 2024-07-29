package it.polimi.ingsw.network;

import it.polimi.ingsw.network.events.DisconnectedEvent;
import it.polimi.ingsw.network.events.Event;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


/**
 * <p> {@code Connection} is an abstract class that is used for the network connection between client and server. </p>
 * <p> It both extends the {@code Observable} class since the fact that any disconnections has to be notified
 *     and implements the {@code Runnable} interface to permit a multithreading solution for a multiple players game. </p>
 * <p> It has: </p>
 * <ul>
 *    <li> A {@param socket} for the network connection </p></li>
 *    <li> A {@param oos} to write Java objects </li>
 *    <li> A {@param ois} to read Java objects </li>
 *    <li> A boolean, {@param active} that if true indicates that the connection is active </li>
 * </ul>
 *
 */
public abstract class Connection extends Observable implements Runnable {

    protected Socket socket;
    protected boolean active = true;
    protected ObjectOutputStream oos;
    protected ObjectInputStream ois;


    /**
     * A boolean method, synchronized between different players' connections.
     * @return true if the connection is active
     */
    public synchronized boolean isActive() {
        return active;
    }

    /**
     * Sends events in the network between client and server.
     * @param event  the event which has to be sent
     * @throws DisconnectedException if it can't write the {@param event} in the {@code ObjectOutputStream}
     */
    public void sendEvent(Event event) throws DisconnectedException {
        if(!isActive()) {
            return;
        }
        try {
            synchronized(oos) {
                oos.writeObject(event);
                oos.flush();
                oos.reset();
            }
        } catch (IOException e) {
            throw new DisconnectedException(e.getMessage());
        }
    }

    /**
     * This method tries to read an {@code Event} from the socket, waiting at most for the specified time. If
     * the timeout elapses before an {@code Event} is received or there is some network issue, a
     * {@code DisconnectedException} is thrown
     * @param timeout the maximum number of milliseconds that the connection waits before throwing a {@code DisconnectedException}
     * @return the {@code Event} read from the socket input stream
     * @throws DisconnectedException if the timeout elapses or there is some network issue
     */
    public Event receiveEvent(int timeout) throws DisconnectedException {

        if(!isActive()) {
            throw new IllegalStateException(">> The connection is not active");
        }

        Object received;
        try {
            socket.setSoTimeout(timeout);
            received = ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new DisconnectedException(e.getMessage());
        }
        if(!(received instanceof Event)) {
            return null;
        }
        return (Event) received;
    }

    /**
     * Closes the {@code Connection} by the closing of the {@code ObjectOutputStream} and the {@code ObjectInputStream}.
     * After this method, the connection is inactive
     */
    public synchronized void close() {
        if(isActive()) {
            try {
                if(oos != null) {
                    oos.close();
                }
                if(ois != null) {
                    ois.close();
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
            try {
                if(socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
            active = false;
        }
    }

    /**
     * Notifies a disconnection to the registered {@code Observer}s
     */
    public synchronized void notifyDisconnection(String disconnectionMessage) {
        setChanged();
        notify(new DisconnectedEvent(disconnectionMessage));
    }
}
