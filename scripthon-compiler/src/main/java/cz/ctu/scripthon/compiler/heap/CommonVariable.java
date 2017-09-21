package cz.ctu.scripthon.compiler.heap;

import cz.ctu.scripthon.compiler.syntax.SupportedStructure;

public abstract class CommonVariable {

    private String name;

    private SupportedStructure type;

    private Object value;

    protected CommonVariable(String name, SupportedStructure type) {
        this.name = name;
        this.type = type;
    }

    protected CommonVariable(String name, SupportedStructure type, Object value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public SupportedStructure getType() {
        return type;
    }
}
