package cz.ctu.scripthon.compiler.exceptions;

public class TokenizationException extends Exception {

    private int lineNumber;
    private int positionInLine;
    private String textOfError;

    public TokenizationException(int lineNumber, int positionInLine, String textOfError) {
        this.lineNumber = lineNumber;
        this.positionInLine = positionInLine;
        this.textOfError = textOfError;
    }

    @Override
    public String getMessage() {
        return textOfError + " on line: " + lineNumber + " at: " + positionInLine;
    }
}
