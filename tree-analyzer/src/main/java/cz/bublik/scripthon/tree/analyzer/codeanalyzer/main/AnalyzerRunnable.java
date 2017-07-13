package cz.bublik.scripthon.tree.analyzer.codeanalyzer.main;

import cz.bublik.scripthon.tree.analyzer.codeanalyzer.processor.CodeAnalyzerController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnalyzerRunnable implements Runnable {

    static final Logger LOG = LoggerFactory.getLogger(AnalyzerRunnable.class);

    private String argument;

    private String dependenciesFolder;

    public AnalyzerRunnable(String argument, String dependencyJarsPath) {
        this.argument = argument;
        this.dependenciesFolder = dependencyJarsPath;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        LOG.debug("Starting analyzing process");
        try {
            if (argument == null || argument.trim().length() <= 0) {
                LOG.debug("Please provide the java source file(s) " + "to be verified as argument");
                LOG.debug("Usage: java Main {<comma separated list of source files>}");
                LOG.debug("Exiting from the program");
                System.exit(0);
            } else {
                CodeAnalyzerController controller = new CodeAnalyzerController();
                controller.invokeProcessor(argument, dependenciesFolder);
            }
        } catch (Exception t) {
            t.printStackTrace();
        }
        long estimatedTime = System.currentTimeMillis() - startTime;
        LOG.info("Analyzing process finished in:" + estimatedTime + " ms");
    }
}