package frontend.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import lombok.Getter;
import lombok.Setter;

public class mediaViewSettings {
    @Getter
    @Setter
    private MediaToQueue controller;

    @FXML
    private VBox mediaVbox;

    @FXML
    public Slider slider;

    @FXML
    public AnchorPane anchorPane;

    @FXML
    private Button btnPlay;

    @FXML
    private Button btnPause;

    @FXML
    private Button btnStop;

    public MediaPlayer player;

    /**
     * Buttons for the MediaPlayer
     */
    public void initialize(){
    }

    /**
     * Pause Button
     * @param event
     */
    @FXML
    void btnPause(ActionEvent event) {
        transparentBtn();
        player.pause();
        btnPause.setStyle("-fx-background-color: lightblue");
        btnStop.setStyle("-fx-background-color: transparent");
        btnPlay.setStyle("-fx-background-color: transparent");
    }

    /**
     * Play Button
     * @param event
     */
    @FXML
    void btnPlay(ActionEvent event) {
        transparentBtn();
        player.play();
        btnPlay.setStyle("-fx-background-color: lightblue");
        btnStop.setStyle("-fx-background-color: transparent");
        btnPause.setStyle("-fx-background-color: transparent");
        if (player.getCurrentTime().toSeconds() == controller.media.getDuration().toSeconds()) {
            player.stop();
            player.play();
        }
    }

    /**
     * Stop Button
     * @param event
     */
    @FXML
    void btnStop(ActionEvent event) {
        transparentBtn();
        player.stop();
        btnStop.setStyle("-fx-background-color: lightblue");
        btnPlay.setStyle("-fx-background-color: transparent");
        btnPause.setStyle("-fx-background-color: transparent");
    }

    /**
     * If loading a new File
     */
    private void transparentBtn() {
        btnStop.setStyle("-fx-background-color: transparent");
        btnPlay.setStyle("-fx-background-color: transparent");
        btnPause.setStyle("-fx-background-color: transparent");
    }
}
