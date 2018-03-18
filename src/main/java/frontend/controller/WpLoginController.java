package frontend.controller;

import backend.fileTransfer.Uploader;
import backend.fileTransfer.UploaderException;
import backend.fileTransfer.UploaderFactory;
import backend.wordpress.Blog;
import backend.wordpress.WordpressConnectionException;
import config.Config;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
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
import lombok.Getter;
import lombok.Setter;
import util.PathUtil;

import java.io.IOException;

public class WpLoginController {

    @Setter
    @Getter
    private ServerLoginController controller;

    @FXML
    public TextField urlTextField;

    @FXML
    public TextField usernameTextField;

    @FXML
    public PasswordField passwordTextField;

    @FXML
    TextField remotePathTextField;

    @FXML
    public Button cancelButton;

    @FXML
    CheckBox remember;

    private Config config;

    public Blog blog = null;

    /**
     * For WordPress Login
     * @throws IOException
     */
    public void initialize() throws IOException {

        config = new Config();

        //Remember true
        if (config.getWpRemember()) {
            System.out.println("Remember");
            remember.selectedProperty().setValue(true);
            usernameTextField.setText(config.getWordpressUsername());
            passwordTextField.setText(config.getWordpressPassword());
            remotePathTextField.setText(config.getRemoteServerPath());
            urlTextField.setText(config.getWordpressXmlrpcUrl());
        }
    }

    /**
     * Closes the Stage
     * @param event
     */
    @FXML
    void cancelButton(ActionEvent event) {
        closeWpLogin();
    }

    /**
     * closes the stage
     */
    void closeWpLogin(){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Logging in
     * @param event
     */
    @FXML
    void loginButton(ActionEvent event) {
        ObservableList<String> errorMsg = FXCollections.observableArrayList();
        if (!urlTextField.getText().isEmpty()) {
            config.setWordpressXmlrpcUrl(urlTextField.getText());
        } else {
            if (errorMsg.isEmpty()) {
                errorMsg.add("URL");
            }
        }
        if (!usernameTextField.getText().isEmpty()) {
            config.setWordpressUsername(usernameTextField.getText());
        } else {
            if (errorMsg.isEmpty()) {
                errorMsg.add("Username");
            } else {
                errorMsg.add(" / Username");
            }
        }
        if (!passwordTextField.getText().isEmpty()) {
            config.setWordpressPassword(passwordTextField.getText());
        } else {
            if (errorMsg.isEmpty()) {
                errorMsg.add("Password");
            } else {
                errorMsg.add(" / Password");
            }
        }
        if (!remotePathTextField.getText().isEmpty()) {
            config.setRemoteServerPath(remotePathTextField.getText());
        } else {
            if (errorMsg.isEmpty()) {
                errorMsg.add("Remote Path");
            } else {
                errorMsg.add(" / Remote Path");
            }
        }
        if (passwordTextField.getText().isEmpty() || usernameTextField.getText().isEmpty() || urlTextField.getText().isEmpty() || remotePathTextField.getText().isEmpty()) {
            new ShowAlert("Couldn't Login to WordPress. Please check " + errorMsg.toString().replace("[", "").replace("]", "") + "!", "Coudln't Login!");
        } else {

            try {
                Uploader uploader = UploaderFactory.getUploader(config.getUploadProtocol(),config.getUploadServerUrl(),config.getUploadServerPort(),config.getUploadServerUsername(),config.getUploadServerPassword(),config.getUploadServerWorkingDir());
                blog = new Blog(usernameTextField.getText(), passwordTextField.getText(), urlTextField.getText(), uploader, remotePathTextField.getText());
                cancelButton(event);
                openMain();
            } catch (IOException|WordpressConnectionException|UploaderException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Remember CheckBox
     * @param event
     */
    @FXML
    void remember(ActionEvent event) {
        if (remember.isSelected()) {
            config.setWpRemember(true);
        } else {
            config.setWpRemember(false);
        }
    }

    /**
     * Open Main Window
     */
    public void openMain() {
        Stage stageMain = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(PathUtil.getResourcePath("Controller/MainGui.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
            MainController controller = fxmlLoader.getController();
            controller.setController(this);
            stageMain.setTitle("- Podcaster");
            stageMain.setScene(new Scene(root));
            stageMain.getIcons().add(new Image("icons/WP-Podcaster-Icon.png"));

            stageMain.show();

            stageMain.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    if(!config.getWpRemember())config.deleteWorPressConfig();
                    if(!config.getServerRemember())config.deleteServerConfig();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}