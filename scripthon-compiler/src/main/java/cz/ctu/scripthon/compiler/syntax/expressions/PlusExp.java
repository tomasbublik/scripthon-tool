package cz.ctu.scripthon.compiler.syntax.expressions;

import cz.ctu.scripthon.compiler.heap.IEvaluator;
import cz.ctu.scripthon.compiler.syntax.Exp;
import cz.ctu.scripthon.compiler.syntax.Visitor;

public class PlusExp extends Exp {

    public Exp e1, e2;

    public PlusExp(Exp a1, Exp a2) {
        e1 = a1;
        e2 = a2;
    }

    public Integer accept(Visitor v) {
        return (Integer) v.visit(this);
    }

    @Override
    public Object eval(IEvaluator h) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
