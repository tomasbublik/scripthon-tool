package cz.ctu.scripthon.compiler.syntax;

import cz.ctu.scripthon.compiler.heap.IEvaluator;

public abstract class Exp<R> {

    public abstract R accept(Visitor v);

    public abstract R eval(IEvaluator h);

}
