package cz.bublik.scripthon;

import cz.bublik.scripthon.tree.matcher.pojo.Result;
import org.junit.Before;
import org.junit.Ignore;
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
    @Ignore
    public void performTest() {
        applicationContext.setScripthonSource("Meth()");
        Performer.perform(applicationContext, new JProgressBar(), new JLabel());
        final Result result = applicationContext.getResult();
        assertNotNull(result);
        final String expectedResults = "\n" +
                "Class name: MyClass on line: 9\n" +
                "Class name: ProbeTest3 on line: 14\n" +
                "Class name: ProbeTest3 on line: 21\n";
        assertEquals("Expected results: " + expectedResults + " but received: " + result, expectedResults, result.toString());
    }

}
