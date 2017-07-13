package cz.cvut.bublik.token;

import cz.bublik.scripthon.compiler.exceptions.TokenizationException;
import cz.bublik.scripthon.compiler.lexical.TokenSequence;
import cz.bublik.scripthon.compiler.lexical.TokensCreator;
import cz.bublik.scripthon.compiler.pojo.Token;
import cz.bublik.scripthon.compiler.utils.FileUtils;
import cz.cvut.bublik.CompilerTest;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TokenizerTest extends CompilerTest {

    @Test
    public void testTokenizerSequence2() {
        List<String> fileLinesList = FileUtils.readFile("../scripthon-compiler/scripthonSources/test-file2.scripthon");
        StringBuilder sb = new StringBuilder();
        TokenSequence tokenSequence = null;
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

        final String expectedResult = "<sof>Class(Name=\"myClass\")<eol><bl>Block(Lines=4)<eol><bl>Statement()<eol><bbl><bbl>Any()<eol><eof>";
        assertNotNull(tokenSequence);
        assertEquals(expectedResult, sb.toString());
    }
}
