package frontend.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;
import util.PathUtil;

import java.io.*;
import java.nio.file.Path;

public class HelpController {

    @FXML
    private TextArea text;

    public void initialize() throws IOException {

        // Load the helpfile
        File file = new File(PathUtil.getResourcePath("HelpText.txt").getPath());

        // Display the content of the helpfile
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