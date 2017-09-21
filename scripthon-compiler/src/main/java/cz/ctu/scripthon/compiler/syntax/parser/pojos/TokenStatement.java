package cz.ctu.scripthon.compiler.syntax.parser.pojos;

import cz.ctu.scripthon.compiler.lexical.TokenSequence;
import cz.ctu.scripthon.compiler.pojo.Token;

import java.util.List;

public class TokenStatement {

    private TokenSequence tokenSequence;

    public TokenStatement(List<Token> tokenList) {
        this.tokenSequence = new TokenSequence(tokenList);
    }

    public TokenStatement() {
    }

    public TokenSequence getTokenSequence() {
        return tokenSequence;
    }

    public void setTokenSequence(TokenSequence tokenSequence) {
        this.tokenSequence = tokenSequence;
    }
}
