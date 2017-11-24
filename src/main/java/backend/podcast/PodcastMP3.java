package backend.podcast;

public class PodcastMP3 {
    private String path;
    private String ID3_Title;
    private String ID3_Artist;
    private String ID3_ReleaseYear;
    private String ID3_Comment;
    private String ID3_Genre;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getID3_Title() {
        return ID3_Title;
    }

    public void setID3_Title(String ID3_Title) {
        this.ID3_Title = ID3_Title;
    }

    public String getID3_Artist() {
        return ID3_Artist;
    }

    public void setID3_Artist(String ID3_Artist) {
        this.ID3_Artist = ID3_Artist;
    }

    public String getID3_ReleaseYear() {
        return ID3_ReleaseYear;
    }

    public void setID3_ReleaseYear(String ID3_ReleaseYear) {
        this.ID3_ReleaseYear = ID3_ReleaseYear;
    }

    public String getID3_Comment() {
        return ID3_Comment;
    }

    public void setID3_Comment(String ID3_Comment) {
        this.ID3_Comment = ID3_Comment;
    }

    public String getID3_Genre() {
        return ID3_Genre;
    }

    public void setID3_Genre(String ID3_Genre) {
        this.ID3_Genre = ID3_Genre;
    }
}
