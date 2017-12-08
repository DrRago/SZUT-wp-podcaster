package backend.fileTransfer;

import com.jcraft.jsch.SftpException;

import java.io.FileNotFoundException;

public interface Uploader {
    void uploadFile(String filePath) throws FileNotFoundException, SftpException;

    void disconnect();
}
