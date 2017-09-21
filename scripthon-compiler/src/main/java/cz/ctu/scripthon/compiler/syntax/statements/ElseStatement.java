package cz.ctu.scripthon.compiler.syntax.statements;

import cz.ctu.scripthon.compiler.heap.IEvaluator;
import cz.ctu.scripthon.compiler.syntax.Statement;
import cz.ctu.scripthon.compiler.syntax.Visitor;

import java.util.List;

public class ElseStatement extends Statement {

    public ElseStatement() {
        this.block = new Block();
    }

    @Override
    public Boolean accept(Visitor v) {
        return (Boolean) v.visit(this);
    }

    @Override
    public void putElement(Statement statement) {
        this.block = (Block) statement;
    }

    @Override
    public Statement getLast() {
        return null;
    }

    @Override
    public List<Statement> getStatements() {
        //TODO
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object eval(IEvaluator h) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String toString() {
        return "ElseStatement{" +
                "block=" + block +
                '}';
    }
}
