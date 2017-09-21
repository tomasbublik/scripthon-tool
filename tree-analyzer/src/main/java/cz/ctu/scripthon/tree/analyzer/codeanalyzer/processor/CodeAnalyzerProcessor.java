package cz.ctu.scripthon.tree.analyzer.codeanalyzer.processor;

import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;
import cz.ctu.scripthon.tree.analyzer.tree.AccessHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

/**
 * The annotation processor class which processes java annotaions in the
 * supplied source file(s). This processor supports v1.6 of java language and
 * can processes all annotation types.
 */
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("*")
public class CodeAnalyzerProcessor extends AbstractProcessor {

    static final Logger LOG = LoggerFactory.getLogger(CodeAnalyzerProcessor.class);

    private Trees trees;

    @Override
    public void init(ProcessingEnvironment pe) {
        super.init(pe);
        trees = Trees.instance(pe);
    }

    /**
     * Processes the annotation types defined for this processor.
     *
     * @param annotations      the annotation types requested to be processed
     * @param roundEnvironment environment to get information about the current and prior
     *                         round
     * @return whether or not the set of annotations are claimed by this
     * processor
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {

        // Scanner class to scan through various component elements
        CodeAnalyzerTreeVisitor visitor = new CodeAnalyzerTreeVisitor();

        //long startTime = System.currentTimeMillis();

        for (Element e : roundEnvironment.getRootElements()) {
            TreePath tp = trees.getPath(e);
            // invoke the scanner
            try {
                visitor.scan(tp, trees);
            } catch (Exception e1) {
                e1.printStackTrace();
                LOG.error(String.valueOf(e1));
            }

            AccessHelper accessHelper = AccessHelper.getInstance();
            accessHelper.addToMap(e.getSimpleName().toString());
            accessHelper.printTree();
        }
        return true;
    }

}
