package backend.LameQueue;

import backend.MediaFactory.Lame;
import backend.wordpress.Blog;

import java.util.ArrayList;

public class LameQueue extends ArrayList<Lame> {
    private Blog wordpress;

    public LameQueue(Blog wordpress) {
        this.wordpress = wordpress;
    }

    public void startQueue() throws Exception {
        for (Lame item : this) {
            item.executeCommand();
            wordpress.addPost(item);
        }
    }

    public void setTitle(final String title) {
        this.forEach(lame -> lame.setID3_Title(title));
    }

    public void setAlbum(final String album) {
        this.forEach(lame -> lame.setID3_Album(album));
    }

    public void setYear(final String year) {
        this.forEach(lame -> lame.setID3_ReleaseYear(year));
    }

    public void setGenre(final String genre) {
        this.forEach(lame -> lame.setID3_Genre(genre));
    }

    public void setArtist(final String artist) {
        this.forEach(lame -> lame.setID3_Artist(artist));
    }
}
