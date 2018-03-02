package backend.wordpress;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyPost {
    int postID;
    String postTitle;
    String author;

    public MyPost(int postID, String postTitle, String author) {
        this.postID = postID;
        this.postTitle = postTitle;
        this.author = author;
    }
}
