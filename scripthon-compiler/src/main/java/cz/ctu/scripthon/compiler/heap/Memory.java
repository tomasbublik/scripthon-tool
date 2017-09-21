package cz.ctu.scripthon.compiler.heap;

import java.util.HashMap;
import java.util.Map;

public class Memory {

    private Map<String, CommonVariable> mapOfVariables;

    private static Memory instance;

    private Memory() {
        mapOfVariables = new HashMap<>();
    }

    public static Memory createMemory() {
        if (instance == null) {
            instance = new Memory();
        }

        return instance;
    }

    public static Memory getInstance() {
        if (instance == null) {
            instance = createMemory();
        }
        return instance;
    }

    public void saveVariable(CommonVariable commonVariable) {
        //saves variable into map
        mapOfVariables.put(commonVariable.getName(), commonVariable);
    }

    public CommonVariable getVariable(String name) {
        //returns requested variable
        return mapOfVariables.get(name);
    }

    public void updateVariable(CommonVariable commonVariable) {
        //find requested variable and updates modified values
    }


}
