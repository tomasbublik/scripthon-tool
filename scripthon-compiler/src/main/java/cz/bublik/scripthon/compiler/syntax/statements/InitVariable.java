package cz.bublik.scripthon.compiler.syntax.statements;

import cz.bublik.scripthon.compiler.heap.IEvaluator;
import cz.bublik.scripthon.compiler.syntax.SupportedStructures;
import cz.bublik.scripthon.compiler.syntax.Visitor;
import cz.bublik.scripthon.compiler.syntax.expressions.Identifier;
import cz.bublik.scripthon.compiler.syntax.expressions.properties.Definition;

public class InitVariable extends Structure {

    public Identifier i0;

    public InitVariable(Identifier i0, SupportedStructures type, Definition definition) {
        this.i0 = i0;
        this.type = type;
        this.definition = definition;
    }

    @Override
    public Boolean accept(Visitor v) {
        return (Boolean) v.visit(this);
    }

    @Override
    public Object eval(IEvaluator h) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
