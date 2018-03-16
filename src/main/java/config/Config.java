package config;

import backend.fileTransfer.Protocols;
import lombok.Getter;

import java.io.*;
import java.util.Properties;

@Getter
public class Config {

    private int port;
    private String workingDir;
    private Protocols protocol;
    private String wordpressUsername;
    private String wordpressPassword;

    private String serverUsername;
    private String serverPassword;

    private String id3_title;
    private String id3_artist;
    private String id3_year;
    private String id3_comment;
    private String id3_genre;
    private int mp3_bitrate;

    private String remotePath;

    private String wordpressURL;
    private String serverUrl;

    private String configPath;
    private Properties prop = new Properties();

    public Config() {
        InputStream input;
        configPath = "config.properties";
        try {
            // load a properties file
            input = new FileInputStream(configPath);
            prop.load(input);

            // get the property value and print it outpublic void setServerURL(String text) {
            port = Integer.parseInt(prop.getProperty("port", "990"));
            workingDir = prop.getProperty("workingDir", "/uploads");
            protocol = Protocols.valueOf(prop.getProperty("protocol", "FTPS"));
            wordpressUsername = prop.getProperty("wordpressUsername", "username");
            wordpressPassword = prop.getProperty("wordpressPassword", "password");

            serverUsername = prop.getProperty("serverUsername", "username");
            serverPassword = prop.getProperty("serverPassword", "password");
            id3_title = prop.getProperty("id3_title", "title");
            id3_artist = prop.getProperty("id3_artist", "artist");
            id3_year = prop.getProperty("id3_year", "1337");
            id3_comment = prop.getProperty("id3_comment", "comment");
            id3_genre = prop.getProperty("id3_genre", "genre");
            mp3_bitrate = Integer.parseInt(prop.getProperty("mp3_bitrate", "320"));

            wordpressURL = prop.getProperty("wordpressURL", "http://localhost/wordpress");
            serverUrl = prop.getProperty("serverURL", "localhost");

            remotePath = prop.getProperty("remotePath", "http://localhost/uploads");
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setConfigEntry(String key, String value) {
        if (value.isEmpty()) {
            return;
        }
        try {
            OutputStream output = new FileOutputStream(configPath);
            prop.setProperty(key, value);
            prop.store(output, null);
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setServerUsername(String serverUsername) {
        this.serverUsername = serverUsername;
        setConfigEntry("serverUsername", serverUsername);
    }

    public void setServerPassword(String serverPassword) {
        this.serverPassword = serverPassword;
        setConfigEntry("serverPassword", serverPassword);
    }


    public void setPort(int port) {
        this.port = port;
        setConfigEntry("port", String.valueOf(port));
    }

    public void setWordpressURL(String wordpressURL) {
        this.wordpressURL = wordpressURL;
        setConfigEntry("wordpressURL", String.valueOf(wordpressURL));
    }

    public void setServerURL(String serverURL) {
        this.serverUrl = serverURL;
        setConfigEntry("serverURL", String.valueOf(serverURL));
    }

    public void setWorkingDir(String workingDir) {
        this.workingDir = workingDir;
        setConfigEntry("workingDir", workingDir);
    }


    public void setProtocol(Protocols protocol) {
        this.protocol = protocol;
        setConfigEntry("protocol", protocol.toString());
    }

    public void setRemotePath(String remotePath) {
        this.remotePath = remotePath;
    }

    public void setWordpressUsername(String wordpressUsername) {
        this.wordpressUsername = wordpressUsername;
        setConfigEntry("wordpressUsername", wordpressUsername);
    }

    public void setWordpressPassword(String wordpressPassword) {
        this.wordpressPassword = wordpressPassword;
        setConfigEntry("wordpressPassword", wordpressPassword);
    }

    public void setBitrate(int bitrate) {
        this.mp3_bitrate = bitrate;
        setConfigEntry("mp3_bitrate", Integer.toString(bitrate));
    }

    public void setId3_title(String id3_title) {
        this.id3_title = id3_title;
        setConfigEntry("id3_title", id3_title);
    }

    public void setId3_artist(String id3_artist) {
        this.id3_artist = id3_artist;
        setConfigEntry("id3_artist", id3_artist);
    }

    public void setId3_year(String id3_year) {
        this.id3_year = id3_year;
        setConfigEntry("id3_year", id3_year);
    }

    public void setId3_comment(String id3_comment) {
        this.id3_comment = id3_comment;
        setConfigEntry("id3_comment", id3_comment);
    }

    public void setId3_genre(String id3_genre) {
        this.id3_genre = id3_genre;
        setConfigEntry("id3_genre", id3_genre);
    }

    public void saveConfig(String wordpressPassword, String serverPassword, String serverUsername,
                           String wordpressUsername, String workingDir, String serverUrl, Protocols protocol,
                           String wordpressURL, int mp3_bitrate, String id3_title, String id3_artist,
                           String id3_year, String id3_comment, String id3_genre, String remotePath) {
        this.setWordpressPassword(wordpressPassword);
        this.setRemotePath(remotePath);
        this.setWordpressURL(wordpressURL);
        this.setServerURL(serverUrl);
        this.setServerPassword(serverPassword);
        this.setServerUsername(serverUsername);
        this.setWordpressUsername(wordpressUsername);
        this.setWorkingDir(workingDir);
        this.setProtocol(protocol);
        this.setWordpressURL(wordpressURL);
        this.setBitrate(mp3_bitrate);
        this.setId3_title(id3_title);
        this.setId3_artist(id3_artist);
        this.setId3_year(id3_year);
        this.setId3_comment(id3_comment);
        this.setId3_genre(id3_genre);
    }
}