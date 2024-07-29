package it.polimi.ingsw.view;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.controller.EStartupPhase;
import it.polimi.ingsw.GUIClientApp;
import it.polimi.ingsw.gui.GameSceneController;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.DisconnectedException;
import it.polimi.ingsw.network.events.*;
import it.polimi.ingsw.utils.ListUtility;
import it.polimi.ingsw.utils.Logger;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.System.exit;

/**
 * This class represents the GUI client-side view component of the application. It is separated from the ClientView to better handle
 * the different input modalities that the GUI offers
 */
public class GUIClientView extends View {

    // Model GUI

    private ClientConnection connection;
    private GameSceneController gameSceneController;

    private Board board;
    private List<Position> positionsToHighlight;
    private Map<String, String> workersColors;
    private final String[] colors = {"White", "Brown", "Purple"};
    private String currentPlayerNickname;
    private String nickname;
    private int nPlayers;
    private List<EGodPower> selectedCards;
    private List<EGodPower> playerCards;
    private EStartupPhase startupPhase;
    private ETurnPhase turnPhase;
    private String question;
    private List<String> choiceOptions;
    private List<String> playersNicknames;
    private Logger logger;
    private boolean acceptedNickname;
    private boolean acceptedNPlayers;

    private boolean gameOver;

    public GUIClientView(ClientConnection connection) {

        this.connection = connection;
        this.connection.registerObserver(this);

        // Inizializzazione attributi
        board = new Board();
        gameOver = false;
        turnPhase = null;
        positionsToHighlight = new ArrayList<>();
        workersColors = new HashMap<>();
        selectedCards = new ArrayList<>();
        playerCards = new ArrayList<>();
        choiceOptions = new ArrayList<>();
        playersNicknames = new ArrayList<>();
        logger = new Logger();
        acceptedNickname = false;
        acceptedNPlayers = false;

    }

    /**
     * This method tries to send the specified event on the connection associated with this view component. If any network
     * issue occurs (resulting in a {@code DisconnectedException} both the connection and the GUI components are closed
     * @param event
     */
    public void sendEvent(Event event) {
        try {
            connection.sendEvent(event);
            if(event instanceof DisconnectedEvent) {
                connection.close();
                GUIClientApp.getInstance().getWindow().close();
                exit(0);
            }
        } catch (DisconnectedException e) {

            // Show connection error alert and close clientConnection and application
            Platform.runLater(() -> {
                Alert connectionErrorAlert = GUIClientApp.buildDialog(Alert.AlertType.ERROR, "Connection error", "Couldn't send event to server", "There was a connection error, ClientConnection#sendEvent() threw DisconnectedException!");
                connectionErrorAlert.getButtonTypes().clear();
                connectionErrorAlert.getButtonTypes().setAll(ButtonType.OK);
                connectionErrorAlert.showAndWait();
                connection.close();
                GUIClientApp.getInstance().getWindow().close();
                exit(0);
            });

        }
    }

    /**
     * This method tries to initialize this {@code GUIClientView}, attempting the connection to the server. If any network
     * issue occurs (resulting in a {@code DisconnectedException} both the connection and the GUI components are closed
     */
    public void init() {
        try {
            connection.connectToServer();
            new Thread(connection).start();
        } catch (DisconnectedException e) {

            // Show connection error alert and close clientConnection and application
            Platform.runLater(() -> {
                Alert connectionErrorAlert = GUIClientApp.buildDialog(Alert.AlertType.ERROR, "Connection error", "Couldn't connect to server", "The application was unable to connect to the server. Please try again later");
                connectionErrorAlert.getButtonTypes().clear();
                connectionErrorAlert.getButtonTypes().setAll(ButtonType.OK);
                connectionErrorAlert.showAndWait();
                connection.close();
                GUIClientApp.getInstance().getWindow().close();
                exit(0);
            });

        }
    }

    /**
     * {@inheritDoc}
     * @param arg The event representing details of the received notification
     */
    @Override
    public void update(Event arg) {
        if(!acceptedNickname) {
            if(arg instanceof SetNicknameEvent) {
                setNickname(((SetNicknameEvent) arg).getAcceptedNickname());
            } else if(arg instanceof ErrorEvent) {
                Platform.runLater(() -> {
                    GUIClientApp.getInstance().getInitialUserSceneController().resetNicknameTextField();
                    GUIClientApp.getInstance().getInitialUserSceneController().resetNPlayersComboBox();
                });
                printError(((ErrorEvent) arg).getWarning(), arg.isReserved());
            }
        } else if(!acceptedNPlayers) {
            if(arg instanceof SetNPlayersEvent) {
                setNPlayers(((SetNPlayersEvent) arg).getAcceptedNPlayers());
            } else if(arg instanceof ErrorEvent) {
                Platform.runLater(() -> {
                    GUIClientApp.getInstance().getInitialUserSceneController().resetNicknameTextField();
                    GUIClientApp.getInstance().getInitialUserSceneController().resetNPlayersComboBox();
                });
                printError(((ErrorEvent) arg).getWarning(), arg.isReserved());
            }
        }
        else if(!arg.isReserved() || nickname == null || (arg.isReserved() && nickname.equals(currentPlayerNickname))) {
            super.update(arg);
        }
    }

    // Not necessary in GUIClientView, we don't have to continuously read from stdin
    @Override
    public Event readEvent() {
        return null;
    }

    /**
     * This method is used to process the input line read from the main scene text field, to map it onto
     * an event type to send through the connection
     * @param inputLine the string read from the text field that has to be parsed
     * @return an {@code Event}, resulting from the parsing of the specified string
     */
    public Event processInput(String inputLine) {

        // Check for quit command
        if(inputLine.equalsIgnoreCase("QUIT")) {
            return new DisconnectedEvent("I quit");
        }

        // Check for a choice option
        if(choiceOptions.size() >= 2) {
            if (inputLine.equalsIgnoreCase(choiceOptions.get(0))) {
                choiceOptions.clear();
                return new ChoiceEvent(true);
            } else if (inputLine.equalsIgnoreCase(choiceOptions.get(1))) {
                choiceOptions.clear();
                return new ChoiceEvent(false);
            }
        }
        // Check for a player's nickname
        for(String nick : playersNicknames) {
            if(nick.equals(inputLine)) {
                return new PlayerNicknameEvent(nick);
            }
        }
        try{
            return new CardEvent(EGodPower.parseGodPower(inputLine));
        } catch (IllegalArgumentException ignored) {
            //
        }

        // Check for a valid position on the board
        try {
            String[] words = inputLine.replaceAll("\\s", "").split(",");
            int row = Integer.parseInt(words[0]);
            int col = Integer.parseInt(words[1]);
            return new ActionEvent(new Position(row, col));
        } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
            // Ignore it
        }

        // Check for a description command (returns null to avoid useless server traffic)
        String[] words = inputLine.trim().split(" ");
        if(words.length == 2) {
            if(words[0].equalsIgnoreCase("DESCRIPTION")) {
                try {
                    printMessage(EGodPower.parseGodPower(words[1]).getPowerInfo());
                    return null;
                } catch (IllegalArgumentException e) {
                    // Ignore it
                }
            }
        }

        return new MessageEvent(inputLine);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void printMessage(String message) {

        Platform.runLater(() -> {
            gameSceneController.printMessage(message);
        });

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void printMessage(String message, boolean currentPlayerReserved) {
        printMessage(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void logMessage(String message) {
        logger.add(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void logMessage(String message, boolean currentPlayerReserved) {
        logMessage(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateBoard(List<Tile> tilesToUpdate) {

        for(Tile t : tilesToUpdate) {
            board.setTile(t);

            Platform.runLater(() -> {

                int row = t.getRow();
                int col = t.getColumn();
                int level = t.getLevel().getHeight();
                boolean hasWorker = t.hasWorker();
                boolean isDomed = t.isDomed();

                ImageView levelImageView;
                Image levelImage;
                ImageView workerOrDomeImageView;
                Image workerOrDomeImage;

                StackPane tile = (StackPane) gameSceneController.getBoardTiles()[row][col];

                tile.getChildren().clear();

                if(level >= 1 && level <= 3) {
                    levelImage = new Image(GUIClientApp.class.getClassLoader().getResource("GUI/Level" + level + ".png").toString());
                    levelImageView = new ImageView(levelImage);
                    levelImageView.setPreserveRatio(true);
                    levelImageView.setFitHeight(50.0);
                    levelImageView.setFitWidth(50.0);
                    tile.getChildren().add(levelImageView);
                }

                Tooltip tooltip = new Tooltip(t.getLevel().toString());
                tooltip.setShowDelay(Duration.seconds(1));
                Tooltip.install(tile, tooltip);

                if(isDomed) {
                    workerOrDomeImage = new Image(GUIClientApp.class.getClassLoader().getResource("GUI/Dome.png").toString());
                    workerOrDomeImageView = new ImageView(workerOrDomeImage);
                    workerOrDomeImageView.setPreserveRatio(true);
                    workerOrDomeImageView.setFitHeight(50.0);
                    workerOrDomeImageView.setFitWidth(50.0);
                    tile.getChildren().add(workerOrDomeImageView);
                }

                if(hasWorker) {

                    String ownerNickname = t.getWorker().getOwnerNickname();

                    workerOrDomeImage = new Image(GUIClientApp.class.getClassLoader().getResource("GUI/Worker" + workersColors.getOrDefault(ownerNickname, "White") + ".png").toString());
                    workerOrDomeImageView = new ImageView(workerOrDomeImage);
                    workerOrDomeImageView.setPreserveRatio(true);
                    workerOrDomeImageView.setFitHeight(50.0);
                    workerOrDomeImageView.setFitWidth(50.0);
                    tile.getChildren().add(workerOrDomeImageView);
                }

            });

        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void highlightTiles(List<Position> positionsToHighlight) {

        for(Position p : this.positionsToHighlight) {
            Platform.runLater(() -> {
                gameSceneController.clearPositionFromHighlighting(p);
            });
        }

        this.positionsToHighlight = positionsToHighlight;

        for(Position p : this.positionsToHighlight) {
            Platform.runLater(() -> {
                gameSceneController.highlightPosition(p);
            });
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void askChoice(String question, String opTrue, String opFalse) {
        this.question = question;
        choiceOptions.add(opTrue);
        choiceOptions.add(opFalse);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void printError(String warning, boolean currentPlayerReserved) {

        Platform.runLater(() -> {
            GUIClientApp.getInstance().getInitialUserSceneController().showErrorDialog(warning, "Content");
        });

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setGameOver() {
        this.gameOver = true;
        connection.close();
        Platform.runLater(() -> {
            gameSceneController.setGamePhaseValueLabel("GAME OVER");
            gameSceneController.setNicknameLabel("Winner: ");
            gameSceneController.setNicknameValueLabel(playersNicknames.get(0));
            for(Position p : positionsToHighlight) {
                gameSceneController.clearPositionFromHighlighting(p);
            }
        });
        List<String> loggedMessages = logger.getLastMessages();
        for(String message : loggedMessages) {
            printMessage(message);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCurrentPlayer(String newCurrentPlayerNick) {

        boolean isOldCurrentPlayer = this.currentPlayerNickname.equals(newCurrentPlayerNick);

        this.currentPlayerNickname = newCurrentPlayerNick;

        Platform.runLater(() -> {
            gameSceneController.setNicknameValueLabel(currentPlayerNickname);
        });

        if(nickname.equals(newCurrentPlayerNick)){
            printMessage("It's your turn!");
            if(!isOldCurrentPlayer) {
                Platform.runLater(() -> {
                    gameSceneController.showDialog(Alert.AlertType.INFORMATION, "It's your turn!", "");
                });
            }
        }
        else {
            printMessage(newCurrentPlayerNick + " is now playing");
            Platform.runLater(() -> {
                for(Position p : positionsToHighlight) {
                    gameSceneController.clearPositionFromHighlighting(p);
                }
            });
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setChallenger(String nickname) {
        this.currentPlayerNickname = nickname;
        Platform.runLater(() -> {
            gameSceneController.showDialog(Alert.AlertType.INFORMATION, currentPlayerNickname + " is the challenger", "");
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setNickname(String acceptedNickname) {
        this.nickname = acceptedNickname;
        this.currentPlayerNickname = acceptedNickname;
        this.acceptedNickname = true;
        Platform.runLater(() -> {
            GUIClientApp.getInstance().getInitialUserSceneController().setNicknameAccepted();
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setNPlayers(int acceptedNPlayers) {
        this.nPlayers = acceptedNPlayers;
        this.acceptedNPlayers = true;
        // Carica schermata principale
        Platform.runLater(() -> {
            try {

                Stage window = GUIClientApp.getInstance().getWindow();

                URL url = GUIClientApp.class.getClassLoader().getResource("GUI/GameScene.fxml");// 1087, 627
                FXMLLoader fxmlLoader;
                fxmlLoader = new FXMLLoader(url);
                Parent parent = fxmlLoader.load();
                gameSceneController = fxmlLoader.getController();
                gameSceneController.setClientView(this);
                gameSceneController.setNicknameValueLabel(nickname);
                Image backgroundImg = new Image(GUIClientApp.class.getClassLoader().getResource("GUI/Odyssey-Olympus.png").toString());
                ImageView imageView = new ImageView(backgroundImg);
                Group root = new Group();
                root.getChildren().addAll(imageView, parent);
                Scene scene = new Scene(root, 1087.0, 727.0);

                window.setScene(scene);
                window.sizeToScene();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSelectedCard(EGodPower selectedCard) {

        if(startupPhase == EStartupPhase.PICKCARDS) {
            selectedCards.add(selectedCard);
            printMessage("Challenger picked " + selectedCard.toString());
        } else if (startupPhase == EStartupPhase.DEALCARDS) {
            playerCards.add(selectedCard);
            printMessage(currentPlayerNickname + " picked " + selectedCard.toString());
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStartupPhase(EStartupPhase startupPhase) {
        this.startupPhase = startupPhase;

        Platform.runLater(() -> {
            if(this.startupPhase == EStartupPhase.GAMESTARTED) {
                gameSceneController.setGamePhaseLabel("Turn phase: ");
            } else {
                gameSceneController.setGamePhaseValueLabel(this.startupPhase.toString());
            }
        });

        switch(this.startupPhase) {
            case PICKCHALLENGER:
                printMessage("Set PICKCHALLENGER");
                break;

            case PICKCARDS:

                printMessage(">> The CHALLENGER will reveal which GODS summoned you here!");
                if(nickname.equals(currentPlayerNickname)) {
                    printMessage(">> The available GOD CARDS are: " + ListUtility.listToString(EGodPower.getCardList(playersNicknames.size())));
                    printMessage(">> Choose " + playersNicknames.size() + " cards");
                }
                break;

            case DEALCARDS:
                if(nickname.equals(currentPlayerNickname)) {
                    printMessage(">> Choose a card");
                }
                break;

            case PICKFIRSTPLAYER:

                ListUtility.shiftRight(playerCards, playersNicknames.indexOf(currentPlayerNickname) + 1);

                List<String> listViewEntries = new ArrayList<>();
                for(int i = 0; i < playersNicknames.size(); i++) {
                    listViewEntries.add(playersNicknames.get(i) + ": " + playerCards.get(i).toString());
                }
                Platform.runLater(() -> {
                    gameSceneController.fillPlayersListView(listViewEntries);
                });

                printMessage(">> The CHALLENGER is choosing who will GO FIRST!");
                if(nickname.equals(currentPlayerNickname)) {
                    printMessage(">> Who should play FIRST?");
                }
                break;

            case PLACEFIRSTWORKER:
                if(nickname.equals(currentPlayerNickname)) {
                    printMessage(">> Select the tile where you want to PLACE your FIRST worker");
                }
                else{
                    printMessage(">> " + currentPlayerNickname + " is choosing...");
                }
                break;

            case PLACESECONDWORKER:
                if(nickname.equals(currentPlayerNickname)) {
                    printMessage(">> Select the tile where you want to PLACE your SECOND worker");
                }
                else{
                    printMessage(">> " + currentPlayerNickname + " is choosing...");
                }
                break;

            case GAMESTARTED:
                printMessage(">> The game is starting!");
                break;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTurnPhase(ETurnPhase turnPhase) {
        this.turnPhase = turnPhase;

        Platform.runLater(() -> {
            gameSceneController.setGamePhaseValueLabel(this.turnPhase.toString());
        });

        List<String> loggedMessages = logger.getLastMessages();
        for(String message : loggedMessages) {
            printMessage(message);
        }
        printInstructions();
    }

    /**
     * Prints turnphase-specific information to the client.
     * This info is different for the player actually playing this turn vs the "spectators"
     */
    public void printInstructions() {
        if(startupPhase == EStartupPhase.GAMESTARTED) {
            switch (this.turnPhase) {
                case WORKERSELECTION:
                    if(nickname.equals(currentPlayerNickname)) {
                        printMessage(">> SELECT one of your workers");
                    } else {
                        printMessage(">> The current player is selecting a worker");
                    }
                    break;
                case MOVE:
                    if(nickname.equals(currentPlayerNickname)) {
                        printMessage(">> Select the tile where you want to MOVE");
                    } else {
                        printMessage(">> The current player is choosing where to move his/her worker");
                    }
                    break;
                case BUILD:
                    if(nickname.equals(currentPlayerNickname)) {
                        printMessage(">> Select the tile where you want to BUILD");
                    } else {
                        printMessage(">> The current player is choosing where to build");
                    }
                    break;
                case GODPOWER:
                    if(nickname.equals(currentPlayerNickname)) {
                        printMessage(">> " + question);
                        printMessage(">> Choose between " + choiceOptions.get(0) + " and " + choiceOptions.get(1));
                    } else {
                        printMessage(">> The current player is deciding whether to use his/her god power or not");
                    }
                    break;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLoser(String loserNickname) {
        printMessage(loserNickname + " lost");

        playerCards.remove(playersNicknames.indexOf(loserNickname));
        playersNicknames.remove(loserNickname);

        List<String> listViewEntries = new ArrayList<>();
        for(int i = 0; i < playersNicknames.size(); i++) {
            listViewEntries.add(playersNicknames.get(i) + ": " + playerCards.get(i).toString());
        }
        Platform.runLater(() -> {
            gameSceneController.fillPlayersListView(listViewEntries);
        });

        if(!(currentPlayerNickname.equals(loserNickname)) && playersNicknames.size() > 1) {
            printInstructions();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPlayersNicknames(List<String> playersNicknames) {
        this.playersNicknames = playersNicknames;
        for(int i = 0; i < this.playersNicknames.size(); i++) {
            workersColors.put(this.playersNicknames.get(i), colors[i]);
        }
        Platform.runLater(() -> {
            gameSceneController.fillPlayersListView(playersNicknames);
        });
    }

}