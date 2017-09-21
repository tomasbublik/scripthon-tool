package cz.ctu.scripthon.tree.analyzer.codeanalyzer.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class MethodCallInfo extends BaseJavaClassModelInfo implements MethodCall, Serializable {

    private Collection<String> parameters = new ArrayList<String>();

    private String name;

    @Override
    public Collection<String> getParameters() {
        return parameters;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void addParameters(String parameter) {
        this.parameters.add(parameter);
    }
}
