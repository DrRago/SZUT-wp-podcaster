package backend.fileTransfer;

public class UploaderFactory {
    public Uploader getUploader(Protocols protocol, String serverURL, int port, String username, String password, String workingDir) throws UploaderException {
        switch (protocol) {
            case FTPS:
                return new FTPSUploader(serverURL, port, username, password, workingDir);
            case SFTP:
                return new SFTPUploader(serverURL, port, username, password, workingDir);
            default:
                throw new IllegalArgumentException("Protocol type not found!");
        }
    }
}
