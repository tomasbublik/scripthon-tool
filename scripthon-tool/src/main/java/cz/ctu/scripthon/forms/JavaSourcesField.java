package cz.ctu.scripthon.forms;

import cz.ctu.scripthon.Main;

import javax.swing.*;
import java.io.File;

public class JavaSourcesField extends JTextField {

    @Override
    public void setText(String t) {
        if (t == null || t.isEmpty()) {
            Main.getApplicationContext().setJavaSourcesFile(null);
        }
        super.setText(t);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void setText(File javaSourcesFile) {
        if (javaSourcesFile != null) {
            Main.getApplicationContext().setJavaSourcesFile(javaSourcesFile);
            super.setText(javaSourcesFile.getAbsolutePath());
        }
    }
}