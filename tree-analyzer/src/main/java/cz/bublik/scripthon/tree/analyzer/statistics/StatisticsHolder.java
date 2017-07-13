package cz.bublik.scripthon.tree.analyzer.statistics;

public class StatisticsHolder {

    private int maxDepth;

    private int nodesNumber;

    private int maxBranchingFactor;

    private String className;

    public StatisticsHolder(String className) {
        this.className = className;
    }

    public void increaseNodesNumber() {
        nodesNumber++;
    }

    public int getMaxBranchingFactor() {
        return maxBranchingFactor;
    }

    public void setMaxBranchingFactor(int maxBranchingFactor) {
        this.maxBranchingFactor = maxBranchingFactor;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public void setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    public int getNodesNumber() {
        return nodesNumber;
    }

    public String getClassName() {
        return className;
    }

    @Override
    public String toString() {
        return "StatisticsHolder{" +
                "className='" + className +
                ", maxDepth=" + maxDepth +
                ", nodesNumber=" + nodesNumber +
                ", maxBranchingFactor=" + maxBranchingFactor + '\'' +
                '}';
    }
}
