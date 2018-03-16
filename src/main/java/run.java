import backend.LameQueue.LameQueue;
import backend.LoggerFormatter.MyFormatter;
import backend.MediaFactory.Lame;
import backend.fileTransfer.Protocols;
import backend.fileTransfer.Uploader;
import backend.fileTransfer.UploaderFactory;
import backend.wordpress.Blog;

import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

public class run {
    private static final Logger LOGGER = Logger.getGlobal();

    public static void main(String[] args) {
        LOGGER.setUseParentHandlers(false);

        MyFormatter formatter = new MyFormatter();
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(formatter);

        LOGGER.addHandler(handler);

        try {
            Uploader uploader = UploaderFactory.getUploader(Protocols.FTPS, "localhost", 990, "root", "12345", "/uploads/");

            Lame lame = new Lame("C:\\Users\\Leonhard.Gahr\\Downloads\\SampleAudio_0.4mb.mp3");
            lame.executeCommand();

            Blog blog = new Blog("admin", "12345", "http://localhost/wp/xmlrpc.php", uploader, "http://localhost/uploads/");
            // blog.addPost(lame);
            //uploader.disconnect();

            LameQueue lq = new LameQueue(blog);
            lq.add(lame);

            lq.startQueue();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
