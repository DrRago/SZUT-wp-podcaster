package config;

import backend.fileTransfer.Protocols;

import java.io.*;
import java.util.Properties;

public class Config {

    private String hostname;
    private int port;
    private String workingDir;
    private Protocols protocol;
    private String username;
    private String password;

    private String id3_title;
    private String id3_artist;
    private String id3_year;
    private String id3_comment;
    private String id3_genre;
    private int mp3_bitrate;

    private String wordpressURL;

    private String configPath;
    private Properties prop = new Properties();

    public Config() {
        InputStream input;
        configPath = "config.properties";
        try {
            // load a properties file
            input = new FileInputStream(configPath);
            prop.load(input);

            // get the property value and print it out
            hostname = prop.getProperty("hostname", "127.0.0.1");
            port = Integer.parseInt(prop.getProperty("port", "990"));
            workingDir = prop.getProperty("workingDir", "/");
            protocol = Protocols.valueOf(prop.getProperty("protocol", "FTPS"));
            username = prop.getProperty("username", "username");
            password = prop.getProperty("password", "password");

            id3_title = prop.getProperty("id3_title", "title");
            id3_artist = prop.getProperty("id3_artist", "artist");
            id3_year = prop.getProperty("id3_year", "1337");
            id3_comment = prop.getProperty("id3_comment", "comment");
            id3_genre = prop.getProperty("id3_genre", "genre");
            mp3_bitrate = Integer.parseInt(prop.getProperty("mp3_bitrate", "320"));

            wordpressURL = prop.getProperty("wordpressURL", "localhost/wordpress");

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

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
        setConfigEntry("hostname", hostname);
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
        setConfigEntry("port", String.valueOf(port));
    }

    public void setWordpressURL(String wordpressURL){
        this.wordpressURL = wordpressURL;
        setConfigEntry("wordpressURL", String.valueOf(wordpressURL));
    }

    public String getWordpressURL(){
        return wordpressURL;
    }

    public String getWorkingDir() {
        return workingDir;
    }

    public void setWorkingDir(String workingDir) {
        this.workingDir = workingDir;
        setConfigEntry("workingDir", workingDir);
    }

    public Protocols getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocols protocol) {
        this.protocol = protocol;
        setConfigEntry("protocol", protocol.toString());
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        setConfigEntry("username", username);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        setConfigEntry("password", password);
    }

    public int getBitrate() {
        return mp3_bitrate;
    }

    public void setBitrate(int bitrate) {
        this.mp3_bitrate = bitrate;
        setConfigEntry("mp3_bitrate", Integer.toString(bitrate));
    }

    public String getId3_title() {
        return id3_title;
    }

    public void setId3_title(String id3_title) {
        this.id3_title = id3_title;
        setConfigEntry("id3_title", id3_title);
    }

    public String getId3_artist() {
        return id3_artist;
    }

    public void setId3_artist(String id3_artist) {
        this.id3_artist = id3_artist;
        setConfigEntry("id3_artist", id3_artist);
    }

    public String getId3_year() {
        return id3_year;
    }

    public void setId3_year(String id3_year) {
        this.id3_year = id3_year;
        setConfigEntry("id3_year", id3_year);
    }

    public String getId3_comment() {
        return id3_comment;
    }

    public void setId3_comment(String id3_comment) {
        this.id3_comment = id3_comment;
        setConfigEntry("id3_comment", id3_comment);
    }

    public String getId3_genre() {
        return id3_genre;
    }

    public void setId3_genre(String id3_genre) {
        this.id3_genre = id3_genre;
        setConfigEntry("id3_genre", id3_genre);
    }

    public void saveConfig(String password, String hostname, String username, String workingDir,
                           Protocols protocol, String wordpressURL, int mp3_bitrate, String id3_title, String id3_artist,
                           String id3_year, String id3_comment, String id3_genre) {
        this.setPassword(password);
        this.setHostname(hostname);
        this.setUsername(username);
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