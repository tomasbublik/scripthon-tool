package cz.bublik.scripthon.compiler.heap;

import cz.bublik.scripthon.compiler.syntax.SupportedStructures;
import cz.bublik.scripthon.compiler.syntax.expressions.properties.Definition;
import cz.bublik.scripthon.compiler.syntax.expressions.properties.MethDefinition;

import java.util.HashMap;
import java.util.Map;

public class ComplexVariable extends CommonVariable {

    private Map<String, Object> variableValues;

    public ComplexVariable(String name, SupportedStructures type) {
        super(name, type);
        //TODO create map from corresponding definition (i.e.: ClassDefinition, ...) ?
        variableValues = new HashMap<>();
    }

    public ComplexVariable(String name, SupportedStructures type, Map<String, Object> values) {
        super(name, type);
        variableValues = values;
    }

    public ComplexVariable(String name, SupportedStructures type, Definition definition) {
        super(name, type);
        if (definition instanceof MethDefinition) {
            MethDefinition methDefinition = (MethDefinition) definition;
            variableValues = new HashMap<>();
            if (methDefinition.getName() != null) {
                //TODO - unifikovat a propojit se syntaxí
                variableValues.put("Name", methDefinition.getName());
            }
            variableValues.put("ParamsNum", methDefinition.getParamsNum());
            if (methDefinition.getRest() != null) {
                variableValues.put("Rest", methDefinition.getRest());
            }
            if (methDefinition.getRet() != null) {
                variableValues.put("Ret", methDefinition.getRet());
            }
        }
        //TODO create map from rest of corresponding definition (i.e.: ClassDefinition, ...)
    }

    public Object getAttributeValue(String attributeName) {
        return variableValues.get(attributeName);
    }
}
