package cz.ctu.scripthon.compiler.heap;

import cz.ctu.scripthon.compiler.syntax.expressions.CompExp;
import cz.ctu.scripthon.compiler.syntax.expressions.IdentifierAttributeCall;
import cz.ctu.scripthon.compiler.syntax.expressions.StringLiteral;
import cz.ctu.scripthon.compiler.syntax.expressions.VariableExp;

public interface IEvaluator<R> {

    R evaluate(CompExp n);

    R evaluate(VariableExp n);

    R evaluate(IdentifierAttributeCall i0);

    R evaluate(StringLiteral s0);
}
