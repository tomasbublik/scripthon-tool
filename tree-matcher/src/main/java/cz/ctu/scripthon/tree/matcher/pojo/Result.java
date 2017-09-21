package cz.ctu.scripthon.tree.matcher.pojo;

import cz.ctu.scripthon.tree.analyzer.tree.Node;

import java.util.ArrayList;
import java.util.List;

public class Result {

    private List<SourceIdentification> sourceIdentifications;

    private List<Node> excludedNodes;

    private String errorMessage;

    private int numberOfComparisons;

    public Result() {
        sourceIdentifications = new ArrayList<>();
        excludedNodes = new ArrayList<>();
    }

    public Result(String errorMessage) {
        this.errorMessage = errorMessage;
    }


    public void addSourceIdentification(SourceIdentification sourceIdentification) {
        sourceIdentifications.add(sourceIdentification);
    }

    public List<SourceIdentification> getSourceIdentifications() {
        return sourceIdentifications;
    }

    public void addExcludedNode(Node node) {
        excludedNodes.add(node);
    }

    public List<Node> getExcludedNodes() {
        return excludedNodes;
    }

    public void setExcludedNodes(List<Node> excludedNodes) {
        this.excludedNodes = excludedNodes;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n");
        if (sourceIdentifications != null && !sourceIdentifications.isEmpty()) {
            stringBuilder.append("Number of results: ").append(sourceIdentifications.size());
            stringBuilder.append("\n");
            stringBuilder.append("Number of comparisons: ").append(numberOfComparisons);
            stringBuilder.append("\n");
            for (SourceIdentification sourceIdentification : sourceIdentifications) {
                stringBuilder.append("Class name: ").append(sourceIdentification.getName()).append(" on line: ").append(sourceIdentification.getLine()).append("\n");
            }
        } else {
            if (errorMessage != null) {
                if (errorMessage.isEmpty()) {
                    return "No result found";
                } else {
                    return errorMessage;
                }
            } else {
                return "No result found";
            }
        }
        return stringBuilder.toString();
    }

    public int getNumberOfComparisons() {
        return numberOfComparisons;
    }

    public void increaseNumberOfComparisons() {
        this.numberOfComparisons++;
    }
}
