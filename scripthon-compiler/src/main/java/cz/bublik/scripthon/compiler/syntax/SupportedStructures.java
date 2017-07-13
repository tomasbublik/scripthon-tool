package cz.bublik.scripthon.compiler.syntax;

public enum SupportedStructures {

    CLASS("Class"),
    METHOD("Meth"),
    BLOCK("Block"),
    METHOD_CALL("MethCall"),
    INIT("Init"),
    ANY("Any");

    private String name;

    SupportedStructures(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
