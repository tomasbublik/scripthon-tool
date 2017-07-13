package cz.bublik.scripthon.tree.analyzer.codeanalyzer.helper;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;
import cz.bublik.scripthon.tree.analyzer.codeanalyzer.common.model.AnnotationInfo;
import cz.bublik.scripthon.tree.analyzer.codeanalyzer.common.model.JavaClassInfo;
import cz.bublik.scripthon.tree.analyzer.codeanalyzer.common.model.LocationInfo;
import cz.bublik.scripthon.tree.analyzer.codeanalyzer.common.model.MethodInfo;
import cz.bublik.scripthon.tree.analyzer.codeanalyzer.common.util.CodeAnalyzerConstants;
import cz.bublik.scripthon.tree.analyzer.codeanalyzer.common.util.CodeAnalyzerUtil;

import javax.lang.model.element.*;
import javax.lang.model.util.SimpleElementVisitor6;
import java.nio.CharBuffer;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Helper class to set the properties of a method
 * to the java class model
 */
public class MethodInfoDataSetter {

    /**
     * Set the attributes of the currently visiting method
     * to the java class model
     *
     * @param clazzInfo  The java class model
     * @param methodTree Curently visiting method tree
     * @param path       tree path
     * @param trees      trees
     */
    public static <T extends Tree> MethodInfo populateMethodInfo(JavaClassInfo clazzInfo, T methodTree, TreePath path, Trees trees, CharBuffer buffer, CompilationUnitTree compileTree) {

        MethodInfo methodInfo = new MethodInfo();
        String methodName = ((MethodTree) methodTree).getName().toString();
        methodInfo.setOwningClass(clazzInfo);
        //Set modifier details
        Element e = trees.getElement(path);
        //Set the param type and return path
        visitExecutable(e, methodInfo);

        //set modifiers
        for (Modifier modifier : e.getModifiers()) {
            DataSetterUtil.setModifiers(modifier.toString(), methodInfo);
        }

        //annotations
        List<? extends AnnotationMirror> annotations = e.getAnnotationMirrors();
        for (AnnotationMirror annotationMirror : annotations) {
            String qualifiedName = annotationMirror.toString().substring(1);
            AnnotationInfo annotationInfo = new AnnotationInfo();
            annotationInfo.setName(qualifiedName);
            methodInfo.addAnnotation(annotationInfo);
        }
        LocationInfo locationInfo = DataSetterUtil.getLocationInfo(trees, path, methodTree);
        methodInfo.setLocationInfo(locationInfo);
        LocationInfo methodNameLoc = (LocationInfo) methodInfo.getLocationInfo();
        int startIndex = methodNameLoc.getStartOffset();
        int endIndex = -1;

        //Check if the method is a default constructor
        if (methodName.equals(CodeAnalyzerConstants.DEFAULT_CONSTRUCTOR_NAME)) {
            methodInfo.setName(CodeAnalyzerUtil.getSimpleNameFromQualifiedName(clazzInfo.getName()));
            clazzInfo.addConstructor(methodInfo);
            //TODO mark a constructor?
        } else {
            clazzInfo.addMethod(methodInfo);
            methodInfo.setName(methodName);
            if (startIndex >= 0) {
                String strToSearch = buffer.subSequence(startIndex, buffer.length()).toString();
                Pattern p = Pattern.compile(methodInfo.getName());
                Matcher matcher = p.matcher(strToSearch);
                boolean found = matcher.find();
                if (found) {
                    startIndex = matcher.start() + startIndex;
                    endIndex = startIndex + methodInfo.getName().length();
                } else {
                    return null;
                }
            }
        }
        methodNameLoc.setStartOffset(startIndex);
        methodNameLoc.setEndOffset(endIndex);
        methodNameLoc.setLineNumber(compileTree.getLineMap().getLineNumber(startIndex));
        return methodInfo;
    }

    /**
     * Visit the element passed to this method to extract the parameter types
     * and return type of the method
     *
     * @param e          Element being visited
     * @param methodInfo Model which holds method-level attributes
     */
    private static void visitExecutable(Element e, final MethodInfo methodInfo) {
        e.accept(new SimpleElementVisitor6<Object, MethodInfo>() {

            @Override
            public Object visitExecutable(ExecutableElement element, MethodInfo mInfo) {

                for (VariableElement var : element.getParameters()) {
                    methodInfo.addParameters(var.asType().toString());
                }
                methodInfo.setReturnType(element.getReturnType().toString());
                return super.visitExecutable(element, methodInfo);
            }
        }, null);
    }
}
