package cz.ctu.scripthon.tree.analyzer.codeanalyzer.common.model;

import java.util.Collection;


/**
 * Stores common attributes of a java class
 */
public interface BaseJavaClassModel {

    String getName();

    Collection<Annotation> getAnnotations();

    boolean isPublic();

    boolean isProtected();

    boolean isFinal();

    boolean isNative();

    boolean isStatic();

    boolean isPrivate();

    boolean isAbstract();

    Location getLocationInfo();
}
