package cz.ctu.scripthon.tree.analyzer.codeanalyzer.common.model;

/**
 * Stores location information of main elements of java class
 */
public interface Location {

    int getStartOffset();

    int getEndOffset();

    long getLineNumber();
}
