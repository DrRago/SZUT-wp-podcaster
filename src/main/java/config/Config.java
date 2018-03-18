package config;

import backend.fileTransfer.Protocols;
import com.sun.org.apache.xpath.internal.operations.Bool;
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
    private String id3_album;
    private int mp3_bitrate;
    private String decodeProperty;
    private String uploadStatus;

    private Boolean wpRemember;
    private Boolean serverRemember;

    private Boolean serverReLoggingIn;
    private Boolean wpReLoggingIn;



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

        id3_title = prop.getProperty("id3_title", "");
        id3_artist = prop.getProperty("id3_artist", "");
        id3_year = prop.getProperty("id3_year", "");
        id3_comment = prop.getProperty("id3_comment", "");
        id3_genre = prop.getProperty("id3_genre", "");
        id3_album = prop.getProperty("id3_album", "");
        decodeProperty = prop.getProperty("id3_decodeProperty", "");
        mp3_bitrate = Integer.parseInt(prop.getProperty("mp3_bitrate", "320"));
        uploadStatus = prop.getProperty("uploadStatus", "publish");

        wpRemember = Boolean.valueOf(prop.getProperty("wpRemember", "false"));
        serverRemember = Boolean.valueOf(prop.getProperty("serverRemember", "false"));

        serverReLoggingIn = Boolean.valueOf(prop.getProperty("serverReLoggingIn", "false"));
        wpReLoggingIn = Boolean.valueOf(prop.getProperty("wpReLoggingIn", "false"));
        input.close();
    }

    /**
     * Set the Config Entry
     * @param key
     * @param value
     */
    private void setConfigEntry(String key, String value) {
        if (value.isEmpty()) {
            return;
        }
        try {
            // write an option to the config file
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
     * Sets the remember of WordPress Login
     *
     * @param wpRemember
     */
    public void setWpRemember(Boolean wpRemember){
        this.wpRemember = wpRemember;
        setConfigEntry("wpRemember", wpRemember.toString());
    }

    /**
     * Sets the remember of Server Login
     *
     * @param serverRemember
     */
    public void setServerRemember(Boolean serverRemember){
        this.serverRemember = serverRemember;
        setConfigEntry("serverRemember", serverRemember.toString());
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
     * Sets Album
     *
     * @param id3_album the album
     */
    public void setId3_album(String id3_album){
        this.id3_album = id3_album;
        setConfigEntry("id3_album", id3_album);
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
     * Set decode
     *
     * @param decodeProperty the decode Type
     */
    public void setDecodeProperty(String decodeProperty){
        this.decodeProperty = decodeProperty;
        setConfigEntry("id3_decodeProperty", decodeProperty);
    }

    /**
     * Set Upload Status
     *
     * @param uploadStatus
     */
    public void setUploadStatus(String uploadStatus){
        this.uploadStatus = uploadStatus;
        setConfigEntry("uploadStatus", uploadStatus);
    }

    /**
     * Set if the User is Logging Out or is starting the Program
     *
     * @param wpReLoggingIn
     */
    public void setWpReLoggingIn(Boolean wpReLoggingIn){
        this.wpReLoggingIn = wpReLoggingIn;
        setConfigEntry("wpReLoggingIn", Boolean.toString(wpReLoggingIn));
    }

    /**
     * Set if the User is Logging Out or is starting the Program
     *
     * @param serverReLoggingIn
     */
    public void setServerReLoggingIn(Boolean serverReLoggingIn){
        this.serverReLoggingIn = serverReLoggingIn;
        setConfigEntry("serverReLoggingIn", Boolean.toString(serverReLoggingIn));
    }


    /**
     * Deletes the config for the Server Login
     */
    public void deleteServerConfig() {
        setUploadServerPort(0);
        setUploadServerWorkingDir(" ");
        setUploadServerPassword(" ");
        setUploadServerURL(" ");
        setUploadProtocol(Protocols.FTPS);
        setUploadServerUsername(" ");
    }

    /**
     * Delets the config for the WordPress Login
     */
    public void deleteWorPressConfig(){
        setWordpressPassword(" ");
        setWordpressUsername(" ");
        setWordpressXmlrpcUrl(" ");
        setRemoteServerPath(" ");
    }
}