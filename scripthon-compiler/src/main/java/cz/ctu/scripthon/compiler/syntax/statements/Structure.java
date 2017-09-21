package cz.ctu.scripthon.compiler.syntax.statements;

import cz.ctu.scripthon.compiler.heap.IEvaluator;
import cz.ctu.scripthon.compiler.syntax.Statement;
import cz.ctu.scripthon.compiler.syntax.SupportedStructure;
import cz.ctu.scripthon.compiler.syntax.expressions.properties.Definition;
import cz.ctu.scripthon.compiler.syntax.Visitor;

import java.util.List;

public class Structure extends Statement {

    public Structure() {
    }

    public Structure(SupportedStructure type) {
        this.type = type;
    }

    public Structure(SupportedStructure type, Definition definition) {
        this.type = type;
        this.definition = definition;
    }

    @Override
    public Boolean accept(Visitor v) {
        return (Boolean) v.visit(this);
    }

    @Override
    public void putElement(Statement statement) {
        /*
         * if (statements == null) {
		 * statements = new Vector<Statement>();
		 * }
		 * statements.add(statement);
		 */
        this.block = (Block) statement;
    }

    @Override
    public Statement getLast() {
        //return statements.lastElement();
        return block;
    }

    @Override
    public List<Statement> getStatements() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object eval(IEvaluator h) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String toString() {
        return "Structure{" +
                "type='" + type.getName() + '\'' +
                ", block=" + block +
                ", definition=" + definition +
                '}';
    }
}
