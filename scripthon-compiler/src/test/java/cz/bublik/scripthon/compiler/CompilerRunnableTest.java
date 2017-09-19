package cz.bublik.scripthon.compiler;

import cz.bublik.scripthon.compiler.utils.FileUtils;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.*;

import static org.assertj.core.api.Assertions.assertThat;

public class CompilerRunnableTest {

    private static final boolean IS_WINDOWS = System.getProperty("os.name").contains("indow");

    @Test
    public void canAllTheGivenFilesBeCompiled() {
        final String filePath = getClass().getResource("/scripthonSources").getPath();
        Path path = getOsSpecificPath(filePath);
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
            for (Path file : stream) {
                CompilerRunnable compilerWorker = new CompilerRunnable(FileUtils.readFileFromPath(file));
                System.out.println("Considering " + file);

                assertThat(compilerWorker.getLinesList().size()).isGreaterThan(0);
            }
        } catch (IOException | DirectoryIteratorException x) {
            // IOException can never be thrown by the iteration.
            // In this snippet, it can only be thrown by newDirectoryStream.
            System.err.println(x);
        }
    }

    private Path getOsSpecificPath(String filePath) {
        String osAppropriatePath = IS_WINDOWS ? filePath.substring(1) : filePath;
        return Paths.get(osAppropriatePath);
    }
}
