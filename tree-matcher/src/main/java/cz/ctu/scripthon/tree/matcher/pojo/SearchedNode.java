package cz.ctu.scripthon.tree.matcher.pojo;

import cz.ctu.scripthon.tree.analyzer.tree.Node;

import static cz.ctu.scripthon.tree.matcher.pojo.SearchedNode.DefaultIndex.*;

public class SearchedNode {

    public enum DefaultIndex {
        NEVER_SET(-2),
        PARENT(-1);

        int index;

        DefaultIndex(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }
    }


    private boolean found = false;

    private Node parent;

    /**
     * For now, -1 means parent itself, -2 never set, 0..n common index otherwise
     */
    private int indexOfChild = NEVER_SET.getIndex();

    public SearchedNode() {
    }

    public SearchedNode(boolean found, Node parent) {
        this.found = found;
        this.parent = parent;
    }

    public boolean isFound() {
        return found;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public int getIndexOfChild() {
        return indexOfChild;
    }

    public void setIndexOfChild(int indexOfChild) {
        this.indexOfChild = indexOfChild;
    }
}
