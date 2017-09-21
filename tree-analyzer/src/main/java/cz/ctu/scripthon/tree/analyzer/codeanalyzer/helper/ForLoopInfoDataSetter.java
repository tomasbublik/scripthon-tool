package cz.ctu.scripthon.tree.analyzer.codeanalyzer.helper;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;
import cz.ctu.scripthon.tree.analyzer.codeanalyzer.common.model.ForLoopInfo;
import cz.ctu.scripthon.tree.analyzer.codeanalyzer.common.model.JavaClassInfo;
import cz.ctu.scripthon.tree.analyzer.codeanalyzer.common.model.LocationInfo;

import java.nio.CharBuffer;

public class ForLoopInfoDataSetter {

    public static <T extends Tree> ForLoopInfo populateForLoopInfo(JavaClassInfo clazzInfo, T forLoopInvocationTree, TreePath path, Trees trees, CharBuffer buffer, CompilationUnitTree compileTree) {

        ForLoopInfo forLoopInfo = new ForLoopInfo();

        LocationInfo locationInfo = DataSetterUtil.getLocationInfo(trees, path, forLoopInvocationTree);
        forLoopInfo.setLocationInfo(locationInfo);
        LocationInfo forLoopLoc = (LocationInfo) forLoopInfo.getLocationInfo();
        int startIndex = forLoopLoc.getStartOffset();
        int endIndex = -1;

        forLoopLoc.setStartOffset(startIndex);
        forLoopLoc.setEndOffset(endIndex);
        forLoopLoc.setLineNumber(compileTree.getLineMap().getLineNumber(startIndex));
        return forLoopInfo;
    }
}
