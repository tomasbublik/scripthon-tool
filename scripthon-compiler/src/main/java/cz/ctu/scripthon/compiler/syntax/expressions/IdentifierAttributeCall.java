package cz.ctu.scripthon.compiler.syntax.expressions;

import cz.ctu.scripthon.compiler.heap.IEvaluator;
import cz.ctu.scripthon.compiler.syntax.Exp;
import cz.ctu.scripthon.compiler.syntax.Visitor;

public class IdentifierAttributeCall extends Exp {

    public Identifier i0;

    public String attributeName;

    public IdentifierAttributeCall(Identifier i0, String attributeName) {
        this.i0 = i0;
        this.attributeName = attributeName;
    }

    @Override
    public Object accept(Visitor v) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object eval(IEvaluator h) {
        return h.evaluate(this);
    }

    @Override
    public String toString() {
        return "IdentifierAttributeCall{" +
                "i0=" + i0 +
                ", attributeName='" + attributeName + '\'' +
                '}';
    }
}
