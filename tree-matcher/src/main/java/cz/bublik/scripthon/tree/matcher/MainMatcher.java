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
import cz.bublik.scripthon.tree.matcher.pojo.SourceIdentification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.util.*;

import static cz.bublik.scripthon.compiler.syntax.SupportedStructures.*;
import static cz.bublik.scripthon.compiler.syntax.SupportedStructures.BLOCK;
import static cz.bublik.scripthon.compiler.syntax.SupportedStructures.CLASS;
import static cz.bublik.scripthon.tree.analyzer.tree.NodeType.*;

/**
 * @deprecated, old and obsolete algorithm
 */
@Deprecated
public class MainMatcher {

    private static final boolean CHECK_STRUCTURES_PROPERTIES = true;
    private static final boolean EVALUATE_EXPRESSIONS = true;
    private static final Logger LOG = LoggerFactory.getLogger(MainMatcher.class);
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

            boolean matched = true;
            List<Node> excludedNodes = null;
            while (matched) {
                if (resultContainer != null) {
                    resultContainer.index = -1;
                }
                matched = findGivenClassNameAgainstScripthonProgram(scripthonProgram, javaSourceNodeEntry, excludedNodes);
                //TODO - possible optimisation - always add only the last one ot the ones from the current class
                //excludedNodes = getNodesFromSourceIdentification(result.getSourceIdentifications());
                //TODO - do something like getExcludedNodesForCurrentClass
                excludedNodes = result.getExcludedNodes();
            }
        }
        return result;
    }

    private static List<Node> getNodesFromSourceIdentification(List<SourceIdentification> sourceIdentifications) {
        List<Node> returnNodes = new ArrayList<>();
        for (SourceIdentification next : sourceIdentifications) {
            returnNodes.add(next.getFoundedNode());
        }
        return returnNodes;
    }

    private static boolean findGivenClassNameAgainstScripthonProgram(Program scripthonProgram, Map.Entry<String, Node> javaSourceNodeEntry, List<Node> excludedNodes) {
        //pessimistic rule - just one mismatch to disable following operations
        boolean founded = false;
        SourceIdentification foundedResult = null;
        List<Statement> statementsOfProgram = scripthonProgram.getStatements();
        Node foundedNode = null;
        int firstStatementIndex = 0;
        int offset = 0;
        //iterate over all statements
        for (int i = firstStatementIndex; i < statementsOfProgram.size(); i++) {
            Statement scripthonStatement = statementsOfProgram.get(i);
            //If there is the Any() statement, roll to the firstStatementIndex after Any()
            if (isAnyAndStructure(scripthonStatement)) {
                //Any() at first place
                if (i != firstStatementIndex) {
                    //if Any() was last statement
                    if (isTheLastIndex(statementsOfProgram, i)) {
                        //TODO - it is necessary to test Any() at the end
                        foundedResult = new SourceIdentification(javaSourceNodeEntry.getKey(), foundedNode.getSourceLine(), foundedNode);
                        //FIXME this is probably not necessary when this is the last index anyway
                        //break;
                    } else {
                        offset += (findFirstMatchNonRecursive(statementsOfProgram.get(currentIndexWithOffset(1, i)), resultContainer.parentNode, currentIndexWithOffset(offset, i)) - 1);
                    }
                } else {
                    firstStatementIndex++;
                }
                continue;
            }
            //search first findGivenClassNameAgainstScripthonProgram - to detect where to start the rest of matching
            if (i == firstStatementIndex) {
                if (isAnIfStatement(scripthonStatement)) {
                    Statement innerIfStatement = evaluateIfStatement(scripthonStatement).block.getStatements().iterator().next();
                    findFirstMatchRecursively(innerIfStatement, javaSourceNodeEntry.getValue(), 0, excludedNodes);
                } else {
                    findFirstMatchRecursively(scripthonStatement, javaSourceNodeEntry.getValue(), 0, excludedNodes);
                }
                if (resultContainer != null) {
                    if (isValidPosition(resultContainer.index)) {
                        foundedNode = getNodesContainedInResults().get(resultContainer.index);
                        offset = resultContainer.index;
                    } else {
                        if (isAnIfStatement(scripthonStatement)) {
                            //TODO finish the case where a zero can be at the end
                            int position = compareInnerContent(evaluateIfStatement(scripthonStatement), javaSourceNodeEntry.getValue(), 0);
                            //position tell us how many subnodes shall be jumped over (cause its were matched in ifStatement)
                            if (isValidPosition(position)) {
                                foundedNode = javaSourceNodeEntry.getValue();
                                offset += (position - 1);
                            } else {
                                foundedNode = null;
                            }
                        } else {
                            foundedNode = compareStatementAndJavaNodeFlat(scripthonStatement, javaSourceNodeEntry.getValue());
                        }
                    }
                }
            } else {
                foundedNode = null;
                if (isAnIfStatement(scripthonStatement)) {
                    int position = compareInnerContent(evaluateIfStatement(scripthonStatement), resultContainer.parentNode, currentIndexWithOffset(offset, i));
                    //position tell us how many subnodes shall be jumped over (cause its were matched in ifStatement)
                    if (isValidPosition(position) && endNotReached(offset, i)) {
                        foundedNode = getNodesContainedInResults().get(currentIndexWithOffset(offset, i));
                        offset += (position - 1);
                    }
                } else {
                    //this can occur, for example, in an empty class
                    if (listNotEmpty(getNodesContainedInResults()) && endNotReached(offset, i)) {
                        foundedNode = compareStatementAndJavaNodeFlat(scripthonStatement, getNodesContainedInResults().get(currentIndexWithOffset(offset, i)));
                    }
                }
            }

            if (foundedNode != null && foundedResult == null) {
                foundedResult = new SourceIdentification(javaSourceNodeEntry.getKey(), foundedNode.getSourceLine(), foundedNode);
                founded = true;
            }
            if (foundedNode == null && foundedResult != null) {
                result.addExcludedNode(foundedResult.getFoundedNode());
                return true;
            }
        }
        if (founded) {
            if (foundedResult != null) {
                result.addSourceIdentification(foundedResult);
                result.addExcludedNode(foundedResult.getFoundedNode());
            }
            return true;
        }
        return false;
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
     * Finds the index of the first findGivenClassNameAgainstScripthonProgram for the given statement and the given node with given offset (index)
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

    private static Node findFirstMatchRecursively(Statement statement, Node top, int index, List<Node> excludedNodes) {
        final String methodName = "findFirstMatchRecursively()";
        if (isAStructure(statement)) {
            if (evaluateStatementAndCompareWithNode(statement, top)) {
                if (compareInnerContent(statement, top, 0) == -1) {
                    logDebug("Inner content of: " + statement + " does not fit to: " + top + " while " + methodName);
                    return null;
                }
                if (!isContainedInExcludedNodes(excludedNodes, top)) {
                    resultContainer = new ResultContainer(-1, top);
                    return top;
                } else {
                    resultContainer = null;
                    logDebug("This node: " + top + " is already contained in results");
                }
            }

            //if the top and statement are not equal, take the node's children
            //and do the same recursively
            final List<Node> subNodes = top.getNodes();
            if (listNotEmpty(subNodes)) {
                for (int i = 0; i < subNodes.size(); i++) {
                    Node founded = findFirstMatchRecursively(statement, subNodes.get(i), i, excludedNodes);
                    if (founded != null) {
                        if (!isContainedInExcludedNodes(excludedNodes, top)) {
                            resultContainer = new ResultContainer(i, top);
                            logDebug(RETURNING_NULL_BECAUSE + top + " is not in excludedNodes while " + methodName);
                            return null;
                        } else {
                            logDebug("This node: " + top + " is already contained in results");
                        }
                    }
                }
            }
            logDebug(RETURNING_NULL_BECAUSE + top + " has empty nodes while " + methodName);
        }
        logDebug(RETURNING_NULL_BECAUSE + statement + " is not instance of Structure " + methodName);
        return null;
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

    private static boolean isContainedInExcludedNodes(List<Node> excludedNodes, Node desiredNode) {
        result.increaseNumberOfComparisons();
        if (excludedNodes != null) {
            for (Node excludedNode : excludedNodes) {
                if (excludedNode.equals(desiredNode)) {
                    return true;
                }
            }
        }
        return false;
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
                statementToReturn= ifStatement.elseStatement;
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
                                " while comparing with: " + scripthonStatement + IN_METHOD + methodName);                        //the javaSourceNode contains only empty BLOCK => positive findGivenClassNameAgainstScripthonProgram
                        return 0;
                    }
                    if (scripthonStatement.getStatements() == null || scripthonStatement.getStatements().isEmpty()) {
                        logDebug(RETURNING_ZERO_BECAUSE + javaSourceNode + " has only one block with zero subnodes" +
                                " and scripthonStatement: " + scripthonStatement + " does not have additional structures in method: " + methodName);                        //the javaSourceNode contains only empty BLOCK => positive findGivenClassNameAgainstScripthonProgram
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
                        " while comparing with: " + scripthonStatement + IN_METHOD + methodName);                //block of the scripthonStatement is null, nodes of the javaSourceNode is null => positive findGivenClassNameAgainstScripthonProgram
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
            //TODO - allow once finished
            //if (!structuresChecking()) return true;
            ClassDefinition classDefinition = (ClassDefinition) ((Structure) scripthonStatement).definition;
            return statementAndNodeHaveSameName(javaSourcesNode, classDefinition) && checkRestrictions(javaSourcesNode, classDefinition);
        }

        if (isStatementInit(scripthonStatement) && isNodeTypeVar(javaSourcesNode)) {
            //in case that check structures properties is disabled, types are equals automatically
            //if (!structuresChecking()) return true;
            InitDefinition initDefinition = (InitDefinition) scripthonStatement.definition;
            return statementAndNodeHaveSameName(javaSourcesNode, initDefinition);
        }

        if (isMethodStatement(scripthonStatement) && isNodeMethod(javaSourcesNode)) {
            //in case that check structures properties is disabled, types are equals automatically
            //TODO - allow once finished
            //if (!structuresChecking()) return true;
            MethDefinition methDefinition = (MethDefinition) scripthonStatement.definition;
            return statementAndNodeHaveSameName(javaSourcesNode, methDefinition) && checkRestrictions(javaSourcesNode, methDefinition);
        }
        if (isMethodCallStatement(scripthonStatement) && isMethodCallNode(javaSourcesNode)) {
            //in case that check structures properties is disabled, types are equals automatically
            if (!structuresChecking()) return true;
        }

        if (isBlockStatement(scripthonStatement) && isBlockNode(javaSourcesNode)) {
            //in case that check structures properties is disabled, types are equals automatically
            if (!structuresChecking()) return true;
        }

        return false;
    }

    private static boolean structuresChecking() {
        //in the case that check structures properties is disabled, types are equals automatically
        if (CHECK_STRUCTURES_PROPERTIES) {
            //TODO set true once finished
            return false;
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
        if (javaSourcesNode == null || definition == null) {
            return false;
        }

        if (javaSourcesNode.getName() == null || definition.getName() == null) {
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
