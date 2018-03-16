package backend.MediaFactory;

import com.sun.xml.internal.org.jvnet.fastinfoset.EncodingAlgorithmException;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.farng.mp3.TagException;
import util.PathUtil;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 * The Lame class holds the mp3 file and its ID3 tags.
 * It can execute the lame encoder to encode this mp3 file.
 */
public class Lame {
    private static final Logger LOGGER = Logger.getGlobal();

    private String command;
    private ID3TagUtil ID3TagUtil;
    @Setter
    @Getter
    private int bitrate;
    @Setter
    private AudioMode audioMode = AudioMode.JOINT;

    @Getter
    @Setter
    private String wp_postTitle;
    @Getter
    @Setter
    private String wp_status;

    /**
     * Instantiates a new Lame.
     *
     * @param file the mp3 file that will be encoded
     * @throws IOException  thrown if the file could not be found
     * @throws TagException the tag exception
     */
    public Lame(String file) throws IOException, TagException {
        String command;
        if (SystemUtils.IS_OS_WINDOWS) {
            command = PathUtil.getResourcePath("LAME/lame.exe").getPath();
        } else {
            command = "lame";
        }
        initialize(command, file);
    }

    private void initialize(String command, String file) throws IOException, TagException {
        this.command = command;
        if (!new File(file).exists()) {
            throw new FileNotFoundException(String.format("MP3 \"%s\" could not be found", file));
        }
        ID3TagUtil = new ID3TagUtil(file);
        bitrate = ID3TagUtil.getBitrate();
        checkCommand();
    }

    private void checkCommand() throws IOException {
        LOGGER.info("checking whether lame exists");
        if (SystemUtils.IS_OS_WINDOWS) {
            if (!new File(command).exists()) {
                LOGGER.severe("lame not found on this machine");
                throw new FileNotFoundException(String.format("Lame not found in internal resources (\"%s\"). Please download lame manually", command));
            }
        } else {
            ProcessBuilder pb = new ProcessBuilder("command -v" + command);
            pb.redirectErrorStream(true);
            Process p = pb.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream(), "UTF-8"));
            String line;
            line = reader.readLine();

            if (!line.equals(command)) {
                LOGGER.severe("lame not found on this machine");
                throw new RuntimeException("command 'lame' not found. Please install lame (e.g. sudo apt-get install lame)");
            }
        }
        LOGGER.info(String.format("Lame found for machine (%s)", System.getProperty("os.name")));
    }

    /**
     * start the encoding of the mp3 file. This method uses the lame encoder, that also sets id3 tags by itself.
     *
     * @throws IOException                the io exception
     * @throws EncodingAlgorithmException the encoding algorithm exception
     */
    public void executeCommand() throws IOException, EncodingAlgorithmException {
        Path source = ID3TagUtil.getFile().toPath();
        Path tmp = source.resolveSibling(ID3TagUtil.getFile().getName() + ".tmp");

        LOGGER.info(String.format("encoding file %s", source));
        try {
            List<String> commandList = new ArrayList<>();

            commandList.add(command);
            commandList.add("--mp3input");
            commandList.addAll(Arrays.asList("--abr", String.valueOf(bitrate)));
            commandList.addAll(Arrays.asList("--tt", "\"" + getID3_Title() + "\""));
            commandList.addAll(Arrays.asList("--ta", "\"" + getID3_Artist() + "\""));
            commandList.addAll(Arrays.asList("--tl", "\"" + getID3_Album() + "\""));
            commandList.addAll(Arrays.asList("--ty", "\"" + getID3_ReleaseYear() + "\""));
            commandList.addAll(Arrays.asList("--tc", "\"" + getID3_Comment() + "\""));
            commandList.addAll(Arrays.asList("--tg", "\"" + getID3_Genre() + "\""));
            commandList.addAll(Arrays.asList("-m", audioMode.getParam()));
            commandList.add("--add-id3v2");
            commandList.addAll(Arrays.asList("--silent", "-q", "0"));
            commandList.add("\"" + source + "\"");
            commandList.add("\"" + tmp + "\"");

            LOGGER.info(String.format("executing command %s", StringUtils.join(commandList, " ")));

            ProcessBuilder pb = new ProcessBuilder(commandList);
            pb.redirectErrorStream(true);
            Process p = pb.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream(), SystemUtils.IS_OS_WINDOWS ? "cp850" : "UTF-8"));
            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append(System.getProperty("line.separator"));
            }

            if (!builder.toString().trim().equals("")) {
                LOGGER.severe("encoding failed");
                throw new EncodingAlgorithmException(builder.toString());
            }
            LOGGER.info(String.format("backing up %s", source));
            if (Files.exists(source.resolveSibling(ID3TagUtil.getFile().getName() + ".old"))) {
                LOGGER.info("file %s already backed up. Skipping backup and removing old file...");
                Files.delete(source);
            } else {
                Files.move(source, source.resolveSibling(ID3TagUtil.getFile().getName() + ".old"));
            }
            LOGGER.info(String.format("writing file %s", source));
            Files.move(tmp, source);
        } finally {
            Files.delete(tmp);
        }
        LOGGER.info(String.format("file %s was successfully encoded", source));
    }

    /**
     * Gets the ID3 title.
     *
     * @return the title
     */
    public String getID3_Title() {
        return ID3TagUtil.getID3_Title();
    }

    /**
     * Sets the ID3 title.
     *
     * @param ID3_TITLE the title
     */
    public void setID3_Title(final String ID3_TITLE) {
        LOGGER.info(String.format("ID3_Title set to %s", ID3_TITLE));
        ID3TagUtil.setID3_Title(ID3_TITLE);
    }

    /**
     * Gets the ID3 artist.
     *
     * @return the artist
     */
    public String getID3_Artist() {
        return ID3TagUtil.getID3_Artist();
    }

    /**
     * Sets the ID3 artist.
     *
     * @param ID3_ARTIST the artist
     */
    public void setID3_Artist(final String ID3_ARTIST) {
        LOGGER.info(String.format("ID3_Artist set to %s", ID3_ARTIST));
        ID3TagUtil.setID3_Artist(ID3_ARTIST);
    }

    /**
     * Gets the ID3 album.
     *
     * @return the album
     */
    public String getID3_Album() {
        return ID3TagUtil.getID3_Album();
    }

    /**
     * Sets ID3 album.
     *
     * @param ID3_ALBUM the album
     */
    public void setID3_Album(final String ID3_ALBUM) {
        LOGGER.info(String.format("ID3_Album set to %s", ID3_ALBUM));
        ID3TagUtil.setID3_Album(ID3_ALBUM);
    }

    /**
     * Gets ID3 release year.
     *
     * @return the release year
     */
    public String getID3_ReleaseYear() {
        return ID3TagUtil.getID3_ReleaseYear();
    }

    /**
     * Sets the ID3 release year.
     *
     * @param ID3_RELEASEYEAR the release year
     */
    public void setID3_ReleaseYear(final String ID3_RELEASEYEAR) {
        LOGGER.info(String.format("ID3_ReleaseYear set to %s", ID3_RELEASEYEAR));
        ID3TagUtil.setID3_ReleaseYear(ID3_RELEASEYEAR);
    }

    /**
     * Gets the ID3 comment.
     *
     * @return the comment
     */
    public String getID3_Comment() {
        return ID3TagUtil.getID3_Comment();
    }

    /**
     * Sets the ID3 comment.
     *
     * @param ID3_COMMENT the comment
     */
    public void setID3_Comment(final String ID3_COMMENT) {
        LOGGER.info(String.format("ID3_Comment set to %s", ID3_COMMENT));
        ID3TagUtil.setID3_Comment(ID3_COMMENT);
    }

    /**
     * Gets the ID3 genre.
     *
     * @return the genre
     */
    public String getID3_Genre() {
        return ID3TagUtil.getID3_Genre();
    }

    /**
     * Sets the ID3 genre.
     *
     * @param ID3_GENRE the genre
     */
    public void setID3_Genre(final String ID3_GENRE) {
        LOGGER.info(String.format("ID3_Genre set to %s", ID3_GENRE));
        ID3TagUtil.setID3_Genre(ID3_GENRE);
    }

    /**
     * Gets the mp3 file.
     *
     * @return the mp3 file
     */
    public File getMP3File() {
        return ID3TagUtil.getFile();
    }
}
