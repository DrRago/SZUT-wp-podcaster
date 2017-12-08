package backend.MediaFactory;

import org.farng.mp3.MP3File;
import org.farng.mp3.TagException;
import org.farng.mp3.id3.AbstractID3v2;

import java.io.File;
import java.io.IOException;

public class PodcastMP3 implements MediaFile{
    private MP3File mp3File;
    private AbstractID3v2 id3Tags;

    private String ID3_Title;
    private String ID3_Artist;
    private String ID3_Album;
    private String ID3_ReleaseYear;
    private String ID3_Comment;
    private String ID3_Genre;

    private int bitrate;

    public PodcastMP3(String path) throws IOException, TagException {
        this.mp3File = new MP3File(new File(path));

        // parse information from mp3 header
        mp3File.seekMP3Frame();

        this.id3Tags = mp3File.getID3v2Tag();

        this.ID3_Title = this.id3Tags.getSongTitle();
        this.ID3_Artist = this.id3Tags.getLeadArtist();
        this.ID3_Album  = this.id3Tags.getAlbumTitle();
        this.ID3_ReleaseYear = this.id3Tags.getYearReleased();
        this.ID3_Comment = this.id3Tags.getSongComment();
        this.ID3_Genre = this.id3Tags.getSongComment();

        this.bitrate = mp3File.getBitRate();
    }

    private void updateTags() {
        try {
            mp3File.save();
        } catch (IOException | TagException e) {
            e.printStackTrace();
        }
    }

    public String getID3_Title() {
        return ID3_Title;
    }

    public void setID3_Title(String ID3_Title) {
        id3Tags.setSongTitle(ID3_Title);
        this.updateTags();
        this.ID3_Title = ID3_Title;
    }

    public String getID3_Artist() {
        return ID3_Artist;
    }

    public void setID3_Artist(String ID3_Artist) {
        this.id3Tags.setLeadArtist(ID3_Artist);
        this.updateTags();
        this.ID3_Artist = ID3_Artist;
    }

    public String getID3_ReleaseYear() {
        return ID3_ReleaseYear;
    }

    public void setID3_ReleaseYear(String ID3_ReleaseYear) {
        this.id3Tags.setYearReleased(ID3_ReleaseYear);
        this.updateTags();
        this.ID3_ReleaseYear = ID3_ReleaseYear;
    }

    public String getID3_Comment() {
        return ID3_Comment;
    }

    public void setID3_Comment(String ID3_Comment) {
        this.id3Tags.setSongComment(ID3_Comment);
        this.updateTags();
        this.ID3_Comment = ID3_Comment;
    }

    public String getID3_Genre() {
        return ID3_Genre;
    }

    public void setID3_Genre(String ID3_Genre) {
        this.id3Tags.setSongGenre(ID3_Genre);
        this.updateTags();
        this.ID3_Genre = ID3_Genre;
    }

    public int getBitrate() {
        return bitrate;
    }

    public String getID3_Album() {
        return ID3_Album;
    }

    public void setID3_Album(String ID3_Ablum) {
        this.id3Tags.setAlbumTitle(ID3_Ablum);
        this.updateTags();
        this.ID3_Album = ID3_Ablum;
    }
}
