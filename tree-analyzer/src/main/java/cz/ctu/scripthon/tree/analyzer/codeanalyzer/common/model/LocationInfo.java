package cz.ctu.scripthon.tree.analyzer.codeanalyzer.common.model;

import java.io.Serializable;

/**
 * Stores location information of main elements of java class
 */
public class LocationInfo implements Location, Serializable {

    private int startOffset;
    private int endOffset;
    private long lineNumber;

    public int getEndOffset() {
        return endOffset;
    }

    public void setEndOffset(int endOffset) {
        this.endOffset = endOffset;
    }

    public int getStartOffset() {
        return startOffset;
    }

    public void setStartOffset(int startOffset) {
        this.startOffset = startOffset;
    }

    public long getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(long lineNumber) {
        this.lineNumber = lineNumber;
    }

}
