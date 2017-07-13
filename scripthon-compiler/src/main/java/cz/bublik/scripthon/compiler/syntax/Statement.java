package cz.bublik.scripthon.compiler.syntax;

import cz.bublik.scripthon.compiler.heap.IEvaluator;
import cz.bublik.scripthon.compiler.syntax.expressions.properties.Definition;
import cz.bublik.scripthon.compiler.syntax.statements.Block;

import java.util.List;

public abstract class Statement<R> {

    public SupportedStructures type;

    public Block block;

    public Definition definition;

    public abstract R accept(Visitor v);

    public abstract void putElement(Statement statement);

    public abstract Statement getLast();

    public abstract List<Statement> getStatements();

    public abstract R eval(IEvaluator h);

    public abstract String toString();

}
