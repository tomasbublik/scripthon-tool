package cz.bublik.scripthon.compiler.syntax;

import cz.bublik.scripthon.compiler.syntax.expressions.Identifier;
import cz.bublik.scripthon.compiler.syntax.expressions.IntegerLiteral;
import cz.bublik.scripthon.compiler.syntax.expressions.PlusExp;
import cz.bublik.scripthon.compiler.syntax.statements.Assign;
import cz.bublik.scripthon.compiler.syntax.statements.Block;
import cz.cvut.bublik.CompilerTest;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;

public class SyntaxTreeTest extends CompilerTest {

    @Test
    public void testAssignExp() {
        Assign assign = new Assign(new Identifier("aaa1"),
                new PlusExp(new IntegerLiteral("6"), new IntegerLiteral("7")));

        Boolean accepted = assign.accept(interpreter);
        assertTrue(accepted);
    }

    @Test
    public void testBlock() {
        Statement statement1 = new Assign(new Identifier("aaa1"), new PlusExp(new IntegerLiteral("6"),
                new IntegerLiteral("7")));
        Statement statement2 = new Assign(new Identifier("aaa2"), new Identifier("aaa2"));
        Block block = new Block(statement1, statement2);
        Boolean accepted = block.accept(interpreter);
        assertTrue(accepted);
    }

}
