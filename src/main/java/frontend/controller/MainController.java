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
import javafx.scene.layout.*;
import javafx.stage.Stage;
import lombok.Setter;
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
    private ProgressBar statusbar;

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

    private Config config = new Config();

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

        uploader = UploaderFactory.getUploader(config.getProtocol(), config.getServerUrl(), config.getPort(), config.getServerUsername(), config.getServerPassword(), config.getWorkingDir());
        blog = new Blog(config.getWordpressUsername(),config.getWordpressPassword(),config.getWordpressURL(), uploader, config.getRemotePath());
        List<MyPost> recentUploadedPosts = blog.getPosts();

        tableView.getItems().addAll(recentUploadedPosts);
    }

    public void openLogin(){
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
     * @param event
     */
    @FXML
    void menuNew(ActionEvent event){
        btnAddNew(event);
    }

    /**
     * Access the Settings-Window through the menubar
     * @param event
     */
    @FXML
    void menuSettings(ActionEvent event) {
        openSetting();
    }

    /**
     * Close the program through the menubar
     * @param event
     */
    @FXML
    void menuClose(ActionEvent event) {
        System.exit(0);
    }

    /**
     * Remove an entry through the menubar
     * @param event
     */
    @FXML
    void menuRemove(ActionEvent event){btnRemove(event);}

    /**
     * Access the Edit-Pane through the menubar
     * @param event
     */
    @FXML
    void menuEdit(MouseEvent event){btnEdit(event);}

    /**
     * Open a Help-Window
     * @param event
     */
    @FXML
    void menuHelp(ActionEvent event){
        new LoadFxml("Controller/Help.fxml", "Help", new Stage());
    }

    /**
     * Open the settings window through the button
     * @param event
     */
    @FXML
    void settingAction(ActionEvent event) {
        openSetting();
    }

    /**
     * Add a new entry through the button
     * @param event
     */
    @FXML
    void btnAddNew(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(PathUtil.getResourcePath("Controller/MediaQueue.fxml"));
        MediaQueue mediaQueueController = null;
        try {
            pane = fxmlLoader.load();
            mediaQueueController = fxmlLoader.getController();
            mediaQueueController.setController(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        VBoxEdt.setMaxWidth(mainHbox.getMaxWidth());
        VBoxEdt.setMaxHeight(mainHbox.getMaxHeight());
        VBoxEdt.setMargin(mainHbox, new Insets(5,5,0,0));
        VBoxEdt.getChildren().add(pane);
        pane.setVisible(true);
    }

    /**
     * Edit an entry through the button
     * @param event
     */
    @FXML
    void btnEdit(MouseEvent event) {
        //if(tableView.getSelectionModel().getSelectedItems() != null && !pane.isVisible()) {
        if(!pane.isVisible())openPane();
                //TODO: Listing
        //}
    }

    /**
     * Remove an entry through the button
     * @param event
     */
    @FXML
    void btnRemove(ActionEvent event) {
        closePane();

        /* Get selected listings
        TODO: Do Listing Service
        final ObservableList<> items = tableView.getSelectionModel().getSelectedItems();
        for (Aufgelistete : items) {

        }
        tableData.removeAll(items)
        */

    }


    public void updatePosts(List<MyPost> posts){
        tableView.setItems((ObservableList) posts);
    }


    /**
     * Main method to initialize and open the settings window
     */
    void openSetting() {
        FXMLLoader fxmlLoader = new FXMLLoader(PathUtil.getResourcePath("Controller/Option.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        OptionController controller = fxmlLoader.getController();
        controller.setController(this);
        stageSettings.setTitle("WP-Podcaster - Option");
        stageSettings.setScene(new Scene(root));
        stageSettings.show();
    }

    /**
     * Main method to open the information part for uploading
     * @throws IOException
     */
    void loadPane() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(PathUtil.getResourcePath("Controller/MediaQueue.fxml"));
        pane = fxmlLoader.load();
        // Create new Anchor Pane for the SidePane

        //EditPane.setMaxWidth(pane.getMaxWidth());

        // Hide the new Anchor Pane for the SidePane
        //pane.setMinHeight(VBoxEdt.getMaxHeight());

        pane.setVisible(false);
    }

    /**
     * Main method to open the sidepane for editing
     */
    void openPane() {
        VBoxEdt.setMaxWidth(mainHbox.getMaxWidth());
        VBoxEdt.setMaxHeight(mainHbox.getMaxHeight());
        VBoxEdt.setMargin(mainHbox, new Insets(5,5,0,0));
        VBoxEdt.getChildren().add(pane);
        pane.setVisible(true);
        statusbar.setProgress(0.5);
    }

    /**
     * Main method to close the sidepane
     */
    public void closePane() {
        VBoxEdt.setMargin(mainHbox, new Insets(0,0,0,0));
        VBoxEdt.setMaxWidth(0);
        VBoxEdt.getChildren().remove(pane);
        pane.setVisible(false);
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
    public void closeOption() { stageSettings.close();
    }
}

