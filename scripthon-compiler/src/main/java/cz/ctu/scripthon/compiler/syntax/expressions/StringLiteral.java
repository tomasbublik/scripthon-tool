package cz.ctu.scripthon.compiler.syntax.expressions;

import cz.ctu.scripthon.compiler.heap.IEvaluator;
import cz.ctu.scripthon.compiler.syntax.Exp;
import cz.ctu.scripthon.compiler.syntax.Visitor;

public class StringLiteral extends Exp {

    public String f0;

    public StringLiteral(String f0) {
        this.f0 = f0;
    }

    @Override
    public Object eval(IEvaluator h) {
        return (String) h.evaluate(this);
    }

    @Override
    public Object accept(Visitor v) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String toString() {
        return "StringLiteral{" +
                "f0='" + f0 + '\'' +
                '}';
    }
}
