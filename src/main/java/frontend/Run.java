package frontend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Run extends Application{

	//Starts the GUI
	@Override
	public void start(Stage primaryStage){
		try {
			//New FXMLLoader
			final FXMLLoader loader = new FXMLLoader();

			//Load the fxml data
			Parent root = loader.load(getClass().getClassLoader().getResource("MainGui.fxml"));

			//Set the GUI title
			primaryStage.setTitle("WP-Podcaster");
			//Set a new Scene
			primaryStage.setScene(new Scene(root));

			//Start the GUI
			primaryStage.show();

		}catch(Exception e){
			e.printStackTrace();
		}
	}

	//Launch the args to start the GUI
	public static void main(String[] args) {
        launch(args);
	}
}
