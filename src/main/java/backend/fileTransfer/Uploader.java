package backend.fileTransfer;

import java.io.IOException;

public interface Uploader {
    String uploadFile(String filePath) throws IOException, UploaderException;

    void downloadFile(String remoteFile, String localPath) throws UploaderException;

    void disconnect() throws IOException;
}
