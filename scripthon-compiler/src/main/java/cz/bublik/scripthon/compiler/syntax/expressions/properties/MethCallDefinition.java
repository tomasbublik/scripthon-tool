package cz.bublik.scripthon.compiler.syntax.expressions.properties;

public class MethCallDefinition extends Definition {

    @Override
    public int numberOfDefinedProperties() {
        int propertiesNumber = 0;
        if (name != null) {
            propertiesNumber++;
        }

        return propertiesNumber;
    }

    @Override
    public String toString() {
        return "MethCallDefinition{" +
                "name='" + name + '\'' +
                '}';
    }
}
