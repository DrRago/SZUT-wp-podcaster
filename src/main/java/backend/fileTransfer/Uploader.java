package backend.fileTransfer;

import com.jcraft.jsch.SftpException;

import java.io.IOException;

public interface Uploader {
    String uploadFile(String filePath) throws IOException, SftpException, UploaderException;

    void disconnect() throws IOException;
}
