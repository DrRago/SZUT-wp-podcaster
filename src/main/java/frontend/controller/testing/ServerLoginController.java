package frontend.controller.testing;

import backend.fileTransfer.Protocols;
import backend.fileTransfer.Uploader;
import backend.fileTransfer.UploaderException;
import backend.fileTransfer.UploaderFactory;
import backend.wordpress.Blog;
import config.Config;
import frontend.controller.MainController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import util.PathUtil;

import java.io.IOException;

public class ServerLoginController {

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

    @FXML
    CheckBox rememberCheckBox;

    public Uploader uploader = null;
    Config config = new Config();
    public Blog blog;

    public void initialize(){
        //Set the Protocols in the ComboBox
        protocolComboBox.getItems().addAll(Protocols.FTPS, Protocols.SFTP);
        protocolComboBox.getSelectionModel().select(0);

        //Set the PotTextField to a numeric TextField
        portTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    portTextField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }


    @FXML
    void cancelBtn(ActionEvent event) {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    @FXML
    void loginBtn(ActionEvent event) throws Exception {
        try{
            uploader.disconnect();
        }
        catch (Exception e){
            //e.printStackTrace();
        }
        config.setWorkingDir(uploadpathTextField.getText());
        config.setWordpressURL(urlTextField.getText());
        config.setUsername(usernameTextField.getText());
        config.setPassword(passwordPasswordField.getText());
        config.setPort(Integer.parseInt(portTextField.getText()));
        config.setProtocol((Protocols) protocolComboBox.getSelectionModel().getSelectedItem());

        try{
            uploader = UploaderFactory.getUploader(config.getProtocol(), config.getHostname(), config.getPort(), config.getUsername(), config.getPassword(), config.getWorkingDir());
        }
        catch (UploaderException e) {
            e.printStackTrace();
        }

        Stage stageMain = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(PathUtil.getResourcePath("Controller/WpLogin.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("Cant initialize, because of wrong Login");
        }
        stageMain.setTitle("- Podcaster");
        stageMain.setScene(new Scene(root));
        stageMain.getIcons().add(new Image("icons/WP-Podcaster-Icon.png"));

        stageMain.show();

        cancelBtn(event);
    }

    @FXML
    void rememberCheckBox(ActionEvent event){

    }

}
