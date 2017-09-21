package cz.ctu.scripthon.tree.matcher;

import cz.ctu.scripthon.compiler.CompilerRunnable;
import cz.ctu.scripthon.compiler.utils.FileUtils;
import cz.ctu.scripthon.tree.analyzer.codeanalyzer.main.AnalyzerRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CompilationUtils {

    public static void compileSources(Runnable compilerWorker, Runnable analyzerWorker) {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        executor.execute(compilerWorker);
        executor.execute(analyzerWorker);
        executor.shutdown();
        // Wait until all threads are finished
        try {
            executor.awaitTermination(50, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static Runnable createScripthonCompilerWorker(String scripthonSources) {
        List<String> scripthonLines = getLines(scripthonSources);
        return new CompilerRunnable(scripthonLines);
    }

    private static List<String> getLines(String scripthonSources) {
        List<String> scripthonLines = new ArrayList<>();
        Scanner scanner = new Scanner(scripthonSources);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            scripthonLines.add(line);
        }
        return scripthonLines;
    }

    public static void createSources(String scripthonFile, String javaSourcesLocation) {
        List<String> scripthonLines = FileUtils.readFile(CompilationUtils.class.getResource(scripthonFile));
        Runnable compilerWorker = new CompilerRunnable(scripthonLines);
        final String javaSourcesPath = CompilationUtils.class.getResource(javaSourcesLocation).getPath();
        Runnable analyzerWorker = new AnalyzerRunnable(javaSourcesPath, "");

        CompilationUtils.compileSources(compilerWorker, analyzerWorker);
    }
}
