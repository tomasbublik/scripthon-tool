package cz.bublik.scripthon.compiler.syntax.parser.pojos;

import cz.bublik.scripthon.compiler.lexical.TokenSequence;
import cz.bublik.scripthon.compiler.pojo.Token;

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
