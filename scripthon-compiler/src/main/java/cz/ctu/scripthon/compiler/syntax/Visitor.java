package cz.ctu.scripthon.compiler.syntax;

import cz.ctu.scripthon.compiler.syntax.expressions.CompExp;
import cz.ctu.scripthon.compiler.syntax.expressions.Identifier;
import cz.ctu.scripthon.compiler.syntax.expressions.IntegerLiteral;
import cz.ctu.scripthon.compiler.syntax.expressions.PlusExp;
import cz.ctu.scripthon.compiler.syntax.statements.*;

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
