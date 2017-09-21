package cz.ctu.scripthon.compiler.syntax.statements;

import cz.ctu.scripthon.compiler.heap.IEvaluator;
import cz.ctu.scripthon.compiler.syntax.expressions.properties.Definition;
import cz.ctu.scripthon.compiler.syntax.SupportedStructure;
import cz.ctu.scripthon.compiler.syntax.Visitor;
import cz.ctu.scripthon.compiler.syntax.expressions.Identifier;

public class InitVariable extends Structure {

    public Identifier i0;

    public InitVariable(Identifier i0, SupportedStructure type, Definition definition) {
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
