package frontend.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;

public class OptionController {

    @Setter
    private MainController controller;

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
    TextField mp3_title;
    @FXML
    TextField mp3_artist;
    @FXML
    TextField mp3_year;
    @FXML
    TextField mp3_comment;
    @FXML
    TextField mp3_genre;

    public void initialize(){
        // Add a listener to change the label after a value change
        bitrateSlider.valueProperty().addListener(((observable, oldValue, newValue) -> {
            bitrateLabel.setText(Double.toString(Math.round(bitrateSlider.getValue()))+ " kBit/s");
        }));
    }

    @FXML
    public void saveOptions(ActionEvent e){
        //Todo: Save content to file
    }

    @FXML
    public void cancelOptions(ActionEvent e){
        controller.closeOption();
    }
}
