package frontend.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class mediaDescription {

    @FXML
    private TextArea textAreaDescrption;

    @FXML
    private TextField textFieldAuthor;

    @FXML
    private AnchorPane mediaDesPane;

    @FXML
    public HBox hBoxListView;

    FileChooser fileChooser = new FileChooser();

    @Getter
    @Setter
    private PodcastController controller;

    public int countingMediaUploads = 0;
    public int contingMediaFiles = 0;

    private ObservableList<String> mediaName = FXCollections.observableArrayList();
    private ObservableList<Media> mediaItems = FXCollections.observableArrayList();

    public void initialize() {
    }

    @FXML
    public Media btnNewMedia(ActionEvent event) {

        configureFileChooser(fileChooser);

        List<File> list = fileChooser.showOpenMultipleDialog(new Stage());

        if (list != null) {

            for (File file : list) {
                contingMediaFiles += 1;
                Media me = new Media(file.toURI().toString());

                if (!mediaItems.contains(me)) mediaItems.add(me);

                String source = me.getSource();
                source = source.substring(0, source.lastIndexOf("."));
                source = source.substring(source.lastIndexOf("/") + 1).replaceAll("%20", " ");

                if (!mediaName.contains(source)) mediaName.add(source);


            }

            countingMediaUploads += 1;

            controller.showMediaList(mediaItems, mediaName);
        }
        return null;
    }

    private static void configureFileChooser(final FileChooser fileChooser) {
        fileChooser.setTitle("New Media");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Media", "*.mp3", "*.mp4"),
                new FileChooser.ExtensionFilter("MP3", "*.mp3"),
                new FileChooser.ExtensionFilter("MP4", "*.mp4")
        );
    }
}