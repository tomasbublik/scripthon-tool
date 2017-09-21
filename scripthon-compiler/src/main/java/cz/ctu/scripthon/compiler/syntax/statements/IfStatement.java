package cz.ctu.scripthon.compiler.syntax.statements;

import cz.ctu.scripthon.compiler.heap.IEvaluator;
import cz.ctu.scripthon.compiler.syntax.Statement;
import cz.ctu.scripthon.compiler.syntax.Visitor;
import cz.ctu.scripthon.compiler.syntax.expressions.CompExp;

import java.util.List;

public class IfStatement extends Statement {

    public ElseStatement elseStatement;

    public CompExp conditionExpression;

    public IfStatement(CompExp conditionExpression) {
        this.conditionExpression = conditionExpression;
        this.block = new Block();
    }

    @Override
    public Boolean accept(Visitor v) {
        if (elseStatement != null) {
            return ((Boolean) v.visit(this) && (Boolean) v.visit(this.elseStatement));
        }
        return (Boolean) v.visit(this);
    }

    @Override
    public void putElement(Statement statement) {
        this.block = (Block) statement;
    }

    public void addElseStatement(ElseStatement elseStatement) {
        this.elseStatement = elseStatement;
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
        return "IfStatement{" +
                "block=" + block +
                ", elseStatement=" + elseStatement +
                ", conditionExpression=" + conditionExpression +
                '}';
    }
}
