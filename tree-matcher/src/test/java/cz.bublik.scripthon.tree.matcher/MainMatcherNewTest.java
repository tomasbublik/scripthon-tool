package cz.bublik.scripthon.tree.matcher;

import cz.bublik.scripthon.compiler.syntax.ProgramContainer;
import cz.bublik.scripthon.tree.analyzer.tree.AccessHelper;
import cz.bublik.scripthon.tree.matcher.pojo.Result;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;

import static junit.framework.TestCase.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

public class MainMatcherNewTest {

    @Before
    public void setUp() {
    }

    @Test
    public void shouldFindOneResultWhenBasicClassIsGiven() {
        CompilationUtils.createSources("/scripthon/test-file.scripthon", "/java");

        Result result = MainMatcherNew.compare(AccessHelper.getClassTrees(), ProgramContainer.getInstance().getProgram(), new JProgressBar(), new JLabel());

        assertNotNull(result);
        assertEquals(result.getSourceIdentifications().size(), 1);
    }

    @Test
    public void shouldFindTwoResultsWhenClassWithTwoVariablesIsGiven() {
        CompilationUtils.createSources("/scripthon/multiple-results.scripthon", "/java");

        Result result = MainMatcherNew.compare(AccessHelper.getClassTrees(), ProgramContainer.getInstance().getProgram(), new JProgressBar(), new JLabel());

        assertNotNull(result);
        assertEquals(result.getSourceIdentifications().size(), 2);
    }

    @Test
    public void shouldFindAtLeastOneResultsWhenTestScriptIsGiven() {
        CompilationUtils.createSources("/scripthon/test-script.scripthon", "/java");

        Result result = MainMatcherNew.compare(AccessHelper.getClassTrees(), ProgramContainer.getInstance().getProgram(), new JProgressBar(), new JLabel());

        assertNotNull(result);
        assertThat(result.getSourceIdentifications().size()).isGreaterThan(1);
    }

    @Test
    public void shouldFindAtLeastOneResultsWhenTestScriptWithTwoLastInitsIsGiven() {
        CompilationUtils.createSources("/scripthon/two-inits.scripthon", "/java");

        Result result = MainMatcherNew.compare(AccessHelper.getClassTrees(), ProgramContainer.getInstance().getProgram(), new JProgressBar(), new JLabel());

        assertNotNull(result);
        assertEquals(result.getSourceIdentifications().size(), 1);
    }

    @Test
    public void shouldFindHelloWorldResultsWhenTheDetailedTestScriptIsGiven() {
        CompilationUtils.createSources("/scripthon/hello-world.scripthon", "/java");

        Result result = MainMatcherNew.compare(AccessHelper.getClassTrees(), ProgramContainer.getInstance().getProgram(), new JProgressBar(), new JLabel());

        assertNotNull(result);
        assertEquals(result.getSourceIdentifications().size(), 1);
    }

    @Test
    public void shouldFindAtLeatOneResultWhenTheConditionalTestScriptIsGiven() {
        CompilationUtils.createSources("/scripthon/condition.scripthon", "/java");

        Result result = MainMatcherNew.compare(AccessHelper.getClassTrees(), ProgramContainer.getInstance().getProgram(), new JProgressBar(), new JLabel());

        assertNotNull(result);
        assertThat(result.getSourceIdentifications().size()).isGreaterThanOrEqualTo(1);
    }

    @Test
    public void shouldFindAtLeatOneResultWhenTestScriptWithAnyInTheMiddleIsGiven() {
        CompilationUtils.createSources("/scripthon/any-test.scripthon", "/java");

        Result result = MainMatcherNew.compare(AccessHelper.getClassTrees(), ProgramContainer.getInstance().getProgram(), new JProgressBar(), new JLabel());

        assertNotNull(result);
        assertThat(result.getSourceIdentifications().size()).isGreaterThanOrEqualTo(1);
    }

    @Test
    public void shouldFindAtLeatOneResultWhenTestScriptWithAnyInsideStructureIsGiven() {
        CompilationUtils.createSources("/scripthon/any-inside-test.scripthon", "/java");

        Result result = MainMatcherNew.compare(AccessHelper.getClassTrees(), ProgramContainer.getInstance().getProgram(), new JProgressBar(), new JLabel());

        assertNotNull(result);
        assertThat(result.getSourceIdentifications().size()).isGreaterThanOrEqualTo(1);
    }
}
