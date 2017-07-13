package cz.bublik.scripthon.tree.analyzer.codeanalyzer.helper;

import com.sun.source.tree.AnnotationTree;
import cz.bublik.scripthon.tree.analyzer.codeanalyzer.common.model.AnnotationInfo;
import cz.bublik.scripthon.tree.analyzer.codeanalyzer.common.model.JavaClassInfo;

import javax.lang.model.type.TypeMirror;

/**
 * Helper class to set the properties of an Annotation
 * to the java class model
 */
public class AnnotationDataSetter {

    /**
     * Set annotation attributes to the java class model
     *
     * @param clazzInfo      The java class model
     * @param annotationTree Curently visiting annotation tree
     * @param mirror         type mirror
     */
    public static void populateAnnotationInfo(JavaClassInfo clazzInfo, AnnotationTree annotationTree, TypeMirror mirror) {
        String qualifiedName = mirror.toString();
        AnnotationInfo annotationInfo = new AnnotationInfo();
        //set the full qualified name of annotation
        annotationInfo.setName(qualifiedName);
        clazzInfo.addAnnotation(annotationInfo);
    }
}
