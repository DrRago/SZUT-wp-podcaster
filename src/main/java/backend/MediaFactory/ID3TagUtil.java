package backend.MediaFactory;

import lombok.Getter;
import org.farng.mp3.MP3File;
import org.farng.mp3.TagException;
import org.farng.mp3.id3.AbstractID3v2;

import java.io.File;
import java.io.IOException;

/**
 * Utility class for ID3 tags.
 * Manages ID3 tags of a mp3 File
 */
public class ID3TagUtil {
    private MP3File mp3File;

    private AbstractID3v2 id3Tags;
    @Getter
    private File file;

    @Getter
    private String ID3_Title;
    @Getter
    private String ID3_Artist;
    @Getter
    private String ID3_Album;
    @Getter
    private String ID3_ReleaseYear;
    @Getter
    private String ID3_Comment;
    @Getter
    private String ID3_Genre;

    /**
     * Instantiates a new Id 3 tag util.
     *
     * @param path the path of the mp3 file
     * @throws IOException  thrown if a exception occurs during file reading
     * @throws TagException the tag exception
     */
    ID3TagUtil(String path) throws IOException, TagException {
        this.file = new File(path);
        this.mp3File = new MP3File(this.file);

        // parse information from mp3 header
        this.mp3File.seekMP3Frame();

        this.id3Tags = mp3File.getID3v2Tag();

        this.ID3_Title = this.id3Tags.getSongTitle();
        this.ID3_Artist = this.id3Tags.getLeadArtist();
        this.ID3_Album = this.id3Tags.getAlbumTitle();
        this.ID3_ReleaseYear = this.id3Tags.getYearReleased();
        this.ID3_Comment = this.id3Tags.getSongComment();
        this.ID3_Genre = this.id3Tags.getSongGenre();
    }

    /**
     * Sets the ID3 title.
     *
     * @param ID3_Title the title
     */
    public void setID3_Title(String ID3_Title) {
        this.id3Tags.setSongTitle(ID3_Title);
        this.ID3_Title = ID3_Title;
    }

    /**
     * Sets the ID3 artist.
     *
     * @param ID3_Artist the artist
     */
    public void setID3_Artist(String ID3_Artist) {
        this.id3Tags.setLeadArtist(ID3_Artist);
        this.ID3_Artist = ID3_Artist;
    }

    /**
     * Sets the ID3 release year.
     *
     * @param ID3_ReleaseYear the release year
     */
    public void setID3_ReleaseYear(String ID3_ReleaseYear) {
        this.id3Tags.setYearReleased(ID3_ReleaseYear);
        this.ID3_ReleaseYear = ID3_ReleaseYear;
    }

    /**
     * Sets the ID3 comment.
     *
     * @param ID3_Comment the comment
     */
    public void setID3_Comment(String ID3_Comment) {
        this.id3Tags.setSongComment(ID3_Comment);
        this.ID3_Comment = ID3_Comment;
    }

    /**
     * Sets the ID3 genre.
     *
     * @param ID3_Genre the genre
     */
    public void setID3_Genre(String ID3_Genre) {
        this.id3Tags.setSongGenre(ID3_Genre);
        this.ID3_Genre = ID3_Genre;
    }

    /**
     * Sets the ID3 album.
     *
     * @param ID3_Album the album
     */
    public void setID3_Album(String ID3_Album) {
        this.id3Tags.setAlbumTitle(ID3_Album);
        this.ID3_Album = ID3_Album;
    }

    /**
     * Gets the bitrate.
     *
     * @return the bitrate
     */
    public int getBitrate() {
        return mp3File.getBitRate();
    }
}
