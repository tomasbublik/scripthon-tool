package cz.ctu.scripthon.tree.analyzer.codeanalyzer.common.util;

import cz.ctu.scripthon.tree.analyzer.codeanalyzer.common.model.Annotation;
import cz.ctu.scripthon.tree.analyzer.codeanalyzer.common.model.ClassFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates the utility methods of verification module
 */
public class CodeAnalyzerUtil {

    /**
     * Returns the simple name of an element from its fully qualified name
     *
     * @param qualifiedName Fully qualified name of the element
     * @return simpleName Simple name of the element
     */
    public static String getSimpleNameFromQualifiedName(String qualifiedName) {

        String simpleName = null;
        if (qualifiedName != null && qualifiedName.length() > 0) {
            simpleName = qualifiedName.substring(1 + qualifiedName.lastIndexOf("."), qualifiedName.length());
        }
        return simpleName;
    }

    public static List getAnnotationAsStringList(ClassFile classFile) {
        List<String> annotationList = new ArrayList<String>();
        for (Annotation clazzAnnotation : classFile.getAnnotations()) {
            annotationList.add(clazzAnnotation.getName());
        }
        return annotationList;
    }
}
