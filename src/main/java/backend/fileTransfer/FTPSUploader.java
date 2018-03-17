package backend.fileTransfer;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPSClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * The ftps uploader.
 */
public class FTPSUploader implements Uploader {
    private static final Logger LOGGER = Logger.getGlobal();

    private FTPSClient client = new FTPSClient(true);
    private String workingDir;

    /**
     * Instantiates a new Ftps uploader.
     *
     * @param hostname   the hostname for the ftps server
     * @param port       the port to connect to
     * @param username   the username
     * @param password   the password
     * @param workingDir the working dir the files should be uploaded to
     * @throws UploaderException the exception if any errors occur during connecting
     */
    FTPSUploader(String hostname, int port, String username, String password, String workingDir) throws UploaderException {
        try {
            // try to connect and login to ftps server
            client.connect(hostname, port);
            LOGGER.info(String.format("connected to %s:%d", hostname, port));
            client.login(username, password);
            if (client.getReplyCode() != 230) {
                // reply code should be 230 (User logged in, proceed)
                LOGGER.severe(client.getReplyString().trim());
                throw new UploaderException(client.getReplyString().trim());
            } else {
                LOGGER.info(client.getReplyString().trim());
            }
            // change filetype to binary to prevent a transmission failure (see issue #1)
            client.setFileType(FTP.BINARY_FILE_TYPE);
        } catch (IOException e) {
            LOGGER.severe(e.getLocalizedMessage());
            throw new UploaderException(e);
        }
        // enter passive mode, the client establishes command and data channel and the server tells the client the ports to use
        client.enterLocalPassiveMode();
        LOGGER.info(client.getReplyString().trim());

        this.workingDir = workingDir;
    }


    @Override
    public String uploadFile(String filePath) throws UploaderException {
        LOGGER.info(String.format("Uploading file \"%s\" to \"%s\"", filePath, client.getRemoteAddress().getHostName()));
        File file = new File(filePath);

        if (!file.exists()) {
            throw new UploaderException(new FileNotFoundException("File " + filePath + " not found"));
        }

        FileInputStream fis;
        try {
            // Create an InputStream of the file to be uploaded
            fis = new FileInputStream(file);

            // Store file to server
            String uploadPath;
            if (workingDir.charAt(workingDir.length() - 1) == '/') {
                uploadPath = workingDir + file.getName();
            } else {
                uploadPath = workingDir + "/" + file.getName();
            }
            client.storeFile(uploadPath, fis);

            fis.close();
        } catch (IOException e) {
            LOGGER.severe(e.getLocalizedMessage());
            throw new UploaderException(e);
        }
        LOGGER.info(client.getReplyString().trim());
        return client.getReplyString().trim();
    }

    /**
     * Checks whether the connection is still active or not
     *
     * @return the response if the connection is active or not
     */
    @Override
    public boolean isConnected() {
        return client.isConnected();
    }

    @Override
    public void disconnect() throws UploaderException {
        try {
            client.logout();
            LOGGER.info(client.getReplyString().trim());
            client.disconnect();
        } catch (IOException e) {
            throw new UploaderException(e);
        }
    }
}