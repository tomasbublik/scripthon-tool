package cz.ctu.scripthon.tree.analyzer.codeanalyzer.processor;

import cz.ctu.scripthon.tree.analyzer.filemanager.FileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.AbstractProcessor;
import javax.tools.*;
import javax.tools.JavaCompiler.CompilationTask;
import java.io.*;
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

    /**
     * Invokes the annotation processor passing the list of file names
     *
     * @param fileNames Names of files to be verified
     */
    public void invokeProcessor(String fileNames, String dependencyDir) {

        //System.setProperty("java.home", "c:\\Program Files (x86)\\Java\\jdk1.8.0_05\\");
        // Gets the Java programming language compiler
        final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

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
            e.printStackTrace();
            return;
        }
        // Get the valid source files as a list
        List<File> files = null;
        Iterable<? extends JavaFileObject> compilationUnits1 = null;
        if (fileNames.contains(".java")) {
            files = getFilesAsList(fileNames);
            // Get the list of java file objects
            compilationUnits1 = fileManager.getJavaFileObjectsFromFiles(files);
        } else {
            //files = getFilesFromDir(fileNames);
            try {
                fileManager.setLocation(StandardLocation.SOURCE_PATH, Arrays.asList(new File(fileNames)));
                Set<JavaFileObject.Kind> kinds = new HashSet<JavaFileObject.Kind>();
                kinds.add(JavaFileObject.Kind.SOURCE);
                compilationUnits1 = fileManager.list(StandardLocation.SOURCE_PATH, "", kinds, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (compilationUnits1 != null) {

            List<String> optionList = new ArrayList<String>();
            // set compiler's classpath to be same as the runtime's
            //optionList.addAll(Arrays.asList("-classpath", System.getProperty("java.class.path")));
            //optionList.addAll(Arrays.asList("-source", "1.7", "-target", "1.8"));

            //String classpath = "C:\\Users\\tomas.bublik\\programs\\apache-maven\\repository\\org\\springframework\\data\\spring-data-mongodb\\1.4.2.RELEASE\\spring-data-mongodb-1.4.2.RELEASE.jar";
            //String classpath = "spring-data-mongodb-1.4.2.RELEASE.jar";

            List<File> dependencyJars = null;
            if (dependencyDir != null) {
                dependencyJars = listJarFiles(dependencyDir);
            }

            if (dependencyJars != null) {
                try {
                    fileManager.setLocation(StandardLocation.CLASS_PATH, dependencyJars);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //optionList.addAll(Arrays.asList("-cp", classpath));

            //destination
            assureDestination();
            optionList.addAll(Arrays.asList("-d", BUILD_CLASSES));
            // any other options you want
            //optionList.addAll(Arrays.asList(options));

            Writer writer = new StringWriter();

            // Create the compilation task
            CompilationTask task = compiler.getTask(null, fileManager, null, optionList, null, compilationUnits1);
            // Get the list of annotation processors
            LinkedList<AbstractProcessor> processors = new LinkedList<AbstractProcessor>();
            processors.add(new CodeAnalyzerProcessor());
            task.setProcessors(processors);
            // Perform the compilation task.
            task.call();
            try {
                fileManager.close();
            } catch (IOException e) {
                LOG.debug(e.getLocalizedMessage());
            }
        } else {
            LOG.debug("No valid source files to process. " + "Extiting from the program");
            System.exit(0);
        }
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
            e.printStackTrace();
        }
        return null;
    }

    private void assureDestination() {
        File file = new File(BUILD_CLASSES);
        if (!file.exists()) {
            file.mkdirs();
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

    private List<File> getFilesFromDir(String dir) {
        List<File> files = new LinkedList<File>();
        List<String> filesArr = null;
        try {
            filesArr = FileManager.getFilesFromDir(dir);
        } catch (IOException e) {
            e.printStackTrace(); //To change body of catch statement use File | Settings | File Templates.
        }
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
