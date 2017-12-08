import backend.MediaFactory.MediaFactory;
import backend.MediaFactory.MediaFile;
import config.Config;
import org.farng.mp3.TagException;

import java.io.IOException;

/**
 * @author Leonhard Gahr
 * @author Pascal de Vries
 */
public class run {
    public static void main(String[] args) throws IOException, TagException {
        MediaFile mp3 = MediaFactory.getMedia("mp3", "C:\\Users\\Leonhard.Gahr\\Downloads\\SFGrenade - Dual Wield.mp3");
        assert mp3 != null;
        mp3.setID3_Title("New Title");
        mp3.setID3_Artist("New Artist");
        mp3.setID3_Comment("New Comment");
        mp3.setID3_Genre("New Genre");
        mp3.setID3_ReleaseYear("2017");
        mp3.setID3_Album("New Album");
        new Config().setPassword("1244254224");
    }
}
