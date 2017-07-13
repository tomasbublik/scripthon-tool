package cz.bublik.scripthon.tree.analyzer.statistics;

import cz.bublik.scripthon.tree.analyzer.tree.AccessHelper;
import cz.bublik.scripthon.tree.analyzer.tree.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class StatisticsAnalysis {

    static final Logger LOG = LoggerFactory.getLogger(StatisticsAnalysis.class);

    private static List<StatisticsHolder> createStatistics() {
        List<StatisticsHolder> statisticsHolders = new ArrayList<>();

        for (String className : AccessHelper.getClassTrees().keySet()) {
            StatisticsHolder statisticsHolder = new StatisticsHolder(className);
            Node classNode = AccessHelper.getClassTrees().get(className);
            traversePreRecursive(classNode, statisticsHolder);
            statisticsHolders.add(statisticsHolder);
        }

        return statisticsHolders;
    }

    private static void traversePreRecursive(Node node, StatisticsHolder statisticsHolder) {
        if (node != null) {
            statisticsHolder.increaseNodesNumber();
            if (node.getLevel() > statisticsHolder.getMaxDepth()) {
                statisticsHolder.setMaxDepth(node.getLevel());
            }
            final List<Node> subNodes = node.getNodes();
            if (subNodes != null && !subNodes.isEmpty()) {
                final int sizeOfSubNodes = subNodes.size();
                if (sizeOfSubNodes > statisticsHolder.getMaxBranchingFactor()) {
                    statisticsHolder.setMaxBranchingFactor(sizeOfSubNodes);
                }

                for (Node subNode : subNodes) {
                    traversePreRecursive(subNode, statisticsHolder);
                }
            }
        }
    }

    private static List<StatisticsHolder> sortByMaxDepth(List<StatisticsHolder> statisticsHolders) {
        return statisticsHolders.parallelStream().sorted(Comparator.comparing(StatisticsHolder::getMaxDepth).reversed()).collect(Collectors.toList());
    }

    private static List<StatisticsHolder> sortByMaxBranchingFactor(List<StatisticsHolder> statisticsHolders) {
        return statisticsHolders.parallelStream().sorted(Comparator.comparing(StatisticsHolder::getMaxBranchingFactor).reversed()).collect(Collectors.toList());
    }

    private static List<StatisticsHolder> sortByMaxNodesSize(List<StatisticsHolder> statisticsHolders) {
        return statisticsHolders.parallelStream().sorted(Comparator.comparing(StatisticsHolder::getNodesNumber).reversed()).collect(Collectors.toList());
    }


    private static int totalNodesNumber(List<StatisticsHolder> statisticsHolders) {
        return statisticsHolders.parallelStream().mapToInt(w -> w.getNodesNumber()).sum();
    }

    /**
     * Creates statistics necessary for thesis and Wolfram Mathematica tool
     *
     * @param sourcesName
     */
    public static void createTopLevelStatistics(String sourcesName) {
        LOG.debug("STATISTICS -------------------------");
        LOG.debug("What is the sources name?: " + sourcesName);
        List<StatisticsHolder> statisticsHolders = StatisticsAnalysis.createStatistics();
        int totalNodesNumber = totalNodesNumber(statisticsHolders);
        LOG.debug("Total nodes number is: " + totalNodesNumber);
        List<StatisticsHolder> sortedByMaxDepth = sortByMaxDepth(statisticsHolders);
        LOG.debug("Sorted by max depth: " + sortedByMaxDepth.stream().limit(10).collect(Collectors.toList()));
        LOG.debug("Depth for Mathematica: {" + sortedByMaxDepth.stream().map(s -> String.valueOf(s.getMaxDepth())).collect(Collectors.joining(", ")) + "}");
        List<StatisticsHolder> sortedByMaxBranchingFactor = sortByMaxBranchingFactor(statisticsHolders);
        LOG.debug("Sorted by max branching factor: " + sortedByMaxBranchingFactor.stream().limit(10).collect(Collectors.toList()));
        LOG.debug("Branching factor for Mathematica: {" + sortedByMaxBranchingFactor.stream().map(s -> String.valueOf(s.getMaxBranchingFactor())).collect(Collectors.joining(", ")) + "}");
        List<StatisticsHolder> sortedByMaxNodes = sortByMaxNodesSize(statisticsHolders);
        LOG.debug("Sorted by max nodes size: " + sortedByMaxNodes.stream().limit(10).collect(Collectors.toList()));
        LOG.debug("Max nodes for Mathematica: {" + sortedByMaxDepth.stream().map(s -> String.valueOf(s.getNodesNumber())).collect(Collectors.joining(", ")) + "}");
        LOG.debug("Finished all threads");
        LOG.debug("Trees number: " + AccessHelper.getClassTrees().size());
        LOG.debug("Statistic: " + AccessHelper.getInstance());
        LOG.debug("STATISTICS -------------------------");
    }

    private static List<StatisticsHolder> getByName(String name, List<StatisticsHolder> statisticsHolders) {
        return statisticsHolders.parallelStream().filter(w -> w.getClassName().equals(name)).collect(Collectors.toList());
    }

}
