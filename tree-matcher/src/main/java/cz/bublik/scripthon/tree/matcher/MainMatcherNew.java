package cz.bublik.scripthon.tree.matcher;

import cz.bublik.scripthon.compiler.heap.ComplexVariable;
import cz.bublik.scripthon.compiler.heap.Evaluator;
import cz.bublik.scripthon.compiler.heap.Memory;
import cz.bublik.scripthon.compiler.syntax.Statement;
import cz.bublik.scripthon.compiler.syntax.expressions.properties.*;
import cz.bublik.scripthon.compiler.syntax.statements.*;
import cz.bublik.scripthon.tree.analyzer.tree.Node;
import cz.bublik.scripthon.tree.analyzer.tree.NodeType;
import cz.bublik.scripthon.tree.matcher.pojo.Result;
import cz.bublik.scripthon.tree.matcher.pojo.SearchedNode;
import cz.bublik.scripthon.tree.matcher.pojo.SourceIdentification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import static cz.bublik.scripthon.compiler.syntax.SupportedStructures.*;
import static cz.bublik.scripthon.compiler.syntax.SupportedStructures.BLOCK;
import static cz.bublik.scripthon.compiler.syntax.SupportedStructures.CLASS;
import static cz.bublik.scripthon.tree.analyzer.tree.NodeType.*;
import static cz.bublik.scripthon.tree.matcher.pojo.SearchedNode.DefaultIndex.NEVER_SET;
import static cz.bublik.scripthon.tree.matcher.pojo.SearchedNode.DefaultIndex.PARENT;

public class MainMatcherNew {

    private static final boolean CHECK_STRUCTURES_PROPERTIES = true;
    private static final boolean EVALUATE_EXPRESSIONS = true;
    private static final Logger LOG = LoggerFactory.getLogger(MainMatcherNew.class);
    private static final boolean DEBUG_MODE = false;
    private static final String RETURNING_NULL_BECAUSE = "Returning null because: ";
    private static final String RETURNING_ZERO_BECAUSE = "Returning 0 because: ";
    private static final String IN_METHOD = " in method: ";
    private static Result result;
    private static Memory memory;
    private static Evaluator evaluator;
    private static ResultContainer resultContainer = null;

    public static Result compare(Map<String, Node> classTrees, Program scripthonProgram, JProgressBar jProgressBar, JLabel notificationField) {
        result = new Result();
        memory = Memory.createMemory();
        evaluator = new Evaluator(memory);
        int doneNumber = 0;

        for (Map.Entry<String, Node> javaSourceNodeEntry : classTrees.entrySet()) {
            doneNumber = updateProgressBar(classTrees, jProgressBar, notificationField, doneNumber, javaSourceNodeEntry);

            if (resultContainer != null) {
                resultContainer.index = -1;
            }
            findGivenClassAgainstScripthonProgram(scripthonProgram, javaSourceNodeEntry);
        }
        return result;
    }

    private static void findGivenClassAgainstScripthonProgram(Program scripthonProgram, Map.Entry<String, Node> javaSourceNodeEntry) {
        List<Statement> statementsOfProgram = scripthonProgram.getStatements();

        // must be the root element at the first pla. here is it "Class" itself
        final Node initialRootNode = javaSourceNodeEntry.getValue();
        Node nodeToStartWith = initialRootNode;
        SearchedNode firstFoundedNode;

        do {
            firstFoundedNode = null;
            for (int i = 0; i < statementsOfProgram.size(); i++) {
                Statement scripthonStatement = statementsOfProgram.get(i);

                SearchedNode foundedNode;
                if (!isAfterAnyStatement(statementsOfProgram, i)) {
                    if (isAnIfStatement(scripthonStatement)) {
                        Statement innerIfStatement = evaluateIfStatement(scripthonStatement).block.getStatements().iterator().next();
                        foundedNode = findMatchRecursively(innerIfStatement, nodeToStartWith);
                    } else {
                        foundedNode = findMatchRecursively(scripthonStatement, nodeToStartWith);
                        //wasn't found in the recursion and it is the same as the "nodeToStartWith"
                        if (indexNeverSet(foundedNode)) {
                            foundedNode.setIndexOfChild(PARENT.getIndex());
                        }
                    }
                } else {
                    //is after Any() statement, so we have to find the first corresponding match non recursively
                    foundedNode = getFirstAfterAnySearchedNode(initialRootNode, nodeToStartWith, scripthonStatement);
                }

                if (foundedNode.isFound()) {
                    if (!isTheLastIndex(statementsOfProgram, i) && !isParent(foundedNode)) {
                        if (hasMoreChildren(foundedNode)) {
                            nodeToStartWith = takeFollowingChild(foundedNode);
                        } else {
                            //there are no additional nodes left, but we've still statements left
                            //so maybe if the next statement id Any()???
                            final Node lastNodeFound = takeChild(foundedNode);
                            nodeToStartWith = pickUpTheRightMost(initialRootNode, lastNodeFound.getLeft(), lastNodeFound.getRight(), true);
                            break;
                        }
                    }

                    if (i == 0) {
                        firstFoundedNode = foundedNode;
                        if (isTheSameNode(nodeToStartWith, foundedNode) && isParent(foundedNode)) {
                            nodeToStartWith = pickUpTheRightMost(initialRootNode, nodeToStartWith.getLeft(), nodeToStartWith.getRight(), false);
                        }
                    }

                    if (isTheLastIndex(statementsOfProgram, i)) {
                        //this must be always true, otherwise the search process was useless
                        if (firstFoundedNode != null) {
                            addToResults(javaSourceNodeEntry, firstFoundedNode);

                            if (!isParent(firstFoundedNode)) {
                                //if has kids, pick up the first one
                                if (hasChildren(firstFoundedNode)) {
                                    nodeToStartWith = takeTheVeryFirstChild(firstFoundedNode);
                                } else {
                                    //now we have to pick up the rightmost one
                                    nodeToStartWith = pickUpTheRightMost(initialRootNode, takeChild(firstFoundedNode).getLeft(), takeChild(firstFoundedNode).getRight(), true);
                                }
                                //if nothing was found, there's no point to continue
                                if (nodeToStartWith == null) {
                                    firstFoundedNode = null;
                                }
                            } else {
                                firstFoundedNode = null;
                                if (nodeToStartWith != null) {
                                    nodeToStartWith = pickUpTheRightMost(initialRootNode, nodeToStartWith.getLeft(), nodeToStartWith.getRight(), true);
                                }
                            }
                        }
                    }

                } else {
                    //does it make sense to continue if one statement wasn't found ???
                    //yes it does - if nothing was found, lets try to pick up the rightmost of the last nodeToStartWith
                    //now we have to pick up the rightmost one
                    //and here is the only place where we don't consider children, because those would be detected by the recursive algorithm already
                    nodeToStartWith = pickUpTheRightMost(initialRootNode, nodeToStartWith.getLeft(), nodeToStartWith.getRight(), false);
                    if (nodeToStartWith == null) {
                        firstFoundedNode = null;
                    }
                }
                if (firstFoundedNode == null) {
                    //if there is even no first match with the first statement, it doesn't make any sense to continue
                    break;
                }
            }
            //of course, the loop ends when not even the first one was found, or when there's no node to start the search
        } while (firstFoundedNode != null || nodeToStartWith != null);
    }

    private static SearchedNode getFirstAfterAnySearchedNode(Node initialRootNode, Node nodeToStartWith, Statement scripthonStatement) {
        SearchedNode foundedNode;
        foundedNode = new SearchedNode();
        Node parent = pickUpParent(initialRootNode, nodeToStartWith.getLeft(), nodeToStartWith.getRight());
        if (parent != null) {
            final int indexToStartWith = parent.getNodes().indexOf(nodeToStartWith);
            int index = findFirstMatchNonRecursive(scripthonStatement, parent, indexToStartWith);
            if (index != 0) {
                foundedNode = new SearchedNode(true, parent);
                //because there is "int result = i - index;" inside the findFirstMatchNonRecursive method
                foundedNode.setIndexOfChild(index + indexToStartWith);
            }
        }
        return foundedNode;
    }

    private static boolean isAfterAnyStatement(List<Statement> statementsOfProgram, int i) {
        return (i - 1) >= 0 && isAnyStatement(statementsOfProgram.get(i - 1));
    }

    private static boolean indexNeverSet(SearchedNode foundedNode) {
        return foundedNode.getIndexOfChild() == NEVER_SET.getIndex();
    }

    private static Node takeTheVeryFirstChild(SearchedNode firstFoundedNode) {
        return takeChild(firstFoundedNode).getNodes().get(0);
    }

    private static boolean hasChildren(SearchedNode firstFoundedNode) {
        return takeChild(firstFoundedNode).getNodes() != null && !takeChild(firstFoundedNode).getNodes().isEmpty();
    }

    private static void addToResults(Map.Entry<String, Node> javaSourceNodeEntry, SearchedNode firstFoundedNode) {
        SourceIdentification sourceIdentification = null;
        if (firstFoundedNode.getIndexOfChild() != PARENT.getIndex()) {
            sourceIdentification = new SourceIdentification(javaSourceNodeEntry.getKey(), takeChild(firstFoundedNode).getSourceLine(), takeChild(firstFoundedNode));
        } else {
            sourceIdentification = new SourceIdentification(javaSourceNodeEntry.getKey(), firstFoundedNode.getParent().getSourceLine(), firstFoundedNode.getParent());
        }

        result.addSourceIdentification(sourceIdentification);
    }

    private static boolean isTheSameNode(Node nodeToStartWith, SearchedNode foundedNode) {
        return nodeToStartWith.getRight() == foundedNode.getParent().getRight() && nodeToStartWith.getLeft() == foundedNode.getParent().getLeft();
    }

    private static Node takeChild(SearchedNode foundedNode) {
        return takeChildFromIndex(foundedNode, foundedNode.getIndexOfChild());
    }

    private static Node takeFollowingChild(SearchedNode foundedNode) {
        return takeChildFromIndex(foundedNode, foundedNode.getIndexOfChild() + 1);
    }

    private static Node takeChildFromIndex(SearchedNode foundedNode, int index) {
        return foundedNode.getParent().getNodes().get(index);
    }

    private static boolean hasMoreChildren(SearchedNode foundedNode) {
        return foundedNode.getParent().getNodes().size() > (foundedNode.getIndexOfChild() + 1);
    }

    private static boolean isParent(SearchedNode foundedNode) {
        return foundedNode.getIndexOfChild() == PARENT.getIndex();
    }

    private static Node pickUpTheRightMost(Node root, long left, long right, boolean considerChildren) {
        Node pickedUp = null;
        if (root.getNodes() != null && !root.getNodes().isEmpty()) {
            for (Node node : root.getNodes()) {
                if (left < node.getRight()) {
                    if (right < node.getLeft()) {
                        pickedUp = node;
                    } else {
                        pickedUp = pickUpTheRightMost(node, left, right, considerChildren);
                    }
                }
                //if it is the same, test kids, if there are any, pick up the first one
                if (left == node.getLeft() && right == node.getRight()) {
                    if (node.getNodes() != null && !node.getNodes().isEmpty()) {
                        //only if we consider children
                        if (considerChildren) {
                            pickedUp = node.getNodes().get(0);
                        }
                    } else {
                        break;
                    }
                }
                if (pickedUp != null) {
                    return pickedUp;
                }
            }
        }
        return pickedUp;
    }

    private static Node pickUpParent(Node root, long left, long right) {
        Node pickedUp = null;
        if (root.getNodes() != null && !root.getNodes().isEmpty()) {
            for (Node node : root.getNodes()) {
                if (left == node.getLeft() && right == node.getRight()) {
                    return root;

                }
                pickedUp = pickUpParent(node, left, right);
                if (pickedUp != null) {
                    return pickedUp;
                }
            }
        }
        return pickedUp;
    }

    private static List<Node> getNodesContainedInResults() {
        if (resultContainer == null) {
            return Collections.emptyList();
        }
        return resultContainer.parentNode.getNodes();
    }

    private static boolean endNotReached(int offset, int i) {
        return currentIndexWithOffset(offset, i) <= getNodesContainedInResults().size() - 1;
    }

    private static boolean isValidPosition(int position) {
        return position != -1;
    }

    private static int currentIndexWithOffset(int offset, int i) {
        return i + offset;
    }

    /**
     * Finds the index of the first findGivenClassAgainstScripthonProgram for the given statement and the given node with given offset (index)
     *
     * @param statement
     * @param top
     * @param index
     * @return
     */
    private static int findFirstMatchNonRecursive(Statement statement, Node top, int index) {
        final String methodName = "findFirstMatchNonRecursive()";
        if (isAStructure(statement)) {
            for (int i = index; i < top.getNodes().size(); i++) {
                Node tempNode = top.getNodes().get(i);
                if (evaluateStatementAndCompareWithNode(statement, tempNode)) {
                    int result = i - index;
                    logDebug("Returning after evaluateStatementAndCompareWithNode: " + statement + " to: " + top + " while " + methodName);
                    return result;
                }
            }
        }
        logDebug("Returning 0 in: " + statement + " to: " + top + " while " + methodName);
        return 0;
    }

    private static SearchedNode findMatchRecursively(Statement statement, Node top) {
        final String methodName = "findMatchRecursively()";
        if (isAStructure(statement)) {
            if (evaluateStatementAndCompareWithNode(statement, top)) {
                if (compareInnerContent(statement, top, 0) == -1) {
                    logDebug("Inner content of: " + statement + " does not fit to: " + top + " while " + methodName);
                    return new SearchedNode(false, top);
                }
                return new SearchedNode(true, top);
            }

            //if the top and statement are not equal, take the node's children
            //and do the same recursively
            final List<Node> subNodes = top.getNodes();
            if (listNotEmpty(subNodes)) {
                for (int i = 0; i < subNodes.size(); i++) {
                    SearchedNode founded = findMatchRecursively(statement, subNodes.get(i));
                    if (founded.isFound() && indexNeverSet(founded)) {
                        founded.setParent(top);
                        founded.setIndexOfChild(i);
                    }
                    return founded;
                }
            }
            logDebug(RETURNING_NULL_BECAUSE + top + " has empty nodes while " + methodName);
        }
        logDebug(RETURNING_NULL_BECAUSE + statement + " is not instance of Structure " + methodName);
        return new SearchedNode(false, top);
    }

    private static boolean listNotEmpty(List list) {
        return list != null && !list.isEmpty();
    }

    private static Node compareStatementAndJavaNodeFlat(Statement statement, Node node) {
        final String methodName = "compareStatementAndJavaNodeFlat()";
        if (isAStructure(statement)) {
            if (evaluateStatementAndCompareWithNode(statement, node)) {
                if (compareInnerContent(statement, node, 0) != -1) {
                    return node;
                }
            }
        }
        logDebug(RETURNING_NULL_BECAUSE + statement + " has no match inside " + methodName);
        return null;
    }

    private static Statement evaluateIfStatement(Statement<IfStatement> statement) {
        final String methodName = "evaluateIfStatement()";
        logDebug("Evaluating following: " + statement + IN_METHOD + methodName);
        //TODO once this will work, than set it to false
        final IfStatement ifStatement = (IfStatement) statement;
        boolean evaluationResult;
        //if expression evaluation is disabled, if statement expression is considered true
        if (!EVALUATE_EXPRESSIONS) {
            evaluationResult = true;
        } else {
            evaluationResult = ifStatement.conditionExpression.eval(evaluator);
        }

        Statement statementToReturn = null;
        if (evaluationResult) {
            statementToReturn = statement;
        } else {
            if (ifStatement.elseStatement != null) {
                statementToReturn = ifStatement.elseStatement;
            }
        }
        return statementToReturn;
    }

    private static int compareInnerContent(Statement scripthonStatement, Node javaSourceNode, int mainOffset) {
        result.increaseNumberOfComparisons();
        final String methodName = "compareInnerContent()";
        int finalPosition = -1;
        Block block = getBlockOfStatement(scripthonStatement);
        final List<Node> subNodes = javaSourceNode.getNodes();
        if (block != null) {
            if (subNodes == null) {
                logDebug("Returning -1 because list of subnodes of  " + javaSourceNode + " is null in method: " + methodName);
                return -1;
            }
            Vector<Statement> subStatements = block.statements;
            Node foundedNode = null;
            boolean containsAnyStatement = containsAnyStatement(subStatements);

            //If the number of subNodes is smaller than the number of subStatements without Any(), it does make not sense to continue
            if ((subNodes.size() >= subStatements.size()) || containsAnyStatement(subStatements)) {
                int firstStatement = 0;
                int offset = 0;
                for (int i = firstStatement; i < subStatements.size(); i++) {
                    Statement currentSubStatement = subStatements.get(i);
                    if (isAnyAndStructure(currentSubStatement)) {
                        //If there is the Any() statement, roll to the index after Any()
                        //Any() at first place
                        if (i == firstStatement) {
                            //Any() is the only scripthonStatement, then return 0, because anything is considered equal to Any()
                            if (subStatements.size() == 1) {
                                return 0;
                            }
                            //Any is not the only statement, so we're looking at least for the second one
                            firstStatement++;
                            offset += (findFirstMatchNonRecursive(getNextSubStatement(subStatements, i), javaSourceNode, currentIndexWithOffset(offset, i)) - 1);
                        } else {
                            //if Any() was the last scripthonStatement
                            if (isTheLastIndex(subStatements, i)) {
                                //TODO - try to have Any() at the end
                                logDebug("Returning -1 because Any()  " + currentSubStatement + " is at the end of: " + scripthonStatement);
                                return -1;
                            } else {
                                offset += (findFirstMatchNonRecursive(getNextSubStatement(subStatements, i), javaSourceNode, currentIndexWithOffset(offset, i)) - 1);
                            }
                        }
                        continue;
                    }
                    //index to determine which node corresponds to the first statement after Any()
                    final int index = i + offset + mainOffset;
                    Node temporaryNode = null;
                    if (subNodes.size() > index) {
                        temporaryNode = subNodes.get(index);
                    } else {
                        //for case where Any() consumed all the subnodes, and no subnode left for comparing
                        logDebug(RETURNING_ZERO_BECAUSE + javaSourceNode + " has not enough subnodes" +
                                " for comparing with: " + scripthonStatement + ", therefore consider them matching in method: " + methodName);
                        //only if currentSubStatement is the last scripthonStatement
                        if (isTheLastIndex(subStatements, i)) {
                            return -1;
                        } else {
                            LOG.info("Could this ever occur?");
                        }
                    }
                    if (temporaryNode != null) {
                        if (isAnIfStatement(currentSubStatement)) {
                            int position = compareInnerContent(evaluateIfStatement(currentSubStatement), javaSourceNode, index);
                            //position tell us how many subnodes shall be jumped over (cause its were matched in ifStatement)
                            if (isValidPosition(position)) {
                                foundedNode = temporaryNode;
                                offset += (position - 1);
                            } else {
                                foundedNode = null;
                            }
                        } else {
                            foundedNode = compareStatementAndJavaNodeFlat(currentSubStatement, temporaryNode);
                        }
                    } else {
                        //until this point every scripthonStatement equals javaSourceNode
                        logDebug("Returning -1 because temporaryNode=null while comparing list of subnodes of " + javaSourceNode + " " +
                                " with subStatements of: " + scripthonStatement + " with index=" + index + IN_METHOD + methodName);
                        return -1;
                    }
                    if (foundedNode == null) {
                        logDebug("Returning -1 because foundedNode=null while comparing list of subnodes of " + javaSourceNode + " " +
                                " with subStatements of: " + scripthonStatement + " with index=" + index + IN_METHOD + methodName);
                        return -1;
                    } else {
                        finalPosition = index;
                    }
                }
            } else {
                logDebug("Returning -1 because while comparing list of subnodes of " + javaSourceNode + " " +
                        " with subStatements of: " + scripthonStatement + " does not following condition" +
                        "((subNodes.size() >= subStatements.size()) || containsAnyStatement) in method: " + methodName);
                return -1;
            }
        } else {
            if (listNotEmpty(subNodes)) {
                if (subNodes.size() == 1 && isBlockNode(subNodes.get(0))) {
                    if (subNodes.get(0).getNodes() == null) {
                        logDebug(RETURNING_ZERO_BECAUSE + javaSourceNode + " has only one block with zero subnodes" +
                                " while comparing with: " + scripthonStatement + IN_METHOD + methodName);                        //the javaSourceNode contains only empty BLOCK => positive findGivenClassAgainstScripthonProgram
                        return 0;
                    }
                    if (scripthonStatement.getStatements() == null || scripthonStatement.getStatements().isEmpty()) {
                        logDebug(RETURNING_ZERO_BECAUSE + javaSourceNode + " has only one block with zero subnodes" +
                                " and scripthonStatement: " + scripthonStatement + " does not have additional structures in method: " + methodName);                        //the javaSourceNode contains only empty BLOCK => positive findGivenClassAgainstScripthonProgram
                        return 0;
                    }
                }
                //for example, comparing: Long var = Long.valueOf(3); with Init(Name="var")
                //so, this variable is initialized by method call
                if (subNodes.size() == 1 && isMethodCallNode(subNodes.get(0))) {
                    logDebug(RETURNING_ZERO_BECAUSE + javaSourceNode + " has only METHCALL" +
                            " while comparing with: " + scripthonStatement + IN_METHOD + methodName);
                    return 0;
                }
            } else {
                logDebug(RETURNING_ZERO_BECAUSE + javaSourceNode + " has zero subnodes" +
                        " while comparing with: " + scripthonStatement + IN_METHOD + methodName);                //block of the scripthonStatement is null, nodes of the javaSourceNode is null => positive findGivenClassAgainstScripthonProgram
                return 0;
            }
        }
        logDebug("Finally returning 0 while comparing " + javaSourceNode + " with: " + scripthonStatement + IN_METHOD + methodName);
        return finalPosition;
    }

    private static Statement getNextSubStatement(Vector<Statement> subStatements, int i) {
        return subStatements.get(currentIndexWithOffset(1, i));
    }

    private static Block getBlockOfStatement(Statement scripthonStatement) {
        if (isAStructure(scripthonStatement)) {
            return ((Structure) scripthonStatement).block;
        }
        if (isAnIfStatement(scripthonStatement)) {
            return ((IfStatement) scripthonStatement).block;
        }
        return null;
    }

    private static boolean containsAnyStatement(Vector<Statement> statements) {
        result.increaseNumberOfComparisons();
        final String methodName = "containsAnyStatement()";
        for (Statement statement : statements) {
            if (isAnyAndStructure(statement)) {
                logDebug("List of following statements: " + statements + " contains Any() in method: " + methodName);
                return true;
            }
        }
        return false;
    }

    private static boolean evaluateStatementAndCompareWithNode(Statement scripthonStatement, Node javaSourcesNode) {
        result.increaseNumberOfComparisons();
        if (EVALUATE_EXPRESSIONS) {
            evaluateStatement(scripthonStatement);
        }

        //in case that
        if (isStatementClass(scripthonStatement) && isNodeClass(javaSourcesNode)) {
            if (!CHECK_STRUCTURES_PROPERTIES) return true;
            ClassDefinition classDefinition = (ClassDefinition) ((Structure) scripthonStatement).definition;
            return statementAndNodeHaveSameName(javaSourcesNode, classDefinition) && checkRestrictions(javaSourcesNode, classDefinition);
        }

        if (isStatementInit(scripthonStatement) && isNodeTypeVar(javaSourcesNode)) {
            if (!CHECK_STRUCTURES_PROPERTIES) return true;
            InitDefinition initDefinition = (InitDefinition) scripthonStatement.definition;
            return statementAndNodeHaveSameName(javaSourcesNode, initDefinition);
        }

        if (isMethodStatement(scripthonStatement) && isNodeMethod(javaSourcesNode)) {
            if (!CHECK_STRUCTURES_PROPERTIES) return true;
            MethDefinition methDefinition = (MethDefinition) scripthonStatement.definition;
            return statementAndNodeHaveSameName(javaSourcesNode, methDefinition) && checkRestrictions(javaSourcesNode, methDefinition);
        }
        if (isMethodCallStatement(scripthonStatement) && isMethodCallNode(javaSourcesNode)) {
            if (!CHECK_STRUCTURES_PROPERTIES) return true;
            MethCallDefinition methCallDefinition = (MethCallDefinition) scripthonStatement.definition;
            return statementAndNodeHaveSameName(javaSourcesNode, methCallDefinition);
        }

        if (isBlockStatement(scripthonStatement) && isBlockNode(javaSourcesNode)) {
            if (!CHECK_STRUCTURES_PROPERTIES) return true;
            //TODO block comparison parameters
            return true;
        }

        if (isAnyAndStructure(scripthonStatement)) {
            return true;
        }

        return false;
    }

    private static void evaluateStatement(Statement statement) {
        if (isAVariableInitializationStatement(statement)) {
            ComplexVariable complexVariable = new ComplexVariable(((InitVariable) statement).i0.f0, ((InitVariable) statement).type, ((InitVariable) statement).definition);
            memory.saveVariable(complexVariable);
        }
    }

    static class ResultContainer {
        int index;
        Node parentNode;

        ResultContainer(int index, Node parentNode) {
            this.index = index;
            this.parentNode = parentNode;
        }
    }

    private static int updateProgressBar(Map<String, Node> classTrees, JProgressBar jProgressBar, JLabel notificationField, int doneNumber, Map.Entry clazz) {
        int x = (int) (((double) doneNumber / (double) classTrees.entrySet().size()) * 100);
        jProgressBar.setValue(x);
        jProgressBar.setString(String.valueOf(x) + " %");
        doneNumber++;
        notificationField.setText((String) clazz.getKey());
        return doneNumber;
    }

    private static boolean isBlockNode(Node javaSourcesNode) {
        return javaSourcesNode.getNodeType().equals(NodeType.BLOCK);
    }

    private static boolean isBlockStatement(Statement scripthonStatement) {
        return scripthonStatement.type.equals(BLOCK);
    }

    private static boolean isMethodCallNode(Node javaSourcesNode) {
        return javaSourcesNode.getNodeType().equals(METHCALL);
    }

    private static boolean isMethodCallStatement(Statement scripthonStatement) {
        return scripthonStatement.type.equals(METHOD_CALL);
    }

    private static boolean isNodeMethod(Node javaSourcesNode) {
        return javaSourcesNode.getNodeType().equals(METH);
    }

    private static boolean isMethodStatement(Statement scripthonStatement) {
        return scripthonStatement.type.equals(METHOD);
    }

    private static boolean statementAndNodeHaveSameName(Node javaSourcesNode, Definition definition) {
        if (javaSourcesNode == null) {
            return false;
        }

        //When no name is defined, every name matches
        if (definition == null || definition.getName() == null) {
            return true;
        }

        if (javaSourcesNode.getName() == null) {
            return false;
        }
        return definition.getName().equals(javaSourcesNode.getName());
    }

    private static boolean checkRestrictions(Node node, Definition definition) {
        Rest rest = definition.getRest();
        if (rest == null) {
            return true;
        }
        if (node.getNodeInfo().isPrivate() && !(rest.equals(Rest.PRIVATE))) {
            return false;
        }
        if (node.getNodeInfo().isPublic() && !(rest.equals(Rest.PUBLIC))) {
            return false;
        }
        return !(node.getNodeInfo().isProtected() && !(rest.equals(Rest.PROTECTED)));
    }

    private static boolean isAnIfStatement(Statement scripthonStatement) {
        return scripthonStatement instanceof IfStatement;
    }

    private static boolean isAStructure(Statement scripthonStatement) {
        return scripthonStatement instanceof Structure;
    }

    private static boolean isAVariableInitializationStatement(Statement statement) {
        return statement instanceof InitVariable;
    }

    private static boolean isAnyStatement(Statement statement) {
        return statement.type.equals(ANY);
    }

    private static boolean isAnyAndStructure(Statement scripthonStatement) {
        return isAStructure(scripthonStatement) && isAnyStatement(scripthonStatement);
    }

    private static boolean isStatementClass(Statement scripthonStatement) {
        return scripthonStatement.type.equals(CLASS);
    }

    private static boolean isStatementInit(Statement scripthonStatement) {
        return scripthonStatement.type.equals(INIT);
    }

    private static boolean isNodeClass(Node javaSourcesNode) {
        return javaSourcesNode.getNodeType().equals(NodeType.CLASS);
    }

    private static boolean isNodeTypeVar(Node javaSourcesNode) {
        return javaSourcesNode.getNodeType().equals(VAR);
    }

    private static boolean isTheLastIndex(List<Statement> statementsOfProgram, int i) {
        return i == statementsOfProgram.size() - 1;
    }

    private static void logDebug(String msg) {
        if (DEBUG_MODE) {
            LOG.debug(msg);
        }
    }
}
