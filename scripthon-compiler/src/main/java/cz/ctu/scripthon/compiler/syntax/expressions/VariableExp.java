package cz.ctu.scripthon.compiler.syntax.expressions;

import cz.ctu.scripthon.compiler.heap.IEvaluator;
import cz.ctu.scripthon.compiler.syntax.Exp;
import cz.ctu.scripthon.compiler.syntax.Visitor;

public class VariableExp extends Exp {

    public String name;

    public String value;

    public VariableExp(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public Object accept(Visitor v) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object eval(IEvaluator h) {
        return h.evaluate(this);
    }
}
