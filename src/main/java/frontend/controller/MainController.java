package frontend.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import util.PathUtil;

import java.io.IOException;

public class MainController {

    @FXML
    public TableView tableView;

    @FXML
    private VBox VBoxEdt;

    @FXML
    private AnchorPane EditPane;

    @FXML
    private Label statusTypeLabel;

    @FXML
    private ProgressBar statusbar;

    private Pane pane;

    private InformationController controller = null;

    private Stage stage = new Stage();

    private ObservableList tableData = FXCollections.observableArrayList();

    public void initialize() {
        statusbar.setProgress(0.0);
        try {
            loadPane();
        } catch (IOException e) {
            e.printStackTrace();
        }
        statusTypeLabel.setVisible(false);
        statusbar.setVisible(false);
        stage.getIcons().add(new Image("icons/WP-Podcaster-Icon.png"));
    }

    @FXML
    void menuNew(ActionEvent event){
        btnAddNew(event);
    }

    @FXML
    void MenuSettings(ActionEvent event) {
        openSetting();
    }

    @FXML
    void MenuClose(ActionEvent event) {
        System.exit(0);
    }


    @FXML
    void menuRemove(ActionEvent event){btnRemove(event);}

    @FXML
    void menuEdit(ActionEvent event){btnEdit(event);}

    @FXML
    void menuHelp(ActionEvent event){
        FXMLLoader fxmlLoader = new FXMLLoader(PathUtil.getResourcePath("Controller/Help.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //HelpController controller = fxmlLoader.getController();
        stage.setTitle("New Podcast");
        stage.setScene(new Scene(root));

        stage.show();
    }

    @FXML
    void settingAction(ActionEvent event) {
        openSetting();
    }

    @FXML
    void btnAddNew(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(PathUtil.getResourcePath("Controller/Podcast.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        PodcastController controller = fxmlLoader.getController();
        controller.init();
        stage.setTitle("New Podcast");
        stage.setScene(new Scene(root));

        stage.show();

    }


    @FXML
    void btnEdit(ActionEvent event) {
        if(tableView.getSelectionModel().getSelectedItems() != null && !pane.isVisible()) {
                openPane();
                //TODO: Listing
        }
    }

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

    void openSetting() {
        FXMLLoader fxmlLoader = new FXMLLoader(PathUtil.getResourcePath("Controller/Settings.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SettingController controller = fxmlLoader.getController();
        controller.init();
        stage.setTitle("WP-Podcaster - Settings");
        stage.setScene(new Scene(root));
        stage.show();
    }

    void loadPane() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PathUtil.getResourcePath("Controller/Information.fxml"));
        AnchorPane box = fxmlLoader.load();
        controller = fxmlLoader.getController();
        // Create new Anchor Pane for the SidePane
        pane = new AnchorPane(box);
        // Hide the new Anchor Pane for the SidePane
        pane.setVisible(false);
    }

    void openPane() {
        EditPane.getChildren().add(pane);
        pane.setVisible(true);
        statusbar.setProgress(0.5);
    }

    void closePane() {
        EditPane.getChildren().remove(pane);
        pane.setVisible(false);
        statusbar.setProgress(1);
    }
}