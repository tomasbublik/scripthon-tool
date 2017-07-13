package cz.bublik.scripthon.compiler.heap;

import cz.bublik.scripthon.compiler.syntax.SupportedStructures;

public abstract class CommonVariable {

    private String name;

    private SupportedStructures type;

    private Object value;

    protected CommonVariable(String name, SupportedStructures type) {
        this.name = name;
        this.type = type;
    }

    protected CommonVariable(String name, SupportedStructures type, Object value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public SupportedStructures getType() {
        return type;
    }
}
