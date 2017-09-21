package cz.ctu.scripthon.tree.analyzer.codeanalyzer.common.model;

import java.io.Serializable;
import java.util.Collection;


/**
 * Stores details of fields in the java code
 */
public interface Field extends BaseJavaClassModel, Serializable {

    void addFieldType(String fieldType);

    /**
     * @return the {@link ClassFile} this method belongs to.
     */
    ClassFile getOwningClass();

    Collection<String> getFieldTypes();

}
