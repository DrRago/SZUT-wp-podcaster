package frontend.controller;


import backend.MediaFactory.Lame;
import backend.fileTransfer.UploaderException;
import backend.wordpress.Blog;
import com.jcraft.jsch.SftpException;
import config.Config;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;

import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import javafx.util.Duration;
import javafx.util.StringConverter;
import lombok.Setter;
import net.bican.wordpress.exceptions.InsufficientRightsException;
import net.bican.wordpress.exceptions.InvalidArgumentsException;
import net.bican.wordpress.exceptions.ObjectNotFoundException;
import redstone.xmlrpc.XmlRpcFault;
import util.PathUtil;

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

    public MediaPlayer player;

    private AnchorPane box;

    public mediaDescription mediaDescriptioncontroller;
    private OptionController optionController;

    private ListView<Lame> itemList = new ListView<>();
    private MediaView mv;
    private Config config = new Config();

    public void init() {
        try {
            openMediaDes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        itemList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Lame>() {
            public void changed(ObservableValue<? extends Lame> observable, Lame oldValue, Lame newValue) {
                player.stop();
                hboxMain.getChildren().remove(0);
                openMediaView(mediaDescriptioncontroller.lameItems, new Media(newValue.getMP3File().toURI().toString()));
            }
        });


        itemList.setCellFactory(lv -> {
            TextFieldListCell<Lame> cell = new TextFieldListCell<>();
            cell.setConverter(new ListViewConverter(cell));
            return cell ;
        });
    }

    //Convert the Object name to the Title of the Media
    public static class ListViewConverter extends StringConverter<Lame>{
        private final ListCell<Lame> cell;
        public ListViewConverter(TextFieldListCell<Lame> cell) {
            this.cell = cell;
        }

        @Override
        public String toString(Lame object) {
            return object.getID3_Title();
        }

        @Override
        public Lame fromString(String string) {
            Lame lame = cell.getItem();
            lame.setID3_Title(string);
            return null;
        }
    }

    public void btnAccept(ActionEvent event) throws IOException, SftpException, XmlRpcFault, ObjectNotFoundException, UploaderException, InvalidArgumentsException, InsufficientRightsException {
        if(player!=null)player.stop();
        controller.closePodcast();

        System.out.println(optionController.getUploader());
        Blog blog = new Blog(config.getWordpressUsername(), config.getWordpressPassword(), config.getHostname() , optionController.getUploader(), config.getWorkingDir());

        ObservableList<Lame> lameItems = mediaDescriptioncontroller.lameItems;
        for(int i = 0; lameItems.size()>i; i++){
            lameItems.get(i).setID3_Title(mediaDescriptioncontroller.textFieldTitle.getText());
            lameItems.get(i).setID3_Artist(mediaDescriptioncontroller.textFieldAuthor.getText());
            lameItems.get(i).setID3_Album(mediaDescriptioncontroller.textFieldAlbum.getText());
            //blog.addPost(mediaDescriptioncontroller.textFieldTitle.getText(), mediaDescriptioncontroller.pendingState.getSelectionModel().toString(), mediaDescriptioncontroller.lameItems.get(i));
        }
    }

    public void btnCancel(ActionEvent event) {
        if(player!=null)player.stop();
        controller.closePodcast();
    }

    void openMediaDes() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PathUtil.getResourcePath("Controller/mediaDescription.fxml"));
        box = fxmlLoader.load();

        mediaDescriptioncontroller = fxmlLoader.getController();
        mediaDescriptioncontroller.setController(this);

        paneLoadMediaDes.getChildren().add(box);
    }

    void showMediaList(ObservableList<Lame> items){
            openMediaView(items, new Media(items.get(0).getMP3File().toURI().toString()));
            for(int i = 0; i<items.size(); i++){
                itemList.getItems().addAll(items.get(i));
                String songTitle = itemList.getItems().get(i).getID3_Title();
            }

    }

    void openMediaView(ObservableList<Lame> items, Media item) {
        //TODO: Neu schreiben und ID3 Tags für einzelne Lame Objektives machen
        //TODO: !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //TODO: !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //TODO: !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //TODO: !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //TODO: !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //TODO: !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!


        VBox mediaVBox = new VBox();

        //Media media = new Media(items.get(0).getMP3File().toURI().toString());

        player = new MediaPlayer(item);
        mv = new MediaView(player);

        hboxMain.setStyle("-fx-start-margin: 5px");

        //For the MediaSettingHbox (TODO: not necessary)
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
                if (player.getCurrentTime().toSeconds() == item.getDuration().toSeconds()) {
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

        Slider slider = new Slider();

        player.setOnReady(new Runnable() {
            @Override
            public void run() {
                //If the Media is bigger then 1920px1080p TODO: SHOW ERROR "CANT DISPLAY SUCH HIGH RESOLUTION"
                if (item.getWidth() > 1920) {
                    System.out.println("Cant display such High Resolution!");
                }

                int w = 240; //Set the width

                    //Set the Style for the Audio File
                    mediaSettingVbox.setStyle(
                            "-fx-border-style: solid inside;" +
                                    "-fx-border-width: 2;" +
                                    "-fx-border-insets: 5;" +
                                    "-fx-border-radius: 5;" +
                                    "-fx-border-color: grey;");
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