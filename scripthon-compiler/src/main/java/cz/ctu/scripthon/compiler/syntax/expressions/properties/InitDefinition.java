package cz.ctu.scripthon.compiler.syntax.expressions.properties;

public class InitDefinition extends Definition {

    protected String type = null;

    protected String value = null;

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int numberOfDefinedProperties() {
        int propertiesNumber = 0;
        if (name != null) {
            propertiesNumber++;
        }
        if (type != null) {
            propertiesNumber++;
        }
        if (value != null) {
            propertiesNumber++;
        }
        return propertiesNumber;
    }

    @Override
    public String toString() {
        return "InitDefinition{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
