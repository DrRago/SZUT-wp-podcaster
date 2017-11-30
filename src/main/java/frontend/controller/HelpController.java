package frontend.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;

import java.io.*;


public class HelpController {

    @FXML
    private AnchorPane mainPane;

    @FXML
    private Label title;

    @FXML
    private TextArea text;

    @FXML
    private WebView web;

    public void initialize() throws IOException {
        title.setStyle("-fx-font: 30px bold");
        title.setTextFill(Color.web("#00769D"));
        title.setText("About WordPress - Podcaster");

        File file = new File("res/HelpText.txt");


        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                text.appendText(line+"\n");
            }
            text.selectHome();
            text.deselect();
        }

        text.setEditable(false);

    }
}