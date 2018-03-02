package frontend.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

public class InformationController {
    //Todo: Add dependency to backend

    @Getter
    @Setter
    private MainController controller;

    @FXML
    public Label titleLabel;
    @FXML
    public VBox vboxInfo;
    @FXML
    private VBox vboxList;
    @FXML
    public AnchorPane informationPane;

    @FXML
    private TextField titleTextField;
    @FXML
    private TextField authorTextField;
    @FXML
    private TextField albumTextField;
    @FXML
    private TextField yearTextField;
    @FXML
    private TextField genreTextField;
    @FXML
    private TextArea commentTextField;

    @FXML
    private ListView<?> titleListView;

    public void initialize() {
    }

    public void setTitle(String title){
        titleTextField.setText(title);
    }

    public void setAuthor(String author){
        titleTextField.setText(author);
    }

    public void setAlbum(String album){
        titleTextField.setText(album);
    }

    public void setYear(String year){
        titleTextField.setText(year);
    }

    public void setGenre(String genre){
        titleTextField.setText(genre);
    }

    public void setComment(String comment){
        titleTextField.setText(comment);
    }

    @FXML
    void closeInformation(ActionEvent event) {
        controller.closePane();
    }
}