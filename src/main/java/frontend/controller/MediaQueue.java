package frontend.controller;


import backend.LameQueue.LameQueue;
import backend.MediaFactory.EncodingException;
import backend.MediaFactory.Lame;
import backend.fileTransfer.UploaderException;
import backend.wordpress.Blog;
import config.Config;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import net.bican.wordpress.exceptions.InsufficientRightsException;
import net.bican.wordpress.exceptions.InvalidArgumentsException;
import net.bican.wordpress.exceptions.ObjectNotFoundException;
import redstone.xmlrpc.XmlRpcException;
import redstone.xmlrpc.XmlRpcFault;
import util.PathUtil;

import java.io.IOException;

public class MediaQueue {

    @Getter
    @Setter
    private mediaViewSettings mediaViewSettings;

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

    private Service<Void> backgroundStartQueueThread;       //New service for Uploading Posts

    protected final ToggleGroup group = new ToggleGroup();
    public ObservableList<Lame> postList = FXCollections.observableArrayList();

    Config config;

    /**
     * Loads the MediaQueue Pane
     * @throws IOException
     */
    public void initialize() throws IOException {
        config=new Config();

        //ListView Lame to PostTitle Name
        postListView.setCellFactory(lv -> {
            TextFieldListCell<Lame> cell = new TextFieldListCell<>();
            cell.setConverter(new StringConverter<Lame>() {
                @Override
                public String toString(Lame object) {
                    return object.getWp_postTitle();
                }

                @Override
                public Lame fromString(String string) {
                    Lame lame = cell.getItem();
                    lame.setWp_postTitle(string);
                    return null;
                }
            });
            return cell;
        });

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

    /**
     * Loads new Scene and saves the Default args
     * @param event
     */
    @FXML
    void addNewPostBtn(ActionEvent event) {
        if(albumCheckBox.isSelected()||defaultCheckBox.isSelected())config.setId3_album(albumTextField.getText());
        else config.setId3_album(" ");
        if(authorCheckBox.isSelected()||defaultCheckBox.isSelected()){
            config.setId3_artist(authorTextField.getText());
            System.out.println("Hat einen Author");
        }
        else config.setId3_artist(" ");
        if(genreCheckBox.isSelected()||defaultCheckBox.isSelected())config.setId3_genre(genreTextField.getText());
        else config.setId3_genre(" ");
        if(bitrateCheckbox.isSelected()||defaultCheckBox.isSelected())config.setBitrate((int) bitrateSlider.getValue());
        else config.setBitrate(320);    //Default Value
        if(yearCheckBox.isSelected()||defaultCheckBox.isSelected())config.setId3_year(yearTextField.getText());
        else config.setId3_year(" ");
        if(decodeCheckBox.isSelected()||defaultCheckBox.isSelected()){
            if(monoRadioBtn.isSelected())config.setDecodeProperty("mono");
            else config.setDecodeProperty("stereo");
        }
        else config.setDecodeProperty("stereo");
        if(uploadCheckBox.isSelected()||defaultCheckBox.isSelected())config.setUploadStatus(uploadChoiceBox.getValue());
        else config.setUploadStatus("publish");


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

    /**
     * Closes the Pane
     * @param event
     */
    @FXML
    void closeBtn(ActionEvent event) {
        // get a handle to the stage
        controller.closePane();
    }

    /**
     * Starts the service and Uploads the Post to the Server
     * @param event
     * @throws Exception
     */
    @FXML
    void uploadPostsBtn(ActionEvent event) throws Exception {
        LameQueue lameQueue = new LameQueue(new Blog(config.getWordpressUsername(), config.getWordpressPassword(), config.getWordpressXmlrpcUrl(), controller.uploader, config.getRemoteServerPath()));
        for (int i = 0; i < postList.size(); i++) {
            lameQueue.add(postList.get(i));
            postList.remove(i);
        }

        controller.closePane();

        backgroundStartQueueThread = new Service<Void>() {

            @Override
            protected Task<Void> createTask() {

                return new Task<Void>() {

                    @Override
                    protected Void call() {
                        try {
                            lameQueue.startQueue();
                        } catch (Exception e) {
                            new ShowAlert(Alert.AlertType.ERROR,"Permission Denied. Check Configuration and then try again.","Permission Denied");
                        }
                        return null;
                    }
                };
            }
        };

        backgroundStartQueueThread.restart();
    }

    /**
     * Album CheckBox
     * @param event
     */
    @FXML
    void albumCheckBox(ActionEvent event) {
        if (!albumCheckBox.isSelected()) {
            albumTextField.setDisable(true);
        } else {
            albumTextField.setDisable(false);
        }
        changeDefaultCheckBox();
    }

    /**
     * Upload CheckBox
     * @param event
     */
    @FXML
    void uploadCheckBox(ActionEvent event) {
        if (!uploadCheckBox.isSelected()) {
            uploadChoiceBox.setDisable(true);
        } else {
            uploadChoiceBox.setDisable(false);
        }
        changeDefaultCheckBox();
    }

    /**
     * Author CheckBox
     * @param event
     */
    @FXML
    void authorCheckBox(ActionEvent event) {
        if (authorCheckBox.isSelected()) {
            authorTextField.setDisable(false);
        } else {
            authorTextField.setDisable(true);
        }
        changeDefaultCheckBox();
    }

    /**
     * Bitrate CheckBox
     * @param event
     */
    @FXML
    void bitrateCheckbox(ActionEvent event) {
        if (bitrateCheckbox.isSelected()) {
            bitrateSlider.setDisable(false);
        } else {
            bitrateSlider.setDisable(true);
        }
        changeDefaultCheckBox();
    }

    /**
     * Decode CheckBox
     * @param event
     */
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

    /**
     * Genre CheckBox
     *
     * @param event
     */
    @FXML
    void genreCheckBox(ActionEvent event) {
        if (genreCheckBox.isSelected()) {
            genreTextField.setDisable(false);
        } else {
            genreTextField.setDisable(true);
        }
        changeDefaultCheckBox();
    }

    /**
     * Year CheckBox
     *
     * @param event
     */
    @FXML
    void yearCheckBox(ActionEvent event) {
        if (yearCheckBox.isSelected()) {
            yearTextField.setDisable(false);
        } else {
            yearTextField.setDisable(true);
        }
        changeDefaultCheckBox();
    }

    /**
     * Default ChechBox to Select or Deselect every CheckBox
     * @param event
     */
    @FXML
    void defaultCheckBox(ActionEvent event) {
        if (defaultCheckBox.isSelected()) {
            changeDisabled(false);
        } else {
            changeDisabled(true);
        }
    }

    /**
     * Disable
     * @param b
     */
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

    /**
     * Check if every Box is checked or not
     */
    private void changeDefaultCheckBox() {
        if (!authorCheckBox.selectedProperty().getValue() || !albumCheckBox.selectedProperty().getValue() ||
                !yearCheckBox.selectedProperty().getValue() || !genreCheckBox.selectedProperty().getValue() ||
                !bitrateCheckbox.selectedProperty().getValue() || !decodeCheckBox.selectedProperty().getValue() ||
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

