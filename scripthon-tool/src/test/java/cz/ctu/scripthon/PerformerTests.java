package cz.ctu.scripthon;

import cz.ctu.scripthon.tree.matcher.pojo.Result;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PerformerTests {

    private static ApplicationContext applicationContext;

    @Before
    public void setUp() {
        applicationContext = new ApplicationContext();
        File file = new File("..\\testSources\\test");
        applicationContext.setJavaSourcesFile(file);
    }

    @Test
    public void performTest() {
        applicationContext.setScripthonSource("Meth()");
        Performer.perform(applicationContext, new JProgressBar(), new JLabel());
        final Result result = applicationContext.getResult();
        assertNotNull(result);
        final String expectedResults = "\n" +
                "Number of results: 1\n" +
                "Number of comparisons: 9\n" +
                "Class name: MyClass on line: 3\n";
        assertEquals("Expected results: " + expectedResults + " but received: " + result, expectedResults, result.toString());
    }

}
