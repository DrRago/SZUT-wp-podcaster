package frontend.controller;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import lombok.Setter;

import java.io.File;

public class PodcastController {

    @Setter
    private MainController controller;

    @FXML
    private MediaView mv;

    @FXML
    private MediaPlayer mp;
    @FXML
    private Media me;

    public void init() {
        String path = new File("E:\\ Musik\\Download Musik\\Airplane Lyrics B.o.B ft. Hayley Williams @pharra @JskwLeeds.mp3").getAbsolutePath();
        me = new Media(new File(path).toURI().toString());
        mp = new MediaPlayer(me);
        mv.setMediaPlayer(mp);

        //TODO: Try to find out, why it throws a nullpointer
        DoubleProperty width = mv.fitWidthProperty();
        DoubleProperty height = mv.fitHeightProperty();

        width.bind(Bindings.selectDouble(mv.sceneProperty(), "width"));
        height.bind(Bindings.selectDouble(mv.sceneProperty(), "height"));
        mp.play();
    }


    @FXML
    public void btnCancel(ActionEvent event) {
        controller.closePodcast();
    }

    public void btnAccept(ActionEvent event) {
    }
}
