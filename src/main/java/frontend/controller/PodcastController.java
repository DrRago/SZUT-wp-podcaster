package frontend.controller;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;

import javafx.scene.control.Slider;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import javafx.stage.FileChooser;

import javafx.util.Duration;
import lombok.Setter;
import util.PathUtil;

import java.io.File;

import java.io.IOException;


public class PodcastController {

    @Setter
    private MainController controller;

    @FXML
    public AnchorPane rootPane;


    @FXML
    private AnchorPane paneLoadMediaDes;

    @FXML
    private HBox hboxMain;

    private mediaDescription mediaDescriptioncontroller = null;

    public void init() {
        try {
            openMediaDes();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void btnAccept(ActionEvent event) {

    }

    public void btnCancel(ActionEvent event) {
        //mv.getMediaPlayer().stop();
        controller.closePodcast();
    }

    private static void configureFileChooser(final FileChooser fileChooser) {
        fileChooser.setTitle("New Media");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Media", "*.mp3","*.mp4"),
                new FileChooser.ExtensionFilter("MP3", "*.mp3"),
                new FileChooser.ExtensionFilter("MP4", "*.mp4")
        );
    }

    private Region box;

    void openMediaDes() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PathUtil.getResourcePath("Controller/mediaDescription.fxml"));
        box = fxmlLoader.load();

        //Region n = fxmlLoader.load();

        //box.maxWidthProperty().bind(pane.widthProperty());
        //box.maxHeightProperty().bind(pane.heightProperty());

        mediaDescriptioncontroller = fxmlLoader.getController();
        mediaDescriptioncontroller.setController(this);
/*
        box.minHeightProperty().bind(paneLoadMediaDes.heightProperty());
        box.maxHeightProperty().bind(paneLoadMediaDes.heightProperty());
        box.minWidthProperty().bind(rootPane.widthProperty().subtract(5));
        box.maxWidthProperty().bind(rootPane.widthProperty().subtract(5));
        paneLoadMediaDes.maxWidthProperty().bind(rootPane.maxWidthProperty());*/
        // Create new Anchor Pane for the SidePane
        //pane = new AnchorPane(box);
        // Hide the new Anchor Pane for the SidePane
        //pane.setVisible(true);

        paneLoadMediaDes.getChildren().add(box);

        //hboxMain.getChildren().add(pane);
    }
    

    void openMedia(Media media) {


        MediaPlayer player = new MediaPlayer(media);
        MediaView mv = new MediaView(player);

        hboxMain.setStyle("-fx-start-margin: 5px");

        VBox mediaVBox = new VBox();
        VBox podcastVBox = new VBox();

        Pane emptyPane = new Pane();

        VBox mediaSettingVbox = new VBox();
        //Stop,Play,Pause
        HBox mediaSettingHbox = new HBox();

        hboxMain.getChildren().add(mv);

        hboxMain.setVisible(false);
        //hboxMain.getChildren().add(podcastVBox);


        mv.setSmooth(true);
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
            @Override
            public void handle(ActionEvent e) {
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
            @Override
            public void handle(ActionEvent e) {
                player.play();
                btnPause.setStyle("-fx-background-color: transparent");
                if (player.getCurrentTime().toSeconds() == media.getDuration().toSeconds()) {
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
            @Override
            public void handle(ActionEvent e) {
                player.stop();
                btnPause.setStyle("-fx-background-color: transparent");
            }
        });
        mediaSettingHbox.getChildren().addAll(btnPlay, btnPause, btnStop);

        btnStop.setStyle("-fx-background-color: transparent");
        btnPlay.setStyle("-fx-background-color: transparent");
        btnPause.setStyle("-fx-background-color: transparent");

        Slider slider = new Slider();

        mediaSettingVbox.getChildren().add(mediaSettingHbox);
        mediaVBox.getChildren().addAll(mv, mediaSettingVbox);

        hboxMain.getChildren().remove(emptyPane);

        //hboxMain.setVisible(false);
        hboxMain.getChildren().add(0, mediaVBox);

        player.setOnReady(new Runnable() {
            @Override
            public void run() {
                System.out.println(media.getWidth());
                int w, h;
                if (media.getSource().contains(".mp4")) {
                    w = 680 / 2;
                    h = 480 / 2;

                } else {
                    w = 480 / 2;
                    h = 100;
                    mediaSettingVbox.setStyle(
                            "-fx-border-style: solid inside;" +
                                    "-fx-border-width: 2;" +
                                    "-fx-border-insets: 5;" +
                                    "-fx-border-radius: 5;" +
                                    "-fx-border-color: grey;");
                }
                mediaSettingHbox.getChildren().add(slider);

                slider.setStyle("-fx-control-inner-background: palegreen");

                //mediaSettingVbox.setTranslateY(((-h / 16) - btnPause.getHeight() / 2) - slider.getHeight());
                //mediaSettingHbox.setTranslateX(w / 2 - ((btnPause.getWidth()) * 3) / 1.5);
                //slider.setTranslateY(mediaSettingHbox.getTranslateY());
                //Is the Video higher then 1920x1080p
                //if (w <= 1920 / 4 && h <= 1080) {
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

                hboxMain.setVisible(true);
                //} else if (w > 1920 / 4) {


                //}

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


//TODO: Erst öffnen und man kann den Namen und so hinzufügen, dann Datei auswählen und daneben auflisten/Anzeigen (AUDIO MACHEN)