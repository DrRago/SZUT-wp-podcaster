package frontend;

import frontend.controller.MainController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import util.PathUtil;

public class Run extends Application{

    public static Stage primaryStage2 = new Stage();
	//Starts the GUI
	@Override
	public void start(Stage primaryStage){
		try {
		    primaryStage2=primaryStage;

			Parent root = FXMLLoader.load(PathUtil.getResourcePath("Controller/MainGui.fxml"));

			//Set the GUI title
			primaryStage.setTitle("- Podcaster");
			//Set a new Scene
			primaryStage.setScene(new Scene(root));

			primaryStage.getIcons().add(new Image("icons/WP-Podcaster-Icon.png"));

			//Start the GUI
			primaryStage.show();

			primaryStage.setMinWidth(400);
			primaryStage.setMinHeight(300);
			primaryStage.setMaxHeight(600);
			primaryStage.setMaxWidth(1000);
			primaryStage.setOnHidden(event -> {
				MainController.shutdown();
				Platform.exit();
			});

            MainController.stageSettings.setOnHidden(e -> primaryStage.show());

		}catch(Exception e){
			e.printStackTrace();
		}
	}

	//Launch the args to start the GUI
	public static void main(String[] args) {
        launch(args);
	}
}
