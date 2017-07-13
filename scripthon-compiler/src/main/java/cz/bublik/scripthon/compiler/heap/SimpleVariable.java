package cz.bublik.scripthon.compiler.heap;

import cz.bublik.scripthon.compiler.syntax.SupportedStructures;

public class SimpleVariable extends CommonVariable {

    public SimpleVariable(String name, SupportedStructures type) {
        super(name, type);
    }

    public SimpleVariable(String name, SupportedStructures type, Object value) {
        super(name, type, value);
    }
}
