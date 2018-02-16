package frontend.controller;

import backend.MediaFactory.Lame;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.farng.mp3.TagException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class mediaDescription {

    @FXML
    private TextArea textAreaDescrption;
    @FXML
    private TextField textFieldAuthor;
    @FXML
    public TextField textFieldTitle;
    @FXML
    private TextField textFieldAlbum;
    @FXML
    private AnchorPane mediaDesPane;
    @FXML
    public HBox hBoxListView;
    @FXML
    public ComboBox pendingState;


    @Getter
    @Setter
    private PodcastController controller;

    public int countingMediaUploads = 0;
    public int contingMediaFiles = 0;

    public ObservableList<Lame> lameItems = FXCollections.observableArrayList();

    public void initialize() {
        ObservableList<String> options =
                FXCollections.observableArrayList(
                        "Draft",
                        "Pending",
                        "Private",
                        "Publish"
                );
        pendingState.setItems(options);
        pendingState.getSelectionModel().select(3);
    }

    @FXML
    public Media btnNewMedia(ActionEvent event) throws Exception {
        //TODO: Multiple Blog Post with always one Media

        List<File> list = configureFileChooser();

        if (list != null) {
            for (File file : list) {
                Lame lame = new Lame(file.toURI().getPath());
                lame.executeCommand();
                lameItems.addAll(lame);
            }
            controller.showMediaList(lameItems);
        }

        return null;
    }

    private static List<File> configureFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("New Media");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Media", "*.mp3"),
                new FileChooser.ExtensionFilter("MP3", "*.mp3")
        );

        return fileChooser.showOpenMultipleDialog(new Stage());
    }
}