package cz.bublik.scripthon;

import cz.bublik.scripthon.compiler.syntax.ProgramContainer;
import cz.bublik.scripthon.compiler.syntax.statements.Program;
import cz.bublik.scripthon.tree.analyzer.codeanalyzer.main.AnalyzerRunnable;
import cz.bublik.scripthon.tree.analyzer.statistics.StatisticsAnalysis;
import cz.bublik.scripthon.tree.analyzer.tree.AccessHelper;
import cz.bublik.scripthon.tree.matcher.CompilationUtils;
import cz.bublik.scripthon.tree.matcher.MainMatcher;
import cz.bublik.scripthon.tree.matcher.pojo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;
import java.lang.management.ManagementFactory;

public class Performer {

    static final Logger LOG = LoggerFactory.getLogger(Performer.class);
    public static final int HUNDRED = 100;

    public static void perform(final ApplicationContext applicationContext, JProgressBar progressBar, JLabel notificationField) {
        AccessHelper.clean();
        String javaSourcesPath = applicationContext.getJavaSourcesFile().getAbsolutePath();
        final File dependencyJarsFolder = applicationContext.getDependencyJarsFolder();
        String dependencyJarsPath = null;
        if (dependencyJarsFolder != null) {
            dependencyJarsPath = dependencyJarsFolder.getAbsolutePath();
        }
        Runnable compilerWorker = CompilationUtils.createScripthonCompilerWorker(applicationContext.getScripthonSource());

        String jvmVersion = ManagementFactory.getRuntimeMXBean().getVmVersion();
        LOG.info("Java version: " + jvmVersion);

        long startTime = System.currentTimeMillis();
        LOG.debug("Number of available processors: " + Runtime.getRuntime().availableProcessors());

        Runnable analyzerWorker = new AnalyzerRunnable(javaSourcesPath, dependencyJarsPath);

        CompilationUtils.compileSources(compilerWorker, analyzerWorker);

        StatisticsAnalysis.createTopLevelStatistics(Main.getApplicationContext().getJavaSourcesFile().getPath());


        final Program program = ProgramContainer.getInstance().getProgram();
        LOG.debug("Scripthon program: " + program);

        //TODO needs more research
        /*AccessHelper.serializeClassTree();
        AccessHelper.deserializeClassTree();
        boolean accuracyImproved = false;

        if (applicationContext.getPreviousProgram() == null) {
            if (program != null) {
                applicationContext.setPreviousProgram(program);
            }
        } else {
            accuracyImproved = ProgramContainer.getInstance().moreAccurateChange(applicationContext.getPreviousProgram());
        }
        */

        long matchingStartTime = System.currentTimeMillis();
        LOG.debug("Starting matching system");

        Result matchingResult = null;

        try {
            if (program != null) {
                matchingResult = MainMatcher.compare(AccessHelper.getClassTrees(), program, progressBar, notificationField);
            } else {
                String errorMessage = ProgramContainer.getInstance().getErrorMessage();
                matchingResult = new Result(errorMessage);
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
        } finally {
            progressBar.setValue(HUNDRED);
            progressBar.setString(HUNDRED + " %");
            notificationField.setText("Completed");
        }


        long matchingEstimatedTime = System.currentTimeMillis() - matchingStartTime;
        LOG.info("Number of comparisons: " + matchingResult.getNumberOfComparisons());
        LOG.info("Matching result is: " + matchingResult);
        LOG.info("Time to matching process is: " + matchingEstimatedTime + " ms");
        long estimatedTime = System.currentTimeMillis() - startTime;
        LOG.info("Time to complete whole process is: " + estimatedTime + " ms");

        applicationContext.setResult(matchingResult);
        //applicationContext.setPreviousProgram(program);
    }
}