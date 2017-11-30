import backend.podcast.PodcastMP3;
import org.farng.mp3.TagException;

import java.io.IOException;

/**
 * @author Leonhard Gahr
 * @author Pascal de Vries
 */
public class run {
    public static void main(String[] args) throws IOException, TagException {
        PodcastMP3 mp3 = new PodcastMP3("C:\\Users\\Leonhard.Gahr\\Downloads\\SFGrenade - Dual Wield.mp3");
        mp3.setID3_Title("New Title");
        mp3.setID3_Artist("New Artist");
        mp3.setID3_Comment("New Comment");
        mp3.setID3_Genre("New Genre");
        mp3.setID3_ReleaseYear("2017");
        mp3.setID3_Ablum("New Album");
    }
}
