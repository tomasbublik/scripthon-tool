package cz.ctu.scripthon.tree.analyzer.tree;

import cz.ctu.scripthon.tree.analyzer.exception.DoNotFitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class AccessHelper {

    static final Logger LOG = LoggerFactory.getLogger(AccessHelper.class);
    private static AtomicInteger numberOfMethodCalls = new AtomicInteger();
    private static AtomicInteger numberOfMethodCalls0Parameters = new AtomicInteger();
    private static AtomicInteger numberOfMethodCalls1Parameters = new AtomicInteger();
    private static AtomicInteger numberOfMethodCalls2Parameters = new AtomicInteger();
    private static AtomicInteger numberOfMethodCalls3Parameters = new AtomicInteger();
    private static AtomicInteger numberOfMethodCallsMore3Parameters = new AtomicInteger();
    private static AtomicInteger numberOfPublicMethods = new AtomicInteger();
    private static AtomicInteger numberOfPrivateMethods = new AtomicInteger();
    private static AtomicInteger numberOfPrivateStaticMethods = new AtomicInteger();
    private static AtomicInteger numberOfPrivateStaticFinalMethods = new AtomicInteger();
    private static AtomicInteger numberOfStaticMethods = new AtomicInteger();
    private static AtomicInteger numberOfAbstractMethods = new AtomicInteger();
    private static AtomicInteger numberOfProtectedMethods = new AtomicInteger();
    private static AtomicInteger numberOfFinalMethods = new AtomicInteger();
    private static AtomicInteger numberOfMethods = new AtomicInteger();
    private static AtomicInteger numberOf0ParametersMethods = new AtomicInteger();
    private static AtomicInteger numberOf1ParametersMethods = new AtomicInteger();
    private static AtomicInteger numberOf2ParametersMethods = new AtomicInteger();
    private static AtomicInteger numberOf3ParametersMethods = new AtomicInteger();
    private static AtomicInteger numberOfMore3ParametersMethods = new AtomicInteger();
    private static AtomicInteger numberOfBlocks = new AtomicInteger();
    private static AtomicInteger numberOfBlocks0Statements = new AtomicInteger();
    private static AtomicInteger numberOfBlocks1Statements = new AtomicInteger();
    private static AtomicInteger numberOfBlocks2Statements = new AtomicInteger();
    private static AtomicInteger numberOfBlocks3Statements = new AtomicInteger();
    private static AtomicInteger numberOfBlocksMore3Statements = new AtomicInteger();
    private static AtomicInteger numberOfInit = new AtomicInteger();
    private static AtomicInteger numberOfClasses = new AtomicInteger();
    private static AtomicInteger numberOfPublicClasses = new AtomicInteger();
    private static AtomicInteger numberOfPrivateClasses = new AtomicInteger();
    private static AtomicInteger numberOfPrivateStaticClasses = new AtomicInteger();
    private static AtomicInteger numberOfPrivateFinalClasses = new AtomicInteger();
    private static AtomicInteger numberOfStaticClasses = new AtomicInteger();
    private static AtomicInteger numberOfAbstractClasses = new AtomicInteger();
    private static AtomicInteger numberOfProtectedClasses = new AtomicInteger();
    private static AtomicInteger numberOfFinalClasses = new AtomicInteger();
    private static AtomicInteger numberOfFinalStaticClasses = new AtomicInteger();
    private static AtomicInteger numberOfFinalStaticPrivateClasses = new AtomicInteger();
    private static Map<String, Node> classTrees = new HashMap<String, Node>();
    private volatile static AccessHelper accessHelper;
    private Node tree = null;

    //private static AccessHelper accessHelper;
    private boolean done = false;

    private AccessHelper() {
    }

    public static void clean() {
        accessHelper = null;
    }

    public static AccessHelper getInstance() {
        if (accessHelper == null) {
            synchronized (AccessHelper.class) {
                if (accessHelper == null) {
                    accessHelper = new AccessHelper();
                }
            }
        }
        return accessHelper;
    }

    public static Map<String, Node> getClassTrees() {
        return classTrees;
    }

    public static AtomicInteger getNumberOfMethodCalls() {
        return numberOfMethodCalls;
    }

    public static void appendNumberOfMethodCalls() {
        numberOfMethodCalls.incrementAndGet();
    }

    public static AtomicInteger getNumberOfMethods() {
        return numberOfMethods;
    }

    public static void appendNumberOfMethods() {
        numberOfMethods.incrementAndGet();
    }

    public static AtomicInteger getNumberOfBlocks() {
        return numberOfBlocks;
    }

    public static AtomicInteger getNumberOfInit() {
        return numberOfInit;
    }

    public static void appendNumberOfBlocks() {
        numberOfBlocks.incrementAndGet();
    }

    public static void appendNumberOfInit() {
        numberOfInit.incrementAndGet();
    }

    public static AtomicInteger getNumberOfMethodCalls0Parameters() {
        return numberOfMethodCalls0Parameters;
    }

    public static void addNumberOfMethodCalls0Parameters() {
        numberOfMethodCalls0Parameters.incrementAndGet();
    }

    public static AtomicInteger getNumberOfMethodCalls1Parameters() {
        return numberOfMethodCalls1Parameters;
    }

    public static void addNumberOfMethodCalls1Parameters() {
        numberOfMethodCalls1Parameters.incrementAndGet();
    }

    public static AtomicInteger getNumberOfMethodCalls2Parameters() {
        return numberOfMethodCalls2Parameters;
    }

    public static void addNumberOfMethodCalls2Parameters() {
        numberOfMethodCalls2Parameters.incrementAndGet();
    }

    public static AtomicInteger getNumberOfMethodCalls3Parameters() {
        return numberOfMethodCalls3Parameters;
    }

    public static void addNumberOfMethodCalls3Parameters() {
        numberOfMethodCalls3Parameters.incrementAndGet();
    }

    public static AtomicInteger getNumberOfMethodCallsMore3Parameters() {
        return numberOfMethodCallsMore3Parameters;
    }

    public static void addNumberOfMethodCallsMore3Parameters() {
        numberOfMethodCallsMore3Parameters.incrementAndGet();
    }

    public static AtomicInteger getNumberOfPublicMethods() {
        return numberOfPublicMethods;
    }

    public static void addNumberOfPublicMethods() {
        numberOfPublicMethods.incrementAndGet();
    }

    public static AtomicInteger getNumberOfPrivateMethods() {
        return numberOfPrivateMethods;
    }

    public static void addNumberOfPrivateMethods() {
        numberOfPrivateMethods.incrementAndGet();
    }

    public static void addNumberOfPrivateStaticMethods() {
        numberOfPrivateStaticMethods.incrementAndGet();
    }

    public static void addNumberOfPrivateStaticFinalMethods() {
        numberOfPrivateStaticFinalMethods.incrementAndGet();
    }

    public static AtomicInteger getNumberOfStaticMethods() {
        return numberOfStaticMethods;
    }

    public static void addNumberOfStaticMethods() {
        numberOfStaticMethods.incrementAndGet();
    }

    public static AtomicInteger getNumberOfAbstractMethods() {
        return numberOfAbstractMethods;
    }

    public static void addNumberOfAbstractMethods() {
        numberOfAbstractMethods.incrementAndGet();
    }

    public static AtomicInteger getNumberOfProtectedMethods() {
        return numberOfProtectedMethods;
    }

    public static void addNumberOfProtectedMethods() {
        numberOfProtectedMethods.incrementAndGet();
    }

    public static AtomicInteger getNumberOfFinalMethods() {
        return numberOfFinalMethods;
    }

    public static void addNumberOfFinalMethods() {
        numberOfFinalMethods.incrementAndGet();
    }

    public static AtomicInteger getNumberOf0ParametersMethods() {
        return numberOf0ParametersMethods;
    }

    public static void addNumberOf0ParametersMethods() {
        numberOf0ParametersMethods.incrementAndGet();
    }

    public static AtomicInteger getNumberOf1ParametersMethods() {
        return numberOf1ParametersMethods;
    }

    public static void addNumberOf1ParametersMethods() {
        numberOf1ParametersMethods.incrementAndGet();
    }

    public static AtomicInteger getNumberOf2ParametersMethods() {
        return numberOf2ParametersMethods;
    }

    public static void addNumberOf2ParametersMethods() {
        numberOf2ParametersMethods.incrementAndGet();
    }

    public static AtomicInteger getNumberOf3ParametersMethods() {
        return numberOf3ParametersMethods;
    }

    public static void addNumberOf3ParametersMethods() {
        numberOf3ParametersMethods.incrementAndGet();
    }

    public static AtomicInteger getNumberOfMore3ParametersMethods() {
        return numberOfMore3ParametersMethods;
    }

    public static void addNumberOfMore3ParametersMethods() {
        numberOfMore3ParametersMethods.incrementAndGet();
    }

    public static AtomicInteger getNumberOfBlocks0Statements() {
        return numberOfBlocks0Statements;
    }

    public static void addNumberOfBlocks0Statements() {
        numberOfBlocks0Statements.incrementAndGet();
    }

    public static AtomicInteger getNumberOfBlocks1Statements() {
        return numberOfBlocks1Statements;
    }

    public static void addNumberOfBlocks1Statements() {
        numberOfBlocks1Statements.incrementAndGet();
    }

    public static AtomicInteger getNumberOfBlocks2Statements() {
        return numberOfBlocks2Statements;
    }

    public static void addNumberOfBlocks2Statements() {
        numberOfBlocks2Statements.incrementAndGet();
    }

    public static AtomicInteger getNumberOfBlocks3Statements() {
        return numberOfBlocks3Statements;
    }

    public static void addNumberOfBlocks3Statements() {
        numberOfBlocks3Statements.incrementAndGet();
    }

    public static AtomicInteger getNumberOfBlocksMore3Statements() {
        return numberOfBlocksMore3Statements;
    }

    public static void addNumberOfBlocksMore3Statements() {
        numberOfBlocksMore3Statements.incrementAndGet();
    }

    public static AtomicInteger getNumberOfClasses() {
        return numberOfClasses;
    }

    public static void addNumberOfClasses() {
        numberOfClasses.incrementAndGet();
    }

    public static AtomicInteger getNumberOfPublicClasses() {
        return numberOfPublicClasses;
    }

    public static void addNumberOfPublicClasses() {
        numberOfPublicClasses.incrementAndGet();
    }

    public static AtomicInteger getNumberOfPrivateClasses() {
        return numberOfPrivateClasses;
    }

    public static void addNumberOfPrivateClasses() {
        numberOfPrivateClasses.incrementAndGet();
    }
    public static void addNumberOfPrivateStaticClasses() {
        numberOfPrivateStaticClasses.incrementAndGet();
    }
    public static void addNumberOfPrivateFinalClasses() {
        numberOfPrivateFinalClasses.incrementAndGet();
    }
    public static void addNumberOfPrivateFinalStaticClasses() {
        numberOfFinalStaticPrivateClasses.incrementAndGet();
    }

    public static AtomicInteger getNumberOfPrivateFinalClasses() {
        return numberOfPrivateFinalClasses;
    }

    public static AtomicInteger getNumberOfPrivateStaticClasses() {
        return numberOfPrivateStaticClasses;
    }

    public static AtomicInteger getNumberOfStaticClasses() {
        return numberOfStaticClasses;
    }

    public static void addNumberOfStaticClasses() {
        numberOfStaticClasses.incrementAndGet();
    }

    public static AtomicInteger getNumberOfAbstractClasses() {
        return numberOfAbstractClasses;
    }

    public static void addNumberOfAbstractClasses() {
        numberOfAbstractClasses.incrementAndGet();
    }

    public static AtomicInteger getNumberOfProtectedClasses() {
        return numberOfProtectedClasses;
    }

    public static void addNumberOfProtectedClasses() {
        numberOfProtectedClasses.incrementAndGet();
    }

    public static AtomicInteger getNumberOfFinalClasses() {
        return numberOfFinalClasses;
    }

    public static void addNumberOfFinalClasses() {
        numberOfFinalClasses.incrementAndGet();
    }

    public static void addNumberOfFinalStaticClasses() {
        numberOfFinalStaticClasses.incrementAndGet();
    }

    public void appendNode(Node node) {
        if (tree == null) {
            assert node.getNodeType().equals(NodeType.CLASS);
            tree = node;
        } else {
            try {
                //node.increaseLevel();
                tree.appendNode(node);
            } catch (DoNotFitException e) {
                e.printStackTrace(); //To change body of catch statement use File | Settings | File Templates.
            }
        }
        //LOG.debug(node + " to start: " + node.getLeft() + " and end: " + node.getRight());
    }

    public void addToMap(String name) {
        classTrees.put(name, tree);
        tree = null;
    }

    public void printTree() {
        //TODO
    }

    public boolean isDone() {
        return done;
    }

    @Override
    public String toString() {
        return "AccessHelper{" +
                "numberOfMethodCalls=" + numberOfMethodCalls +
                ", numberOfMethodCalls0Parameters=" + numberOfMethodCalls0Parameters +
                ", numberOfMethodCalls1Parameters=" + numberOfMethodCalls1Parameters +
                ", numberOfMethodCalls2Parameters=" + numberOfMethodCalls2Parameters +
                ", numberOfMethodCalls3Parameters=" + numberOfMethodCalls3Parameters +
                ", numberOfMethodCallsMore3Parameters=" + numberOfMethodCallsMore3Parameters +
                ", numberOfPublicMethods=" + numberOfPublicMethods +
                ", numberOfPrivateMethods=" + numberOfPrivateMethods +
                ", numberOfPrivatesStaticMethods=" + numberOfPrivateStaticMethods +
                ", numberOfPrivatesStaticFinalMethods=" + numberOfPrivateStaticFinalMethods +
                ", numberOfStaticMethods=" + numberOfStaticMethods +
                ", numberOfAbstractMethods=" + numberOfAbstractMethods +
                ", numberOfProtectedMethods=" + numberOfProtectedMethods +
                ", numberOfFinalMethods=" + numberOfFinalMethods +
                ", numberOfMethods=" + numberOfMethods +
                ", numberOf0ParametersMethods=" + numberOf0ParametersMethods +
                ", numberOf1ParametersMethods=" + numberOf1ParametersMethods +
                ", numberOf2ParametersMethods=" + numberOf2ParametersMethods +
                ", numberOf3ParametersMethods=" + numberOf3ParametersMethods +
                ", numberOfMore3ParametersMethods=" + numberOfMore3ParametersMethods +
                ", numberOfBlocks=" + numberOfBlocks +
                ", numberOfBlocks0Statements=" + numberOfBlocks0Statements +
                ", numberOfBlocks1Statements=" + numberOfBlocks1Statements +
                ", numberOfBlocks2Statements=" + numberOfBlocks2Statements +
                ", numberOfBlocks3Statements=" + numberOfBlocks3Statements +
                ", numberOfBlocksMore3Statements=" + numberOfBlocksMore3Statements +
                ", numberOfInit=" + numberOfInit +
                ", numberOfClasses=" + numberOfClasses +
                ", numberOfPublicClasses=" + numberOfPublicClasses +
                ", numberOfPrivateClasses=" + numberOfPrivateClasses +
                ", numberOfPrivateStaticClasses=" + numberOfPrivateStaticClasses +
                ", numberOfPrivateFinalClasses=" + numberOfPrivateFinalClasses +
                ", numberOfStaticClasses=" + numberOfStaticClasses +
                ", numberOfAbstractClasses=" + numberOfAbstractClasses +
                ", numberOfProtectedClasses=" + numberOfProtectedClasses +
                ", numberOfFinalClasses=" + numberOfFinalClasses +
                ", numberOfFinalStaticClasses=" + numberOfFinalStaticClasses +
                ", numberOfFinalStaticPrivateClasses=" + numberOfFinalStaticPrivateClasses +
                '}';
    }
}
