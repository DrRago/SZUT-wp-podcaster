package frontend.controller.testing;

import javafx.scene.control.Alert;

public class ShowAlert {
    public ShowAlert(String contentText, String title){
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(Alert.AlertType.INFORMATION, contentText);
        alert.setTitle(title);
        alert.showAndWait();
    }
}
