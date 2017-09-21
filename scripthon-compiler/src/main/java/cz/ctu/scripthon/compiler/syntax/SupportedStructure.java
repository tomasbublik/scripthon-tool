package cz.ctu.scripthon.compiler.syntax;

public enum SupportedStructure {

    CLASS("Class"),
    METHOD("Meth"),
    BLOCK("Block"),
    METHOD_CALL("MethCall"),
    INIT("Init"),
    ANY("Any");

    private String name;

    SupportedStructure(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
