package it.polimi.ingsw;

import it.polimi.ingsw.server.Server;
import java.io.IOException;

import static it.polimi.ingsw.model.Constants.DEFAULT_PORT;

public class ServerApp {

    public static void main(String[] args) {

        Server server;

        int port = DEFAULT_PORT;

        if(args.length >= 1) {
            String firstArg = args[0];
            int proposedPort ;
            try {
                proposedPort = Integer.parseInt(firstArg);
                if(proposedPort >= 1024 && proposedPort <= 65535) {
                    port = proposedPort;
                } else {
                    System.out.println("The port number you passed is too big or too small, the default one will be used instead");
                }
            } catch (NumberFormatException e) {
                System.out.println("You passed an invalid port number, the default one will be used instead");
            }
        }

        try {
            server = new Server(port);
            server.run();
        } catch (IOException e) {
            System.out.println("Server can not run");

        }

    }

}
