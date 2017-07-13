package cz.bublik.scripthon.compiler.syntax.expressions;

import cz.bublik.scripthon.compiler.heap.IEvaluator;
import cz.bublik.scripthon.compiler.syntax.Exp;
import cz.bublik.scripthon.compiler.syntax.Visitor;
import cz.bublik.scripthon.compiler.syntax.expressions.properties.CompType;

public class CompExp extends Exp {

    public Exp e1, e2;

    public CompType compType;

    public CompExp(Exp e1, Exp e2, CompType compType) {
        this.e1 = e1;
        this.e2 = e2;
        this.compType = compType;
    }

    @Override
    public Boolean accept(Visitor v) {
        return (Boolean) v.visit(this);
    }

    @Override
    public Boolean eval(IEvaluator h) {
        return (Boolean) h.evaluate(this);
    }

    @Override
    public String toString() {
        return "CompExp{" +
                "e1=" + e1 +
                ", e2=" + e2 +
                ", compType=" + compType +
                '}';
    }
}
