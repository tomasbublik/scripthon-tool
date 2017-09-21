package cz.ctu.scripthon.forms;

import cz.ctu.scripthon.Main;

import javax.swing.*;
import java.io.File;

public class JarsDirField extends JTextField {

    @Override
    public void setText(String t) {
        if (t == null || t.isEmpty()) {
            Main.getApplicationContext().setDependencyJarsFolder(null);
        }
        super.setText(t);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void setText(File jarsDirFile) {
        if (jarsDirFile != null) {
            Main.getApplicationContext().setDependencyJarsFolder(jarsDirFile);
            super.setText(jarsDirFile.getAbsolutePath());
        }
    }


}
