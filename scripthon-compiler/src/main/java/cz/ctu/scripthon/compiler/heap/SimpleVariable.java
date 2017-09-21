package cz.ctu.scripthon.compiler.heap;

import cz.ctu.scripthon.compiler.syntax.SupportedStructure;

public class SimpleVariable extends CommonVariable {

    public SimpleVariable(String name, SupportedStructure type) {
        super(name, type);
    }

    public SimpleVariable(String name, SupportedStructure type, Object value) {
        super(name, type, value);
    }
}
