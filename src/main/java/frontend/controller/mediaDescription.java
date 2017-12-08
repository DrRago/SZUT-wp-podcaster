package frontend.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.List;

public class mediaDescription {

    @FXML
    private TextArea textAreaDescrption;

    @FXML
    private TextField textFieldAuthor;

    @FXML
    private AnchorPane mediaDesPane;

    FileChooser fileChooser = new FileChooser();

    @Getter
    @Setter
    private  PodcastController controller;

    @FXML
    public Media btnNewMedia(ActionEvent event) {
        configureFileChooser(fileChooser);
        List<File> list = fileChooser.showOpenMultipleDialog(new Stage());
        if (list != null) {
            for (File file : list) {
                Media me = new Media(file.toURI().toString());
                controller.openMedia(me);
            }
            if(controller.rootPane.isVisible()){
                //mediaDesPane.setPrefWidth(controller.pane.getWidth());


            }
        }
        return null;
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

    public void init() {
        //controller.rootPane.maxWidthProperty().bind(textAreaDescrption.maxWidthProperty());
        MainController.stagePodcast.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                mediaDesPane.setMaxWidth(mediaDesPane.getWidth()+(newValue.doubleValue()-oldValue.doubleValue()));
            }
        });
        MainController.stagePodcast.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

            }
        });
    }
}
