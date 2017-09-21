package cz.ctu.scripthon.compiler.heap;

import cz.ctu.scripthon.compiler.syntax.expressions.IdentifierAttributeCall;
import cz.ctu.scripthon.compiler.syntax.expressions.StringLiteral;
import cz.ctu.scripthon.compiler.syntax.expressions.VariableExp;
import cz.ctu.scripthon.compiler.syntax.expressions.CompExp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Evaluator<R> implements IEvaluator {

    static final Logger LOG = LoggerFactory.getLogger(Evaluator.class);

    private Memory memory;

    public Evaluator(Memory memory) {
        this.memory = memory;
    }

    @Override
    public Object evaluate(CompExp n) {
        Boolean result = Boolean.FALSE;
        switch (n.compType) {
            case EQ:
                //TODO sofistikovanější porovnání neznámých typů
                if (n.e1.eval(this).equals(n.e2.eval(this))) {
                    result = Boolean.TRUE;
                }
                break;
            default:
                break;
        }
        return (R) result;
    }

    @Override
    public Object evaluate(VariableExp n) {
        //TODO load and check variable from heap
        return null;
    }

    @Override
    public Object evaluate(IdentifierAttributeCall i0) {
        CommonVariable commonVariable = memory.getVariable(i0.i0.f0);
        if (commonVariable instanceof ComplexVariable) {
            Object value = ((ComplexVariable) commonVariable).getAttributeValue(i0.attributeName);
            return value;
        }
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object evaluate(StringLiteral s0) {
        return s0.f0;
    }
}
