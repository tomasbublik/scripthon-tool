package cz.bublik.scripthon.tree.analyzer.tree;

import java.io.Serializable;

public enum NodeType implements Serializable {
    BLOCK("BLOCK"),
    METH(""),
    VAR("VAR"),
    CLASS(""),
    METHCALL("METHODCALL"),
    FORLOOP("FORLOOP"),
    IF("IF"),
    CATCH("CATCH");

    private String name;

    NodeType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
