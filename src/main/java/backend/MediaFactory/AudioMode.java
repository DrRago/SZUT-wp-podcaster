package backend.MediaFactory;

/**
 * The enum Audio mode.
 */
public enum AudioMode {
    JOINT("j"),
    MONO("m");

    private String param;

    AudioMode(String m) {
        param = m;
    }

    /**
     * Gets the param for the enum that lame accepts.
     *
     * @return the param
     */
    public String getParam() {
        return param;
    }
}
