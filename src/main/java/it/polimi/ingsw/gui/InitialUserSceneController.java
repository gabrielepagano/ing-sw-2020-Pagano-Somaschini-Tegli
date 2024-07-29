package it.polimi.ingsw.gui;

import it.polimi.ingsw.GUIClientApp;
import it.polimi.ingsw.network.events.MessageEvent;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.System.exit;

/**
 * This class represents the controller for the initial user scene of the GUI
 */
public class InitialUserSceneController implements Initializable {

    @FXML
    private ComboBox<Integer> nPlayersComboBox;

    @FXML
    private ImageView playImageView;

    @FXML
    private Label playersLabel;

    @FXML
    private TextField nicknameTextField;

    private Integer proposedNPlayers;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Integer> elements = FXCollections.observableArrayList(2,3);
        nPlayersComboBox.setItems(elements);
        nPlayersComboBox.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observableValue, Integer oldValue, Integer newValue) {
                nPlayersComboBox.setPromptText(newValue.toString());
            }
        });
        nPlayersComboBox.setValue(2);

        nicknameTextField.setOnAction(actionEvent -> {
            sendDataToServer();
        });

    }

    public void sendDataToServer() {

        String proposedNickname = nicknameTextField.getText();

        // Invalid nickname
        if(proposedNickname == null || proposedNickname.equals("")) {
            resetNicknameTextField();
            Alert nicknameErrorAlert = GUIClientApp.buildDialog(Alert.AlertType.ERROR,null,"Invalid nickname", "Please insert a valid nickname");
            nicknameErrorAlert.showAndWait();
            return;
        }

        nicknameTextField.setDisable(true);

        proposedNPlayers = nPlayersComboBox.getSelectionModel().getSelectedItem();

        // No selection made, but it should never happen, because we set a default value
        if(proposedNPlayers == null) {
            Alert nPlayersErrorAlert = GUIClientApp.buildDialog(Alert.AlertType.ERROR,null,"Invalid number of players", "Please select the number of players you fancy");
            nPlayersErrorAlert.showAndWait();
            return;
        }

        // Prevent multiple clicks one after the other
        nPlayersComboBox.setDisable(true);

        GUIClientApp.getInstance().getClientView().sendEvent(new MessageEvent(proposedNickname));

    }

    public void resetNicknameTextField() {
        nicknameTextField.clear();
        nicknameTextField.setDisable(false);
    }

    public void resetNPlayersComboBox() {
        nPlayersComboBox.setValue(2);
        nPlayersComboBox.setDisable(false);
    }

    public void setNicknameAccepted() {
        GUIClientApp.getInstance().getClientView().sendEvent(new MessageEvent(proposedNPlayers.toString()));
    }

    public void showErrorDialog(String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR, headerText, ButtonType.OK);
        alert.showAndWait();
    }

}
