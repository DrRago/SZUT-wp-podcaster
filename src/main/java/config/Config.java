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
}