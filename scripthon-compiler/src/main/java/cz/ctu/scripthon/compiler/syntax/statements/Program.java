package cz.ctu.scripthon.compiler.syntax.statements;

import cz.ctu.scripthon.compiler.heap.IEvaluator;
import cz.ctu.scripthon.compiler.syntax.Statement;
import cz.ctu.scripthon.compiler.syntax.Visitor;

import java.util.Collections;
import java.util.Vector;

public class Program extends Statement {

    public Vector<Statement> statements;

    public Program() {
        statements = new Vector<Statement>();
    }

    public Program(Vector<Statement> statements) {
        this.statements = statements;
    }

    public Program(Statement... multipleStatements) {
        if (multipleStatements != null && multipleStatements.length > 0) {
            statements = new Vector<Statement>();
            Collections.addAll(statements, multipleStatements);
        } else {
            //TODO error message or exception
        }
    }

    @Override
    public Boolean accept(Visitor v) {
        return (Boolean) v.visit(this);
    }

    @Override
    public void putElement(Statement statement) {
        statements.add(statement);
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Statement getLast() {
        return statements.lastElement();
    }

    public Vector<Statement> getStatements() {
        return statements;
    }

    @Override
    public Object eval(IEvaluator h) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String toString() {
        return "Program{" +
                "statements=" + statements +
                '}';
    }
}
