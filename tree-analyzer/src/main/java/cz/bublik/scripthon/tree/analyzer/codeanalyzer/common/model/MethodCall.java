package cz.bublik.scripthon.tree.analyzer.codeanalyzer.common.model;

import java.util.Collection;

public interface MethodCall {

    String getName();

    Collection<String> getParameters();

}
