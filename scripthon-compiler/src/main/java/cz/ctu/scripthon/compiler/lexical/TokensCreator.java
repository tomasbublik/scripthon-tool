package cz.ctu.scripthon.compiler.lexical;

import cz.ctu.scripthon.compiler.exceptions.TokenizationException;
import cz.ctu.scripthon.compiler.pojo.Token;

import java.util.ArrayList;
import java.util.List;

import static cz.ctu.scripthon.compiler.constant.Constants.*;

public class TokensCreator {

    public static final int INITIAL_VALUE = -1;

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
            addStartOfFile(tokenList);
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
                    if (isSpace(character) && actualBlockSpaces != INITIAL_VALUE) {
                        actualBlockSpaces++;
                    }
                    if (!Character.isSpaceChar(character)) {
                        if (changedActualBlockSpaces(lineBlockSpaces, actualBlockSpaces)) {
                            int numberOfBlocks = absoluteDifference(lineBlockSpaces, actualBlockSpaces) / LINE_BLOCK_DIFFERENCE;
                            if (actualBlockSpaces > lineBlockSpaces) {
                                addBlockTokens(tokenList, lineNumber, positionInLine, numberOfBlocks);
                            } else {
                                addBackBlockTokens(tokenList, lineNumber, positionInLine, numberOfBlocks);
                            }
                            lineBlockSpaces = actualBlockSpaces;
                            actualBlockSpaces = INITIAL_VALUE;
                        }
                        if (Character.isLetterOrDigit(character)) {
                            word.append(character);
                            actualBlockSpaces = INITIAL_VALUE;
                        } else {
                            if (isNotEmpty(word)) {
                                addTokenWord(tokenList, lineNumber, positionInLine, word);
                                word.setLength(0);
                            }
                            //test for doubled values like (==, <=, ...)
                            if (isNotLast(line, i)) {
                                char nextCharacter = getNextCharacter(line, i);
                                if (!isSpaceLetterOrDigit(nextCharacter)) {
                                    final String concatenated = concatenateCharacters(character, nextCharacter);
                                    if (SupportedCharacters.containsConcatenatedString(concatenated)) {
                                        i++;
                                        addTokenToList(tokenList, lineNumber, positionInLine, concatenated);
                                        continue;
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
                if (isNotEmpty(word)) {
                    addTokenWord(tokenList, lineNumber, positionInLine, word);
                }
                addEndOfLine(tokenList, lineNumber, positionInLine);
            }
            addEndOfFile(tokenList, lineNumber, positionInLine);
            return new TokenSequence(tokenList);
        }
        return null;
    }

    private void addTokenWord(List<Token> tokenList, int lineNumber, int positionInLine, StringBuilder word) {
        tokenList.add(new Token(TOKEN_WORD, word.toString(), lineNumber, positionInLine - word.length()));
    }

    private boolean isNotEmpty(StringBuilder word) {
        return word.length() > 0;
    }

    private String concatenateCharacters(char character, char nextCharacter) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(character).append(nextCharacter);
        return stringBuilder.toString();
    }

    private boolean isSpaceLetterOrDigit(char nextCharacter) {
        return Character.isSpaceChar(nextCharacter) || Character.isLetterOrDigit(nextCharacter);
    }

    private char getNextCharacter(String line, int i) {
        return line.charAt(i + 1);
    }

    private boolean isNotLast(String line, int i) {
        return i < (line.length() - 1);
    }

    private boolean changedActualBlockSpaces(int lineBlockSpaces, int actualBlockSpaces) {
        return actualBlockSpaces != lineBlockSpaces && actualBlockSpaces != -1;
    }

    private int absoluteDifference(int lineBlockSpaces, int actualBlockSpaces) {
        return Math.abs(actualBlockSpaces - lineBlockSpaces);
    }

    private void addStartOfFile(List<Token> tokenList) {
        tokenList.add(new Token(TOKEN_MARK, START_OF_FILE, 0, 0));
    }

    private boolean isSpace(char character) {
        return character == ' ';
    }

    private void addEndOfLine(List<Token> tokenList, int lineNumber, int positionInLine) {
        tokenList.add(new Token(TOKEN_MARK, END_OF_LINE, lineNumber, positionInLine));
    }

    private void addEndOfFile(List<Token> tokenList, int lineNumber, int positionInLine) {
        tokenList.add(new Token(TOKEN_MARK, END_OF_FILE, lineNumber, positionInLine));
    }

    private void addBlockTokens(List<Token> tokenList, int lineNumber, int positionInLine, int howMany) {
        addToken(tokenList, lineNumber, positionInLine, howMany, BLOCK);
    }

    private void addBackBlockTokens(List<Token> tokenList, int lineNumber, int positionInLine, int howMany) {
        addToken(tokenList, lineNumber, positionInLine, howMany, BACK_BLOCK);
    }

    private void addToken(List<Token> tokenList, int lineNumber, int positionInLine, int howMany, String value) {
        for (int i = 0; i < howMany; i++) {
            tokenList.add(new Token(TOKEN_MARK, value, lineNumber, positionInLine - 1));
        }
    }

    private void addTokenToList(List<Token> tokenList, int lineNumber, int positionInLine, String value) {
        tokenList.add(new Token(TOKEN_CHARACTER, value, lineNumber, positionInLine));
    }

    private void addTokenToList(List<Token> tokenList, int lineNumber, int positionInLine, char character) {
        tokenList.add(new Token(TOKEN_CHARACTER, Character.toString(character), lineNumber, positionInLine));
    }
}
