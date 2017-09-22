package cz.ctu.scripthon;

import cz.ctu.scripthon.forms.MainForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

public class Main {

    static final Logger LOG = LoggerFactory.getLogger(Main.class);

    private static ApplicationContext applicationContext;

    public static void main(String[] args) {
        applicationContext = new ApplicationContext();
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            LOG.error(e.getMessage());
        }
        JFrame frame = new JFrame("Scripthon tool");
        MainForm mainForm = new MainForm(frame);
        frame.setContentPane(mainForm.getMainPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}