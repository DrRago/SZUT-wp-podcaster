package backend.wordpress;

import backend.MediaFactory.Lame;
import backend.fileTransfer.Uploader;
import backend.fileTransfer.UploaderException;
import com.google.common.net.UrlEscapers;
import com.jcraft.jsch.SftpException;
import net.bican.wordpress.Post;
import net.bican.wordpress.Wordpress;
import net.bican.wordpress.exceptions.InsufficientRightsException;
import net.bican.wordpress.exceptions.InvalidArgumentsException;
import net.bican.wordpress.exceptions.ObjectNotFoundException;
import redstone.xmlrpc.XmlRpcFault;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

public class Blog {
    private static final Logger LOGGER = Logger.getGlobal();

    private Wordpress wp;
    private Uploader uploader;
    private URL remotePath;

    public Blog(String wp_user, String wp_password, String xmlRpcUrl, Uploader uploader, String remotePath) throws IOException {
        this.remotePath = new URL(remotePath);
        wp = new Wordpress(wp_user, wp_password, xmlRpcUrl);
        this.uploader = uploader;
    }

    public void addPost(String title, Lame encoder) throws SftpException, XmlRpcFault, IOException, ObjectNotFoundException, UploaderException, InvalidArgumentsException, InsufficientRightsException {
        addPost(title, "publish", encoder);
    }

    public void addPost(String title, String status, Lame encoder) throws InsufficientRightsException, InvalidArgumentsException, XmlRpcFault, ObjectNotFoundException, UploaderException, SftpException, IOException {
        LOGGER.info(String.format("adding post %s as %s", title, status));
        File file = encoder.getMP3File();
        String uploadStatus = uploader.uploadFile(file.getPath());


        if (!uploadStatus.startsWith("226") && !uploadStatus.equals("200")) {
            LOGGER.severe(uploadStatus);
            throw new UploaderException(uploadStatus);
        }

        String mediaLink = UrlEscapers.urlFragmentEscaper().escape(remotePath + file.getName());
        String postcontent = "<a href=\"" + mediaLink + "\">" + title + "</a>";

        Post recentPost = new Post();
        recentPost.setPost_title(title);
        recentPost.setPost_content(postcontent);
        recentPost.setPost_status(status);

        Integer result = wp.newPost(recentPost);
        LOGGER.info(String.format("Post created with id %d", result));
    }

    public URL getRemotePath() {
        return remotePath;
    }

    public void setRemotePath(URL remotePath) {
        this.remotePath = remotePath;
    }
}
