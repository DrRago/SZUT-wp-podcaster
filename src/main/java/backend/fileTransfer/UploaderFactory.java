package backend.fileTransfer;

/**
 * @author Leonhard Gahr
 * @author Pascal de Vries
 */
public class UploaderFactory {
    public Uploader getUploader(Protocols protocol, String serverURL, int port, String username, String password, String workingDir) {
        switch (protocol) {
            case FTPS:
                return null;
            case SFTP:
                return new SFTPUploader(serverURL, port, username, password, workingDir);
            default:
                throw new IllegalArgumentException("Protocol type not found!");
        }
    }
}
