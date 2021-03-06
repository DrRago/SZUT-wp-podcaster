package frontend.controller;

import backend.fileTransfer.Protocols;
import backend.fileTransfer.Uploader;
import backend.fileTransfer.UploaderException;
import backend.fileTransfer.UploaderFactory;
import config.Config;
import frontend.Run;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lombok.Setter;
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
    private Button cancelBtn;

    @FXML
    CheckBox rememberCheckBox;

    Uploader uploader;
    private Config config;

    /**
     *
     * @throws IOException
     */
    public void initialize() throws IOException {
        config = new Config();

        config.setWpReLoggingIn(true);
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

        //Remember true
        if (config.getServerRemember()) {
            rememberCheckBox.selectedProperty().setValue(true);
            usernameTextField.setText(config.getUploadServerUsername());
            passwordPasswordField.setText(config.getUploadServerPassword());
            portTextField.setText(String.valueOf(config.getUploadServerPort()));
            protocolComboBox.getSelectionModel().select(config.getUploadProtocol());
            uploadpathTextField.setText(config.getUploadServerWorkingDir());
            urlTextField.setText(config.getUploadServerUrl());
            if(config.getServerReLoggingIn()) {
                try {
                    uploader = UploaderFactory.getUploader((Protocols) protocolComboBox.getSelectionModel().getSelectedItem(), urlTextField.getText(), Integer.parseInt(portTextField.getText()), usernameTextField.getText(), passwordPasswordField.getText(), uploadpathTextField.getText());
                    openWpLogin();
                    closeCurrentStage();
                } catch (Exception e) {
                    new ShowAlert(Alert.AlertType.WARNING, "Couldn't connect to Server!", "Couldn't connect to Server! Please Check the Arguments.");
                }

            }
        } else {
            config.deleteServerConfig();
        }

    }


    /**
     *
     * @param event
     */
    @FXML
    void cancelBtn(ActionEvent event) {
        closeCurrentStage();
    }

    /**
     *
     * @param event
     * @throws Exception
     */
    @FXML
    void loginBtn(ActionEvent event) throws Exception {
        try {
            uploader.disconnect();
        } catch (Exception e) {
        }

        ObservableList<String> errorMsg = FXCollections.observableArrayList();
        if (!urlTextField.getText().isEmpty()) {
            config.setUploadServerURL(urlTextField.getText());
        }else{
            if (errorMsg.isEmpty()) {
                errorMsg.add("URL");
            }
        }
        if (!usernameTextField.getText().isEmpty()) {
            config.setUploadServerUsername(usernameTextField.getText());
        }else{
            if (errorMsg.isEmpty()) {
                errorMsg.add("Username");
            } else {
                errorMsg.add("/ Username");
            }
        }
        if (!passwordPasswordField.getText().isEmpty()) {
            config.setUploadServerPassword(passwordPasswordField.getText());
        }else{
            if (errorMsg.isEmpty()) {
                errorMsg.add("Password");
            } else {
                errorMsg.add("/ Password");
            }
        }
        if (!uploadpathTextField.getText().isEmpty()) {
            config.setUploadServerWorkingDir(uploadpathTextField.getText());
        }else{
            if (errorMsg.isEmpty()) {
                errorMsg.add("Upload Path");
            } else {
                errorMsg.add("/ Upload Path");
            }
        }
        if (!portTextField.getText().isEmpty()) {
            config.setUploadServerPort(Integer.parseInt(portTextField.getText()));
        }else{
            if (errorMsg.isEmpty()) {
                errorMsg.add("Port");
            } else {
                errorMsg.add("/ Port");
            }
        }
        config.setUploadProtocol((Protocols) protocolComboBox.getSelectionModel().getSelectedItem());

        if (passwordPasswordField.getText().isEmpty() || usernameTextField.getText().isEmpty() || urlTextField.getText().isEmpty() || portTextField.getText().isEmpty() ||uploadpathTextField.getText().isEmpty()) {
            new ShowAlert("Couldn't Login to WordPress. Please check " + errorMsg.toString().replace("[","").replace("]","") + "!", "Coudln't Login!");
        } else {
            try {
                uploader = UploaderFactory.getUploader((Protocols) protocolComboBox.getSelectionModel().getSelectedItem(), urlTextField.getText(), Integer.parseInt(portTextField.getText()), usernameTextField.getText(), passwordPasswordField.getText(), uploadpathTextField.getText());
                openWpLogin();
                closeCurrentStage();
            } catch (UploaderException e) {
                e.printStackTrace();
                new ShowAlert("Couldn't connect to the Upload-Server. Please check Arguments!", "Couldn't connect");
            }

        }
    }

    /**
     *
     * @param event
     */
    @FXML
    void rememberCheckBox(ActionEvent event) {
        if (rememberCheckBox.isSelected()) {
            config.setWpRemember(true);
        } else {
            config.setWpRemember(false);
        }
    }


    private void openWpLogin(){
        Stage stageMain = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(PathUtil.getResourcePath("Controller/WpLogin.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {

        }
        WpLoginController controller = fxmlLoader.getController();
        controller.setController(this);
        stageMain.setTitle("- Podcaster");
        stageMain.setScene(new Scene(root));
        stageMain.getIcons().add(new Image("icons/WP-Podcaster-Icon.png"));

        stageMain.show();

        //Check if Stage is closed
        stageMain.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                if(!config.getWpRemember())config.deleteWorPressConfig();
                if(!config.getServerRemember())config.deleteServerConfig();
            }
        });

        if(stageMain.isShowing())closeCurrentStage();
    }

    /**
     * Closes current Stage
     */
    void closeCurrentStage(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Stage stage = (Stage) cancelBtn.getScene().getWindow();
                stage.close();
            }
        });
    }
}
