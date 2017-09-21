package cz.ctu.scripthon.compiler.syntax.statements;

import cz.ctu.scripthon.compiler.heap.IEvaluator;
import cz.ctu.scripthon.compiler.syntax.Exp;
import cz.ctu.scripthon.compiler.syntax.Statement;
import cz.ctu.scripthon.compiler.syntax.Visitor;
import cz.ctu.scripthon.compiler.syntax.expressions.Identifier;

import java.util.List;

public class Assign<R> extends Statement {

    public Identifier i0;

    public Exp e0;

    public Assign(Identifier identifier, Exp value) {
        i0 = identifier;
        e0 = value;
    }

    @Override
    public Boolean accept(Visitor v) {
        return (Boolean) v.visit(this);
    }

    @Override
    public Object eval(IEvaluator h) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Statement> getStatements() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Statement getLast() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void putElement(Statement statement) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String toString() {
        return "Assign{" +
                "i0=" + i0 +
                ", e0=" + e0 +
                '}';
    }
}
