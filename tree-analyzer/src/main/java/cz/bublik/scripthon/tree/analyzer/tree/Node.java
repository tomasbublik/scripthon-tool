package cz.bublik.scripthon.tree.analyzer.tree;

import cz.bublik.scripthon.tree.analyzer.codeanalyzer.common.model.BaseJavaClassModelInfo;
import cz.bublik.scripthon.tree.analyzer.exception.DoNotFitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Node implements Serializable {

    static final Logger LOG = LoggerFactory.getLogger(Node.class);

    private BaseJavaClassModelInfo nodeInfo;

    private String name;

    private NodeType nodeType;

    private List<Node> nodes;

    private long left;

    private long right;

    private int level;

    private int subLevels;

    private long sourceLine;

    public Node(String name, NodeType nodeType) {
        this.name = name;
        this.nodeType = nodeType;
    }

    public Node(String name, NodeType nodeType, long left, long right, long sourceLine, BaseJavaClassModelInfo baseJavaClassModelInfo) {
        this.name = name;
        this.nodeType = nodeType;
        this.left = left;
        this.right = right;
        this.level = 1;
        this.sourceLine = sourceLine;
        this.nodeInfo = baseJavaClassModelInfo;
    }

    public void appendNode(Node newNode) throws DoNotFitException {
        if (newNode.getRight() == -1) {
            newNode.setRight(this.getRight());
        }
        //1)  check if it belongs to here
        if ((newNode.getLeft() <= this.getLeft()) || (newNode.getRight() > this.getRight())) {
            throw new DoNotFitException("Node " + newNode + " does not fit into node " + this);
        }

        newNode.increaseLevel();
        if (nodes != null && !nodes.isEmpty()) {
            int intendedPosition = -1;
            for (int i = 0; i < nodes.size(); i++) {
                Node tempNode = nodes.get(i);
                if ((newNode.getLeft() > tempNode.getLeft()) && (newNode.getRight() <= tempNode.getRight())) {
                    //newNode.increaseLevel();
                    tempNode.appendNode(newNode);
                    break;
                } else {
                    if (((i + 1) < nodes.size())) {
                        if (newNode.getRight() < tempNode.getLeft()) {
                            // put in front
                            intendedPosition = i;
                            break;
                        }
                        if ((newNode.getLeft() > tempNode.getRight())
                                && (newNode.getRight() < nodes.get(i + 1).getLeft())) {
                            // put in between
                            intendedPosition = (i + 1);
                            break;
                        }
                    } else {
                        //the last one
                        if ((newNode.getLeft() > tempNode.getRight())) {
                            // put into the last position
                            nodes.add(newNode);
                        }
                    }
                }
            }
            if (intendedPosition != -1) {
                List<Node> newNodes = new ArrayList<Node>();
                Iterator iterator = nodes.iterator();
                int index = 0;
                while (iterator.hasNext()) {
                    Node nodeTemp = (Node) iterator.next();
                    if (index == intendedPosition) {
                        newNodes.add(newNode);
                    } else {
                        newNodes.add(nodeTemp);
                    }
                    index++;
                }
            }
        } else {
            nodes = new ArrayList<Node>();
            nodes.add(newNode);
        }
    }

    public void increaseLevel() {
        this.level++;
    }

    public String getName() {
        return name;
    }

    public NodeType getNodeType() {
        return nodeType;
    }

    /**
     * returns all subnodes without parameters in case of the method node
     *
     * @return
     */
    public List<Node> getNodes() {
        if (this.getNodeType().equals(NodeType.METH)) {
            if (this.nodes != null && this.nodes.size() > 0) {
                List<Node> returnList = new ArrayList();
                for (Node next : nodes) {
                    if (next.getNodeType().equals(NodeType.BLOCK)) {
                        returnList.add(next);
                        return returnList;
                    }
                }
            }
        }
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public List<Node> getAllNodes() {
        return nodes;
    }

    public long getLeft() {
        return left;
    }

    public void setLeft(long left) {
        this.left = left;
    }

    public long getRight() {
        return right;
    }

    public void setRight(long right) {
        this.right = right;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getSubLevels() {
        return subLevels;
    }

    public void setSubLevels(int subLevels) {
        this.subLevels = subLevels;
    }

    public long getSourceLine() {
        return sourceLine;
    }

    public void setSourceLine(long sourceLine) {
        this.sourceLine = sourceLine;
    }

    public BaseJavaClassModelInfo getNodeInfo() {
        return nodeInfo;
    }

    @Override
    public String toString() {
        return "Node{" +
                "nodeInfo=" + nodeInfo.getName() +
                ", name='" + name + '\'' +
                ", nodeType=" + nodeType.getName() +
                ", left=" + left +
                ", right=" + right +
                ", sourceLine=" + sourceLine +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        if (left != node.left) return false;
        if (right != node.right) return false;
        if (nodeInfo != null && node.nodeInfo != null) {
            try {
                if (!(nodeInfo.getName()).equals(node.nodeInfo.getName())) {
                    return false;
                }
            } catch (Exception e) {
                LOG.error(e.getMessage());
                return false;
            }
        } else {
            return false;
        }

        return true;
    }
}
