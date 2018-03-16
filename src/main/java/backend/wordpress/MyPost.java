package backend.wordpress;

import lombok.Getter;
import lombok.Setter;

/**
 * My post. Used to display recent posts for the user.
 */
@Getter
@Setter
public class MyPost {
    int postID;
    String postTitle;
    String postAuthor;

    /**
     * Instantiates a new My post.
     *
     * @param postID    the post id
     * @param postTitle the post title
     * @param author    the post author
     */
    public MyPost(int postID, String postTitle, String author) {
        this.postID = postID;
        this.postTitle = postTitle;
        this.postAuthor = author;
    }
}
