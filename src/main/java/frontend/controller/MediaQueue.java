package frontend.controller;


import backend.LameQueue.LameQueue;
import backend.MediaFactory.Lame;
import backend.wordpress.Blog;
import config.Config;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import lombok.Getter;
import lombok.Setter;
import util.PathUtil;

import java.io.IOException;

public class MediaQueue {

    @Setter
    private MainController controller;

    @FXML
    protected ListView<Lame> postListView = new ListView<>();

    @FXML
    protected CheckBox defaultCheckBox;

    @FXML
    protected CheckBox authorCheckBox;

    @FXML
    protected TextField authorTextField;

    @FXML
    protected CheckBox albumCheckBox;

    @FXML
    protected TextField albumTextField;

    @FXML
    protected CheckBox yearCheckBox;

    @FXML
    protected TextField yearTextField;

    @FXML
    protected CheckBox genreCheckBox;

    @FXML
    protected TextField genreTextField;

    @FXML
    protected CheckBox bitrateCheckbox;

    @FXML
    protected Slider bitrateSlider;

    @FXML
    protected CheckBox decodeCheckBox;

    @FXML
    protected RadioButton monoRadioBtn;

    @FXML
    private Label bitrateLabel;

    @FXML
    protected RadioButton stereoRadioBtn;

    @FXML
    protected CheckBox uploadCheckBox;

    @FXML
    protected ChoiceBox<String> uploadChoiceBox;

    @Getter
    @Setter
    private mediaViewSettings mediaViewSettings;

    protected final ToggleGroup group = new ToggleGroup();
    public ObservableList<Lame> postList = FXCollections.observableArrayList();

    Config config;

    public void initialize() {
        postListView.setCellFactory(lv -> {
            TextFieldListCell<Lame> cell = new TextFieldListCell<>();
            cell.setConverter(new StringConverter<Lame>() {
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
            });
            return cell ;
        });

        ObservableList<String> options =
                FXCollections.observableArrayList(
                        "Draft",
                        "Pending",
                        "Private",
                        "Publish"
                );
        uploadChoiceBox.setItems(options);
        uploadChoiceBox.getSelectionModel().select(3);

        authorTextField.setDisable(true);
        albumTextField.setDisable(true);
        yearTextField.setDisable(true);
        genreTextField.setDisable(true);
        bitrateSlider.setDisable(true);
        monoRadioBtn.setDisable(true);
        stereoRadioBtn.setDisable(true);
        uploadChoiceBox.setDisable(true);

        // Add a listener to change the label after a value change
        bitrateSlider.valueProperty().addListener(((observable, oldValue, newValue) -> {
            bitrateLabel.setText(Double.toString(Math.round(bitrateSlider.getValue())) + " kBit/s");
        }));

        monoRadioBtn.setToggleGroup(group);
        stereoRadioBtn.setToggleGroup(group);

        postList.addListener(new ListChangeListener<Lame>() {
            @Override
            public void onChanged(Change<? extends Lame> c) {
                postListView.setItems(postList);
            }
        });
    }

    /*
     * Loads new Scene
     */
    @FXML
    void addNewPostBtn(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(PathUtil.getResourcePath("Controller/MediaToQueue.fxml"));
        Parent root = null;
        Stage stage = new Stage();
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        MediaToQueue controller = fxmlLoader.getController();
        controller.setController(this);
        stage.setTitle("Add Post to Queue");
        stage.setScene(new Scene(root));

        stage.show();
    }

    @FXML
    void closeBtn(ActionEvent event) {
        //TODO: CLOSE PANE
        // get a handle to the stage
        controller.closePane();
    }

    @FXML
    void uploadPostsBtn(ActionEvent event) throws Exception {

        //closeBtn(event);

        //TODO: Close Process for the Media
        config = new Config();
        LameQueue lameQueue = new LameQueue(new Blog(config.getWordpressUsername(),config.getWordpressPassword(),config.getWordpressXmlrpcUrl(), controller.uploader, config.getRemoteServerPath()));
        for(int i = 0; i<postList.size(); i++) {
            lameQueue.add(postList.get(i));
            postList.remove(i);
        }
        lameQueue.startQueue();
    }

    @FXML
    void albumCheckBox(ActionEvent event) {
        if (!albumCheckBox.isSelected()) {
            albumTextField.setDisable(true);
        } else {
            albumTextField.setDisable(false);
        }
        changeDefaultCheckBox();
    }

    @FXML
    void uploadCheckBox(ActionEvent event) {
        if (!uploadCheckBox.isSelected()) {
            uploadChoiceBox.setDisable(true);
        } else {
            uploadChoiceBox.setDisable(false);
        }
        changeDefaultCheckBox();
    }

    @FXML
    void authorCheckBox(ActionEvent event) {
        if (authorCheckBox.isSelected()) {
            authorTextField.setDisable(false);
        } else {
            authorTextField.setDisable(true);
        }
        changeDefaultCheckBox();
    }

    @FXML
    void bitrateCheckbox(ActionEvent event) {
        if (bitrateCheckbox.isSelected()) {
            bitrateSlider.setDisable(false);
        } else {
            bitrateSlider.setDisable(true);
        }
        changeDefaultCheckBox();
    }

    @FXML
    void decodeCheckBox(ActionEvent event) {
        if (decodeCheckBox.isSelected()) {
            monoRadioBtn.setDisable(false);
            stereoRadioBtn.setDisable(false);
        } else {
            monoRadioBtn.setDisable(true);
            stereoRadioBtn.setDisable(true);
        }
        changeDefaultCheckBox();
    }

    @FXML
    void genreCheckBox(ActionEvent event) {
        if (genreCheckBox.isSelected()) {
            genreTextField.setDisable(false);
        } else {
            genreTextField.setDisable(true);
        }
        changeDefaultCheckBox();
    }

    @FXML
    void yearCheckBox(ActionEvent event) {
        if (yearCheckBox.isSelected()) {
            yearTextField.setDisable(false);
        } else {
            yearTextField.setDisable(true);
        }
        changeDefaultCheckBox();
    }

    @FXML
    void defaultCheckBox(ActionEvent event) {
        if (defaultCheckBox.isSelected()) {
            changeDisabled(false);
        } else {
            changeDisabled(true);
        }
    }

    private void changeDisabled(Boolean b) {
        authorTextField.setDisable(b);
        albumTextField.setDisable(b);
        yearTextField.setDisable(b);
        genreTextField.setDisable(b);
        bitrateSlider.setDisable(b);
        monoRadioBtn.setDisable(b);
        stereoRadioBtn.setDisable(b);
        uploadChoiceBox.setDisable(b);
        authorCheckBox.selectedProperty().setValue(!b);
        albumCheckBox.selectedProperty().setValue(!b);
        yearCheckBox.selectedProperty().setValue(!b);
        genreCheckBox.selectedProperty().setValue(!b);
        bitrateCheckbox.selectedProperty().setValue(!b);
        decodeCheckBox.selectedProperty().setValue(!b);
        uploadCheckBox.selectedProperty().setValue(!b);
    }

    private void changeDefaultCheckBox() {
        if (!authorCheckBox.selectedProperty().getValue() || !albumCheckBox.selectedProperty().getValue() ||
                !yearCheckBox.selectedProperty().getValue() || !genreCheckBox.selectedProperty().getValue() ||
                !bitrateCheckbox.selectedProperty().getValue()|| !decodeCheckBox.selectedProperty().getValue()||
                !uploadCheckBox.selectedProperty().getValue()) {
            defaultCheckBox.selectedProperty().setValue(false);
        } else if (authorCheckBox.isSelected() && albumCheckBox.isSelected() &&
                yearCheckBox.isSelected() && genreCheckBox.isSelected() &&
                bitrateCheckbox.isSelected() && decodeCheckBox.isSelected() &&
                uploadCheckBox.isSelected()) {
            defaultCheckBox.selectedProperty().setValue(true);
        }
    }
}

