package it.polimi.ingsw.client;

import it.polimi.ingsw.network.Connection;
import it.polimi.ingsw.network.DisconnectedException;
import it.polimi.ingsw.network.events.Event;
import it.polimi.ingsw.network.events.PingEvent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * This class represents the client-side endpoint of a client-server socket connection
 */
public class ClientConnection extends Connection {
    private String ipAddress;
    private int port;

    /**
     * Creates a new {@code ClientConnection} instance, setting ip address and port number of the remote server
     * @param ipAddress A string representing the ip address of the server
     * @param port The port number the server listens to
     */
    public ClientConnection(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    /**
     * This method initializes this clientConnection, attempting to connect to the server and then constructing
     * the object streams on which the network events will be sent and received
     * @throws DisconnectedException if this clientConnection is unable to connect to the server
     */
    public void connectToServer() throws DisconnectedException {

        try {
            this.socket = new Socket(ipAddress, port);
            this.oos = new ObjectOutputStream(socket.getOutputStream());
            this.ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new DisconnectedException(">> Couldn't connect to the server");
        }

    }

    /**
     * A clientConnection continuously waits for {@code Event}s from the network. With the exception of {@code PingEvent},
     * which is immediately bounced back when received, any {@code Event} received is notified to this clientConnection's
     * observers (a clientView)
     */
    @Override
    public void run() {
        active = true;
        while(isActive()) {
            try {
                Event received = receiveEvent(70 * 1000);
                while (received instanceof PingEvent) {
                    sendEvent(new PingEvent());
                    received = receiveEvent(70 * 1000);
                }
                setChanged();
                notify(received);
            } catch (DisconnectedException e) {
                notifyDisconnection(e.getMessage());
                close();
            }
        }
    }

}