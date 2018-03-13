package frontend.controller.testing;

import backend.MediaFactory.Lame;
import config.Config;
import frontend.controller.PodcastController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.media.Media;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.farng.mp3.TagException;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class UploadeDecode {

    @FXML
    private Button addMediaBtn;

    @FXML
    private MediaView mediaView;

    @FXML
    private Label songTitleLabel;

    @FXML
    private ListView<Lame> mediaList;

    @FXML
    private TextField titleTextField;

    @FXML
    private RadioButton monoRadioBtn;

    @FXML
    private RadioButton stereoRadioBtn;

    @FXML
    private ComboBox<String> uploadConfig;

    @FXML
    private TextField authorTextField;

    @FXML
    private TextField albumTextField;

    @FXML
    private TextField yearTextField;

    @FXML
    private TextField genreTextField;

    @FXML
    private TextArea commentTextArea;

    @FXML
    private Button acceptBtn;

    @FXML
    private Button cancelBtn;

    private Config config = new Config();

    private ObservableList<Lame> lameItems = FXCollections.observableArrayList();

    public void initialize(){
        final ToggleGroup group = new ToggleGroup();
        monoRadioBtn.setToggleGroup(group);
        stereoRadioBtn.setToggleGroup(group);

        ObservableList<String> options =
                FXCollections.observableArrayList(
                        "Draft",
                        "Pending",
                        "Private",
                        "Publish"
                );
        uploadConfig.setItems(options);
        uploadConfig.getSelectionModel().select(3);

        mediaList.setCellFactory(lv -> {
            TextFieldListCell<Lame> cell = new TextFieldListCell<>();
            cell.setConverter(new ListViewConverter(cell));
            return cell ;
        });

        mediaList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Lame>() {
            public void changed(ObservableValue<? extends Lame> observable, Lame oldValue, Lame newValue) {

            }
        });
    }

    @FXML
    void acceptBtn(ActionEvent event) {
        if(monoRadioBtn.isSelected());
        else if(stereoRadioBtn.isSelected());
        else{
            Alert radioAlert = new Alert(Alert.AlertType.INFORMATION, "No decode type detected\nPlease select a decode Type!");
            radioAlert.setTitle("No decode type detected");
            radioAlert.showAndWait();
        }

    }

    @FXML
    void addMediaBtn(ActionEvent event) throws IOException, TagException {
        List<File> list = configureFileChooser();

        if (list != null) {
            //TODO: remove already existing data in list
            for (File file : list) {
                Lame lame = new Lame(file.toURI().getPath());
                try {
                    //lame.executeCommand();
                    if(!lameItems.contains(lame)) lameItems.addAll(lame);
                }catch (Exception e){
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR, ""+e.getMessage().split(": ")[1]);
                    errorAlert.setTitle("Error");
                    errorAlert.showAndWait();
                }
            }

        }

        for(int i = 0; i<lameItems.size(); i++){
            mediaList.getItems().add(lameItems.get(i));
            String songTitle = mediaList.getItems().get(i).getID3_Title();
        }
    }

    @FXML
    void cancelBtn(ActionEvent event) {

    }

    //Convert the Object name to the Title of the Media
    public static class ListViewConverter extends StringConverter<Lame> {
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

    private static List<File> configureFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("New Media");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );

        fileChooser.getExtensionFilters().addAll(
                //new FileChooser.ExtensionFilter("All Media", "*.mp3"),
                new FileChooser.ExtensionFilter("MP3", "*.mp3")
        );

        return fileChooser.showOpenMultipleDialog(new Stage());
    }
}
