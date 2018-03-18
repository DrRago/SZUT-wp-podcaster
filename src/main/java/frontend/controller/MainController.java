package frontend.controller;

import backend.fileTransfer.Uploader;
import backend.fileTransfer.UploaderException;
import backend.fileTransfer.UploaderFactory;
import backend.wordpress.Blog;
import backend.wordpress.MyPost;
import config.Config;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Setter;
import net.bican.wordpress.exceptions.InsufficientRightsException;
import net.bican.wordpress.exceptions.ObjectNotFoundException;
import redstone.xmlrpc.XmlRpcFault;
import util.PathUtil;

import java.io.IOException;
import java.util.List;

public class MainController {

    @Setter
    private ServerLoginController serverLoginController;

    @Setter
    private WpLoginController wpLoginController;

    @FXML
    public TableView<MyPost> tableView;

    @FXML
    private VBox VBoxEdt;

    @FXML
    private AnchorPane EditPane;

    @FXML
    private Label statusTypeLabel;

    @FXML
    public HBox mainHbox;

    @FXML
    public ProgressBar statusbar;

    @FXML
    public static Button btnAddNew;

    @FXML
    TableColumn<MyPost, Integer> idColumn;

    @FXML
    TableColumn<MyPost, String> titleColumn;

    @FXML
    TableColumn<MyPost, String> authorColumn;

    private Region pane;
    private Blog blog;

    private Config config;

    // Initialize windows for easy access
    public static Stage stagePodcast = new Stage();
    public static Stage stageSettings = new Stage();
    private Stage stageHelp = new Stage();

    public static Uploader uploader;

    private ObservableList tableData = FXCollections.observableArrayList();
    private static Stage stageLogin = new Stage();


    /**
     * Initialize the main window
     * Set a status bar for progress on upload
     * Load default images
     */
    public void initialize() throws Exception {
        loadPane();
        config = new Config();

        idColumn.setCellValueFactory(new PropertyValueFactory<>("postID"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("postTitle"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("postAuthor"));

        statusbar.setProgress(0.0);
/*        try {
            loadPane();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        statusTypeLabel.setVisible(false);
        statusbar.setVisible(false);

        stagePodcast.getIcons().add(new Image("icons/WP-Podcaster-Icon.png"));
        stageHelp.getIcons().add(new Image("icons/WP-Podcaster-Icon.png"));
        stageSettings.getIcons().add(new Image("icons/WP-Podcaster-Icon.png"));

        tableView.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

            }
        });

        uploader.isConnected();

        uploader = UploaderFactory.getUploader(config.getUploadProtocol(), config.getUploadServerUrl(), config.getUploadServerPort(), config.getUploadServerUsername(), config.getUploadServerPassword(), config.getUploadServerWorkingDir());
        blog = new Blog(config.getWordpressUsername(), config.getWordpressPassword(), config.getWordpressXmlrpcUrl(), uploader, config.getRemoteServerPath());

        refreshTableView();
    }

    @FXML
    private void refreshTableView() throws ObjectNotFoundException, XmlRpcFault, InsufficientRightsException {
        tableView.getItems().clear();
        List<MyPost> recentUploadedPosts = blog.getPosts();

        tableView.getItems().addAll(recentUploadedPosts);
    }

    public void openLogin() {
        FXMLLoader fxmlLoader = new FXMLLoader(PathUtil.getResourcePath("Controller/WpLogin.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stageLogin.setTitle("Login");
        stageLogin.setScene(new Scene(root));
        shutdown();
        stageLogin.show();
    }

    public static void shutdown() {
        try {
            uploader.disconnect();
        } catch (UploaderException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add a new entry through the menubar
     *
     * @param event
     */
    @FXML
    void menuNew(ActionEvent event) throws IOException {
        btnAddNew(event);
    }

    /**
     * Close the program through the menubar
     *
     * @param event
     */
    @FXML
    void menuClose(ActionEvent event) {
        System.exit(0);
    }

    /**
     * Remove an entry through the menubar
     *
     * @param event
     */
    @FXML
    void menuRemove(ActionEvent event) throws ObjectNotFoundException, XmlRpcFault, InsufficientRightsException {
        btnRemove(event);
    }

    /**
     * Access the Edit-Pane through the menubar
     * @param event
     */

    /**
     * Open a Help-Window
     *
     * @param event
     */
    @FXML
    void menuHelp(ActionEvent event) {
        new LoadFxml("Controller/Help.fxml", "Help", new Stage());
    }

    /**
     * Add a new entry through the button
     *
     * @param event
     */
    @FXML
    void btnAddNew(ActionEvent event) throws IOException {
        if(!pane.isVisible()) {
            openPane();
        }
    }

    /**
     * Remove an entry through the button
     *
     * @param event
     */
    @FXML
    void btnRemove(ActionEvent event) throws InsufficientRightsException, XmlRpcFault, ObjectNotFoundException {
        closePane();
        int deleteID = tableView.getSelectionModel().getSelectedItem().getPostID();
        try {
            if (blog.deletePost(deleteID)) {
                new ShowAlert(Alert.AlertType.INFORMATION, String.format("Post %d was successfully moved to bin. You can undo this action by logging in on wordpress", deleteID), "Success", "Deletion successful");
                tableView.getItems().remove(tableView.getSelectionModel().getFocusedIndex());
            } else {
                new ShowAlert(Alert.AlertType.ERROR, String.format("Post %d wasn't successfully moved to bin. The data might not exist anymore, please reload the table", deleteID), "Error", "Deletion failure");
            }
        } catch (ObjectNotFoundException e) {
            new ShowAlert(Alert.AlertType.ERROR, String.format("Post %d wasn't successfully moved to bin. The data might not exist anymore, please reload the table", deleteID), "Error", "Deletion failure");
        }
    }

    @FXML
    void logoutBtn(ActionEvent event){
            closeMainWindow();
            new LoadFxml("Controller/WpLogin.fxml","Login to WordPress", new Stage());
    }

    @FXML
    void changeUploadBtn(ActionEvent event){
        try {
            uploader.disconnect();
        } catch (UploaderException e) {
            e.printStackTrace();
        }
        closeMainWindow();
        new LoadFxml("Controller/ServerLogin.fxml","Change Server", new Stage());
    }

    void closeMainWindow(){
        Stage stage = (Stage)tableView.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void refreshTableView(ActionEvent event) {
    }

    public void updatePosts(List<MyPost> posts) {
        tableView.setItems((ObservableList) posts);
    }

    /**
     * Main method to open the information part for uploading
     *
     * @throws IOException
     */
    void loadPane() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(PathUtil.getResourcePath("Controller/MediaQueue.fxml"));
        pane = fxmlLoader.load();
        MediaQueue mediaQueueController = fxmlLoader.getController();
        mediaQueueController.setController(this);

        pane.setVisible(false);
    }

    /**
     * Main method to open the sidepane for editing
     */
    void openPane() {
        VBoxEdt.setMaxWidth(mainHbox.getMaxWidth());
        VBoxEdt.setMaxHeight(mainHbox.getMaxHeight());
        VBoxEdt.setMargin(mainHbox, new Insets(5, 5, 0, 0));
        VBoxEdt.getChildren().add(pane);
        pane.setVisible(true);
        statusbar.setProgress(0.5);
    }

    /**
     * Main method to close the sidepane
     */
    public void closePane() {
        VBoxEdt.setMargin(mainHbox, new Insets(0, 0, 0, 0));
        VBoxEdt.setMaxWidth(0);
        VBoxEdt.getChildren().remove(pane);
        if (pane != null) {
            pane.setVisible(false);
        }
    }

    /**
     * Close the podcast window
     */
    public void closePodcast() {
        stagePodcast.close();
    }

    /**
     * Close the settings window
     */
    public void closeOption() {
        stageSettings.close();
    }
}

