package backend.LameQueue;

import backend.MediaFactory.Lame;
import backend.fileTransfer.UploaderException;
import backend.wordpress.Blog;
import net.bican.wordpress.exceptions.InsufficientRightsException;
import net.bican.wordpress.exceptions.InvalidArgumentsException;
import net.bican.wordpress.exceptions.ObjectNotFoundException;
import redstone.xmlrpc.XmlRpcFault;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LameQueue extends ArrayList<LameQueueItem> {
    private Blog wordpress;

    public LameQueue(Blog wordpress) {
        this.wordpress = wordpress;
    }

    public void add(Lame encoder, String title, String status) {
        this.add(new LameQueueItem(encoder, title, status));
    }

    public boolean startQueue(List<String> titles, List<String> states) {
        assert titles.size() == states.size() && states.size() == this.size() : String.format("Assert lists to have a size of %d, but %d and %d were given", this.size(), titles.size(), states.size());

        this.forEach(lameQueueItem -> {
            try {
                wordpress.addPost(lameQueueItem.getTitle(), lameQueueItem.getStatus(), lameQueueItem.getEncoder());
            } catch (InsufficientRightsException | InvalidArgumentsException | XmlRpcFault | UploaderException | ObjectNotFoundException | IOException e) {
                e.printStackTrace();
            }
        });
        return true;
    }
}
