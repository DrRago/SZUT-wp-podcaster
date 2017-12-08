package frontend.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

public class SettingController {

    @FXML
    PasswordField passwdOption;

    @FXML
    TextField urlOption;

    @FXML
    TextField usrnameOption;

    @FXML
    Slider bitrateSlider;

    @FXML
    Label bitrateLabel;

    @FXML
    void cancelOptions(ActionEvent event) {

    }

    @FXML
    void saveOptions(ActionEvent event) {

    }

    public void init() {
        bitrateSlider.valueProperty().addListener(((observable, oldValue, newValue) -> {
            bitrateLabel.setText(Double.toString(Math.round(bitrateSlider.getValue()))+ " kBit/s");
        }));
    }
}
