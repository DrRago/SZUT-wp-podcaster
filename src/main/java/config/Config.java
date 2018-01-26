package config;

import backend.fileTransfer.Protocols;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import util.PathUtil;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class Config {

    private Document doc;
    private Element eElement;
    private NodeList nList;

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
    private double mp3_bitrate;

    public Config() {
        try {
            File fXmlFile = new File(PathUtil.getResourcePath("config.xml").getPath());
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(fXmlFile);


            doc.getDocumentElement().normalize();

            nList = doc.getElementsByTagName("config");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    eElement = (Element) nNode;

                    hostname = getElementText("hostname");
                    port = Integer.parseInt(getElementText("port"));
                    workingDir = getElementText("workingDir");
                    protocol = Protocols.valueOf(getElementText("protocol").toUpperCase());
                    username = getElementText("username");
                    password = getElementText("password");

                    id3_title = getElementText("id3_title");
                    id3_artist = getElementText("id3_artist");
                    id3_year = getElementText("id3_year");
                    id3_comment = getElementText("id3_comment");
                    id3_genre = getElementText("id3_genre");
                    mp3_bitrate = Double.parseDouble(getElementText("mp3_bitrate"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getElementText(String element) {
        return eElement.getElementsByTagName(element).item(0).getTextContent();
    }

    private void setElementText(String element, String text) {
        eElement.getElementsByTagName(element).item(0).setTextContent(text);
        //Todo: make it work
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
        setElementText("hostname", hostname);
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
        setElementText("port", String.valueOf(port));
    }

    public String getWorkingDir() {
        return workingDir;
    }

    public void setWorkingDir(String workingDir) {
        this.workingDir = workingDir;
        setElementText("workingDir", workingDir);
    }

    public Protocols getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocols protocol) {
        this.protocol = protocol;
        setElementText("protocol", protocol.toString());
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        setElementText("username", username);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        setElementText("password", password);
    }

    public double getBitrate(){ return mp3_bitrate; }

    public void setBitrate(double bitrate){
        this.mp3_bitrate = bitrate;
        setElementText("mp3_bitrate", Double.toString(bitrate));
    }

    public String getId3_title() { return id3_title;  }

    public void setId3_title(String id3_title) {
        this.id3_title = id3_title;
        setElementText("id3_title", id3_title);
    }

    public String getId3_artist() { return id3_artist; }

    public void setId3_artist(String id3_artist) {
        this.id3_artist = id3_artist;
        setElementText("id3_artist", id3_artist);
    }

    public String getId3_year() { return id3_year; }

    public void setId3_year(String id3_year) {
        this.id3_year = id3_year;
        setElementText("id3_year", id3_year);
    }

    public String getId3_comment() { return id3_comment; }

    public void setId3_comment(String id3_comment) {
        this.id3_comment = id3_comment;
        setElementText("id3_comment", id3_comment);
    }

    public String getId3_genre() { return id3_genre; }

    public void setId3_genre(String id3_genre) {
        this.id3_genre = id3_genre;
        setElementText("id3_genre", id3_genre);
    }

    public void saveConfig(String password, String hostname, String username, String workingDir,
                                  Protocols protocol , double mp3_bitrate, String id3_title, String id3_artist,
                                  String id3_year, String id3_comment, String id3_genre) {
        this.setPassword(password);
        this.setHostname(hostname);
        this.setUsername(username);
        this.setWorkingDir(workingDir);
        this.setProtocol(protocol);
        this.setBitrate(mp3_bitrate);
        this.setId3_title(id3_title);
        this.setId3_artist(id3_artist);
        this.setId3_year(id3_year);
        this.setId3_comment(id3_comment);
        this.setId3_genre(id3_genre);
    }
}