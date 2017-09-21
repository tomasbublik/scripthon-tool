package cz.ctu.scripthon.compiler.utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public final class FileUtils {

    private FileUtils() {
    }

    public static List<String> readFile(String name) {
        Path path = FileSystems.getDefault().getPath(".", name);
        return readFileFromPath(path);
    }

    public static List<String> readFile(URL url) {
        try {
            Path dest = Paths.get(url.toURI()).toAbsolutePath();
            return readFileFromPath(dest);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static List<String> readFileFromPath(Path path) {
        try {
            return Files.readAllLines(path, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace(); //To change body of catch statement use File | Settings | File Templates.
        }
        return new ArrayList<>();
    }
}
