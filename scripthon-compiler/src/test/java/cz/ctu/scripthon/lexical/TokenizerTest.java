package cz.ctu.scripthon.lexical;

import cz.ctu.scripthon.compiler.CompilerTest;
import cz.ctu.scripthon.compiler.exceptions.TokenizationException;
import cz.ctu.scripthon.compiler.utils.FileUtils;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TokenizerTest extends CompilerTest {

    @Test
    public void shallTokensSequenceCorrespondToTheExpectedResultWhenTestSourceIsGiven01() throws TokenizationException {
        List<String> fileLinesList = FileUtils.readFile(TokenizerTest.class.getResource("/scripthonSources/test-file2.scripthon"));
        String tokenSequenceString = createTokenSequenceString(fileLinesList);

        final String expectedResult = "<sof>Class(Name=\"myClass\")<eol><bl>Block(Lines=4)<eol><bl>Statement()<eol><bbl><bbl>Any()<eol><eof>";

        assertNotNull(tokenSequence);
        assertEquals(expectedResult, tokenSequenceString);
    }

    @Test
    public void shallTokensSequenceCorrespondToTheExpectedResultWhenTestSourceIsGiven02() throws TokenizationException {
        List<String> fileLinesList = FileUtils.readFile(TokenizerTest.class.getResource("/scripthonSources/test-file.scripthon"));
        String tokenSequenceString = createTokenSequenceString(fileLinesList);

        final String expectedResult = "<sof>Class(Name=\"MyClass\";Rest=public)<eol><bl>Meth(Name=\"newMethod\";Rest=public)a<eol>if(a.Name==\"newMethod\")<eol><bl>Init(Name=\"var\")<eol>Init(Name=\"var\")<eol><bbl>else<eol><bl>Init(Name=\"var2\")<eol><bbl>Init(Name=\"var3\")<eol><eof>";

        assertNotNull(tokenSequence);
        assertEquals(expectedResult, tokenSequenceString);
    }

    @Test
    public void shallTokensSequenceCorrespondToTheExpectedResultWhenTestSourceIsGiven03() throws TokenizationException {
        List<String> fileLinesList = FileUtils.readFile(TokenizerTest.class.getResource("/scripthonSources/first-file.scripthon"));
        String tokenSequenceString = createTokenSequenceString(fileLinesList);

        final String expectedResult = "<sof>Class(Name=\"myClass\")c<eol><bl>Init(Type=\"Long\";Value=3)<eol>Meth()<eol>Meth(Name=\"newMethod\")<eol><bl>Block()<eol><bl>MethCall(Name=\"System.out.println\")<eol>Any()<eol>MethCall()<eol><eof>";

        assertNotNull(tokenSequence);
        assertEquals(expectedResult, tokenSequenceString);
    }

    @Test
    public void shallTokensSequenceCorrespondToTheExpectedResultWhenTestSourceIsGiven04() throws TokenizationException {
        List<String> fileLinesList = FileUtils.readFile(TokenizerTest.class.getResource("/scripthonSources/multipleResults.scripthon"));
        String tokenSequenceString = createTokenSequenceString(fileLinesList);

        final String expectedResult = "<sof>Init(Name=\"var1\")<eol>Init(Name=\"var2\")<eol><eof>";

        assertNotNull(tokenSequence);
        assertEquals(expectedResult, tokenSequenceString);
    }

    @Test
    public void shallTokensSequenceCorrespondToTheExpectedResultWhenTestSourceIsGiven05() throws TokenizationException {
        List<String> fileLinesList = FileUtils.readFile(TokenizerTest.class.getResource("/scripthonSources/real-file.scripthon"));
        String tokenSequenceString = createTokenSequenceString(fileLinesList);

        final String expectedResult = "<sof>Meth()<eol><bl>MethCall()<eol><bl>MethCall()<eol><bbl>Init()<eol>Any()<eol>MethCall()<eol>MethCall()<eol><eof>";

        assertNotNull(tokenSequence);
        assertEquals(expectedResult, tokenSequenceString);
    }

    @Test
    public void shallTokensSequenceCorrespondToTheExpectedResultWhenTestSourceIsGiven06() throws TokenizationException {
        List<String> fileLinesList = FileUtils.readFile(TokenizerTest.class.getResource("/scripthonSources/singleton.scripthon"));
        String tokenSequenceString = createTokenSequenceString(fileLinesList);

        final String expectedResult = "<sof>Class(Order=false)a<eol><bl>Any()<eol>Meth(Name=a.Name;Rest=private)<eol>Any()<eol>Meth(Rest=[public,static];Ret=a.Name)<eol><eof>";

        assertNotNull(tokenSequence);
        assertEquals(expectedResult, tokenSequenceString);
    }

    @Test(expected = TokenizationException.class)
    public void shallThrowTokenizationExceptionWhenWrongTestSourceIsGiven() throws TokenizationException {
        List<String> fileLinesList = FileUtils.readFile(TokenizerTest.class.getResource("/scripthonSources/wrong-file.scripthon"));
        createTokenSequenceString(fileLinesList);
    }
}
