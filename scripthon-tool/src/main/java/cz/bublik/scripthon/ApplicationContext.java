package cz.bublik.scripthon;

import cz.bublik.scripthon.compiler.syntax.statements.Program;
import cz.bublik.scripthon.tree.matcher.pojo.Result;

import java.io.File;

public class ApplicationContext {

    private Program previousProgram;

    private File javaSourcesFile;

    private File dependencyJarsFolder;

    private String scripthonSource;

    private Result result;

    public File getJavaSourcesFile() {
        return javaSourcesFile;
    }

    public void setJavaSourcesFile(File javaSourcesFile) {
        this.javaSourcesFile = javaSourcesFile;
    }

    public String getScripthonSource() {
        return scripthonSource;
    }

    public void setScripthonSource(String scripthonSource) {
        this.scripthonSource = scripthonSource;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public Program getPreviousProgram() {
        return previousProgram;
    }

    public void setPreviousProgram(Program previousProgram) {
        this.previousProgram = previousProgram;
    }

    public File getDependencyJarsFolder() {
        return dependencyJarsFolder;
    }

    public void setDependencyJarsFolder(File dependencyJarsFolder) {
        this.dependencyJarsFolder = dependencyJarsFolder;
    }
}