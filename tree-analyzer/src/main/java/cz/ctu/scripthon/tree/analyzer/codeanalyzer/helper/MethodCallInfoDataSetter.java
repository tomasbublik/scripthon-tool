package cz.ctu.scripthon.tree.analyzer.codeanalyzer.helper;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;
import cz.ctu.scripthon.tree.analyzer.codeanalyzer.common.model.JavaClassInfo;
import cz.ctu.scripthon.tree.analyzer.codeanalyzer.common.model.LocationInfo;
import cz.ctu.scripthon.tree.analyzer.codeanalyzer.common.model.MethodCallInfo;

import java.nio.CharBuffer;
import java.util.List;

public class MethodCallInfoDataSetter {

    /**
     * Set the attributes of the currently visiting method
     * to the java class model
     *
     * @param clazzInfo            The java class model
     * @param methodInvocationTree Curently visiting method call tree
     * @param path                 tree path
     * @param trees                trees
     */
    public static <T extends Tree> MethodCallInfo populateMethodCallInfo(JavaClassInfo clazzInfo, T methodInvocationTree, TreePath path, Trees trees, CharBuffer buffer, CompilationUnitTree compileTree) {

        MethodCallInfo methodCallInfo = new MethodCallInfo();
        String methodName = ((MethodInvocationTree) methodInvocationTree).getMethodSelect().toString();
        methodCallInfo.setName(methodName);
        List<? extends com.sun.source.tree.ExpressionTree> expressionTreeList = ((MethodInvocationTree) methodInvocationTree).getArguments();
        if (expressionTreeList != null && !expressionTreeList.isEmpty()) {
            for (ExpressionTree expressionTree : expressionTreeList) {
                methodCallInfo.addParameters(expressionTree.toString());
            }
        }

        LocationInfo locationInfo = DataSetterUtil.getLocationInfo(trees, path, methodInvocationTree);
        methodCallInfo.setLocationInfo(locationInfo);
        LocationInfo methodNameLoc = (LocationInfo) methodCallInfo.getLocationInfo();
        int startIndex = methodNameLoc.getStartOffset();
        int endIndex = -1;

        methodNameLoc.setStartOffset(startIndex);
        methodNameLoc.setEndOffset(endIndex);
        methodNameLoc.setLineNumber(compileTree.getLineMap().getLineNumber(startIndex));
        return methodCallInfo;
    }
}
