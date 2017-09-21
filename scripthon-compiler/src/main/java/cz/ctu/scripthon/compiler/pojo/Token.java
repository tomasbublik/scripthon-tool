package cz.ctu.scripthon.compiler.pojo;

public class Token {

    private String type;

    private Object value;

    private int line;

    private int positionInLine;

    public Token(String type, Object value) {
        this.type = type;
        this.value = value;
    }

    public Token(String type, Object value, int line, int positionInLine) {
        this.type = type;
        this.value = value;
        this.line = line;
        this.positionInLine = positionInLine;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getPositionInLine() {
        return positionInLine;
    }

    public void setPositionInLine(int positionInLine) {
        this.positionInLine = positionInLine;
    }
}
