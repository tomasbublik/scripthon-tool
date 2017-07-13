package cz.bublik.scripthon.tree.analyzer.filemanager;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    private static List<String> files = new ArrayList<>();

    public static List<String> getFilesFromDir(String dir) throws IOException {
        FileVisitor<Path> fileProcessor = new ProcessFile();
        Files.walkFileTree(Paths.get(dir), fileProcessor);
        return files;
    }

    private static final class ProcessFile extends SimpleFileVisitor<Path> {
        @Override
        public FileVisitResult visitFile(Path aFile, BasicFileAttributes aAttrs) throws IOException {
            //LOG.debug("Processing file:" + aFile);
            if (aFile.toString().endsWith(".java")) {
                files.add(aFile.toString());
            }
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult preVisitDirectory(Path aDir, BasicFileAttributes aAttrs) throws IOException {
            //LOG.debug("Processing directory:" + aDir);
            return FileVisitResult.CONTINUE;
        }
    }
}
