package frontend.controller;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;

import javafx.scene.control.ListView;
import javafx.scene.control.Slider;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import javafx.util.Duration;
import lombok.Setter;
import util.PathUtil;

import java.io.IOException;
import java.util.stream.Collectors;


public class PodcastController {

    @Setter
    private MainController controller;

    @FXML
    public AnchorPane rootPane;


    @FXML
    private AnchorPane paneLoadMediaDes;

    @FXML
    private HBox hboxMain;

    private MediaPlayer player;

    private AnchorPane box;

    private mediaDescription mediaDescriptioncontroller = null;

    public void init() {
        try {

            openMediaDes();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void btnAccept(ActionEvent event) {
        player.stop();
        controller.closePodcast();
    }

    public void btnCancel(ActionEvent event) {
        player.stop();
        controller.closePodcast();
    }

    void openMediaDes() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PathUtil.getResourcePath("Controller/mediaDescription.fxml"));
        box = fxmlLoader.load();

        mediaDescriptioncontroller = fxmlLoader.getController();
        mediaDescriptioncontroller.setController(this);

        paneLoadMediaDes.getChildren().add(box);
    }


    private ListView<String> itemList = new ListView<>(); //TODO: ADD LISTENER WITH CELL FACTORY AND MORE

    void showMediaList(ObservableList<Media> items, ObservableList<String> mediaName){
        int countingMediaUploads = mediaDescriptioncontroller.countingMediaUploads;
        int countingMediaFiles = mediaDescriptioncontroller.contingMediaFiles;
        if(countingMediaUploads==1){
            openMediaView(items.get(0), items, itemList);
            itemList.setItems(mediaName.sorted());
        }
        else{

//            /*if(!mediaName.contains(itemList.getItems()))*/itemList.getItems().addAll(mediaName);

        }
        System.out.println(items);
    }

    void openMediaView(Media media, ObservableList<Media> items, ListView<String> itemList) {

        VBox mediaVBox = new VBox();

        player = new MediaPlayer(media);
        MediaView mv = new MediaView(player);

        hboxMain.setStyle("-fx-start-margin: 5px");

        //For the MediaSettingHbox (TODO: not nessecery)
        VBox mediaSettingVbox = new VBox();

        //For the Stop,Play,Pause Buttons
        HBox mediaSettingHbox = new HBox();

        hboxMain.getChildren().add(mv);

        hboxMain.setVisible(false);

        mv.setSmooth(true); //Loads the Mediafile before starting the player -> no lags while playing

        /*
         * Button pause
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
                btnPause.setStyle("-fx-background-color: lightblue");
            }
        });

        /*
         * Button play
         */
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

        /*
         * Button Stop
         */
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
        //Style of the Buttons
        btnStop.setStyle("-fx-background-color: transparent");
        btnPlay.setStyle("-fx-background-color: transparent");
        btnPause.setStyle("-fx-background-color: transparent");


        //Add the children
        mediaSettingVbox.getChildren().add(mediaSettingHbox);
        mediaVBox.getChildren().addAll(mv, mediaSettingVbox);

        hboxMain.getChildren().add(0, mediaVBox);


        //If the MediaPlayer is in the mediaVBox
        if (mediaVBox.getChildren().get(0) == mv) {
            //Add the itemList to the mediaVBox
            mediaVBox.getChildren().add(mediaVBox.getChildren().size(), itemList);
        }
        //Add the media to the items
        items.add(media);

        Slider slider = new Slider();

        player.setOnReady(new Runnable() {
            @Override
            public void run() {
                //If the Media is bigger then 1920px1080p TODO: SHOW ERROR "CANT DISPLAY SUCH HIGH RESOLUTION"
                if (media.getWidth() > 1920) {
                    System.out.println("Cant display such High Resolution!");
                }

                int w = 240; //Set the width

                //Check if the file is a Video or Audio
                if (media.getSource().contains(".mp4")) {
                }
                else {
                    //Set the Style for the Audio File
                    mediaSettingVbox.setStyle(
                            "-fx-border-style: solid inside;" +
                                    "-fx-border-width: 2;" +
                                    "-fx-border-insets: 5;" +
                                    "-fx-border-radius: 5;" +
                                    "-fx-border-color: grey;");
                }

                //Add the Slider to the mediaSettingBox
                mediaSettingHbox.getChildren().add(slider);

                slider.setStyle("-fx-control-inner-background: palegreen");

                mv.setFitWidth(w);
                slider.setMin(0.0);
                slider.setValue(0.0);
                slider.setMax(player.getTotalDuration().toSeconds());
                slider.setMaxWidth(w);
                slider.setTranslateY(5);

                mv.setVisible(true);

                slider.setVisible(true);
                player.setAutoPlay(true);
                //Start the Media
                player.play();

                hboxMain.setVisible(true);
            }
        });

        /*
         * Slider Listener
         */
        player.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration current) {
                slider.setValue(current.toSeconds());
            }
        });

        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (slider.getValue() != player.getCurrentTime().toSeconds())
                    player.seek(Duration.seconds(slider.getValue()));
            }
        });
    }
}