package backend.LameQueue;

import backend.MediaFactory.EncodingException;
import backend.MediaFactory.Lame;
import backend.fileTransfer.UploaderException;
import backend.wordpress.Blog;
import net.bican.wordpress.exceptions.InsufficientRightsException;
import net.bican.wordpress.exceptions.InvalidArgumentsException;
import net.bican.wordpress.exceptions.ObjectNotFoundException;
import redstone.xmlrpc.XmlRpcFault;

import java.io.IOException;
import java.util.ArrayList;

/**
 * The Lame queue.
 * Holds items of the type Lame.
 */
public class LameQueue extends ArrayList<Lame> {
    private Blog blog;

    /**
     * Instantiates a new lame queue.
     *
     * @param blog the blog on what the queue should be started on
     */
    public LameQueue(Blog blog) {
        this.blog = blog;
    }

    /**
     * Start the queue.
     * It iterates over every Lame object in itself, starts the encoding and adds a new post to the blog.
     *
     * @throws IOException                 the io exception
     * @throws EncodingException  the encoding algorithm exception
     * @throws InsufficientRightsException the insufficient rights exception
     * @throws InvalidArgumentsException   the invalid arguments exception
     * @throws XmlRpcFault                 the xml rpc fault
     * @throws ObjectNotFoundException     the object not found exception
     * @throws UploaderException           the uploader exception
     */
    public void startQueue() throws IOException, EncodingException, InsufficientRightsException, InvalidArgumentsException, XmlRpcFault, ObjectNotFoundException, UploaderException {
        for (Lame item : this) {
            item.executeCommand();
            blog.addPost(item);
        }
    }

    /**
     * Sets the ID3 title for all objects inside this.
     *
     * @param title the title
     */
    public void setTitle(final String title) {
        this.forEach(lame -> lame.setID3_Title(title));
    }

    /**
     * Sets the ID3 album for all objects inside this.
     *
     * @param album the album
     */
    public void setAlbum(final String album) {
        this.forEach(lame -> lame.setID3_Album(album));
    }

    /**
     * Sets the ID3 year for all objects inside this.
     *
     * @param year the year
     */
    public void setYear(final String year) {
        this.forEach(lame -> lame.setID3_ReleaseYear(year));
    }

    /**
     * Sets the ID3 genre for all objects inside this.
     *
     * @param genre the genre
     */
    public void setGenre(final String genre) {
        this.forEach(lame -> lame.setID3_Genre(genre));
    }

    /**
     * Sets the ID3 artist for all objects inside this.
     *
     * @param artist the artist
     */
    public void setArtist(final String artist) {
        this.forEach(lame -> lame.setID3_Artist(artist));
    }
}
