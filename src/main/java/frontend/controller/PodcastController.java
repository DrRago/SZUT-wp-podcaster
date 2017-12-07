package frontend.controller;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import javafx.scene.control.Slider;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.util.Duration;
import lombok.Setter;

import java.io.File;


public class PodcastController {

    @Setter
    private MainController controller;

    @FXML
    private HBox hboxMain;


    public void init() {

        FileChooser chooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("select your media(*.mp4)", "*.mp4");
        chooser.getExtensionFilters().add(filter);
        File file = chooser.showOpenDialog(new Stage());
        if (file != null) {

            //Create new MediaView within the MediaPlayer which plays the Media
            Media me = new Media(file.toURI().toString());
            MediaPlayer player = new MediaPlayer(me);
            MediaView mv = new MediaView(player);



            //MediaPlayer stops with closing the window
            controller.stagePodcast.setOnCloseRequest(event -> {
                mv.getMediaPlayer().stop();
            });

            //MediaViewer, mediaSettingHbox, Slider
            VBox mediaVBox = new VBox();
            VBox podcastVBox = new VBox();

            //Stop,Play,Pause
            HBox mediaSettingHbox = new HBox();

            /*
             * Buttons
             */
            Image imgPause = new Image(getClass().getResourceAsStream("/icons/pause-icon.png"));
            Button btnPause = new Button();
            ImageView imgVPause = new ImageView(imgPause);
            imgVPause.setFitHeight(20);
            imgVPause.setFitWidth(20);
            btnPause.setGraphic(imgVPause);
            btnPause.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                    player.pause();
                    btnPause.setStyle("-fx-background-color: aliceblue");
                }
            });

            Image imgPlay = new Image(getClass().getResourceAsStream("/icons/play-icon.png"));
            Button btnPlay = new Button();
            ImageView imgVPlay = new ImageView(imgPlay);
            imgVPlay.setFitHeight(20);
            imgVPlay.setFitWidth(20);
            btnPlay.setGraphic(imgVPlay);
            btnPlay.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                    player.play();
                    btnPause.setStyle("-fx-background-color: transparent");
                    if (player.getCurrentTime().toSeconds() == me.getDuration().toSeconds()) {
                        player.stop();
                        player.play();
                    }
                }
            });

            Image imgStop = new Image(getClass().getResourceAsStream("/icons/stop-icon.png"));
            Button btnStop = new Button();
            ImageView imgVStop = new ImageView(imgStop);
            imgVStop.setFitHeight(20);
            imgVStop.setFitWidth(20);
            btnStop.setGraphic(imgVStop);
            btnStop.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                    player.stop();
                    btnPause.setStyle("-fx-background-color: transparent");
                }
            });
            mediaSettingHbox.getChildren().addAll(btnPlay,btnPause,btnStop);

            btnStop.setStyle("-fx-background-color: transparent");
            btnPlay.setStyle("-fx-background-color: transparent");
            btnPause.setStyle("-fx-background-color: transparent");


            Button chooseMedia = new Button("Add new Media");
            chooseMedia.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    FileChooser chooser = new FileChooser();
                    FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("select your media(*.mp4)", "*.mp4");
                    chooser.getExtensionFilters().add(filter);
                    File file = chooser.showOpenDialog(new Stage());
                    if (file != null) {

                        Media me = new Media(file.toURI().toString());
                        MediaPlayer player = new MediaPlayer(me);
                        MediaView mv = new MediaView(player);
                    }

                }
            });

            Slider slider = new Slider();

            mediaVBox.getChildren().addAll(mv, mediaSettingHbox, slider);
            podcastVBox.getChildren().add(chooseMedia);

            hboxMain.getChildren().addAll(mediaVBox, podcastVBox);

            player.setOnReady(new Runnable() {
                @Override
                public void run() {
                    System.out.println(me.getWidth());
                    int w = player.getMedia().getWidth() / 4;
                    int h = player.getMedia().getHeight() / 4;

                    mediaSettingHbox.setTranslateY((-h/16)-btnPause.getHeight()/2);
                    mediaSettingHbox.setTranslateX((w+(btnPause.getWidth()*3))/4);
                    //Is the Video higher then 1920x1080p
                    if (w <= 1920 / 4 && h <= 1080) {
                        System.out.println("New width: " + w);
                        /////hboxBtn.setVisible(true);
                        /////hboxBtn.setMaxWidth(w);
                        /////hboxBtn.setMinWidth(w);

                        mv.setFitHeight(h);
                        mv.setFitWidth(w);
                        slider.setMin(0.0);
                        slider.setValue(0.0);
                        slider.setMax(player.getTotalDuration().toSeconds());
                        slider.setMaxWidth(w);

                        mv.setVisible(true);

                        slider.setVisible(true);
                        player.setAutoPlay(true);
                        player.play();
                    } else if (w > 1920 / 4) {


                    }
                }
            });

            player.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                @Override
                public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration current) {
                    slider.setValue(current.toSeconds());
                }
            });

            slider.valueProperty().addListener(new ChangeListener<Number>() {

                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    if (slider.getValue() != player.getCurrentTime().toSeconds())
                        player.seek(Duration.seconds(slider.getValue()));
                }

            });

        }
    }




    public void btnAccept(ActionEvent event) {

    }

    public void btnCancel(ActionEvent event) {
        //mv.getMediaPlayer().stop();
        controller.closePodcast();
    }


}

//TODO: Erst öffnen und man kann den Namen und so hinzufügen, dann Datei auswählen und daneben auflisten/Anzeigen (AUDIO MACHEN)