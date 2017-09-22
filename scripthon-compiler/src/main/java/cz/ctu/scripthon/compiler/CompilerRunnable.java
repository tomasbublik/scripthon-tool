package cz.ctu.scripthon.compiler;

import cz.ctu.scripthon.compiler.exceptions.EmptySourceException;
import cz.ctu.scripthon.compiler.exceptions.SyntaxException;
import cz.ctu.scripthon.compiler.exceptions.TokenizationException;
import cz.ctu.scripthon.compiler.lexical.TokenSequence;
import cz.ctu.scripthon.compiler.lexical.TokensCreator;
import cz.ctu.scripthon.compiler.syntax.Interpreter;
import cz.ctu.scripthon.compiler.syntax.ProgramContainer;
import cz.ctu.scripthon.compiler.syntax.parser.StatementsRecognizer;
import cz.ctu.scripthon.compiler.syntax.parser.SyntaxCreator;
import cz.ctu.scripthon.compiler.syntax.parser.pojos.StatementsBlock;
import cz.ctu.scripthon.compiler.syntax.statements.Program;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class CompilerRunnable implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(CompilerRunnable.class);

    private List<String> linesList = new ArrayList<>();

    public CompilerRunnable(List<String> linesList) {
        this.linesList = linesList;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        LOG.debug("Starting compilation process");
        try {
            compile();
        } catch (EmptySourceException e) {
            LOG.error("Compilation failed: " + e.getMessage());
        } catch (SyntaxException e) {
            LOG.error("Syntax exception: " + e.getMessage());
        }
        long estimatedTime = System.currentTimeMillis() - startTime;
        LOG.debug(String.format("Compilation process finished in:%d ms", estimatedTime));
    }

    private void compile() throws EmptySourceException, SyntaxException {
        TokenSequence tokenSequence = new TokenSequence();
        List<String> fileLinesList;
        String tokensPrinted = "";

        if (linesList != null && !linesList.isEmpty()) {
            fileLinesList = linesList;
        } else {
            throw new EmptySourceException("No sources");
        }
        if (!fileLinesList.isEmpty()) {
            TokensCreator tokensCreator = new TokensCreator();
            try {
                tokenSequence = tokensCreator.makeTokenSequence(fileLinesList);
            } catch (TokenizationException e) {
                ProgramContainer.getInstance().setErrorMessage(e.getMessage());
                return;
            }
            tokensPrinted = tokenSequence.printTokensVertically();
        }

        LOG.debug(String.format("Tokens: %s", tokensPrinted));

        StatementsBlock statementsBlock = new SyntaxCreator().createSyntaxTree(tokenSequence);

        Program program = new StatementsRecognizer().recognizeProgram(statementsBlock);
        ProgramContainer programContainer = ProgramContainer.getInstance();
        //checking syntax and semantics
        Interpreter interpreter = new Interpreter(0);
        program.accept(interpreter);
        programContainer.setProgram(program);
    }

    List<String> getLinesList() {
        return linesList;
    }
}
