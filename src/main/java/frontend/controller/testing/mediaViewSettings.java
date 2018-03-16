package frontend.controller.testing;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
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

    public void initialize(){
    }

    @FXML
    void btnPause(ActionEvent event) {
        transparentBtn();
        player.pause();
        btnPause.setStyle("-fx-background-color: lightblue");
        btnStop.setStyle("-fx-background-color: transparent");
        btnPlay.setStyle("-fx-background-color: transparent");
    }

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

    @FXML
    void btnStop(ActionEvent event) {
        transparentBtn();
        player.stop();
        btnStop.setStyle("-fx-background-color: lightblue");
        btnPlay.setStyle("-fx-background-color: transparent");
        btnPause.setStyle("-fx-background-color: transparent");
    }

    private void transparentBtn() {
        btnStop.setStyle("-fx-background-color: transparent");
        btnPlay.setStyle("-fx-background-color: transparent");
        btnPause.setStyle("-fx-background-color: transparent");
    }
}
