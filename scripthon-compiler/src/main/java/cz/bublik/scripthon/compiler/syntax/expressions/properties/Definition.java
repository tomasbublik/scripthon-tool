package cz.bublik.scripthon.compiler.syntax.expressions.properties;

public abstract class Definition {

    protected Rest rest = null;

    protected String name = null;

    public abstract String toString();

    public abstract int numberOfDefinedProperties();

    public String getName() {
        return name;
    }

    public Rest getRest() {
        return rest;
    }
}
