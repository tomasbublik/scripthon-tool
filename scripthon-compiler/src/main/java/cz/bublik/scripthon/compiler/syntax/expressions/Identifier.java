package cz.bublik.scripthon.compiler.syntax.expressions;

import cz.bublik.scripthon.compiler.heap.IEvaluator;
import cz.bublik.scripthon.compiler.syntax.Exp;
import cz.bublik.scripthon.compiler.syntax.Visitor;

public class Identifier extends Exp {

    public String f0;

    public Identifier(String n0) {
        f0 = n0;
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
