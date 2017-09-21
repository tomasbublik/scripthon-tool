package cz.ctu.scripthon.compiler.exceptions;

public class EmptySourceException extends Exception {

    private String error;

    public EmptySourceException(String error) {
        this.error = error;
    }
}
