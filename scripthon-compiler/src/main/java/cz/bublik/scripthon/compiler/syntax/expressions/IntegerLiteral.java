package cz.bublik.scripthon.compiler.syntax.expressions;

import cz.bublik.scripthon.compiler.heap.IEvaluator;
import cz.bublik.scripthon.compiler.syntax.Exp;
import cz.bublik.scripthon.compiler.syntax.Visitor;

public class IntegerLiteral<R> extends Exp {

    public String f0;

    public IntegerLiteral(String n0) {
        f0 = n0;
    }

    public Integer accept(Visitor v) {
        return (Integer) v.visit(this);
    }

    @Override
    public Object eval(IEvaluator h) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String toString() {
        return "IntegerLiteral{" +
                "f0='" + f0 + '\'' +
                '}';
    }
}
