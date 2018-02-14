package backend.fileTransfer;

import com.jcraft.jsch.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class SFTPUploader implements Uploader {

    private ChannelSftp sftpChannel;
    private Session session;

    public SFTPUploader(String URL, int port, String username, String password, String workingDir) throws UploaderException {
        Channel channel;

        try {
            JSch jsch = new JSch();
            session = jsch.getSession(username, URL, port);
            session.setPassword(password);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            channel = session.openChannel("sftp");
            channel.connect();
            sftpChannel = (ChannelSftp) channel;
            setRemotePath(workingDir);
        } catch (JSchException | SftpException e) {
            throw new UploaderException(e);
        }
    }

    public String uploadFile(String filePath) throws FileNotFoundException, SftpException {
        File f = new File(filePath);
        sftpChannel.put(new FileInputStream(f), f.getName());
        return "200";
    }

    private void setRemotePath(String path) throws SftpException {
        sftpChannel.cd(path);
    }

    public void disconnect() {
        sftpChannel.exit();
        session.disconnect();
    }
}
