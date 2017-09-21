package cz.ctu.scripthon.compiler.syntax;

import cz.ctu.scripthon.compiler.CompilerTest;
import cz.ctu.scripthon.compiler.exceptions.TokenizationException;
import cz.ctu.scripthon.compiler.syntax.parser.StatementsRecognizer;
import cz.ctu.scripthon.compiler.syntax.parser.SyntaxCreator;
import cz.ctu.scripthon.compiler.syntax.statements.Program;
import cz.ctu.scripthon.compiler.utils.FileUtils;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ProgramContainerTest extends CompilerTest {

    @Test
    public void canProgramInstanceBeObtainedAnytimeWhenNothigIsGiven() {
         assertNotNull(ProgramContainer.getInstance());
    }

    @Test
    public void areTwoProgramsSameWhenCreatedFromTheSameFile() throws TokenizationException {
        createTokenSequenceString(fileLinesList);
        createStatementBlock(new SyntaxCreator());
        Program program1 = new StatementsRecognizer().recognizeProgram(statementsBlock);

        fileLinesList = FileUtils.readFile(CompilerTest.class.getResource("/scripthonSources/test-file.scripthon"));
        createTokenSequenceString(fileLinesList);
        createStatementBlock(new SyntaxCreator());
        Program program2 = new StatementsRecognizer().recognizeProgram(statementsBlock);

        ProgramContainer.getInstance().setProgram(program1);

        assertTrue(ProgramContainer.getInstance().moreAccurateChange(program2));
    }

    @Test
    public void isTheSecondMoreAccurateWhenCreatedFromTheImprovedFile() throws TokenizationException {
        fileLinesList = FileUtils.readFile(CompilerTest.class.getResource("/scripthonSources/test-file-less.scripthon"));
        createTokenSequenceString(fileLinesList);
        createStatementBlock(new SyntaxCreator());
        Program program1 = new StatementsRecognizer().recognizeProgram(statementsBlock);

        fileLinesList = FileUtils.readFile(CompilerTest.class.getResource("/scripthonSources/test-file.scripthon"));
        createTokenSequenceString(fileLinesList);
        createStatementBlock(new SyntaxCreator());
        Program program2 = new StatementsRecognizer().recognizeProgram(statementsBlock);


        ProgramContainer.getInstance().setProgram(program1);

        assertTrue(ProgramContainer.getInstance().moreAccurateChange(program2));
    }
}
