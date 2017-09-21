package cz.ctu.scripthon.compiler.syntax;

public enum SupportedStatements {

    IF("if"),
    ELSE("else");

    private String name;

    SupportedStatements(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
