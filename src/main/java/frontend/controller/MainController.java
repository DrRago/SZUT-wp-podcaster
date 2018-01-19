package frontend.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
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
    public HBox mainHbox;

    @FXML
    private ProgressBar statusbar;

    private Region pane;

    private InformationController controller = null;

    public static Stage stagePodcast = new Stage();
    private Stage stageSettings = new Stage();
    private Stage stageHelp = new Stage();

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
        stagePodcast.getIcons().add(new Image("icons/WP-Podcaster-Icon.png"));
        stageHelp.getIcons().add(new Image("icons/WP-Podcaster-Icon.png"));
        stageSettings.getIcons().add(new Image("icons/WP-Podcaster-Icon.png"));
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
        stageHelp.setTitle("Help");
        stageHelp.setScene(new Scene(root));

        stageHelp.show();
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
        //TODO: WARUM WIRD DAS ROT MAKIERT???? XD
        controller.setController(this);
        controller.init();
        stagePodcast.setTitle("New Podcast");
        stagePodcast.setScene(new Scene(root));

        stagePodcast.show();
        stagePodcast.setMinHeight(425);
        stagePodcast.setMinWidth(600);
    }


    @FXML
    void btnEdit(ActionEvent event) {
        //if(tableView.getSelectionModel().getSelectedItems() != null && !pane.isVisible()) {
        if(!pane.isVisible())openPane();
                //TODO: Listing
        //}
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

    void loadPane() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(PathUtil.getResourcePath("Controller/Information.fxml"));
        pane = fxmlLoader.load();
        controller = fxmlLoader.getController();
        controller.setController(this);
        // Create new Anchor Pane for the SidePane

        //EditPane.setMaxWidth(pane.getMaxWidth());

        // Hide the new Anchor Pane for the SidePane
        //pane.setMinHeight(VBoxEdt.getMaxHeight());

        pane.setVisible(false);
    }

    void openPane() {
        VBoxEdt.setMaxWidth(mainHbox.getMaxWidth());
        VBoxEdt.setMaxHeight(mainHbox.getMaxHeight()); //??????????????
        VBoxEdt.setMargin(mainHbox, new Insets(5,5,0,0));
        VBoxEdt.getChildren().add(pane);
        pane.setVisible(true);
        statusbar.setProgress(0.5);
    }

    void closePane() {
        VBoxEdt.setMargin(mainHbox, new Insets(0,0,0,0));
        VBoxEdt.setMaxWidth(0);
        VBoxEdt.getChildren().remove(pane);
        pane.setVisible(false);
    }

    public void closePodcast() {
        stagePodcast.close();
    }

    public void closeOption() { stageSettings.close();
    }
}

