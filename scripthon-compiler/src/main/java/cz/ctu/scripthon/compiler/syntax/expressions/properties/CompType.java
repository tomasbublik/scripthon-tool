package cz.ctu.scripthon.compiler.syntax.expressions.properties;

public enum CompType {

    EQ("=="), GT_EQ(">="), LT_EQ("<="), LT("<"), GT(">");

    private final String sign;

    private CompType(String sign) {
        this.sign = sign;
    }

    public static CompType getByLiteral(String literal) {
        for (CompType compType : CompType.values()) {
            if (compType.sign.equals(literal)) {
                return compType;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "CompType{" +
                "sign='" + sign + '\'' +
                '}';
    }
}
