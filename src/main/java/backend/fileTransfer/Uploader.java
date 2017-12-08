package backend.fileTransfer;

import java.io.FileNotFoundException;

public interface Uploader {
    void uploadFile(String filePath) throws FileNotFoundException;

    void disconnect();
}
