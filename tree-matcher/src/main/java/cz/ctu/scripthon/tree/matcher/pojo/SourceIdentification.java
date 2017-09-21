package cz.ctu.scripthon.tree.matcher.pojo;

import cz.ctu.scripthon.tree.analyzer.tree.Node;

public class SourceIdentification {

    private String name;

    private long line;

    private Node foundedNode;

    public SourceIdentification(String name, long line) {
        this.name = name;
        this.line = line;
    }

    public SourceIdentification(String name, long line, Node foundedNode) {
        this.name = name;
        this.line = line;
        this.foundedNode = foundedNode;
    }

    public String getName() {
        return name;
    }

    public long getLine() {
        return line;
    }

    public Node getFoundedNode() {
        return foundedNode;
    }

    public void setFoundedNode(Node foundedNode) {
        this.foundedNode = foundedNode;
    }
}
