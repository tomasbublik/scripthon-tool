package cz.bublik.scripthon.compiler.syntax;

import cz.bublik.scripthon.compiler.exceptions.SyntaxException;
import cz.bublik.scripthon.compiler.exceptions.TokenizationException;
import cz.bublik.scripthon.compiler.lexical.TokenSequence;
import cz.bublik.scripthon.compiler.lexical.TokensCreator;
import cz.bublik.scripthon.compiler.pojo.Token;
import cz.bublik.scripthon.compiler.syntax.parser.StatementsRecognizer;
import cz.bublik.scripthon.compiler.syntax.parser.SyntaxCreator;
import cz.bublik.scripthon.compiler.syntax.parser.pojos.StatementsBlock;
import cz.bublik.scripthon.compiler.syntax.statements.Program;
import cz.cvut.bublik.CompilerTest;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TreeCreatorTest extends CompilerTest {

    private TokenSequence tokenSequence;

    private StatementsBlock statementsBlock;

    @Test
    public void testTokenSequence() {
        String result = createTokenSequence(fileLinesList);

        final String expectedResult = "<sof>Class(Name=\"MyClass\";Rest=public)<eol><bl>Meth(Name=\"newMethod\";Rest=public)a<eol>if(a.Name==\"newMethod\")<eol><bl>Init(Name=\"var\")<eol>Init(Name=\"var\")<eol><bbl>else<eol><bl>Init(Name=\"var2\")<eol><bbl>Init(Name=\"var3\")<eol><eof>";
        assertNotNull(tokenSequence);
        assertEquals(expectedResult, result);
        assertNotNull(tokenSequence);
    }

    private String createTokenSequence(List<String> fileLinesList) {
        StringBuilder sb = new StringBuilder();
        if (fileLinesList != null && !fileLinesList.isEmpty()) {
            TokensCreator tokensCreator = new TokensCreator();
            try {
                tokenSequence = tokensCreator.makeTokenSequence(fileLinesList);
            } catch (TokenizationException e) {
                e.printStackTrace();
            }
            for (Token token : tokenSequence.getTokenList()) {
                sb.append(token.getValue());
            }
        }
        return sb.toString();
    }

    @Test
    public void testStatementBlock() {
        SyntaxCreator syntaxCreator = new SyntaxCreator();
        createTokenSequence(fileLinesList);
        createStatementBlock(syntaxCreator);

        assertNotNull(statementsBlock);
    }

    private void createStatementBlock(SyntaxCreator syntaxCreator) {
        try {
            statementsBlock = syntaxCreator.createSyntaxTree(tokenSequence);
        } catch (SyntaxException e) {
            e.printStackTrace(); //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Test
    public void testsStatementsRecognizer() {
        SyntaxCreator syntaxCreator = new SyntaxCreator();
        StatementsRecognizer statementsRecognizer = new StatementsRecognizer();
        createTokenSequence(fileLinesList);
        createStatementBlock(syntaxCreator);
        Program program1 = statementsRecognizer.recognize(statementsBlock, new Program());
        assertNotNull(program1);
    }
}
