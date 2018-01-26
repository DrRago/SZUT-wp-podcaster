package frontend.controller;

import backend.fileTransfer.Protocols;
import config.Config;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;

public class OptionController {

    @Setter
    private MainController controller;

    // Options for server access

    //Todo: add Port Option

    @FXML
    PasswordField passwdOption; // in config = password
    @FXML
    TextField urlOption; // in config = hostname
    @FXML
    TextField usrnameOption; // in config = username
    @FXML
    TextField uploadpathOption; // in config = workingDir
    @FXML
    ComboBox protocolOption; // For choosing which protocol to use

    // Bitrate option
    @FXML
    Slider bitrateSlider; // in config = mp3_bitrate
    @FXML
    Label bitrateLabel;

    // ID3-Tags
    @FXML
    TextField mp3_title; // in config = id3_title
    @FXML
    TextField mp3_artist; // in config = id3_artist
    @FXML
    TextField mp3_year; // in config = id3_year
    @FXML
    TextField mp3_comment; // in config = id3_comment
    @FXML
    TextField mp3_genre; // in config = id3_genre

    Config config = new Config();
    /**
     * Initialize the settings window
     * Set default values
     */
    public void initialize(){
        bitrateSlider.setValue(32.0);
        // Add a listener to change the label after a value change
        bitrateSlider.valueProperty().addListener(((observable, oldValue, newValue) -> {
            bitrateLabel.setText(Double.toString(Math.round(bitrateSlider.getValue()))+ " kBit/s");
        }));

        protocolOption.getItems().addAll(Protocols.FTPS, Protocols.SFTP);
        protocolOption.getSelectionModel().select(0);

        //Set default values
        //Todo: config.xml
    }

    /**
     * Save the current settings and close the window
     * @param e
     */
    @FXML
    public void saveOptions(ActionEvent e){
        config.saveConfig(
                passwdOption.getText(),
                urlOption.getText(),
                usrnameOption.getText(),
                uploadpathOption.getText(),
                (Protocols) protocolOption.getSelectionModel().getSelectedItem(),
                bitrateSlider.getValue(),
                mp3_title.getText(),
                mp3_artist.getText(),
                mp3_year.getText(),
                mp3_comment.getText(),
                mp3_genre.getText()
        );
        //Todo: use Config class to save


        cancelOptions(e);
    }

    @FXML
    public void cancelOptions(ActionEvent e){
        controller.closeOption();
    }
}
