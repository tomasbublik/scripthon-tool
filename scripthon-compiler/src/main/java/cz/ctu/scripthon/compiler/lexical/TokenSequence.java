package cz.ctu.scripthon.compiler.lexical;

import cz.ctu.scripthon.compiler.pojo.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TokenSequence {

    static final Logger LOG = LoggerFactory.getLogger(TokenSequence.class);

    private List<Token> tokenList;

    public TokenSequence(List<Token> tokenList) {
        this.tokenList = tokenList;
    }

    public String printTokensVertically() {
        StringBuilder stringBuilder = new StringBuilder();
        if (tokenList != null && !tokenList.isEmpty()) {
            for (Token token : getTokenList()) {
                final String s = "value:" + token.getValue() + " type:" + token.getType() + " line:"
                        + token.getLine() + " position:" + token.getPositionInLine() + "|";
                LOG.debug(s);
                stringBuilder.append(s);
            }
        }
        return stringBuilder.toString();
    }

    public List<Token> getTokenList() {
        return tokenList;
    }

    public void setTokenList(List<Token> tokenList) {
        this.tokenList = tokenList;
    }

}
