package cz.bublik.scripthon.tree.analyzer.codeanalyzer.common.model;

import java.io.Serializable;

/**
 * Code analyzer model for storing details of annotation
 */
public class AnnotationInfo implements Annotation, Serializable {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Annotation Name: ").append(name);
        buffer.append("\n");
        return buffer.toString();
    }
}
