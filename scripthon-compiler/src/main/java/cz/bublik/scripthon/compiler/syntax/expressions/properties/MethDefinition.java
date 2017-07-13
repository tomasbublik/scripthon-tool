package cz.bublik.scripthon.compiler.syntax.expressions.properties;

public class MethDefinition extends Definition {

    protected int paramsNum = -1;

    protected String ret = null;

    public String getName() {
        return name;
    }

    public Rest getRest() {
        return rest;
    }

    public int getParamsNum() {
        return paramsNum;
    }

    public String getRet() {
        return ret;
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
        if (paramsNum != -1) {
            propertiesNumber++;
        }
        if (ret != null) {
            propertiesNumber++;
        }
        return propertiesNumber;
    }

    @Override
    public String toString() {
        return "MethDefinition{" +
                "name='" + name + '\'' +
                ", rest=" + rest +
                ", paramsNum=" + paramsNum +
                ", ret='" + ret + '\'' +
                '}';
    }
}
