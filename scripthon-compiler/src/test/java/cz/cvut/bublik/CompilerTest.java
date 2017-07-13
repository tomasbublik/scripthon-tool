package cz.cvut.bublik;

import cz.bublik.scripthon.compiler.syntax.Interpreter;
import cz.bublik.scripthon.compiler.utils.FileUtils;
import org.junit.Before;

import java.util.List;

public abstract class CompilerTest {

    private static final int default_value = 0;

    protected Interpreter interpreter;

    protected List<String> fileLinesList;

    @Before
    public void setUp() {
        interpreter = new Interpreter(default_value);
        fileLinesList = FileUtils.readFile("../scripthon-compiler/scripthonSources/test-file.scripthon");
    }

}
