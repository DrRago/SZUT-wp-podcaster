package util;

import java.net.URL;

public class PathUtil {
    /**
     * Get resource url
     *
     * @param path The path of the resource
     *
     * @return Resource URL
     */
    public static URL getResourcePath(final String path) {
        return PathUtil.class.getResource(path);
    }
}
