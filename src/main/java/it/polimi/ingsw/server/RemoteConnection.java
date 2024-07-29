package it.polimi.ingsw.server;

import it.polimi.ingsw.network.Connection;
import it.polimi.ingsw.network.DisconnectedException;
import it.polimi.ingsw.network.events.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static it.polimi.ingsw.model.Constants.*;

/**
 * This class represents the server-side endpoint of a client-server socket connection
 */
public class RemoteConnection extends Connection {

    /**
     * This class represents a thread dedicated to continuously send {@code PingEvent}s through the socket connection,
     * ensuring that there is always traffic, so that a disconnection or a network issue could be promptly detected
     */
    private class PingerThread extends Thread {

        private boolean active = true;

        public void doStop() {
            this.active = false;
        }

        @Override
        public void run() {

            try {
                while(active) {
                    sendEvent(new PingEvent());
                    try {
                        Thread.sleep(PING_TIMEOUT_IN_SECONDS * 1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            } catch (DisconnectedException e) {
                RemoteConnection.this.close();
                notifyDisconnection(e.getMessage() + " " + getNickname());
            }

        }

    }

    private Server server;
    private String nickname;
    private PingerThread pingerThread;


    public RemoteConnection(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    /**
     * This method initializes this remoteConnection, building the object streams for network communication
     * and executing the "handshaking" phase with che client, during which the client has to insert a valid nickname
     * and number of players before it can start a game. If any communication exception is thrown during those operations,
     * the connection is automatically closed and the client disconnected
     */
    public void init() {
        try {

            this.oos = new ObjectOutputStream(socket.getOutputStream());
            this.ois = new ObjectInputStream(socket.getInputStream());

            readNickname();

            readNPlayers();

        } catch (IOException | DisconnectedException e) {
            close();
        }

    }

    /**
     * With this method the {@code RemoteConnection} iterates asking the client for a nickname, validates it and sends
     * back an ack message ({@code SetNicknameEvent}) if the nickname is valid and hence accepted
     * @throws DisconnectedException if any network issue happens
     */
    private void readNickname() throws DisconnectedException {

        Event received;
        // Inserimento nick
        while(true) {
            received = receiveEvent(LONG_NETWORK_TIMEOUT_IN_SECONDS * 1000);
            if(!(received instanceof MessageEvent)) {
                sendEvent(new ErrorEvent("Invalid input, try again"));
            } else {
                String nick = ((MessageEvent) received).getMessage();
                if(nick != null && !(nick.isEmpty())) {
                    if(!(server.isNicknameAlreadyConnected(nick))) {
                        this.nickname = nick;
                        sendEvent(new SetNicknameEvent(nick));
                        break;
                    } else {
                        sendEvent(new ErrorEvent("This nickname is not available, please choose another one"));
                    }
                } else {
                    sendEvent(new ErrorEvent("The nickname cannot be null or the empty string"));
                }
            }
        }

    }

    /**
     * With this method the {@code RemoteConnection} iterates asking the client for a number of players, validates it and sends
     * back an ack message ({@code SetNPlayersEvent}) if the number of players is 2 or 3
     * @throws DisconnectedException if any network issue happens
     */
    private void readNPlayers() throws DisconnectedException {

        Event received;
        // Inserimento numero di giocatori
        while(true) {
            received = receiveEvent(LONG_NETWORK_TIMEOUT_IN_SECONDS * 1000);
            if(!(received instanceof MessageEvent)) {
                sendEvent(new ErrorEvent("Invalid input, try again"));
            } else {
                String nPlayersString = ((MessageEvent) received).getMessage();
                try {
                    int nPlayers = Integer.parseInt(nPlayersString);
                    if(nPlayers == 2) {
                        sendEvent(new SetNPlayersEvent(nPlayers));
                        server.lobby2Players(this);
                        break;
                    } else if (nPlayers == 3) {
                        sendEvent(new SetNPlayersEvent(nPlayers));
                        server.lobby3Players(this);
                        break;
                    } else {
                        sendEvent(new ErrorEvent("Invalid input, please choose either 2 or 3"));
                    }
                } catch (NumberFormatException e) {
                    sendEvent(new ErrorEvent("Invalid input, please choose either 2 or 3"));
                }
            }
        }

    }

    /**
     * Getter for the nickname of the player associated with this remoteConnection
     * @return
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * A remoteConnection continuously waits for {@code Event}s from the network. Any {@code Event} received is
     * notified to this clientConnection's observers (a messageReceiver inside a remoteView). If the received event
     * is a {@code DisconnectedEvent}, the connection is immediately closed
     */
    @Override
    public void run() {
        while(isActive()) {
            try {

                Event receivedEvent;
                do {
                    receivedEvent = receiveEvent(NORMAL_NETWORK_TIMEOUT_IN_SECONDS * 1000);
                } while(receivedEvent instanceof PingEvent);
                // Not necessary in case of a disconnection during initialization
                if(receivedEvent instanceof DisconnectedEvent) {
                    close();
                }
                setChanged();
                notify(receivedEvent);

            } catch (DisconnectedException e) {
                close();
                notifyDisconnection(e.getMessage() + " " + getNickname());
            }
        }
    }

    /**
     * This method starts the pingerThread associated with this remoteConnection
     */
    public void startPinger() {
        pingerThread = new PingerThread();
        pingerThread.start();
    }

    /**
     * {@inheritDoc}
     * This {@code RemoteConnection} is deregistered from the list of active connections maintained by the server
     */
    @Override
    public synchronized void close() {
        if(isActive()) {
            server.removeConnection(this);
        }
        super.close();
    }

}
