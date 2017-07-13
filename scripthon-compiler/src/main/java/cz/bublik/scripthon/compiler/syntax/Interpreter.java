package cz.bublik.scripthon.compiler.syntax;

import cz.bublik.scripthon.compiler.syntax.expressions.CompExp;
import cz.bublik.scripthon.compiler.syntax.expressions.Identifier;
import cz.bublik.scripthon.compiler.syntax.expressions.IntegerLiteral;
import cz.bublik.scripthon.compiler.syntax.expressions.PlusExp;
import cz.bublik.scripthon.compiler.syntax.statements.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Interpreter<R> implements Visitor {

    static final Logger LOG = LoggerFactory.getLogger(Interpreter.class);

    private final R DEFAULT_VALUE;

    public Interpreter(R default_value) {
        DEFAULT_VALUE = default_value;
    }

    public R visit(PlusExp n) {
        Integer result = (Integer) n.e1.accept(this) + (Integer) n.e2.accept(this);
        return (R) result;
    }

    public R visit(CompExp n) {
        Boolean result = Boolean.FALSE;
        switch (n.compType) {
            case EQ:
                if (n.e1.accept(this) == n.e2.accept(this)) {
                    result = Boolean.TRUE;
                }
                break;
            default:
                break;
        }
        return (R) result;
    }

    public R visit(Identifier n) {
        Boolean indentifierLookup = lookup(n.f0);
        return (R) indentifierLookup;
    }

    public R visit(IntegerLiteral n) {
        Integer integer = Integer.parseInt(n.f0);
        return (R) integer;
    }

    public R visit(Assign n) {
        if ((n.i0.accept(this)) && (n.e0.accept(this) instanceof Integer)) {
            return (R) Boolean.TRUE;
        }
        if ((n.i0.accept(this)) && ((Boolean) n.e0.accept(this))) {
            return (R) Boolean.TRUE;
        }
        return (R) Boolean.FALSE;
    }

    public R visit(Block n) {
        if (n.statements != null && !n.statements.isEmpty()) {
            for (Statement statement : n.statements) {
                if (!(Boolean) ((Statement) statement).accept(this)) {
                    return (R) Boolean.FALSE;
                }
            }
        }
        return (R) Boolean.TRUE;
    }

    public R visit(Program n) {
        if (n.statements != null && !n.statements.isEmpty()) {
            for (Statement statement : n.statements) {
                if (!(Boolean) ((Statement) statement).accept(this)) {
                    return (R) Boolean.FALSE;
                }
            }
        }
        return (R) Boolean.TRUE;
    }

    public R visit(Structure n) {
        if (n.block != null) {
            return (R) n.block.accept(this);
        }
        return (R) Boolean.TRUE;
    }

    public R visit(InitVariable n) {
        if (n.block != null) {
            return (R) n.block.accept(this);
        }
        n.i0.accept(this);
        return (R) Boolean.TRUE;
    }

    public R visit(IfStatement n) {
        return (R) Boolean.TRUE;
    }

    public R visit(ElseStatement n) {
        return (R) Boolean.TRUE;
    }

    private Boolean lookup(String name) {
        if (name.equals("aaa")) {
            LOG.error("As cannot be the name of a variable!");
            return false;
        }
        return true;
    }

}
