package backend.fileTransfer;

import com.jcraft.jsch.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Logger;

/**
 * The SFTP uploader.
 */
public class SFTPUploader implements Uploader {
    private static final java.util.logging.Logger LOGGER = Logger.getGlobal();

    private ChannelSftp sftpChannel;
    private Session session;

    /**
     * Instantiates a new SFTP uploader.
     *
     * @param hostname   the hostname of the sftp server
     * @param port       the port to connect to
     * @param username   the username
     * @param password   the password
     * @param workingDir the working dir the files should be uploaded to
     * @throws UploaderException the exception if any errors occur during connecting
     */
    SFTPUploader(String hostname, int port, String username, String password, String workingDir) throws UploaderException {
        Channel channel;

        try {
            JSch jsch = new JSch();
            session = jsch.getSession(username, hostname, port);
            session.setPassword(password);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            channel = session.openChannel("sftp");
            channel.connect();
            sftpChannel = (ChannelSftp) channel;
            LOGGER.info(String.format("connected to %s:%d", hostname, port));
            setRemotePath(workingDir);
        } catch (JSchException | SftpException e) {
            LOGGER.severe(e.getMessage());
            throw new UploaderException(e);
        }
    }

    @Override
    public String uploadFile(String filePath) throws FileNotFoundException, UploaderException {
        LOGGER.info(String.format("uploading file \"%s\" to %s", filePath, session.getHost()));
        File f = new File(filePath);
        try {
            sftpChannel.put(new FileInputStream(f), f.getName());
        } catch (SftpException e) {
            throw new UploaderException(e);
        }
        LOGGER.info("upload successful");
        return "200";
    }

    private void setRemotePath(String path) throws SftpException {
        sftpChannel.cd(path);
        LOGGER.info(String.format("remote path set to \"%s\"", path));
    }

    @Override
    public void disconnect() {
        sftpChannel.exit();
        session.disconnect();
        LOGGER.info(String.format("disconnected from %s", session.getHost()));
    }
}
