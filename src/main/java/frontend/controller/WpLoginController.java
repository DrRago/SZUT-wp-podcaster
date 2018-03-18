package frontend.controller;

import backend.fileTransfer.Uploader;
import backend.fileTransfer.UploaderException;
import backend.fileTransfer.UploaderFactory;
import backend.wordpress.Blog;
import backend.wordpress.WordpressConnectionException;
import config.Config;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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

    @FXML
    CheckBox remember;

    private Config config;

    Blog blog = null;

    //LoadingScreen loadingScreen = new LoadingScreen();

    public void initialize() throws IOException {
//        try {
        //    loadingScreen.start(new Stage(StageStyle.UNDECORATED));
        //      LoadingScreen.launch();
        //    } catch (Exception e) {
        //          e.printStackTrace();
//        }
        config = new Config();

        //Remember true
        if (config.getWpRemember()) {
            usernameTextField.setText(config.getWordpressUsername());
            passwordTextField.setText(config.getWordpressPassword());
            remotePathTextField.setText(config.getRemoteServerPath());
            urlTextField.setText(config.getWordpressXmlrpcUrl());
        }
    }

    @FXML
    void cancelButton(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void loginButton(ActionEvent event) {
        ObservableList<String> errorMsg = FXCollections.observableArrayList();
        if (!urlTextField.getText().isEmpty()) {
            config.setWordpressXmlrpcUrl(urlTextField.getText());
        }else{
            if (errorMsg.isEmpty()) {
                errorMsg.add("URL");
            }
        }
        if (!usernameTextField.getText().isEmpty()) {
            config.setWordpressUsername(usernameTextField.getText());
        }else{
            if (errorMsg.isEmpty()) {
                errorMsg.add("Username");
            } else {
                errorMsg.add(" / Username");
            }
        }
        if (!passwordTextField.getText().isEmpty()) {
            config.setWordpressPassword(passwordTextField.getText());
        } else{
            if (errorMsg.isEmpty()) {
                errorMsg.add("Password");
            } else {
                errorMsg.add(" / Password");
            }
        }
        if (!remotePathTextField.getText().isEmpty()) {
            config.setRemoteServerPath(remotePathTextField.getText());
        }else{
            if (errorMsg.isEmpty()) {
                errorMsg.add("Remote Path");
            } else {
                errorMsg.add(" / Remote Path");
            }
        }
        if (passwordTextField.getText().isEmpty() || usernameTextField.getText().isEmpty() || urlTextField.getText().isEmpty() || remotePathTextField.getText().isEmpty()) {
            new ShowAlert("Couldn't Login to WordPress. Please check " + errorMsg.toString().replace("[","").replace("]","") + "!", "Coudln't Login!");
        } else {
            try {


                //Loading Stage
                /*Stage stage = new Stage(StageStyle.UNDECORATED);
                FXMLLoader fxmlLoader = new FXMLLoader(PathUtil.getResourcePath("Controller/Loading.fxml"));
                Parent root = null;
                try {
                    root = fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                LoadingController loadingController = fxmlLoader.getController();
                loadingController.setController(this);
                stage.setScene(new Scene(root));
                stage.show();*/
//                loadingScreen.show();

                Uploader uploader = UploaderFactory.getUploader(config.getUploadProtocol(), config.getUploadServerUrl(), config.getUploadServerPort(), config.getUploadServerUsername(), config.getUploadServerPassword(), config.getUploadServerWorkingDir());

                //blog = new Blog(config.getWordpressUsername(), config.getWordpressPassword(), config.getWordpressXmlrpcUrl(), uploader, config.getRemoteServerPath());
                blog = new Blog(usernameTextField.getText(), passwordTextField.getText(), urlTextField.getText(), uploader, remotePathTextField.getText());

                openMain();
                //loadingController.closeLoading();
                cancelButton(event);
            } catch (WordpressConnectionException | UploaderException | IOException e) {
                e.printStackTrace();

                new ShowAlert("Couldn't Login to WordPress. Please check Arguments!", "Couldn't Login");
            }
        }
    }

    @FXML
    void remember(ActionEvent event) {
        if (remember.isSelected()) {
            config.setWpRemember(true);
        } else {
            config.setWpRemember(false);
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