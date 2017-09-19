package cz.bublik.scripthon.compiler;

import cz.bublik.scripthon.compiler.exceptions.SyntaxException;
import cz.bublik.scripthon.compiler.exceptions.TokenizationException;
import cz.bublik.scripthon.compiler.lexical.TokenSequence;
import cz.bublik.scripthon.compiler.lexical.TokensCreator;
import cz.bublik.scripthon.compiler.syntax.Interpreter;
import cz.bublik.scripthon.compiler.syntax.ProgramContainer;
import cz.bublik.scripthon.compiler.syntax.parser.StatementsRecognizer;
import cz.bublik.scripthon.compiler.syntax.parser.SyntaxCreator;
import cz.bublik.scripthon.compiler.syntax.parser.pojos.StatementsBlock;
import cz.bublik.scripthon.compiler.syntax.statements.Program;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CompilerRunnable implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(CompilerRunnable.class);

    private List<String> linesList;

    public CompilerRunnable(List<String> linesList) {
        this.linesList = linesList;
    }

    private static void p(Object line) {
        LOG.debug((String) line);
    }

    private static void p() {
        LOG.debug("\\n");
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        LOG.debug("Starting compilation process");
        compile();
        long estimatedTime = System.currentTimeMillis() - startTime;
        LOG.debug("Compilation process finished in:" + estimatedTime + " ms");
    }

    private void compile() {
        TokenSequence tokenSequence = null;
        List<String> fileLinesList = null;
        String tokensPrinted = "";

        if (linesList != null && !linesList.isEmpty()) {
            fileLinesList = linesList;
        } else {
            throw new RuntimeException("Sources empty exception");
        }
        if (fileLinesList != null && !fileLinesList.isEmpty()) {
            p();
            p("Tokenizer started");
            TokensCreator tokensCreator = new TokensCreator();
            try {
                tokenSequence = tokensCreator.makeTokenSequence(fileLinesList);
            } catch (TokenizationException e) {
                ProgramContainer.getInstance().setErrorMessage(e.getMessage());
                return;
            }
            p();
            tokensPrinted = tokenSequence.printTokensVertically();
        }
        p("Tokenizer finished");

        SyntaxCreator syntaxCreator = new SyntaxCreator();
        StatementsBlock statementsBlock = null;
        try {
            statementsBlock = syntaxCreator.createSyntaxTree(tokenSequence);
        } catch (SyntaxException e) {
            e.printStackTrace(); //To change body of catch statement use File | Settings | File Templates.
        }

        StatementsRecognizer statementsRecognizer = new StatementsRecognizer();
        Program program1 = statementsRecognizer.recognize(statementsBlock, new Program());
        ProgramContainer programContainer = ProgramContainer.getInstance();
        //checking syntax and semantics
        Interpreter interpreter = new Interpreter(0);
        program1.accept(interpreter);
        programContainer.setProgram(program1);
    }

    public List<String> getLinesList() {
        return linesList;
    }
}
