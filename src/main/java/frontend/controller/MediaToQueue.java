package frontend.controller;

import backend.MediaFactory.AudioMode;
import backend.MediaFactory.Lame;
import config.Config;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;
import org.farng.mp3.TagException;
import util.PathUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class MediaToQueue {

    // New Toggle Group for Buttons
    private final ToggleGroup group = new ToggleGroup();
    public Media media;
    public MediaPlayer player = null;
    @FXML
    protected RadioButton monoRadioBtn;

    @FXML
    protected RadioButton stereoRadioBtn;

    @FXML
    protected ComboBox<String> uploadConfig;

    @FXML
    protected Slider bitRateSlider;

    @FXML
    protected TextField authorTextField;

    @FXML
    protected TextField albumTextField;

    @FXML
    protected TextField yearTextField;

    @FXML
    protected TextField genreTextField;
    @FXML
    Button selectMediaBtn;
    @FXML
    Slider slider;
    @FXML
    VBox mediaVBox;
    @FXML
    Label bitRateLabel;
    @FXML
    TextField wpTitleTextField;
    @Setter
    @Getter
    private MediaQueue controller;
    @Setter
    @Getter
    private mediaViewSettings mediaViewSettings;
    @FXML
    private TextField titleTextField;
    @FXML
    private TextArea commentTextArea;
    @FXML
    private Button acceptBtn;
    @FXML
    private Button cancelBtn;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private HBox playerHbox;
    private Pane pane;
    private MediaView mediaView;

    private Lame lame = null;
    private File file;
    private Config config;


    /*
     * After opening the FXML
     */
    public void initialize() throws IOException {
        config = new Config();
        // Add a listener to change the label after a value change
        bitRateSlider.valueProperty().addListener(((observable, oldValue, newValue) -> {
            bitRateLabel.setText(Double.toString(Math.round(bitRateSlider.getValue())) + " kBit/s");
        }));

        //Hide the Slider
        slider.setVisible(false);

        //Set the RadioButtons to a group
        monoRadioBtn.setToggleGroup(group);
        stereoRadioBtn.setToggleGroup(group);

        //Set the Upload Options
        ObservableList<String> options =
                FXCollections.observableArrayList(
                        "Draft",
                        "Pending",
                        "Private",
                        "Publish"
                );
        uploadConfig.setItems(options);
        //Default is Publish
        uploadConfig.getSelectionModel().select(3);

        //Set the PotTextField to a numeric TextField
        yearTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    yearTextField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });


        //Check if default Arguments are set and show them in the TextFields
        authorTextField.setText(config.getId3_artist());
        albumTextField.setText(config.getId3_album());
        yearTextField.setText(config.getId3_year());
        genreTextField.setText(config.getId3_genre());
        bitRateSlider.setValue(config.getMp3_bitrate());
        if (Objects.equals(config.getDecodeProperty(), "mono")) monoRadioBtn.setSelected(true);
        else if (Objects.equals(config.getDecodeProperty(), "stereo")) stereoRadioBtn.setSelected(true);
        uploadConfig.setValue(config.getUploadStatus());

    }

    /**
     * Loads the Post to PostQueue
     * @param event
     * @throws IOException
     */
    @FXML
    void acceptBtn(ActionEvent event) throws IOException {
        //Set the ID3 Tags to the Lame Object
        lame.setID3_Title(titleTextField.getText());
        lame.setID3_Album(albumTextField.getText());
        lame.setID3_Genre(genreTextField.getText());
        lame.setID3_Artist(authorTextField.getText());
        lame.setID3_Comment(commentTextArea.getText());
        lame.setID3_ReleaseYear(yearTextField.getText());
        lame.setWp_status(uploadConfig.getSelectionModel().getSelectedItem());
        lame.setWp_postTitle(wpTitleTextField.getText());
        lame.setBitrate(((int) bitRateSlider.getValue()));

        AudioMode audioMode = null;
        Toggle currentToggle = group.getSelectedToggle();
        if (audioMode == null) {
            if (currentToggle == monoRadioBtn) audioMode = AudioMode.MONO;
            else if (currentToggle == stereoRadioBtn) audioMode = AudioMode.JOINT;
        }

        ObservableList<String> errormsg = FXCollections.observableArrayList();
        if(wpTitleTextField.getText().isEmpty())errormsg.add("Blog title");
        if(group.getSelectedToggle() == null){
            if(errormsg.isEmpty())errormsg.add("Decode");
            else errormsg.add("/ Decode");
        }
        errormsg.toString().replace("[","").replace("]","");
        if (group.getSelectedToggle() == null||wpTitleTextField.getText().isEmpty()) {
            new ShowAlert("No"+errormsg+" type detected!", "No"+errormsg+" detected");
        } else {
            lame.setAudioMode(audioMode);
            if (lame != null) {
                controller.postList.add(lame);
                cancelBtn(event);
            }
        }

        player.stop();
        player.dispose();
    }

    /**
     * Cancel the Post
     * @param event
     */
    @FXML
    void cancelBtn(ActionEvent event) {
        // get a handle to the stage
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    /**
     * Selects the Media
     * @param event
     * @throws IOException
     * @throws TagException
     */
    @FXML
    void selectMediaBtn(ActionEvent event) throws IOException, TagException {
        if (player != null) player.stop();

        //Close pane, if already a file was Selected
        closeButtonPane();

        //Load a new File
        file = getFile();
        if (file != null) {
            //Create a temp media
            Path tmpFile = Files.createTempFile(file.getName(), ".mediaplayer.tmp");

            copyFileContents(file, tmpFile.toFile());

            if (!tmpFile.toFile().exists()) {
                Files.copy(file.toPath(), tmpFile);
            }
            //Create new Lame Object
            lame = new Lame(file.toURI().getPath());
            //Set the Title to the Name of the File
            titleTextField.setText(lame.getMP3File().getName());

            //Create a Media Object so the Data could be played
            media = new Media(tmpFile.toUri().toString());

            player = new MediaPlayer(media);
            mediaView = new MediaView(player);

            //Hide the Box while loading the File
            playerHbox.setVisible(false);

            mediaView.setSmooth(true); //Loads the Mediafile before starting the player -> no lags while playing

            try {
                loadButtonPane(player);
            } catch (IOException e) {
                e.printStackTrace();
            }

            anchorPane.getChildren().add(pane);
            double currentWidth = mediaVBox.getWidth();
            anchorPane.setMinWidth(110);                //Width of Buttons
            mediaVBox.setMaxWidth(currentWidth - anchorPane.getWidth());    //Minus width of Buttons

            mediaVBox.getChildren().addAll(mediaView);

            //If the Player is loaded
            player.setOnReady(new Runnable() {
                @Override
                public void run() {

                    slider.setMin(0.0);
                    slider.setMax(player.getTotalDuration().toSeconds());

                    //Set all visible
                    mediaView.setVisible(true);
                    slider.setVisible(true);

                    playerHbox.setVisible(true);
                }
            });

            //If the Slider is moved synchronise the Audio
            player.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                @Override
                public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration current) {
                    slider.setValue(current.toSeconds());
                }
            });

            //Synchronise the Slider with the Audio
            slider.valueProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    if (slider.getValue() != player.getCurrentTime().toSeconds())
                        player.seek(Duration.seconds(slider.getValue()));
                }
            });
        }

    }

    /**
     * Copy File Contents
     * @param source
     * @param destination
     * @throws IOException
     */
    private void copyFileContents(File source, File destination) throws IOException {
        FileChannel srcChannel = new FileInputStream(source).getChannel();

        // Create channel on the destination
        FileChannel dstChannel = new FileOutputStream(destination).getChannel();

        // Copy file contents from source to destination
        dstChannel.transferFrom(srcChannel, 0, srcChannel.size());

        // Close the channels
        srcChannel.close();
        dstChannel.close();
    }


    //New FileChooser
    private File getFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Media");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("MP3", "*.mp3")
        );

        File file = fileChooser.showOpenDialog(new Stage());
        return file;
    }

    //Loads a Pane for the Buttons
    void loadButtonPane(MediaPlayer player) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PathUtil.getResourcePath("Controller/mediaViewSettings.fxml"));
        pane = fxmlLoader.load();
        mediaViewSettings mediaViewSettings;
        mediaViewSettings = fxmlLoader.getController();
        mediaViewSettings.setController(this);
        mediaViewSettings.player = player;
    }

    //Removes the Buttons
    void closeButtonPane() {
        anchorPane.getChildren().remove(pane);
    }
}
