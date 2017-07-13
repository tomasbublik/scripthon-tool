package cz.bublik.scripthon.compiler.heap;

import cz.bublik.scripthon.compiler.syntax.expressions.CompExp;
import cz.bublik.scripthon.compiler.syntax.expressions.IdentifierAttributeCall;
import cz.bublik.scripthon.compiler.syntax.expressions.StringLiteral;
import cz.bublik.scripthon.compiler.syntax.expressions.VariableExp;

public interface IEvaluator<R> {

    R evaluate(CompExp n);

    R evaluate(VariableExp n);

    R evaluate(IdentifierAttributeCall i0);

    R evaluate(StringLiteral s0);
}
