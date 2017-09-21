package cz.ctu.scripthon.tree.analyzer.codeanalyzer.processor;

import com.sun.source.tree.*;
import com.sun.source.util.TreePath;
import com.sun.source.util.TreePathScanner;
import com.sun.source.util.Trees;
import com.sun.tools.javac.tree.JCTree;
import cz.ctu.scripthon.tree.analyzer.codeanalyzer.helper.*;
import cz.ctu.scripthon.tree.analyzer.tree.AccessHelper;
import cz.ctu.scripthon.tree.analyzer.tree.Node;
import cz.ctu.scripthon.tree.analyzer.tree.NodeType;
import cz.ctu.scripthon.tree.analyzer.codeanalyzer.common.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.lang.model.element.Element;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.nio.CharBuffer;
import java.util.Collection;

/**
 * Visitor class which visits different nodes of the input source file,
 * extracts the required attribute of the visiting class, its methods,
 * fields, annotations etc and set it in the java class model.
 */
public class CodeAnalyzerTreeVisitor extends TreePathScanner<Object, Trees> {

    static final Logger LOG = LoggerFactory.getLogger(CodeAnalyzerTreeVisitor.class);

    //Model class stores the details of the visiting class
    JavaClassInfo clazzInfo = null;

    private AccessHelper accessHelper = AccessHelper.getInstance();

    /**
     * Converts the java file content to character buffer
     *
     * @param javaFile Content of java file being processed
     * @return Character buffer representation of the java source file
     */
    private static CharBuffer getCharacterBufferOfSource(String javaFile) {
        CharBuffer charBuffer = CharBuffer.wrap(javaFile.toCharArray());
        return charBuffer;
    }

    /**
     * Visits the class
     *
     * @param classTree
     * @param trees
     * @return
     */
    @Override
    public Object visitClass(ClassTree classTree, Trees trees) {
        AccessHelper.addNumberOfClasses();
        TreePath path = getCurrentPath();
        JCTree.JCClassDecl innerClassTree = (JCTree.JCClassDecl) classTree;
        if (innerClassTree.getSimpleName() == null || innerClassTree.getSimpleName().length() == 0) {
            LOG.debug("This is inner class - not supported, exiting to next structure");
            return null;
        }
        clazzInfo = new JavaClassInfo();
        //populate required class information to model
        ClassInfoDataSetter.populateClassInfo(clazzInfo, classTree, path, trees);

        if (clazzInfo.isPublic()) {
            AccessHelper.addNumberOfPublicClasses();
        }
        if (clazzInfo.isPrivate()) {
            AccessHelper.addNumberOfPrivateClasses();
            if (clazzInfo.isStatic()) {
                AccessHelper.addNumberOfPrivateStaticClasses();
            }
            if (clazzInfo.isFinal()) {
                AccessHelper.addNumberOfPrivateFinalClasses();
            }
        }
        if (clazzInfo.isProtected()) {
            AccessHelper.addNumberOfProtectedClasses();
        }
        if (clazzInfo.isStatic()) {
            AccessHelper.addNumberOfStaticClasses();
        }
        if (clazzInfo.isFinal()) {
            AccessHelper.addNumberOfFinalClasses();
            if (clazzInfo.isStatic()) {
                AccessHelper.addNumberOfFinalStaticClasses();
                if (clazzInfo.isPrivate()) {
                    AccessHelper.addNumberOfPrivateFinalStaticClasses();
                }
            }
        }
        if (clazzInfo.isAbstract()) {
            AccessHelper.addNumberOfAbstractClasses();
        }


        long start = trees.getSourcePositions().getStartPosition(path.getCompilationUnit(), classTree);
        long end = trees.getSourcePositions().getEndPosition(path.getCompilationUnit(), classTree);
        Node node = new Node(classTree.getSimpleName().toString(), NodeType.CLASS, start, end, 0, clazzInfo);

        accessHelper.appendNode(node);
        Object object = null;
        try {
            object = super.visitClass(classTree, trees);
        } catch (Exception e) {
            LOG.error("Class with name " + node.getName() + " could not be compiled");
            //TODO remove in production
            e.printStackTrace();
        }
        //accessHelper.setDone();
        return object;
    }

    /**
     * Visits all methods of the input java source file
     *
     * @param methodTree
     * @param trees
     * @return
     */
    @Override
    public Object visitMethod(MethodTree methodTree, Trees trees) {
        AccessHelper.appendNumberOfMethods();
        TreePath path = getCurrentPath();

        MethodInfo methodInfo = (MethodInfo) createObjectInfo(methodTree, trees, path);

        if (methodInfo != null) {
            if (methodInfo.isPublic()) {
                AccessHelper.addNumberOfPublicMethods();
            }
            if (methodInfo.isPrivate()) {
                AccessHelper.addNumberOfPrivateMethods();
                if (methodInfo.isStatic()) {
                    AccessHelper.addNumberOfPrivateStaticMethods();
                    if (methodInfo.isFinal()) {
                        AccessHelper.addNumberOfPrivateStaticFinalMethods();
                    }
                }
            }
            if (methodInfo.isProtected()) {
                AccessHelper.addNumberOfProtectedMethods();
            }
            if (methodInfo.isStatic()) {
                AccessHelper.addNumberOfStaticMethods();
            }
            if (methodInfo.isFinal()) {
                AccessHelper.addNumberOfFinalMethods();
            }
            if (methodInfo.isAbstract()) {
                AccessHelper.addNumberOfAbstractMethods();
            }

            final Collection<String> parameters = methodInfo.getParameters();
            if (parameters != null && !parameters.isEmpty()) {
                if (parameters.size() == 1) {
                    AccessHelper.addNumberOf1ParametersMethods();
                }
                if (parameters.size() == 2) {
                    AccessHelper.addNumberOf2ParametersMethods();
                }
                if (parameters.size() == 3) {
                    AccessHelper.addNumberOf3ParametersMethods();
                }
                if (parameters.size() > 3) {
                    AccessHelper.addNumberOfMore3ParametersMethods();
                }
            } else {
                AccessHelper.addNumberOf0ParametersMethods();
            }

            long start = trees.getSourcePositions().getStartPosition(path.getCompilationUnit(), methodTree);
            long end = trees.getSourcePositions().getEndPosition(path.getCompilationUnit(), methodTree);
            long sourceLine = 0;
            if (methodInfo.getLocationInfo() != null) {
                sourceLine = methodInfo.getLocationInfo().getLineNumber();
            }
            Node node = new Node(methodTree.getName().toString(), NodeType.METH, start, end, sourceLine, methodInfo);

            //podmínka kvůli konstruktoru - ten se bude dělat zvlášť
            if (end != -1) {
                accessHelper.appendNode(node);
            }
        }
        return super.visitMethod(methodTree, trees);
    }

    /**
     * Visits all variables of the java source file
     *
     * @param variableTree
     * @param trees
     * @return
     */
    @Override
    public Object visitVariable(VariableTree variableTree, Trees trees) {
        AccessHelper.appendNumberOfInit();
        TreePath path = getCurrentPath();
        Element e = trees.getElement(path);

        FieldInfo fieldInfo = (FieldInfo) createObjectInfo(variableTree, trees, path);

        long start = trees.getSourcePositions().getStartPosition(path.getCompilationUnit(), variableTree);
        long end = trees.getSourcePositions().getEndPosition(path.getCompilationUnit(), variableTree);
        long sourceLine = 0;
        if (fieldInfo.getLocationInfo() != null) {
            sourceLine = fieldInfo.getLocationInfo().getLineNumber();
        }
        Node node = new Node(variableTree.getName().toString(), NodeType.VAR, start, end, sourceLine, fieldInfo);

        if (end != -1) {
            accessHelper.appendNode(node);
        }
        return super.visitVariable(variableTree, trees);
    }

    @Override
    public Object visitExpressionStatement(ExpressionStatementTree expressionStatementTree, Trees trees) {
        return super.visitExpressionStatement(expressionStatementTree, trees);
    }

    @Override
    public Object visitAssignment(AssignmentTree assignmentTree, Trees trees) {
        return super.visitAssignment(assignmentTree, trees);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public Object visitConditionalExpression(ConditionalExpressionTree conditionalExpressionTree, Trees trees) {
        return super.visitConditionalExpression(conditionalExpressionTree, trees);
    }

    @Override
    public Object visitBinary(BinaryTree binaryTree, Trees trees) {
        return super.visitBinary(binaryTree, trees);
    }

    @Override
    public Object visitEmptyStatement(EmptyStatementTree emptyStatementTree, Trees trees) {
        return super.visitEmptyStatement(emptyStatementTree, trees); //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public Object visitIf(IfTree ifTree, Trees trees) {
        TreePath path = getCurrentPath();

        IfInfo ifInfo = (IfInfo) createObjectInfo(ifTree, trees, path);

        long start = trees.getSourcePositions().getStartPosition(path.getCompilationUnit(), ifTree);
        long end = trees.getSourcePositions().getEndPosition(path.getCompilationUnit(), ifTree);

        long sourceLine = 0;
        if (ifInfo.getLocationInfo() != null) {
            sourceLine = ifInfo.getLocationInfo().getLineNumber();
        }

        Node node = new Node(NodeType.IF.getName(), NodeType.IF, start, end, sourceLine, ifInfo);

        if (end != -1) {
            accessHelper.appendNode(node);
        }
        return super.visitIf(ifTree, trees);
    }

    @Override
    public Object visitForLoop(ForLoopTree forLoopTree, Trees trees) {
        TreePath path = getCurrentPath();

        ForLoopInfo forLoopInfo = (ForLoopInfo) createObjectInfo(forLoopTree, trees, path);

        long start = trees.getSourcePositions().getStartPosition(path.getCompilationUnit(), forLoopTree);
        long end = trees.getSourcePositions().getEndPosition(path.getCompilationUnit(), forLoopTree);

        long sourceLine = 0;
        if (forLoopInfo.getLocationInfo() != null) {
            sourceLine = forLoopInfo.getLocationInfo().getLineNumber();
        }

        Node node = new Node(NodeType.FORLOOP.getName(), NodeType.FORLOOP, start, end, sourceLine, forLoopInfo);

        if (end != -1) {
            accessHelper.appendNode(node);
        }
        return super.visitForLoop(forLoopTree, trees);
    }

    @Override
    public Object visitMethodInvocation(MethodInvocationTree methodInvocationTree, Trees trees) {
        AccessHelper.appendNumberOfMethodCalls();
        //returning in case of supertypes
        if (methodInvocationTree.toString().equals("super()")) {
            return super.visitMethodInvocation(methodInvocationTree, trees);
        }
        TreePath path = getCurrentPath();

        MethodCallInfo methodCallInfo = (MethodCallInfo) createObjectInfo(methodInvocationTree, trees, path);

        final Collection<String> parameters = methodCallInfo.getParameters();
        if (parameters != null && !parameters.isEmpty()) {
            if (parameters.size() == 1) {
                AccessHelper.addNumberOfMethodCalls1Parameters();
            }
            if (parameters.size() == 2) {
                AccessHelper.addNumberOfMethodCalls2Parameters();
            }
            if (parameters.size() == 3) {
                AccessHelper.addNumberOfMethodCalls3Parameters();
            }
            if (parameters.size() > 3) {
                AccessHelper.addNumberOfMethodCallsMore3Parameters();
            }
        } else {
            AccessHelper.addNumberOfMethodCalls0Parameters();
        }

        long start = trees.getSourcePositions().getStartPosition(path.getCompilationUnit(), methodInvocationTree);
        long end = trees.getSourcePositions().getEndPosition(path.getCompilationUnit(), methodInvocationTree);

        long sourceLine = 0;
        if (methodCallInfo.getLocationInfo() != null) {
            sourceLine = methodCallInfo.getLocationInfo().getLineNumber();
        }

        Node node = new Node(methodCallInfo.getName(), NodeType.METHCALL, start, end, sourceLine, methodCallInfo);

        if (end != -1) {
            accessHelper.appendNode(node);
        }

        return super.visitMethodInvocation(methodInvocationTree, trees);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public Object visitBlock(BlockTree blockTree, Trees trees) {
        AccessHelper.appendNumberOfBlocks();
        //returning in case of supertypes
        if (blockTree.getStatements() != null && !blockTree.getStatements().isEmpty()) {
            if (blockTree.getStatements().size() == 1) {
                StatementTree statementTree = blockTree.getStatements().get(0);
                if (statementTree.toString().equals("super()")) {
                    return super.visitBlock(blockTree, trees); //To change body of overridden methods use File | Settings | File Templates.
                }
            }
        }
        TreePath path = getCurrentPath();

        BlockInfo blockInfo = (BlockInfo) createObjectInfo(blockTree, trees, path);

        int statementsNumber = blockInfo.getStatementsNumber();
        if (statementsNumber == 0) {
            AccessHelper.addNumberOfBlocks0Statements();
        }
        if (statementsNumber == 1) {
            AccessHelper.addNumberOfBlocks1Statements();
        }
        if (statementsNumber == 2) {
            AccessHelper.addNumberOfBlocks2Statements();
        }
        if (statementsNumber == 3) {
            AccessHelper.addNumberOfBlocks3Statements();
        }
        if (statementsNumber > 3) {
            AccessHelper.addNumberOfBlocksMore3Statements();
        }

        long start = trees.getSourcePositions().getStartPosition(path.getCompilationUnit(), blockTree);
        long end = trees.getSourcePositions().getEndPosition(path.getCompilationUnit(), blockTree);

        long sourceLine = 0;
        if (blockInfo.getLocationInfo() != null) {
            sourceLine = blockInfo.getLocationInfo().getLineNumber();
        }

        Node node = new Node(NodeType.BLOCK.getName(), NodeType.BLOCK, start, end, sourceLine, blockInfo);

        if (end != -1) {
            accessHelper.appendNode(node);
        }
        return super.visitBlock(blockTree, trees); //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public Object visitCompilationUnit(CompilationUnitTree compilationUnitTree, Trees trees) {
        LOG.debug("Current structure: " + compilationUnitTree.getPackageName());
        return super.visitCompilationUnit(compilationUnitTree, trees); //To change body of overridden methods use File | Settings | File Templates.
    }

    /**
     * Returns the Java class model which holds the details of java source
     *
     * @return clazzInfo Java class model
     */
    public JavaClassInfo getClassInfo() {
        return clazzInfo;
    }

    private <T extends Tree> BaseJavaClassModelInfo createObjectInfo(T commonTree, Trees trees, TreePath path) {
        BaseJavaClassModelInfo baseInfo = null;
        try {
            //Get compilation unit tree
            CompilationUnitTree compileTree = clazzInfo.getSourceTreeInfo().getCompileTree();
            //Java file which is being processed
            JavaFileObject file = compileTree.getSourceFile();
            String javaFileContent = file.getCharContent(true).toString();
            //Convert the java file content to character buffer
            CharBuffer buffer = getCharacterBufferOfSource(javaFileContent);
            if (commonTree instanceof MethodTree) {
                //populate required method information to model
                baseInfo = MethodInfoDataSetter.populateMethodInfo(clazzInfo, commonTree, path, trees, buffer, compileTree);
                return baseInfo;
            }
            if (commonTree instanceof VariableTree) {
                //populate required method information to model
                baseInfo = FieldInfoDataSetter.populateFieldInfo(clazzInfo, commonTree, path, trees, buffer, compileTree);
                return baseInfo;
            }
            if (commonTree instanceof BlockTree) {
                //populate required method information to model
                BaseJavaClassModelInfo parentBlockInfo = createObjectInfo(path.getParentPath().getLeaf(), trees, path.getParentPath());
                baseInfo = BlockInfoDataSetter.populateBlockInfo(clazzInfo, commonTree, path, trees, buffer, compileTree, parentBlockInfo);
                return baseInfo;
            }
            if (commonTree instanceof MethodInvocationTree) {
                //populate required method information to model
                baseInfo = MethodCallInfoDataSetter.populateMethodCallInfo(clazzInfo, commonTree, path, trees, buffer, compileTree);
                return baseInfo;
            }
            if (commonTree instanceof IfTree) {
                baseInfo = IfInfoDataSetter.populateIfInfo(clazzInfo, commonTree, path, trees, buffer, compileTree);
                //baseInfo.setName(NodeType.IF.getName());
                return baseInfo;
            }
            if (commonTree instanceof ForLoopTree) {
                baseInfo = ForLoopInfoDataSetter.populateForLoopInfo(clazzInfo, commonTree, path, trees, buffer, compileTree);
                //baseInfo.setName("FOR");
                return baseInfo;
            }
            if (commonTree instanceof CatchTree) {
                //TODO
                baseInfo = new CatchInfo();
                baseInfo.setName(NodeType.CATCH.getName());
                return baseInfo;
            }

            /**
             * not well specified structures, just common class of structures
             */
            if (commonTree instanceof StatementTree) {
                //TODO
                baseInfo = new CommonStatementInfo();
                baseInfo.setName("CommonStatementInfo");
                return baseInfo;
            }
            //setLocInfoOfMethods(clazzInfo, buffer, compileTree);
        } catch (IOException e1) {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return baseInfo;
    }
}
