package cz.ctu.scripthon.tree.analyzer.fromtut;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Analyzing {

    public static void main(String[] args) {
        init();
    }

    private static void init() {
        //Get an instance of java compiler
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        //Get a new instance of the standard file manager implementation
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);

        // Get the list of java file objects, in this case we have only
        // one file, TestClass.java
        List files = new ArrayList<File>();
        files.add(new File("src/cz/tomik/fromtut/TestClass.java"));
        Iterable<? extends JavaFileObject> compilationUnits1 = fileManager.getJavaFileObjectsFromFiles(files);

        // Create the compilation task
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, null, null, null, compilationUnits1);

        // Perform the compilation task.
        task.call();
    }

}
