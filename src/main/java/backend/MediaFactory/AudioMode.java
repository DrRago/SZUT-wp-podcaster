package backend.MediaFactory;

public enum AudioMode {
    JOINT("j"), MONO("m");

    private String param;

    AudioMode(String m) {
        param = m;
    }

    public String getParam() {
        return param;
    }
}
