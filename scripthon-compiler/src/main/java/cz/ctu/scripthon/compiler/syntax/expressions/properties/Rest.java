package cz.ctu.scripthon.compiler.syntax.expressions.properties;

import java.util.NoSuchElementException;

public enum Rest {

    PUBLIC("public"), PRIVATE("private"), PROTECTED("protected");

    public final String code;

    private Rest(String code) {
        this.code = code;
    }

    public static Rest getEnum(String code) {
        for (Rest e : values()) {
            if (e.code.equals(code))
                return e;
        }
        throw new NoSuchElementException("Unknown code: " + code);
    }
}
