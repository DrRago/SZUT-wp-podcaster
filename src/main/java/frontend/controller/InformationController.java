package frontend.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

public class InformationController {

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
    public ListView titleListView;

    @FXML
    void closeInformation(ActionEvent event) {
        controller.closePane();
    }

    public void initialize() {

        titleLabel.setStyle("-fx-font: 50px bold");
        titleLabel.setTextFill(Color.web("#00769D"));
        titleLabel.setText("Hey");
        titleLabel.setVisible(false);
        if(titleLabel.isVisible()) titleListView.setVisible(false);
        else titleListView.setVisible(true);
    }
}