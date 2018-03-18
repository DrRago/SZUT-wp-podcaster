package frontend;

import config.Config;
import frontend.controller.ServerLoginController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import util.PathUtil;

import java.io.IOException;

public class Run extends Application {

    //Starts the GUI

    private Config config;

    public void start(Stage loginStage) throws IOException {
        try {
            config = new Config();
            Parent root = FXMLLoader.load(PathUtil.getResourcePath("Controller/ServerLogin.fxml"));
            //Set the GUI title
            loginStage.setTitle("- Podcaster");
            //Set a new Scene
            loginStage.setScene(new Scene(root));

            loginStage.getIcons().add(new Image("icons/WP-Podcaster-Icon.png"));


            //Start the GUI
            loginStage.show();

            config.setServerReLoggingIn(true);

            loginStage.setMinWidth(300);
            loginStage.setMinHeight(275);
            loginStage.setMaxHeight(500);
            loginStage.setMaxWidth(1000);
            loginStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    if(!config.getServerRemember())config.deleteServerConfig();
                }
            });



        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    //Launch the args to start the GUI
    public static void main(String[] args) {
        launch(args);

    }
}
