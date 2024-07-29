package it.polimi.ingsw.gui;

import it.polimi.ingsw.model.ETileLevel;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.network.events.Event;
import it.polimi.ingsw.view.GUIClientView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * This class represents the controller for the main scene of the GUI (the "game scene")
 */
public class GameSceneController implements Initializable {

    private GUIClientView clientView;

    private TextField inputTextField;

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private GridPane boardGridPane;

    @FXML
    private Label gamePhaseLabel;

    @FXML
    private Label gamePhaseValueLabel;

    @FXML
    private Label nicknameLabel;

    @FXML
    private Label nicknameValueLabel;

    @FXML
    private ListView<String> playersListView;

    @FXML
    private Label playersListViewLabel;

    @FXML
    private ListView<String> terminalListView;

    private Node[][] boardTiles;

    public void setClientView(GUIClientView clientView) {
        this.clientView = clientView;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        boardTiles = new Node[5][5];

        nicknameLabel.setText("Current player: ");
        gamePhaseLabel.setText("Startup phase: ");
        gamePhaseValueLabel.setText("WAITING FOR OTHER PLAYERS");

        playersListViewLabel.setFont(Font.font(15.0));

        playersListView.setPrefHeight(200.0);
        playersListView.setOpacity(0.8);

        VBox vBox = new VBox();
        vBox.setPrefHeight(200.0);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(3,3,3,3));

        terminalListView = new ListView<>();
        terminalListView.setPrefHeight(200.0);
        terminalListView.setOpacity(0.8);

        HBox hBox = new HBox();
        hBox.setSpacing(5);
        hBox.setPadding(new Insets(3,3,3,3));
        hBox.setAlignment(Pos.CENTER);

        Label label = new Label("Insert your input:");
        label.setFont(Font.font(15));

        inputTextField = new TextField();
        inputTextField.setPromptText("Input");
        inputTextField.setPrefWidth(500.0);
        inputTextField.setOnAction((actionEvent) -> {
            processInput();
        });

        hBox.getChildren().addAll(label, inputTextField);

        vBox.getChildren().addAll(terminalListView, hBox);

        mainBorderPane.setBottom(vBox);

        for(Node node : boardGridPane.getChildren()) {
            int row = GridPane.getRowIndex(node);
            int col = GridPane.getColumnIndex(node);
            node.setOnMouseClicked(mouseEvent -> {
                clientView.sendEvent(new it.polimi.ingsw.network.events.ActionEvent(new Position(row, col)));
            });
            Tooltip tooltip = new Tooltip(ETileLevel.GROUND.toString());
            tooltip.setShowDelay(Duration.seconds(1));
            Tooltip.install(node, tooltip);
            boardTiles[row][col] = node;
        }

    }

    public void showDialog(Alert.AlertType alertType, String headerText, String contentText) {
        Alert alert = new Alert(alertType, headerText, ButtonType.OK);
        alert.showAndWait();
    }

    public void printMessage(String message) {
        terminalListView.getItems().add(message);
    }

    public void processInput() {

        String inputLine = inputTextField.getText();
        inputTextField.clear();

        Event eventToSend = clientView.processInput(inputLine);

        if(eventToSend != null) {
            clientView.sendEvent(eventToSend);
        }

    }

    public Node[][] getBoardTiles() {
        return boardTiles;
    }

    public void setGamePhaseLabel(String newValue) {
        gamePhaseLabel.setText(newValue);
    }

    public void setGamePhaseValueLabel(String newValue) {
        gamePhaseValueLabel.setText(newValue);
    }

    // Not necessary
    public void setNicknameLabel(String newValue) {
        nicknameLabel.setText(newValue);
    }

    public void setNicknameValueLabel(String newValue) {
        nicknameValueLabel.setText(newValue);
    }

    public void fillPlayersListView(List<String> playersNicknames) {

        playersListView.getItems().clear();

        for(String nick : playersNicknames) {
            playersListView.getItems().add(nick);
        }

        playersListView.setStyle("-fx-font-size: 20px;");
        playersListView.setOnMouseClicked(mouseEvent -> {
            String selectedNickname = playersListView.getSelectionModel().getSelectedItems().toString();
        });

    }

    public void highlightPosition(Position p) {

        int row = p.getRow();
        int col = p.getCol();

        Node tile = boardTiles[row][col];

        tile.setStyle("-fx-border-color: #FF0000; -fx-border-width: 3px;");

    }

    public void clearPositionFromHighlighting(Position p) {

        int row = p.getRow();
        int col = p.getCol();

        Node tile = boardTiles[row][col];

        tile.setStyle("-fx-border-color: transparent; -fx-border-width: 3px;");

    }

}
