package frontend.controller.testing;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.PathUtil;

import java.io.IOException;

public class LoadFxml {
    public LoadFxml(String path, String title, Stage stage){
        FXMLLoader fxmlLoader = new FXMLLoader(PathUtil.getResourcePath(path));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //controller.setController(this);
        stage.setTitle(title);
        stage.setScene(new Scene(root));

        stage.show();
    }
}
