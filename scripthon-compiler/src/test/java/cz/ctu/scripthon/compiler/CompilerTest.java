package cz.ctu.scripthon.compiler;

import cz.ctu.scripthon.compiler.exceptions.SyntaxException;
import cz.ctu.scripthon.compiler.exceptions.TokenizationException;
import cz.ctu.scripthon.compiler.lexical.TokenSequence;
import cz.ctu.scripthon.compiler.lexical.TokensCreator;
import cz.ctu.scripthon.compiler.pojo.Token;
import cz.ctu.scripthon.compiler.syntax.Interpreter;
import cz.ctu.scripthon.compiler.syntax.parser.SyntaxCreator;
import cz.ctu.scripthon.compiler.syntax.parser.pojos.StatementsBlock;
import cz.ctu.scripthon.compiler.utils.FileUtils;
import org.junit.Before;

import java.util.List;

public abstract class CompilerTest {

    private static final int default_value = 0;

    protected Interpreter interpreter;
    protected List<String> fileLinesList;
    protected TokenSequence tokenSequence;
    protected StatementsBlock statementsBlock;

    @Before
    public void setUp() {
        interpreter = new Interpreter(default_value);
        fileLinesList = FileUtils.readFile(CompilerTest.class.getResource("/scripthonSources/test-file.scripthon"));
    }

    protected String createTokenSequenceString(List<String> fileLinesList) throws TokenizationException {
        StringBuilder sb = new StringBuilder();
        if (fileLinesList != null && !fileLinesList.isEmpty()) {
            TokensCreator tokensCreator = new TokensCreator();
            tokenSequence = tokensCreator.makeTokenSequence(fileLinesList);
            for (Token token : tokenSequence.getTokenList()) {
                sb.append(token.getValue());
            }
        }
        return sb.toString();
    }

    protected void createStatementBlock(SyntaxCreator syntaxCreator) {
        try {
            statementsBlock = syntaxCreator.createSyntaxTree(tokenSequence);
        } catch (SyntaxException e) {
            e.printStackTrace(); //To change body of catch statement use File | Settings | File Templates.
        }
    }

}
