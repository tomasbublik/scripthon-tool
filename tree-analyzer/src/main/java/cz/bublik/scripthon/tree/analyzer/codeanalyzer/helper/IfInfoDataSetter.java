package cz.bublik.scripthon.tree.analyzer.codeanalyzer.helper;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;
import cz.bublik.scripthon.tree.analyzer.codeanalyzer.common.model.IfInfo;
import cz.bublik.scripthon.tree.analyzer.codeanalyzer.common.model.JavaClassInfo;
import cz.bublik.scripthon.tree.analyzer.codeanalyzer.common.model.LocationInfo;

import java.nio.CharBuffer;

public class IfInfoDataSetter {

    public static <T extends Tree> IfInfo populateIfInfo(JavaClassInfo clazzInfo, T ifInvocationTree, TreePath path, Trees trees, CharBuffer buffer, CompilationUnitTree compileTree) {

        IfInfo ifInfo = new IfInfo();

        LocationInfo locationInfo = DataSetterUtil.getLocationInfo(trees, path, ifInvocationTree);
        ifInfo.setLocationInfo(locationInfo);
        LocationInfo ifLoc = (LocationInfo) ifInfo.getLocationInfo();
        int startIndex = ifLoc.getStartOffset();
        int endIndex = -1;

        ifLoc.setStartOffset(startIndex);
        ifLoc.setEndOffset(endIndex);
        ifLoc.setLineNumber(compileTree.getLineMap().getLineNumber(startIndex));
        return ifInfo;
    }
}
