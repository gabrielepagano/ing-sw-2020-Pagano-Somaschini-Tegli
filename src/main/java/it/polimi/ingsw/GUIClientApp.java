package it.polimi.ingsw;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.gui.InitialUserSceneController;
import it.polimi.ingsw.network.events.DisconnectedEvent;
import it.polimi.ingsw.view.GUIClientView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;

import static it.polimi.ingsw.model.Constants.DEFAULT_PORT;
import static it.polimi.ingsw.model.Constants.DEFAULT_SERVER_IP;
import static java.lang.System.exit;

public class GUIClientApp extends Application {

    private static GUIClientApp instance;
    private InitialUserSceneController initialUserSceneController;
    private GUIClientView clientView;
    private Scene initialScene;
    private Stage window;
    private ClientConnection clientConnection;

    /**
     * Singleton design pattern
     */
    public GUIClientApp() {
        instance = this;
    }

    public static GUIClientApp getInstance() {
        return instance;
    }

    public GUIClientView getClientView() {
        return clientView;
    }

    public InitialUserSceneController getInitialUserSceneController() {
        return initialUserSceneController;
    }

    public Scene getInitialScene() {
        return initialScene;
    }

    public Stage getWindow() {
        return this.window;
    }

    public ClientConnection getClientConnection() {
        return clientConnection;
    }

    public static void main (String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        Parameters params = getParameters();
        List<String> args = params.getRaw();

        String ip;
        int port;

        ip = DEFAULT_SERVER_IP;
        port = DEFAULT_PORT;

        if(!(args.isEmpty())) {

            if(!(args.get(0).isEmpty())) {
                ip = args.get(0);
            } else {
                System.out.println("You passed an invalid IP address, the default one will be used instead");
            }

            if(args.size() >= 2) {

                String secondArg = args.get(1);
                int proposedPort ;
                try {
                    proposedPort = Integer.parseInt(secondArg);
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

        clientConnection = new ClientConnection(ip, port);
        clientView = new GUIClientView(clientConnection);
        window = stage;

        clientView.init();

        try {

            URL url = GUIClientApp.class.getClassLoader().getResource("GUI/InitialUserScene.fxml");// 1087, 727
            FXMLLoader fxmlLoader;
            fxmlLoader = new FXMLLoader(url);
            Parent parent = fxmlLoader.load();
            initialUserSceneController = fxmlLoader.getController();
            Image backgroundImg = new Image(GUIClientApp.class.getClassLoader().getResource("GUI/title_water.png").toString());
            ImageView imageView = new ImageView(backgroundImg);
            Group root = new Group();
            root.getChildren().addAll(imageView, parent);
            initialScene = new Scene(root, 670.0, 510.0);
            window.setTitle("Santorini by GC21");
            window.setScene(initialScene);
            window.setOnCloseRequest(event -> {
                event.consume();
                closeProgram();
            });
            window.setResizable(false);
            Image gameIcon = new Image(GUIClientApp.class.getClassLoader().getResource("GUI/santorini_img.jpg").toString());
            window.getIcons().add(gameIcon);
            window.sizeToScene();
            window.show();

        } catch (IOException e) {
            e.printStackTrace();
            Alert connectionErrorAlert = GUIClientApp.buildDialog(Alert.AlertType.ERROR, "FXML load error", "Couldn't load FXML markup for the scene", "There was an error while loading the FXML file for this scene, the application will be terminated");
            connectionErrorAlert.getButtonTypes().clear();
            connectionErrorAlert.getButtonTypes().setAll(ButtonType.OK);
            connectionErrorAlert.showAndWait();
            clientConnection.close();
            window.close();
            exit(0);
        }

    }

    public static Alert buildDialog(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        if(title != null) {
            alert.setTitle(title);
        }
        if(headerText != null) {
            alert.setHeaderText(headerText);
        }
        alert.setContentText(contentText);
        return alert;
    }

    public void closeProgram() {

        Alert closeConfirmationAlert = GUIClientApp.buildDialog(Alert.AlertType.CONFIRMATION, null, "Do you really want to terminate the application?", "If you close you will be disconnected from the server");
        closeConfirmationAlert.getButtonTypes().clear();
        closeConfirmationAlert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = closeConfirmationAlert.showAndWait();

        if(result.orElse(ButtonType.NO) == ButtonType.YES) {
            clientView.sendEvent(new DisconnectedEvent("I quit"));
            clientConnection.close();
            window.close();
        }

    }

}
