package frontend.controller.testing;

import backend.fileTransfer.Protocols;
import backend.fileTransfer.Uploader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LoginController {

    @FXML
    private TextField urlTextField;

    @FXML
    private TextField uploadpathTextField;

    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordPasswordField;

    @FXML
    private ComboBox protocolComboBox;

    @FXML
    private TextField portTextField;

    @FXML
    private Button loginBtn;

    @FXML
    private Button cancelBtn;

    private Uploader uploader = null;

    public void initilize(){
        protocolComboBox.getItems().addAll(Protocols.FTPS, Protocols.SFTP);
        protocolComboBox.getSelectionModel().select(0);
    }

    @FXML
    void cancelBtn(ActionEvent event) {

    }

    @FXML
    void loginBtn(ActionEvent event) {
        try{
            uploader.disconnect();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

}
