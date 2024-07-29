package it.polimi.ingsw.server;


import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.utils.GameFactory;
import it.polimi.ingsw.utils.ListUtility;
import it.polimi.ingsw.view.RemoteView;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static it.polimi.ingsw.model.Constants.*;

/**
 * This class represents the game server. It continuously waits for connection requests from clients
 * and handles them in separate threads. Depending on their initial choice on the number of players,
 * clients are enqueued in lobbies, waiting for other clients to connect and start a game. When a game is over
 * the server automatically removes it from the list of active games
 */
public class Server {

    private int port;
    private ServerSocket serverSocket;

    private ExecutorService executor = Executors.newFixedThreadPool(128);

    private final List<RemoteConnection> activeConnections = new ArrayList<>();

    private final LinkedList<RemoteConnection> queue2Players = new LinkedList<>();
    private final LinkedList<RemoteConnection> queue3Players = new LinkedList<>();

    private final List<Controller> activeGames = new ArrayList<>();

    /**
     * Creates a new server running on this machine and listening to the specified port
     * @param port The port number the server listens to
     * @throws IOException if an issue occurs during the construction of the {@code ServerSocket} object
     */
    public Server(int port) throws IOException {
        this.port = port;
        this.serverSocket = new ServerSocket(port);
    }

    /**
     * This method adds an active {@code RemoteConnection} to the list of active connections maintained by the server
     * @param c The remoteConnection to add to the active connections list
     */
    public void registerConnection(RemoteConnection c) {
        synchronized (activeConnections){
            if(!activeConnections.contains(c)) {
                activeConnections.add(c);
            }
        }
    }

    /**
     * This method removes a {@code RemoteConnection} from the list of active connections kept by the server. This
     * happens after a voluntary disconnection by the client, a network problem or after a game is over
     * @param c The remoteConnection to deregister from the active connections list
     */
    public void removeConnection(RemoteConnection c) {
        System.out.println();
        synchronized (activeConnections) {
            activeConnections.remove(c);
            System.out.println("Active Connections: [" + ListUtility.listToString(activeConnections.stream().map(con -> con.getNickname()).collect(Collectors.toList())) + "]");
        }
        synchronized (queue2Players) {
            queue2Players.remove(c);
            System.out.println("Queue2Players: [" + ListUtility.listToString(queue2Players) + "]");
        }
        synchronized (queue3Players) {
            queue3Players.remove(c);
            System.out.println("Queue3Players: [" + ListUtility.listToString(queue3Players) + "]");
        }
        System.out.println();
    }

    public void removeGame(Controller finishedGame){
        synchronized (activeGames){
            activeGames.remove(finishedGame);
        }
    }

    /**
     * With this method, the server continuously waits for connection requests from clients and dispatches them
     * to a new dedicated thread, that handles their initialization. Besides, the server iterates on a list of active games,
     * checking that they are still active and removing them if they are no longer active (that is, if they are over)
     */
    public void run() {

        System.out.println("Server listening on port " + port);

        new Thread(new Runnable() {

            @Override
            public void run() {
                while(true) {
                    synchronized (activeGames) {
                        activeGames.removeIf(c -> !c.isActive());
                        System.out.println("Active games: [" + ListUtility.listToString(activeGames) + "]");
                    }
                    try {
                        Thread.sleep(30 * 1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }

                }
            }

        }).start();

        // The server should run until it is explicitly terminated
        while(true) {

            try {
                Socket socket = serverSocket.accept();
                RemoteConnection connection = new RemoteConnection(socket, this);
                registerConnection(connection);
                executor.submit(() -> connection.init());
            } catch (IOException e) {
                System.out.println(SERVER_CONNECTION_ERROR);
            }

        }

    }

    /**
     * This method checks if the specified nickname is already connected to the server. Nicknames MUST be unique
     * @param nickname The nickname to check
     * @return {@code true} if the specified nickname is already connected, {@code false} otherwise
     */
    public boolean isNicknameAlreadyConnected(String nickname) {
        synchronized (activeConnections) {
            for (RemoteConnection c : activeConnections) {
                if (nickname.equals(c.getNickname())) {
                    return true;
                }
            }
            return false;
        }
    }

    // Executed by the 2nd active remoteConnection
    /**
     * This method is executed after a {@code RemoteConnection} is initialized, that is after the client user
     * has specified a valid nickname and a valid number of players. Clients that have selected a 2-players game as
     * their choice are put in a dedicated queue, where they wait for other clients with the same preference to start
     * a new game. When the needed number of players (2) is reached, a new game is created and the clients involved are removed
     * from the queue
     * @param remoteConnection The remoteConnection to enqueue
     */
    public void lobby2Players(RemoteConnection remoteConnection) {
        synchronized (queue2Players) {
            queue2Players.add(remoteConnection);

            remoteConnection.startPinger();

            if (queue2Players.size() == 2) {
                List<RemoteView> remoteViews = new ArrayList<>();
                // Build list of remoteViews from list of remoteConnections
                queue2Players.forEach(c -> remoteViews.add(new RemoteView(c.getNickname())));
                for(int i = 0; i < queue2Players.size(); i++) {
                    remoteViews.get(i).initializeConnection(queue2Players.get(i));
                }
                // Start all the remoteConnections
                queue2Players.forEach(c -> new Thread(c).start());
                // Create a new Controller with an empty game. Register observers (remoteViews and the controller itself) when needed
                Controller newController = new Controller(remoteViews, GameFactory.buildEmptyGameFromRemoteViewsList(remoteViews));
                synchronized(activeGames) {
                    activeGames.add(newController);
                }
                queue2Players.clear();
            }
        }
    }

    // Executed by the 3rd active remoteConnection
    /**
     * This method is executed after a {@code RemoteConnection} is initialized, that is after the client user
     * has specified a valid nickname and a valid number of players. Clients that have selected a 3-players game as
     * their choice are put in a dedicated queue, where they wait for other clients with the same preference to start
     * a new game. When the needed number of players (3) is reached, a new game is created and the clients involved are removed
     * from the queue
     * @param remoteConnection The remoteConnection to enqueue
     */
    public void lobby3Players(RemoteConnection remoteConnection) {
        synchronized (queue3Players) {
            queue3Players.add(remoteConnection);

            remoteConnection.startPinger();

            if(queue3Players.size() == 3) {
                List<RemoteView> remoteViews = new ArrayList<>();
                // Build list of remoteViews from list of remoteConnections
                queue3Players.forEach(c -> remoteViews.add(new RemoteView(c.getNickname())));
                for(int i = 0; i < queue3Players.size(); i++) {
                    remoteViews.get(i).initializeConnection(queue3Players.get(i));
                }
                // Start all the remoteConnections
                queue3Players.forEach(c -> new Thread(c).start());
                // Create a new Controller with an empty game. Register observers (remoteViews and the controller itself) when needed
                Controller newController = new Controller(remoteViews, GameFactory.buildEmptyGameFromRemoteViewsList(remoteViews));
                synchronized(activeGames) {
                    activeGames.add(newController);
                }
                queue3Players.clear();
            }
        }
    }

    public List<RemoteConnection> getActiveConnections() {
        return activeConnections;
    }

    public List<RemoteConnection> getQueue2Players() {
        return queue2Players;
    }

    public List<RemoteConnection> getQueue3Players() {
        return queue3Players;
    }

}
