package backend.fileTransfer;

import org.apache.commons.net.ftp.FTPSClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;

public class FTPSUploader implements Uploader {

    private FTPSClient client = new FTPSClient(true);
    private String remote;

    public FTPSUploader(String hostname, int port, String username, String password, String workingDir) throws UploaderException {
        try {
            client.connect(hostname, port);
            client.login(username, password);
        } catch (IOException e) {
            throw new UploaderException(e);
        }
        client.enterLocalPassiveMode();
        remote = workingDir;
    }

    @Override
    public String uploadFile(String filePath) throws UploaderException {

        File file = new File(filePath);

        if (!file.exists()) {
            throw new UploaderException(new FileNotFoundException("File " + filePath + " not found"));
        }

        // Create an InputStream of the file to be uploaded
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            // Store file to server
            client.storeFile(Paths.get(remote, file.getName()).toString(), fis);

            fis.close();
        } catch (IOException e) {
            throw new UploaderException(e);
        }
        return client.getReplyString();
    }

    @Override
    public void disconnect() throws IOException {
        client.logout();
        client.disconnect();
    }
}