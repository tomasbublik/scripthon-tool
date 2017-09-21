package cz.ctu.scripthon.tree.analyzer.codeanalyzer.helper;

import com.sun.source.tree.ClassTree;
import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;
import cz.ctu.scripthon.tree.analyzer.codeanalyzer.common.model.AnnotationInfo;
import cz.ctu.scripthon.tree.analyzer.codeanalyzer.common.model.JavaClassInfo;
import cz.ctu.scripthon.tree.analyzer.codeanalyzer.common.model.JavaSourceTreeInfo;
import cz.ctu.scripthon.tree.analyzer.codeanalyzer.common.model.LocationInfo;
import cz.ctu.scripthon.tree.analyzer.codeanalyzer.common.util.CodeAnalyzerConstants;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.util.List;

/**
 * Helper class to set the properties of a java class
 * to the java class model
 */
public class ClassInfoDataSetter {

    /**
     * Set the attributes of the currently visiting class
     * to the java class model
     *
     * @param clazzInfo The java class model
     * @param classTree Curently visiting class tree
     * @param path      tree path
     * @param trees     trees
     */
    public static void populateClassInfo(JavaClassInfo clazzInfo, ClassTree classTree, TreePath path, Trees trees) {

        TypeElement element = (TypeElement) trees.getElement(path);

        //Set qualified class name
        clazzInfo.setName(element.getQualifiedName().toString());

        //Set Nesting kind
        clazzInfo.setNestingKind(element.getNestingKind().toString());

        //Set modifier details
        for (Modifier modifier : element.getModifiers()) {
            DataSetterUtil.setModifiers(modifier.toString(), clazzInfo);
        }

        //Set extending class info
        clazzInfo.setNameOfSuperClass(element.getSuperclass().toString());

        //Set implementing interface details
        for (TypeMirror mirror : element.getInterfaces()) {
            clazzInfo.addNameOfInterface(mirror.toString());
        }
        //Set serializable property
        try {
            Class serializable = Class.forName(CodeAnalyzerConstants.SERIALIZABLE_PKG);
            Class thisClass = Class.forName(element.getQualifiedName().toString());
            if (serializable.isAssignableFrom(thisClass)) {
                clazzInfo.setSerializable(true);
            } else {
                clazzInfo.setSerializable(false);
            }

        } catch (ClassNotFoundException ex) {
            clazzInfo.setSerializable(false);
        }

        List<? extends AnnotationMirror> annotations = element.getAnnotationMirrors();
        for (AnnotationMirror annotationMirror : annotations) {
            String qualifiedName = annotationMirror.toString().substring(1);
            AnnotationInfo annotationInfo = new AnnotationInfo();
            annotationInfo.setName(qualifiedName);
            clazzInfo.addAnnotation(annotationInfo);
        }

        LocationInfo locationInfo = DataSetterUtil.getLocationInfo(trees, path, classTree);
        clazzInfo.setLocationInfo(locationInfo);

        //setJavaTreeDetails
        JavaSourceTreeInfo treeInfo = new JavaSourceTreeInfo();
        TreePath tp = trees.getPath(element);
        treeInfo.setCompileTree(tp.getCompilationUnit());
        treeInfo.setSourcePos(trees.getSourcePositions());
        clazzInfo.setSourceTreeInfo(treeInfo);

    }
}
