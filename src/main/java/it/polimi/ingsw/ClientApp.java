package it.polimi.ingsw;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.client.ClientView;
import javafx.application.Application;

import static it.polimi.ingsw.model.Constants.DEFAULT_PORT;
import static it.polimi.ingsw.model.Constants.DEFAULT_SERVER_IP;


public class ClientApp {

    public static void main(String[] args) {

        String ip = DEFAULT_SERVER_IP;
        int port = DEFAULT_PORT;
        boolean useCLI = true;

        if(args.length >= 1) {

            if(args[0].equalsIgnoreCase("GUI")) {
                useCLI = false;
            } else if(args[0].equalsIgnoreCase("CLI")) {
                useCLI = true;
            } else {
                System.out.println("You need to specify which client to launch, CLI or GUI");
                System.exit(0);
            }

            if(args.length >= 2) {

                if(!(args[1].isEmpty())) {
                    ip = args[1];
                } else {
                    System.out.println("You passed an invalid IP address, the default one will be used instead");
                }

                if(args.length >= 3) {

                    String thirdArg = args[2];
                    int proposedPort;
                    try {
                        proposedPort = Integer.parseInt(thirdArg);
                        if(proposedPort >= 1024 && proposedPort <= 65535) {
                            port = proposedPort;
                        } else {
                            System.out.println("The port number you passed is too big or too small, the default one will be used instead");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("You passed an invalid port number, the default one will be used instead");
                    }

                }

            }

        } else {
            System.out.println("You need to specify which client to launch, CLI or GUI");
            System.exit(0);
        }

        if(useCLI) {
            ClientConnection clientConnection = new ClientConnection(ip, port);
            ClientView clientView = new ClientView(clientConnection);
            clientView.run();
        } else {
            Application.launch(GUIClientApp.class, ip, Integer.toString(port));
        }

    }

}
