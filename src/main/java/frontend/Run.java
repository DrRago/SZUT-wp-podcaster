package frontend;

import com.sun.javafx.application.LauncherImpl;
import config.Config;
import frontend.controller.MainController;
import frontend.controller.ServerLoginController;
import frontend.controller.WpLoginController;
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

import static javafx.application.Application.launch;

public class Run extends Application {

    //Starts the GUI

    public void start(Stage loginStage) throws IOException {
        try {
            Parent root = FXMLLoader.load(PathUtil.getResourcePath("Controller/ServerLogin.fxml"));
            //Set the GUI title
            loginStage.setTitle("- Podcaster");
            //Set a new Scene
            loginStage.setScene(new Scene(root));

            loginStage.getIcons().add(new Image("icons/WP-Podcaster-Icon.png"));

            //Start the GUI
            loginStage.show();

            loginStage.setMinWidth(300);
            loginStage.setMinHeight(275);
            loginStage.setMaxHeight(500);
            loginStage.setMaxWidth(1000);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //Launch the args to start the GUI
    public static void main(String[] args) {
        launch(args);
 //       LauncherImpl.launchApplication(MainController.class, LoadingScreen.class, args);

    }
}
