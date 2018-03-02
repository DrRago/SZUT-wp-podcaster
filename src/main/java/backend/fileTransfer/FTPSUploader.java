package backend.fileTransfer;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPSClient;

import java.io.*;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class FTPSUploader implements Uploader {
    private static final Logger LOGGER = Logger.getGlobal();

    private FTPSClient client = new FTPSClient(true);
    private String remote;

    public FTPSUploader(String hostname, int port, String username, String password, String workingDir) throws UploaderException {
        try {
            client.connect(hostname, port);
            LOGGER.info(String.format("connected to %s:%d", hostname, port));
            client.login(username, password);
            if (client.getReplyCode() != 230) {
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
        client.enterLocalPassiveMode();
        LOGGER.info(client.getReplyString().trim());

        remote = workingDir;
    }

    @Override
    public String uploadFile(String filePath) throws UploaderException {
        LOGGER.info(String.format("Uploading file \"%s\" to \"%s\"", filePath, client.getRemoteAddress().getHostName()));
        File file = new File(filePath);

        if (!file.exists()) {
            throw new UploaderException(new FileNotFoundException("File " + filePath + " not found"));
        }

        // Create an InputStream of the file to be uploaded
        FileInputStream fis;
        try {
            fis = new FileInputStream(file);
            // Store file to server
            client.storeFile(Paths.get(remote, file.getName()).toString(), fis);

            fis.close();
        } catch (IOException e) {
            LOGGER.severe(e.getLocalizedMessage());
            throw new UploaderException(e);
        }
        LOGGER.info(client.getReplyString().trim());
        return client.getReplyString().trim();
    }

    @Override
    public void downloadFile(String remoteFile, String localPath) throws UploaderException {
        remoteFile = Paths.get(remote, remoteFile).toString();
        LOGGER.info(String.format("Starting download of \"%s\" to \"%s\"", remoteFile, localPath));
        try {
            OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(localPath));
            client.retrieveFile(remoteFile, outputStream);
            if (client.getReplyCode() != 226) {
                LOGGER.severe(client.getReplyString().trim());
                throw new UploaderException(client.getReplyString().trim());
            }
            outputStream.close();
            LOGGER.info(String.format("Action completed with success code %d: file stored to %s", client.getReplyCode(), localPath));
        } catch (IOException e) {
            throw new UploaderException(e);
        }
    }

    @Override
    public void disconnect() throws IOException {
        client.logout();
        LOGGER.info(client.getReplyString().trim());
        client.disconnect();
    }
}