package config;

import backend.fileTransfer.Protocols;
import lombok.Getter;

import java.io.*;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * The config file utility.
 */
@Getter
public class Config {
    private static final Logger LOGGER = Logger.getGlobal();

    private String configFilePath;
    private Properties prop = new Properties();

    private String uploadServerUrl;
    private int uploadServerPort;
    private Protocols uploadProtocol;
    private String uploadServerUsername;
    private String uploadServerPassword;
    private String uploadServerWorkingDir;


    private String wordpressXmlrpcUrl;
    private String wordpressUsername;
    private String wordpressPassword;

    private String remoteServerPath;

    private String id3_title;
    private String id3_artist;
    private String id3_year;
    private String id3_comment;
    private String id3_genre;
    private int mp3_bitrate;

    /**
     * Instantiates a new Config.
     *
     * @throws IOException thrown if an error with the file occurs (e.g. not found)
     */
    public Config() throws IOException {
        InputStream input;
        configFilePath = "config.properties";
        // load a properties file
        input = new FileInputStream(configFilePath);
        prop.load(input);

        // get the property values
        uploadServerUrl = prop.getProperty("serverURL", "localhost");
        uploadServerPort = Integer.parseInt(prop.getProperty("uploadServerPort", "990"));
        uploadProtocol = Protocols.valueOf(prop.getProperty("uploadProtocol", "FTPS"));
        uploadServerUsername = prop.getProperty("uploadServerUsername", "username");
        uploadServerPassword = prop.getProperty("uploadServerPassword", "password");
        uploadServerWorkingDir = prop.getProperty("uploadServerWorkingDir", "/uploads");

        wordpressXmlrpcUrl = prop.getProperty("wordpressXmlrpcUrl", "http://localhost/wordpress");
        wordpressUsername = prop.getProperty("wordpressUsername", "username");
        wordpressPassword = prop.getProperty("wordpressPassword", "password");

        remoteServerPath = prop.getProperty("remoteServerPath", "http://localhost/uploads");

        id3_title = prop.getProperty("id3_title", "title");
        id3_artist = prop.getProperty("id3_artist", "artist");
        id3_year = prop.getProperty("id3_year", "1337");
        id3_comment = prop.getProperty("id3_comment", "comment");
        id3_genre = prop.getProperty("id3_genre", "genre");
        mp3_bitrate = Integer.parseInt(prop.getProperty("mp3_bitrate", "320"));

        input.close();
    }

    private void setConfigEntry(String key, String value) {
        if (value.isEmpty()) {
            return;
        }
        try {
            OutputStream output = new FileOutputStream(configFilePath);
            prop.setProperty(key, value);
            prop.store(output, null);
            output.close();
        } catch (IOException e) {
            LOGGER.severe("Config not properly initialized");
        }
    }

    /**
     * Sets upload server username.
     *
     * @param uploadServerUsername the upload server username
     */
    public void setUploadServerUsername(String uploadServerUsername) {
        this.uploadServerUsername = uploadServerUsername;
        setConfigEntry("uploadServerUsername", uploadServerUsername);
    }

    /**
     * Sets upload server password.
     *
     * @param uploadServerPassword the upload server password
     */
    public void setUploadServerPassword(String uploadServerPassword) {
        this.uploadServerPassword = uploadServerPassword;
        setConfigEntry("uploadServerPassword", uploadServerPassword);
    }

    /**
     * Sets upload server port.
     *
     * @param uploadServerPort the upload server port
     */
    public void setUploadServerPort(int uploadServerPort) {
        this.uploadServerPort = uploadServerPort;
        setConfigEntry("uploadServerPort", String.valueOf(uploadServerPort));
    }

    /**
     * Sets wordpress xmlrpc url.
     *
     * @param wordpressXmlrpcUrl the wordpress xmlrpc url
     */
    public void setWordpressXmlrpcUrl(String wordpressXmlrpcUrl) {
        this.wordpressXmlrpcUrl = wordpressXmlrpcUrl;
        setConfigEntry("wordpressXmlrpcUrl", String.valueOf(wordpressXmlrpcUrl));
    }

    /**
     * Sets upload server url.
     *
     * @param uploadServerURL the server url
     */
    public void setUploadServerURL(String uploadServerURL) {
        this.uploadServerUrl = uploadServerURL;
        setConfigEntry("uploadServerURL", String.valueOf(uploadServerURL));
    }

    /**
     * Sets upload server working dir.
     *
     * @param uploadServerWorkingDir the upload server working dir
     */
    public void setUploadServerWorkingDir(String uploadServerWorkingDir) {
        this.uploadServerWorkingDir = uploadServerWorkingDir;
        setConfigEntry("uploadServerWorkingDir", uploadServerWorkingDir);
    }


    /**
     * Sets upload protocol.
     *
     * @param uploadProtocol the upload protocol
     */
    public void setUploadProtocol(Protocols uploadProtocol) {
        this.uploadProtocol = uploadProtocol;
        setConfigEntry("uploadProtocol", uploadProtocol.toString());
    }

    /**
     * Sets remote server path.
     *
     * @param remoteServerPath the remote server path
     */
    public void setRemoteServerPath(String remoteServerPath) {
        this.remoteServerPath = remoteServerPath;
        setConfigEntry("remoteServerPath", remoteServerPath);
    }

    /**
     * Sets wordpress username.
     *
     * @param wordpressUsername the wordpress username
     */
    public void setWordpressUsername(String wordpressUsername) {
        this.wordpressUsername = wordpressUsername;
        setConfigEntry("wordpressUsername", wordpressUsername);
    }

    /**
     * Sets wordpress password.
     *
     * @param wordpressPassword the wordpress password
     */
    public void setWordpressPassword(String wordpressPassword) {
        this.wordpressPassword = wordpressPassword;
        setConfigEntry("wordpressPassword", wordpressPassword);
    }

    /**
     * Sets bitrate.
     *
     * @param bitrate the bitrate
     */
    public void setBitrate(int bitrate) {
        this.mp3_bitrate = bitrate;
        setConfigEntry("mp3_bitrate", Integer.toString(bitrate));
    }

    /**
     * Sets id 3 title.
     *
     * @param id3_title the id 3 title
     */
    public void setId3_title(String id3_title) {
        this.id3_title = id3_title;
        setConfigEntry("id3_title", id3_title);
    }

    /**
     * Sets id 3 artist.
     *
     * @param id3_artist the id 3 artist
     */
    public void setId3_artist(String id3_artist) {
        this.id3_artist = id3_artist;
        setConfigEntry("id3_artist", id3_artist);
    }

    /**
     * Sets id 3 year.
     *
     * @param id3_year the id 3 year
     */
    public void setId3_year(String id3_year) {
        this.id3_year = id3_year;
        setConfigEntry("id3_year", id3_year);
    }

    /**
     * Sets id 3 comment.
     *
     * @param id3_comment the id 3 comment
     */
    public void setId3_comment(String id3_comment) {
        this.id3_comment = id3_comment;
        setConfigEntry("id3_comment", id3_comment);
    }

    /**
     * Sets id 3 genre.
     *
     * @param id3_genre the id 3 genre
     */
    public void setId3_genre(String id3_genre) {
        this.id3_genre = id3_genre;
        setConfigEntry("id3_genre", id3_genre);
    }

    /**
     * Save config.
     *
     * @param wordpressPassword      the wordpress password
     * @param uploadServerPassword   the upload server password
     * @param uploadServerUsername   the upload server username
     * @param wordpressUsername      the wordpress username
     * @param uploadServerWorkingDir the upload server working dir
     * @param uploadServerUrl        the upload server url
     * @param uploadProtocol         the upload protocol
     * @param wordpressURL           the wordpress url
     * @param mp3_bitrate            the mp3 bitrate
     * @param id3_title              the id3 title
     * @param id3_artist             the id3 artist
     * @param id3_year               the id3 year
     * @param id3_comment            the id3 comment
     * @param id3_genre              the id3 genre
     * @param remoteServerPath       the remote server path
     */
    public void saveConfig(String wordpressPassword, String uploadServerPassword, String uploadServerUsername,
                           String wordpressUsername, String uploadServerWorkingDir, String uploadServerUrl, Protocols uploadProtocol,
                           String wordpressURL, int mp3_bitrate, String id3_title, String id3_artist,
                           String id3_year, String id3_comment, String id3_genre, String remoteServerPath) {
        this.setWordpressPassword(wordpressPassword);
        this.setRemoteServerPath(remoteServerPath);
        this.setWordpressXmlrpcUrl(wordpressURL);
        this.setUploadServerURL(uploadServerUrl);
        this.setUploadServerPassword(uploadServerPassword);
        this.setUploadServerUsername(uploadServerUsername);
        this.setWordpressUsername(wordpressUsername);
        this.setUploadServerWorkingDir(uploadServerWorkingDir);
        this.setUploadProtocol(uploadProtocol);
        this.setWordpressXmlrpcUrl(wordpressURL);
        this.setBitrate(mp3_bitrate);
        this.setId3_title(id3_title);
        this.setId3_artist(id3_artist);
        this.setId3_year(id3_year);
        this.setId3_comment(id3_comment);
        this.setId3_genre(id3_genre);
    }
}