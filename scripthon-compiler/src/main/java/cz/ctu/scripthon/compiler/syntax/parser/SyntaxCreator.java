package cz.ctu.scripthon.compiler.syntax.parser;

import cz.ctu.scripthon.compiler.exceptions.SyntaxException;
import cz.ctu.scripthon.compiler.lexical.TokenSequence;
import cz.ctu.scripthon.compiler.constant.Constants;
import cz.ctu.scripthon.compiler.pojo.Token;
import cz.ctu.scripthon.compiler.syntax.parser.pojos.StatementsBlock;
import cz.ctu.scripthon.compiler.syntax.parser.pojos.TokenStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class SyntaxCreator {

    static final Logger LOG = LoggerFactory.getLogger(SyntaxCreator.class);

    public SyntaxCreator() {
        //TODO some settings ?
    }

    public StatementsBlock createSyntaxTree(TokenSequence tokenSequence) throws SyntaxException {
        List<Token> tokenList = tokenSequence.getTokenList();
        if (tokenList != null && !tokenList.isEmpty()) {
            Iterator iterator = tokenList.iterator();
            int index = 0;
            while (iterator.hasNext()) {
                Token token = (Token) iterator.next();
                index++;
                if (index == 1) {
                    LOG.debug("Expecting <sof>");
                    if (token.getValue().equals(Constants.START_OF_FILE)) {
                        LOG.debug("Success");
                    } else {
                        throw new SyntaxException("Expected <sof>, but received: " + token.getValue());
                    }
                }
                if (index == 2) {
                    return readTheBlock(iterator, token);
                }
            }
        }
        return null;
    }

    private StatementsBlock readTheBlock(Iterator iterator, Token token) {
        int index = 1;
        List<TokenStatement> tokenStatements = new ArrayList<TokenStatement>();
        Map<Integer, Object> statementsOrBlocks = new LinkedHashMap<Integer, Object>();
        StatementsBlock statementsBlock = new StatementsBlock(statementsOrBlocks);

        while (!token.getValue().equals(Constants.END_OF_FILE)) {
            if (token.getValue().equals(Constants.BACK_BLOCK)) {
                break;
            }
            if (token.getValue().equals(Constants.BLOCK)) {
                if (!tokenStatements.isEmpty()) {
                    statementsBlock.getStatementsOrBlocks().put(index, tokenStatements);
                    index++;
                    tokenStatements = new ArrayList<TokenStatement>();
                }
                token = (Token) iterator.next();
                statementsBlock.getStatementsOrBlocks().put(index, readTheBlock(iterator, token));
                index++;
            } else {
                TokenStatement tokenStatement = readTheStatement(iterator, token);
                tokenStatements.add(tokenStatement);
                //teď by měl být lexical roven <eol>
            }
            if (iterator.hasNext()) {
                token = (Token) iterator.next();
            } else {
                break;
            }
        }
        if (!tokenStatements.isEmpty()) {
            statementsBlock.getStatementsOrBlocks().put(index, tokenStatements);
        }

        //most probably the lexical here will be <eof>
        return statementsBlock;
    }

    private TokenStatement readTheStatement(Iterator iterator, Token token) {
        List<Token> tokens = new ArrayList<Token>();
        while (!token.getValue().equals(Constants.END_OF_LINE)) {
            tokens.add(token);
            token = (Token) iterator.next();
        }
        return new TokenStatement(tokens);
    }


}
