package backend.wordpress;

import backend.MediaFactory.Lame;
import backend.fileTransfer.Uploader;
import backend.fileTransfer.UploaderException;
import com.google.common.net.UrlEscapers;
import lombok.Getter;
import lombok.Setter;
import net.bican.wordpress.FilterPost;
import net.bican.wordpress.Post;
import net.bican.wordpress.Wordpress;
import net.bican.wordpress.exceptions.InsufficientRightsException;
import net.bican.wordpress.exceptions.InvalidArgumentsException;
import net.bican.wordpress.exceptions.ObjectNotFoundException;
import redstone.xmlrpc.XmlRpcFault;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * The interface for the frontend to communicate with wordpress
 */
public class Blog {
    private static final Logger LOGGER = Logger.getGlobal();

    private Wordpress wp;
    @Getter
    @Setter
    private Uploader uploader;
    @Getter
    @Setter
    private URL remotePath;

    /**
     * Instantiates a new Blog.
     *
     * @param wp_user     the wordpress user
     * @param wp_password the wordpress password
     * @param xmlRpcUrl   the wordpress xml rpc url
     * @param uploader    the uploader
     * @param remotePath  the remote path (the web URL to the upload directory e.g. "http://localhost/uploads/"
     * @throws IOException                  the io exception
     * @throws WordpressConnectionException the wordpress connection exception
     */
    public Blog(String wp_user, String wp_password, String xmlRpcUrl, Uploader uploader, String remotePath) throws IOException, WordpressConnectionException {
        this.remotePath = new URL(remotePath);
        wp = new Wordpress(wp_user, wp_password, xmlRpcUrl);

        try {
            checkConnection();
        } catch (XmlRpcFault | InsufficientRightsException e) {
            throw new WordpressConnectionException(e.getMessage());
        }

        this.uploader = uploader;
    }

    private void checkConnection() throws XmlRpcFault, InsufficientRightsException {
        // simply make a xmlrpc call to check if the connection was successfully established
        wp.getAuthors();
    }

    /**
     * Get recent posts on the blog and fetch them into a usable data structure for the frontend
     *
     * @return the posts
     * @throws XmlRpcFault                 an xml rpc fault
     * @throws InsufficientRightsException the insufficient rights exception
     * @throws ObjectNotFoundException     the object not found exception
     */
    public List<MyPost> getPosts() throws XmlRpcFault, InsufficientRightsException, ObjectNotFoundException {
        FilterPost filter = new FilterPost();
        filter.setNumber(30);
        List<Post> postList = wp.getPosts(filter);
        List<MyPost> posts = new ArrayList<>();
        for (Post post : postList) {
            posts.add(new MyPost(post.getPost_id(), post.getPost_title(), wp.getUser(post.getPost_author()).getNickname()));
        }
        return posts;
    }

    /**
     * Add a post to wordpress and upload the mp3 file.
     *
     * @param encoder the lame encoder
     * @throws InsufficientRightsException the insufficient rights exception
     * @throws InvalidArgumentsException   the invalid arguments exception
     * @throws XmlRpcFault                 the xml rpc fault
     * @throws ObjectNotFoundException     the object not found exception
     * @throws UploaderException           the uploader exception
     * @throws IOException                 the io exception
     */
    public void addPost(Lame encoder) throws InsufficientRightsException, InvalidArgumentsException, XmlRpcFault, ObjectNotFoundException, UploaderException, IOException {
        LOGGER.info(String.format("adding post %s as %s", encoder.getWp_postTitle(), encoder.getWp_status()));
        File file = encoder.getMP3File();
        String uploadStatus = uploader.uploadFile(file.getPath());

        // check whether ftps ot sftp had an error during uploading
        if (!uploadStatus.startsWith("226") && !uploadStatus.equals("200")) {
            LOGGER.severe(uploadStatus);
            throw new UploaderException(uploadStatus);
        }

        // the content of the post should look like this;
        // <a href="http://url/to/mp3file">Title of the file</a>
        String mediaLink = UrlEscapers.urlFragmentEscaper().escape(remotePath + file.getName());
        String postcontent = "<a href=\"" + mediaLink + "\">" + encoder.getWp_postTitle() + "</a>";

        Post recentPost = new Post();
        recentPost.setPost_title(encoder.getWp_postTitle());
        recentPost.setPost_content(postcontent);
        recentPost.setPost_status(encoder.getWp_status());

        // add the post to wordpress
        Integer result = wp.newPost(recentPost);
        LOGGER.info(String.format("Post created with id %d", result));
    }

    /**
     * Move a post to bin on wordpress
     *
     * @param postID the post id of the post to be deleted
     * @return the boolean if the post was successfully deleted
     * @throws XmlRpcFault                 the xml rpc fault
     * @throws ObjectNotFoundException     the object not found exception
     * @throws InsufficientRightsException the insufficient rights exception
     */
    public boolean deletePost(final int postID) throws XmlRpcFault, ObjectNotFoundException, InsufficientRightsException {
        LOGGER.info(String.format("moving post %d to bin", postID));
        return wp.deletePost(postID);
    }
}
