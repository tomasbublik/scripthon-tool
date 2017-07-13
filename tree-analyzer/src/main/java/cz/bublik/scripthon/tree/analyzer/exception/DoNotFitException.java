package cz.bublik.scripthon.tree.analyzer.exception;

public class DoNotFitException extends Exception {

    private String error;

    public DoNotFitException() {
        super(); // call superclass constructor
        error = "Do not fit exception";
    }

    public DoNotFitException(String err) {
        super(err); // call super class constructor
        error = err; // save message
    }

    public String getError() {
        return error;
    }
}
