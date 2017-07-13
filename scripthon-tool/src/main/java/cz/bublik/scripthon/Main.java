package cz.bublik.scripthon;

import cz.bublik.scripthon.forms.MainForm;
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
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InstantiationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
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