package cz.ctu.scripthon.compiler.syntax.expressions.properties;

public class ClassDefinition extends Definition {

    public String getName() {
        return name;
    }

    @Override
    public int numberOfDefinedProperties() {
        int propertiesNumber = 0;
        if (name != null) {
            propertiesNumber++;
        }
        if (rest != null) {
            propertiesNumber++;
        }
        return propertiesNumber;
    }

    @Override
    public String toString() {
        return "ClassDefinition{" +
                "name='" + name + '\'' +
                ", rest=" + rest +
                '}';
    }

}
