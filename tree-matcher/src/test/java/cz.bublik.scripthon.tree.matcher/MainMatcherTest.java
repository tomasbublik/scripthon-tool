package cz.bublik.scripthon.tree.matcher;


import cz.bublik.scripthon.compiler.syntax.ProgramContainer;
import cz.bublik.scripthon.tree.analyzer.tree.AccessHelper;
import cz.bublik.scripthon.tree.matcher.pojo.Result;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MainMatcherTest {

    @Before
    public void setUp() {
    }

    @Test
    public void shouldFindOneResultWhenBasicClassIsGiven() {
        CompilationUtils.createSources("/scripthon/test-file.scripthon", "/java");

        Result result = MainMatcher.compare(AccessHelper.getClassTrees(), ProgramContainer.getInstance().getProgram(), new JProgressBar(), new JLabel());

        assertNotNull(result);
        assertEquals(result.getSourceIdentifications().size(), 1);
    }

    @Test
    public void shouldFindTwoResultsWhenClassWithTwoVariablesIsGiven() {
        CompilationUtils.createSources("/scripthon/multiple-results.scripthon", "/java");

        Result result = MainMatcher.compare(AccessHelper.getClassTrees(), ProgramContainer.getInstance().getProgram(), new JProgressBar(), new JLabel());

        assertNotNull(result);
        assertEquals(result.getSourceIdentifications().size(), 2);
    }
}
