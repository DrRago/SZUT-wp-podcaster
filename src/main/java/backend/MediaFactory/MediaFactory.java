package backend.MediaFactory;

import org.farng.mp3.TagException;

import java.io.IOException;

public class MediaFactory {
    public static MediaFile getMedia(String type, String path) throws IOException, TagException {
        switch (type) {
            case "mp3":
                return new PodcastMP3(path);
            case "mp4":
                return null;
            default:
                throw new IllegalArgumentException(String.format("type '%s' not found", type));
        }
    }
}
