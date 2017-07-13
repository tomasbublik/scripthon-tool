package cz.bublik.scripthon.compiler.syntax;

import cz.bublik.scripthon.compiler.syntax.expressions.CompExp;
import cz.bublik.scripthon.compiler.syntax.expressions.Identifier;
import cz.bublik.scripthon.compiler.syntax.expressions.IntegerLiteral;
import cz.bublik.scripthon.compiler.syntax.expressions.PlusExp;
import cz.bublik.scripthon.compiler.syntax.statements.*;

public interface Visitor<R> {

    R visit(PlusExp n);

    R visit(CompExp n);

    R visit(Identifier n);

    R visit(IntegerLiteral n);

    R visit(Assign n);

    R visit(Block n);

    R visit(Program n);

    R visit(Structure n);

    R visit(InitVariable n);

    R visit(IfStatement n);

    R visit(ElseStatement n);
}
