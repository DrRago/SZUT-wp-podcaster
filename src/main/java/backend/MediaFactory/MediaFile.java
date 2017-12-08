package backend.MediaFactory;

public interface MediaFile {
    String getID3_Title();
    String getID3_Artist();
    String getID3_ReleaseYear();
    String getID3_Comment();
    String getID3_Genre();
    String getID3_Album();

    void setID3_Title(String title);
    void setID3_Artist(String artist);
    void setID3_ReleaseYear(String releaseYear);
    void setID3_Comment(String comment);
    void setID3_Genre(String genre);
    void setID3_Album(String album);

    int getBitrate();
}
