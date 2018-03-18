package frontend.controller;

import javafx.scene.control.Alert;

public class ShowAlert {
    public ShowAlert(String contentText, String title){
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(Alert.AlertType.INFORMATION, contentText);
        alert.setTitle(title);
        alert.showAndWait();
    }

    public ShowAlert(Alert.AlertType alertType, String contentText, String title) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(alertType, contentText);
        alert.setTitle(title);
        alert.showAndWait();
    }

    public ShowAlert(Alert.AlertType alertType, String contentText, String title, String headerText) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(alertType, contentText);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }

}
