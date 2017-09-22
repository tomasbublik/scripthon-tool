package cz.ctu.scripthon.tree.analyzer.codeanalyzer.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.AbstractProcessor;
import javax.tools.*;
import javax.tools.JavaCompiler.CompilationTask;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The controller class to initiate verification of java files using custom
 * annotation processor. The files to be verified can be supplied to this class
 * as comma-separated argument.
 */
public class CodeAnalyzerController {

    private static final String BUILD_CLASSES = "build/classes";
    static final Logger LOG = LoggerFactory.getLogger(CodeAnalyzerController.class);
    private static final String FILE_DELIMITER = ",";
    public static final String JAVA_SUFFIX = ".java";

    /**
     * Invokes the annotation processor passing the list of file names
     *
     * @param fileNames Names of files to be verified
     */
    public void invokeProcessor(String fileNames, String dependencyDir) throws Exception {
        // Gets the Java programming language compiler
        final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        StandardJavaFileManager fileManager = getStandardJavaFileManager(compiler);
        if (fileManager == null) return;

        Iterable<? extends JavaFileObject> compilationUnits = getCompilationUnits(fileNames, fileManager);

        if (compilationUnits != null) {
            try {
            List<String> optionList = addDependency(dependencyDir, fileManager);

            // Create the compilation task
            CompilationTask task = compiler.getTask(null, fileManager, null, optionList, null, compilationUnits);

            setProcessors(task);

            // Perform the compilation task.
            task.call();

                fileManager.close();
            } catch (IOException e) {
                LOG.debug(e.getMessage());
            }
        } else {
            throw  new Exception("No valid source files to process");
        }
    }

    private void setProcessors(CompilationTask task) {
        // Get the list of annotation processors
        LinkedList<AbstractProcessor> processors = new LinkedList<AbstractProcessor>();
        processors.add(new CodeAnalyzerProcessor());
        task.setProcessors(processors);
    }

    private List<String> addDependency(String dependencyDir, StandardJavaFileManager fileManager) throws IOException {
        List<String> optionList = new ArrayList<String>();

        List<File> dependencyJars = new ArrayList<>();
        if (dependencyDir != null) {
            dependencyJars = listJarFiles(dependencyDir);
        }

        if (!dependencyJars.isEmpty()) {
            fileManager.setLocation(StandardLocation.CLASS_PATH, dependencyJars);
        }

        //destination
        assureDestination();
        optionList.addAll(Arrays.asList("-d", BUILD_CLASSES));
        return optionList;
    }

    private Iterable<? extends JavaFileObject> getCompilationUnits(String fileNames, StandardJavaFileManager fileManager) {
        // Get the valid source files as a list
        Iterable<? extends JavaFileObject> compilationUnits = null;
        if (fileNames.contains(JAVA_SUFFIX)) {
            // Get the list of java file objects
            compilationUnits = fileManager.getJavaFileObjectsFromFiles(getFilesAsList(fileNames));
        } else {
            try {
                fileManager.setLocation(StandardLocation.SOURCE_PATH, Arrays.asList(new File(fileNames)));
                Set<JavaFileObject.Kind> kinds = new HashSet<JavaFileObject.Kind>();
                kinds.add(JavaFileObject.Kind.SOURCE);
                compilationUnits = fileManager.list(StandardLocation.SOURCE_PATH, "", kinds, true);
            } catch (IOException e) {
                LOG.error("Error during obtaining the sources from a directory: " + e.getMessage());
            }
        }
        return compilationUnits;
    }

    private StandardJavaFileManager getStandardJavaFileManager(JavaCompiler compiler) {
        // Get a new instance of the standard file manager implementation
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        try {
            final File file = new File(BUILD_CLASSES);
            if (!file.exists()) {
                LOG.info("Creating directory: " + file.getAbsolutePath() + ", because it does not exist");
                final boolean mkdirs = file.mkdirs();
            }
            fileManager.setLocation(StandardLocation.CLASS_OUTPUT, Arrays.asList(file));
        } catch (IOException e) {
            LOG.error("Exception while creating dir: " + e.getMessage());
            return null;
        }
        return fileManager;
    }

    private List<File> listJarFiles(String dependencyDir) {
        try {
            try (Stream<Path> stream = Files.list(Paths.get(dependencyDir))) {
                return stream.map(String::valueOf)
                        .filter(path -> path.endsWith(".jar"))
                        .map(File::new)
                        .collect(Collectors.toList());
            }
        } catch (IOException e) {
            LOG.error("Error during JAR files assembly: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    private void assureDestination() throws IOException {
        File file = new File(BUILD_CLASSES);
        if (!file.exists()) {
            boolean success = file.mkdirs();
            if (!success) {
                throw new IOException("Error during mkdir");
            }
        }
    }

    /**
     * This method accepts the comma-separated file names, splits it using the
     * defined delimiter. A list of valid file objects will be created and
     * returned to main method.
     *
     * @param fileNames Comma-separated file names
     * @return List of valid source file objects
     */
    private List<File> getFilesAsList(String fileNames) {
        List<File> files = new LinkedList<File>();
        // split the filenames using the delimiter
        String[] filesArr = fileNames.split(FILE_DELIMITER);
        File sourceFile = null;
        for (String fileName : filesArr) {
            sourceFile = new File(fileName);
            if (sourceFile.exists()) {
                files.add(sourceFile);
            } else {
                LOG.debug(fileName + " is not a valid file. " + "Ignoring the file ");
            }
        }
        return files;
    }
}
