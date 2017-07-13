package cz.bublik.scripthon.tree.analyzer.codeanalyzer.common.model;

import java.util.Collection;


/**
 * Stores method information of java class
 */
public interface Method extends BaseJavaClassModel {

    /**
     * @return the {@link ClassFile} this method belongs to.
     */
    ClassFile getOwningClass();

    /**
     * @return the internal names of the method's exception classes. May be
     * null.
     */
    Collection<String> getExceptions();

    Collection<String> getParameters();

    String getReturnType();

}
