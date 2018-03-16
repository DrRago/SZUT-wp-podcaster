package frontend.controller.testing;

import backend.fileTransfer.Uploader;
import backend.fileTransfer.UploaderException;
import backend.fileTransfer.UploaderFactory;
import backend.wordpress.Blog;
import config.Config;
import frontend.OpenMainWindowLogin;
import frontend.controller.MainController;
import frontend.controller.testing.ServerLoginController;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.Setter;
import sun.applet.Main;
import util.PathUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static frontend.controller.MainController.uploader;

public class WpLoginController {

    @Setter
    private MainController controller;

    @FXML
    public TextField urlTextField;

    @FXML
    public TextField uploadPathTextField;

    @FXML
    public TextField usernameTextField;

    @FXML
    public PasswordField passwordTextField;

    @FXML
    TextField remotePathTextField;

    @FXML
    public Button cancelButton;

    private Config config = new Config();

    ServerLoginController serverLoginController = new ServerLoginController();

    Blog blog = null;

    @FXML
    void cancelButton(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void loginButton(ActionEvent event){
        try {
            blog = new Blog(usernameTextField.getText(), passwordTextField.getText(), urlTextField.getText(), serverLoginController.uploader,remotePathTextField.getText());
            openMain();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openMain() {
        Stage stageMain = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(PathUtil.getResourcePath("Controller/MainGui.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
            MainController controller = fxmlLoader.getController();
            controller.setWpLoginController(this);
            stageMain.setTitle("- Podcaster");
            stageMain.setScene(new Scene(root));
            stageMain.getIcons().add(new Image("icons/WP-Podcaster-Icon.png"));

            stageMain.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}