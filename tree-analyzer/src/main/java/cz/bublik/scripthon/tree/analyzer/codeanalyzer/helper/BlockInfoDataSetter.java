package cz.bublik.scripthon.tree.analyzer.codeanalyzer.helper;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;
import com.sun.tools.javac.tree.JCTree;
import cz.bublik.scripthon.tree.analyzer.codeanalyzer.common.model.BaseJavaClassModelInfo;
import cz.bublik.scripthon.tree.analyzer.codeanalyzer.common.model.BlockInfo;
import cz.bublik.scripthon.tree.analyzer.codeanalyzer.common.model.JavaClassInfo;
import cz.bublik.scripthon.tree.analyzer.codeanalyzer.common.model.LocationInfo;

import java.nio.CharBuffer;

public class BlockInfoDataSetter {

    /**
     * Set the attributes of the currently visiting method
     * to the java class model
     *
     * @param clazzInfo The java class model
     * @param blockTree Curently visiting block tree
     * @param path      tree path
     * @param trees     trees
     */
    public static <T extends Tree> BlockInfo populateBlockInfo(JavaClassInfo clazzInfo, T blockTree, TreePath path, Trees trees, CharBuffer buffer, CompilationUnitTree compileTree, BaseJavaClassModelInfo parentBlockInfo) {

        BlockInfo blockInfo = new BlockInfo();

        if (((JCTree.JCBlock) blockTree).getStatements() != null) {
            blockInfo.setStatementsNumber(((JCTree.JCBlock) blockTree).getStatements().size());
        }

        if (parentBlockInfo != null) {    //for example, lambda expressions have no parent block at all
            if (parentBlockInfo.getName() != null) {
                blockInfo.setName(parentBlockInfo.getName());
            }
        }

        //Set modifier details
        LocationInfo locationInfo = DataSetterUtil.getLocationInfo(trees, path, blockTree);
        blockInfo.setLocationInfo(locationInfo);
        LocationInfo blockNameLoc = (LocationInfo) blockInfo.getLocationInfo();
        int startIndex = blockNameLoc.getStartOffset();

        blockNameLoc.setStartOffset(startIndex);
        //endIndex won't be needed
        int endIndex = -1;
        blockNameLoc.setEndOffset(endIndex);
        blockNameLoc.setLineNumber(compileTree.getLineMap().getLineNumber(startIndex));
        return blockInfo;
    }
}
