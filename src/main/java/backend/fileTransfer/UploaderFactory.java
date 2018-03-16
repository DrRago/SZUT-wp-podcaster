package backend.fileTransfer;

/**
 * The uploader factory.
 */
public class UploaderFactory {
    /**
     * Gets an uploader based on the protocol enum that is given.
     *
     * @param protocol   the protocol enum that should be used
     * @param hostname   the hostname of the upload server
     * @param port       the port to connect to
     * @param username   the username
     * @param password   the password
     * @param workingDir the working dir the files should be uploaded to
     * @return the uploader that can be used by the frontend
     * @throws UploaderException the exception if any errors occur during connecting
     */
    public static Uploader getUploader(Protocols protocol, String hostname, int port, String username, String password, String workingDir) throws UploaderException {
        switch (protocol) {
            case FTPS:
                return new FTPSUploader(hostname, port, username, password, workingDir);
            case SFTP:
                return new SFTPUploader(hostname, port, username, password, workingDir);
            default:
                throw new IllegalArgumentException("Protocol type not found!");
        }
    }
}
