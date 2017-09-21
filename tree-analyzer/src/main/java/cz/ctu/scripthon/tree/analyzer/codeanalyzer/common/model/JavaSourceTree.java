package cz.ctu.scripthon.tree.analyzer.codeanalyzer.common.model;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.SourcePositions;

/**
 * Stores tree information of java class
 */
public interface JavaSourceTree {

    CompilationUnitTree getCompileTree();

    Tree getTree();

    SourcePositions getSourcePos();

}
