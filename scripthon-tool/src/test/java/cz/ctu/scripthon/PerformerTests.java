package cz.ctu.scripthon;

import cz.ctu.scripthon.tree.matcher.pojo.Result;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.io.File;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PerformerTests {

    private static ApplicationContext applicationContext;

    @Before
    public void setUp() {
       /* try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        applicationContext = new ApplicationContext();
        final String filePath = getClass().getResource("/testSources/test").getPath();
        //File file = new File("..\\testSources\\test");
        applicationContext.setJavaSourcesFile(new File(filePath));
    }

    @Test
    public void shouldGetTheExpectedResultsWhenMethIsSearched() {
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

    @Test
    public void shouldGetTheExpectedPropertiesWhenMethIsSearched() {
        applicationContext.setScripthonSource("Meth()");
        Performer.perform(applicationContext, new JProgressBar(), new JLabel());

        final Result result = applicationContext.getResult();

        assertNotNull(result);
        assertFalse(result.getSourceIdentifications().isEmpty());
        assertEquals(result.getSourceIdentifications().get(0).getName(), "MyClass");
    }

}
