package cz.ctu.scripthon.compiler.exceptions;

public class SyntaxException extends Exception {

    private String error;

    public SyntaxException() {
        super(); // call superclass constructor
        error = "unknown";
    }

    public SyntaxException(String err) {
        super(err); // call super class constructor
        error = err; // save message
    }

    public String getError() {
        return error;
    }
}
