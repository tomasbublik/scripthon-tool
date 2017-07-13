package cz.bublik.scripthon.compiler.lexical;

import cz.bublik.scripthon.compiler.constant.Constants;
import cz.bublik.scripthon.compiler.exceptions.TokenizationException;
import cz.bublik.scripthon.compiler.pojo.Token;

import java.util.ArrayList;
import java.util.List;

public class TokensCreator {

    /**
     * creates a tokens list from a source
     * uses: <bl> for block
     * <bbl> for back block
     * <eol> for end of line
     * <eof> for end of file
     * <sof> for start of file
     * the important suspection: the <bl> and <bbl> is 4
     *
     * @param source
     * @return
     */
    public TokenSequence makeTokenSequence(List<String> source) throws TokenizationException {
        List<Token> tokenList = new ArrayList<Token>();
        if (source != null && !source.isEmpty()) {
            tokenList.add(new Token(Constants.TOKEN_MARK, Constants.START_OF_FILE, 0, 0));
            int lineBlockSpaces = 0;
            int lineNumber = 0;
            int positionInLine = 0;
            for (String line : source) {
                lineNumber++;
                int actualBlockSpaces = 0;
                StringBuilder word = new StringBuilder();
                positionInLine = 0;
                for (int i = 0; i < line.length(); i++) {
                    positionInLine++;
                    char character = line.charAt(i);
                    if (character == ' ' && actualBlockSpaces != -1) {
                        actualBlockSpaces++;
                    }
                    if (!Character.isSpaceChar(character)) {
                        if (actualBlockSpaces != lineBlockSpaces && actualBlockSpaces != -1) {
                            int howMany = (Math.abs(actualBlockSpaces - lineBlockSpaces))
                                    / Constants.LINE_BLOCK_DIFFERENCE;
                            if (actualBlockSpaces > lineBlockSpaces) {
                                for (int h = 0; h < howMany; h++) {
                                    tokenList.add(new Token(Constants.TOKEN_MARK, Constants.BLOCK, lineNumber,
                                            positionInLine - 1));
                                }
                            } else {
                                for (int h = 0; h < howMany; h++) {
                                    tokenList.add(new Token(Constants.TOKEN_MARK, Constants.BACK_BLOCK, lineNumber,
                                            positionInLine - 1));
                                }
                            }
                            lineBlockSpaces = actualBlockSpaces;
                            actualBlockSpaces = -1;
                        }
                        if (Character.isLetterOrDigit(character)) {
                            word.append(character);
                            actualBlockSpaces = -1;
                        } else {
                            if (word.length() > 0) {
                                tokenList.add(new Token(Constants.TOKEN_WORD, word.toString(), lineNumber,
                                        positionInLine - word.length()));
                                word = new StringBuilder();
                            }
                            //test for doubled values like (==, <=, ...)
                            if (i < (line.length() - 1)) {
                                char nextCharacter = line.charAt(i + 1);
                                if (!Character.isSpaceChar(nextCharacter) && !Character.isLetterOrDigit(nextCharacter)) {
                                    StringBuilder stringBuilder = new StringBuilder();
                                    stringBuilder.append(character).append(nextCharacter);
                                    if (SupportedCharacters.containsConcatenatedString(stringBuilder.toString())) {
                                        i++;
                                        addTokenToList(tokenList, lineNumber, positionInLine, stringBuilder);
                                        continue;
                                    } else {
                                        //possible place to throw an exception ?
                                    }
                                }
                            }
                            if (SupportedCharacters.isThisCharacterSupported(character)) {
                                addTokenToList(tokenList, lineNumber, positionInLine, character);
                            } else {
                                throw new TokenizationException(lineNumber, positionInLine, "Unsupported character: " + character);
                            }
                        }
                    }
                }
                if (word.length() > 0) {
                    tokenList.add(new Token(Constants.TOKEN_WORD, word.toString(), lineNumber, positionInLine
                            - word.length()));
                    //word = new StringBuilder();
                }
                tokenList.add(new Token(Constants.TOKEN_MARK, Constants.END_OF_LINE, lineNumber, positionInLine));
            }
            tokenList.add(new Token(Constants.TOKEN_MARK, Constants.END_OF_FILE, lineNumber, positionInLine));
            return new TokenSequence(tokenList);
        }
        return null;
    }

    private void addTokenToList(List<Token> tokenList, int lineNumber, int positionInLine, StringBuilder stringBuilder) {
        tokenList.add(new Token(Constants.TOKEN_CHARACTER, stringBuilder.toString(), lineNumber, positionInLine));
    }

    private void addTokenToList(List<Token> tokenList, int lineNumber, int positionInLine, char character) {
        tokenList.add(new Token(Constants.TOKEN_CHARACTER, Character.toString(character), lineNumber, positionInLine));
    }
}
