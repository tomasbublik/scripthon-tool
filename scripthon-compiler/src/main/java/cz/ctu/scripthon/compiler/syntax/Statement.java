package cz.ctu.scripthon.compiler.syntax;

import cz.ctu.scripthon.compiler.heap.IEvaluator;
import cz.ctu.scripthon.compiler.syntax.expressions.properties.Definition;
import cz.ctu.scripthon.compiler.syntax.statements.Block;

import java.util.List;

public abstract class Statement<R> {

    public SupportedStructure type;

    public Block block;

    public Definition definition;

    public abstract R accept(Visitor v);

    public abstract void putElement(Statement statement);

    public abstract Statement getLast();

    public abstract List<Statement> getStatements();

    public abstract R eval(IEvaluator h);

    public abstract String toString();

}
