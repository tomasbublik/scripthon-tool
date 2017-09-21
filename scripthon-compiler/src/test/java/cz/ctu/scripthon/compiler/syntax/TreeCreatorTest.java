package cz.ctu.scripthon.compiler.syntax;

import cz.ctu.scripthon.compiler.CompilerTest;
import cz.ctu.scripthon.compiler.exceptions.TokenizationException;
import cz.ctu.scripthon.compiler.syntax.parser.StatementsRecognizer;
import cz.ctu.scripthon.compiler.syntax.parser.SyntaxCreator;
import cz.ctu.scripthon.compiler.syntax.statements.Program;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TreeCreatorTest extends CompilerTest {

    @Test
    public void canTokenSequenceBeCreatedWhenCodeLinesAreGiven() throws TokenizationException {
        String result = createTokenSequenceString(fileLinesList);

        final String expectedResult = "<sof>Class(Name=\"MyClass\";Rest=public)<eol><bl>Meth(Name=\"newMethod\";Rest=public)a<eol>if(a.Name==\"newMethod\")<eol><bl>Init(Name=\"var\")<eol>Init(Name=\"var\")<eol><bbl>else<eol><bl>Init(Name=\"var2\")<eol><bbl>Init(Name=\"var3\")<eol><eof>";

        assertNotNull(tokenSequence);
        assertEquals(expectedResult, result);
        assertNotNull(tokenSequence);
    }

    @Test
    public void canBlockOfStatementsBeCreatedWhenCodeLinesAreGiven() throws TokenizationException {
        SyntaxCreator syntaxCreator = new SyntaxCreator();
        createTokenSequenceString(fileLinesList);
        createStatementBlock(syntaxCreator);

        assertNotNull(statementsBlock);
    }

    @Test
    public void canProgramBeCreatedWhenCodeLinesAreGiven() throws TokenizationException {
        createTokenSequenceString(fileLinesList);
        createStatementBlock(new SyntaxCreator());

        Program program = new StatementsRecognizer().recognizeProgram(statementsBlock);

        assertNotNull(program);
    }
}
