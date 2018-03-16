package frontend.controller.testing;

import backend.wordpress.Blog;
import backend.wordpress.WordpressConnectionException;
import config.Config;
import frontend.controller.MainController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.Setter;
import util.PathUtil;

import java.io.IOException;

public class WpLoginController {

    @Setter
    private ServerLoginController controller;

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

    Blog blog = null;

    @FXML
    void cancelButton(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void loginButton(ActionEvent event){
        config.setWordpressURL(urlTextField.getText());
        config.setWordpressUsername(usernameTextField.getText());
        config.setWordpressPassword(passwordTextField.getText());
        config.setRemotePath(remotePathTextField.getText());
        try {
            blog = new Blog(config.getWordpressUsername(),config.getWordpressPassword(),config.getWordpressURL(), controller.uploader, config.getRemotePath());
            openMain();
        } catch (WordpressConnectionException|IOException e) {
            e.printStackTrace();

            new ShowAlert("Couldn't Login to WordPress. Please check Arguments!", "Couldn't Login");
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