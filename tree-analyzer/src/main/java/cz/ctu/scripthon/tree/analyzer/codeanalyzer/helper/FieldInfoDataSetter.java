package cz.ctu.scripthon.tree.analyzer.codeanalyzer.helper;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;
import cz.ctu.scripthon.tree.analyzer.codeanalyzer.common.model.AnnotationInfo;
import cz.ctu.scripthon.tree.analyzer.codeanalyzer.common.model.FieldInfo;
import cz.ctu.scripthon.tree.analyzer.codeanalyzer.common.model.JavaClassInfo;
import cz.ctu.scripthon.tree.analyzer.codeanalyzer.common.model.LocationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import java.nio.CharBuffer;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Helper class to set the properties of
 * fields to the java class model
 */
public class FieldInfoDataSetter {

    static final Logger LOG = LoggerFactory.getLogger(FieldInfoDataSetter.class);

    public static <T extends Tree> FieldInfo populateFieldInfo(JavaClassInfo clazzInfo, T variableTree, TreePath path, Trees trees, CharBuffer buffer, CompilationUnitTree compileTree) {

        //Set modifier details
        Element e = trees.getElement(path);

        if (e == null) {
            return null;
        }

        FieldInfo fieldInfo = new FieldInfo();
        String fieldName = ((VariableTree) variableTree).getName().toString();
        fieldInfo.setOwningClass(clazzInfo);
        //Set modifier details
        for (Modifier modifier : e.getModifiers()) {
            DataSetterUtil.setModifiers(modifier.toString(), fieldInfo);
        }
        List<? extends AnnotationMirror> annotations = e.getAnnotationMirrors();
        for (AnnotationMirror annotationMirror : annotations) {
            String qualifiedName = annotationMirror.toString().substring(1);
            AnnotationInfo annotationInfo = new AnnotationInfo();
            annotationInfo.setName(qualifiedName);
            fieldInfo.addAnnotation(annotationInfo);
        }

        //Set line in a source code
        LocationInfo locationInfo = DataSetterUtil.getLocationInfo(trees, path, variableTree);
        fieldInfo.setLocationInfo(locationInfo);
        fieldInfo.setName(fieldName);
        clazzInfo.addField(fieldInfo);
        LocationInfo variableNameLoc = (LocationInfo) fieldInfo.getLocationInfo();
        int startIndex = variableNameLoc.getStartOffset();
        int endIndex = -1;
        if (startIndex >= 0) {
            String strToSearch = buffer.subSequence(startIndex, buffer.length()).toString();
            Pattern p = Pattern.compile(fieldInfo.getName());
            Matcher matcher = p.matcher(strToSearch);
            matcher.find();
            try {
                startIndex = matcher.start() + startIndex;
            } catch (Exception e1) {
                LOG.error(e1.getMessage());
            }
            endIndex = startIndex + fieldInfo.getName().length();
        }
        variableNameLoc.setStartOffset(startIndex);
        variableNameLoc.setEndOffset(endIndex);
        variableNameLoc.setLineNumber(compileTree.getLineMap().getLineNumber(startIndex));


        fieldInfo.setLocationInfo(locationInfo);
        return fieldInfo;
    }
}
