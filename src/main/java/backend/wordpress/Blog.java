package backend.wordpress;

import backend.fileTransfer.FTPSUploader;
import backend.fileTransfer.Uploader;
import backend.fileTransfer.UploaderException;
import com.jcraft.jsch.SftpException;
import net.bican.wordpress.Post;
import net.bican.wordpress.Wordpress;
import net.bican.wordpress.exceptions.InsufficientRightsException;
import net.bican.wordpress.exceptions.InvalidArgumentsException;
import net.bican.wordpress.exceptions.ObjectNotFoundException;
import redstone.xmlrpc.XmlRpcFault;

import java.io.IOException;
import java.net.MalformedURLException;

public class Blog {
    private Wordpress wp;
    private Uploader uploader;

    public Blog() throws MalformedURLException, UploaderException {
        wp = new Wordpress("admin", "12345", "http://localhost/wp/xmlRpc.php");
        uploader = new FTPSUploader("localhost", 990, "root", "12345", "/uploads/");
    }

    public void addPost(String filePath, String title, String status) throws InsufficientRightsException, InvalidArgumentsException, XmlRpcFault, ObjectNotFoundException, UploaderException, SftpException, IOException {
        uploader.uploadFile(filePath);
        String postcontent = "<a href=\"http://localhost/uploads/SFGrenade - Rhythm of the Dancefloor.mp3\">Test</a>";

        Post recentPost = new Post();
        recentPost.setPost_title(title);
        recentPost.setPost_content(postcontent);
        recentPost.setPost_status(status);
        Integer result = wp.newPost(recentPost);
        System.out.println("new post page id: " + result);
    }
}
