package cz.ctu.scripthon.tree.analyzer.codeanalyzer.common.model;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.SourcePositions;

import java.io.IOException;
import java.io.Serializable;

/**
 * Stores tree information of java class
 */
public class JavaSourceTreeInfo implements JavaSourceTree, Serializable {

    private Tree tree = null;
    private CompilationUnitTree compileTree = null;
    private SourcePositions sourcePos = null;

    public CompilationUnitTree getCompileTree() {
        return compileTree;
    }

    public void setCompileTree(CompilationUnitTree compileTree) {
        this.compileTree = compileTree;
    }

    public Tree getTree() {
        return tree;
    }

    public void setTree(Tree tree) {
        this.tree = tree;
    }

    public SourcePositions getSourcePos() {
        return sourcePos;
    }

    public void setSourcePos(SourcePositions sourcePos) {
        this.sourcePos = sourcePos;
    }

    private void writeObject(java.io.ObjectOutputStream out)
            throws IOException {
        // note, here we don't need out.defaultWriteObject(); because
        // JavaSourceTreeInfo has no other state to serialize
        //out.writeInt(super.getNonSerializableProperty().getQuantity());
    }

    private void readObject(java.io.ObjectInputStream in)
            throws IOException {
        // note, here we don't need in.defaultReadObject();
        // because JavaSourceTreeInfo has no other state to deserialize
        //super.setNonSerializableProperty(new NonSerializableClass(in.readInt()));
    }

}
