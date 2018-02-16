import backend.MediaFactory.Lame;
import backend.fileTransfer.Protocols;
import backend.fileTransfer.Uploader;
import backend.fileTransfer.UploaderFactory;
import backend.wordpress.Blog;
import backend.LoggerFormatter.MyFormatter;

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

            Lame lame = new Lame("C:\\Users\\Leonhard.Gahr\\Downloads\\SFGrenade - Rhythm of the Dancefloor.mp3");
            lame.executeCommand();

            Blog blog = new Blog("admin", "12345", "http://localhost/wp/xmlRpc.php", uploader);
            blog.addPost("Test", "publish", "http://localhost/uploads/", lame);
            uploader.disconnect();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
