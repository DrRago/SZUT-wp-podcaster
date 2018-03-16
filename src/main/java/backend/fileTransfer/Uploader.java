package backend.fileTransfer;

import java.io.IOException;

/**
 * The interface Uploader.
 */
public interface Uploader {
    /**
     * Upload a file.
     *
     * @param filePath the file path
     * @return the response if the upload was successful
     * @throws IOException       threw on file read error
     * @throws UploaderException threw on exception in uploading
     */
    String uploadFile(String filePath) throws IOException, UploaderException;

    /**
     * Disconnect from the server.
     *
     * @throws UploaderException threw if an error occurs during disconnect
     */
    void disconnect() throws UploaderException;
}
