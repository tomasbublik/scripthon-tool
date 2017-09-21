package cz.ctu.scripthon.forms;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import cz.ctu.scripthon.Main;
import cz.ctu.scripthon.Performer;
import cz.ctu.scripthon.Settings;
import cz.ctu.scripthon.tree.matcher.pojo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

public class MainForm {

    static final Logger LOG = LoggerFactory.getLogger(MainForm.class);


    private JPanel mainPanel;
    private JTextArea scripthonInputArea;
    private JavaSourcesField javaSourcesField;
    private JButton openButton;
    private JButton findButton;
    private JTextArea resultsArea;
    private JarsDirField dependenciesFolderField;
    private JButton openDependenciesButton;
    private JProgressBar progressBar;
    private JLabel notificationLabel;

    public MainForm(final JFrame jFrame) {
        createButtons(jFrame);
        loadFieldValues();
        createCommonFields(jFrame);
        performWindowRoutines(jFrame);
    }

    private void performWindowRoutines(JFrame jFrame) {
        synchronizeJavaSourcesWithContext(javaSourcesField.getText());
        synchronizeScripthonSourcesWithContext(scripthonInputArea.getText());
        synchronizeDependencyJarsWithContext(dependenciesFolderField.getText());
        buttonsCanBeShownForCommonPanel();
        jFrame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void windowClosing(WindowEvent e) {
                if (Main.getApplicationContext().getScripthonSource() != null && !Main.getApplicationContext().getScripthonSource().isEmpty()) {
                    Settings.savePreference(Settings.SCRIPTHON_SOURCES, Main.getApplicationContext().getScripthonSource());
                }
                if (Main.getApplicationContext().getJavaSourcesFile() != null) {
                    Settings.savePreference(Settings.JAVA_SOURCES_DIR_PATH, Main.getApplicationContext().getJavaSourcesFile().getAbsolutePath());
                }
                if (Main.getApplicationContext().getDependencyJarsFolder() != null) {
                    Settings.savePreference(Settings.DEPENDENCIES_DIR_PATH, Main.getApplicationContext().getDependencyJarsFolder().getAbsolutePath());
                }
            }

            @Override
            public void windowClosed(WindowEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void windowIconified(WindowEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void windowActivated(WindowEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
    }

    private void createButtons(final JFrame jFrame) {
        findButton.setVisible(false);
        openButton.addActionListener(e -> {
            File javaSourcesFile = openDirectory(jFrame);
            if (javaSourcesFile != null) {
                javaSourcesField.setText(javaSourcesFile);
                buttonsCanBeShownForCommonPanel();
                Settings.savePreference(Settings.JAVA_SOURCES_DIR_PATH, javaSourcesFile.getAbsolutePath());
            }
        });

        progressBar.setMinimum(MY_MINIMUM);
        progressBar.setMaximum(MY_MAXIMUM);
        progressBar.setStringPainted(true);

        openDependenciesButton.addActionListener(e -> {
            File jarsFolderFile = openDirectory(jFrame);
            if (jarsFolderFile != null) {
                dependenciesFolderField.setText(jarsFolderFile);
                buttonsCanBeShownForCommonPanel();
                Settings.savePreference(Settings.DEPENDENCIES_DIR_PATH, jarsFolderFile.getAbsolutePath());
            }
        });

        findButton.addActionListener(e -> {
            Runnable runnable = new Runnable() {
                void grayOutButtons() {
                    setButtonStatus(false);
                }

                void recoverButtons() {
                    setButtonStatus(true);
                }

                private void setButtonStatus(boolean b) {
                    openButton.setEnabled(b);
                    openDependenciesButton.setEnabled(b);
                    findButton.setEnabled(b);
                }

                public void run() {
                    grayOutButtons();
                    resultsArea.setText("");
                    try {
                        Performer.perform(Main.getApplicationContext(), progressBar, notificationLabel);
                    } catch (Exception e1) {
                        LOG.error(e1.getMessage());
                    } finally {
                        recoverButtons();
                    }
                }
            };

            Callback callback = new Callback();
            CallbackTask callbackTask = new CallbackTask(runnable, callback);
            (new Thread(callbackTask)).start();
            LOG.debug("Detection task was fired");
        });
    }

    private File openDirectory(JFrame jFrame) {
        File file = null;
        //Create a file chooser
        JFileChooser jFileChooser = null;
        final File javaSourcesFile = Main.getApplicationContext().getJavaSourcesFile();
        if (javaSourcesFile != null) {
            jFileChooser = new JFileChooser(javaSourcesFile);
        } else {
            new JFileChooser();
        }

        jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //In response to a button click:
        int returnVal = jFileChooser.showOpenDialog(jFrame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = jFileChooser.getSelectedFile();
            //This is where a real application would open the file.
            System.out.println("Opening: " + file.getName());
        } else {
            System.out.println("Open command cancelled by user");
        }
        return file;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private void buttonsCanBeShownForCommonPanel() {
        findButton.setVisible(sourcesLoaded());
    }

    private boolean sourcesLoaded() {
        return Main.getApplicationContext().getJavaSourcesFile() != null && Main.getApplicationContext().getScripthonSource() != null && !Main.getApplicationContext().getScripthonSource().isEmpty();
    }

    private void loadFieldValues() {
        String javaSourcesPath = Settings.getValue(Settings.JAVA_SOURCES_DIR_PATH);
        if (javaSourcesPath != null) {
            File javaSourcesDir = new File(javaSourcesPath);
            javaSourcesField.setText(javaSourcesDir);
        }

        String jarsDirPath = Settings.getValue(Settings.DEPENDENCIES_DIR_PATH);
        if (jarsDirPath != null) {
            File jarsDirDir = new File(jarsDirPath);
            dependenciesFolderField.setText(jarsDirDir);
        }


        String scripthonSource = Settings.getValue(Settings.SCRIPTHON_SOURCES);
        if (scripthonSource != null) {
            scripthonInputArea.setText(scripthonSource);
        }

    }

    private void createCommonFields(final JFrame jFrame) {
        javaSourcesField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                synchronizeJavaSourcesWithContext(javaSourcesField.getText());
                synchronizeDependencyJarsWithContext(dependenciesFolderField.getText());
                buttonsCanBeShownForCommonPanel();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                synchronizeJavaSourcesWithContext(javaSourcesField.getText());
                synchronizeDependencyJarsWithContext(dependenciesFolderField.getText());
                buttonsCanBeShownForCommonPanel();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                synchronizeJavaSourcesWithContext(javaSourcesField.getText());
                synchronizeDependencyJarsWithContext(dependenciesFolderField.getText());
                buttonsCanBeShownForCommonPanel();
            }
        });
        scripthonInputArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                synchronizeScripthonSourcesWithContext(scripthonInputArea.getText());
                buttonsCanBeShownForCommonPanel();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                synchronizeScripthonSourcesWithContext(scripthonInputArea.getText());
                buttonsCanBeShownForCommonPanel();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                synchronizeScripthonSourcesWithContext(scripthonInputArea.getText());
                buttonsCanBeShownForCommonPanel();
            }
        });
        Font font = new Font("Verdana", Font.BOLD, 12);
        scripthonInputArea.setFont(font);
    }

    private void synchronizeJavaSourcesWithContext(String fileName) {
        File javaSourcesFromContext = Main.getApplicationContext().getJavaSourcesFile();
        if (javaSourcesFromContext != null) {
            if (!javaSourcesFromContext.getAbsolutePath().equals(fileName)) {
                if (fileName == null || fileName.isEmpty()) {
                    Main.getApplicationContext().setJavaSourcesFile(null);
                } else {
                    File file = new File(fileName);
                    Main.getApplicationContext().setJavaSourcesFile(file);
                }
            }
        } else {
            if (fileName != null && !fileName.isEmpty()) {
                File file = new File(fileName);
                Main.getApplicationContext().setJavaSourcesFile(file);
            }
        }
    }

    private void synchronizeScripthonSourcesWithContext(String sources) {
        String scripthonSourcesFromContext = Main.getApplicationContext().getScripthonSource();
        if (scripthonSourcesFromContext != null) {
            if (!scripthonSourcesFromContext.equals(sources)) {
                if (sources == null || sources.isEmpty()) {
                    Main.getApplicationContext().setScripthonSource(null);
                } else {
                    String scripthonSources = sources;
                    Main.getApplicationContext().setScripthonSource(scripthonSources);
                }
            }
        } else {
            if (sources != null && !sources.isEmpty()) {
                String scripthonSources = sources;
                Main.getApplicationContext().setScripthonSource(scripthonSources);
            }
        }
    }

    private void synchronizeDependencyJarsWithContext(String jarsFolder) {
        File jarsFolderFromContext = Main.getApplicationContext().getDependencyJarsFolder();
        if (jarsFolderFromContext != null) {
            if (!jarsFolderFromContext.getAbsolutePath().equals(jarsFolder)) {
                if (jarsFolder == null || jarsFolder.isEmpty()) {
                    Main.getApplicationContext().setDependencyJarsFolder(null);
                } else {
                    File file = new File(jarsFolder);
                    Main.getApplicationContext().setDependencyJarsFolder(file);
                }
            }
        } else {
            if (jarsFolder != null && !jarsFolder.isEmpty()) {
                File file = new File(jarsFolder);
                Main.getApplicationContext().setDependencyJarsFolder(file);
            }
        }
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(8, 3, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.setPreferredSize(new Dimension(800, 400));
        javaSourcesField = new JavaSourcesField();
        mainPanel.add(javaSourcesField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(492, 22), null, 0, false));
        openButton = new JButton();
        openButton.setText("Open");
        mainPanel.add(openButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Java sources:");
        mainPanel.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Results:");
        mainPanel.add(label2, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Scripthon source:");
        mainPanel.add(label3, new GridConstraints(2, 0, 3, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        findButton = new JButton();
        findButton.setText("Find");
        mainPanel.add(findButton, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(44, 130), null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        mainPanel.add(scrollPane1, new GridConstraints(2, 1, 3, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        scripthonInputArea = new JTextArea();
        scripthonInputArea.setText("1");
        scrollPane1.setViewportView(scripthonInputArea);
        final JScrollPane scrollPane2 = new JScrollPane();
        mainPanel.add(scrollPane2, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        resultsArea = new JTextArea();
        resultsArea.setFont(new Font(resultsArea.getFont().getName(), resultsArea.getFont().getStyle(), 12));
        scrollPane2.setViewportView(resultsArea);
        final JLabel label4 = new JLabel();
        label4.setText("Dependencies:");
        mainPanel.add(label4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        dependenciesFolderField = new JarsDirField();
        mainPanel.add(dependenciesFolderField, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        openDependenciesButton = new JButton();
        openDependenciesButton.setText("Open jars");
        mainPanel.add(openDependenciesButton, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        progressBar = new JProgressBar();
        mainPanel.add(progressBar, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        notificationLabel = new JLabel();
        notificationLabel.setText("");
        mainPanel.add(notificationLabel, new GridConstraints(5, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final Spacer spacer1 = new Spacer();
        mainPanel.add(spacer1, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        mainPanel.add(spacer2, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        mainPanel.add(spacer3, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        mainPanel.add(spacer4, new GridConstraints(7, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

    class Callback {
        public void complete() {
            final Result result = Main.getApplicationContext().getResult();
            if (result != null && !result.toString().isEmpty()) {
                resultsArea.setText(result.toString());
            }
        }
    }

    class CallbackTask implements Runnable {

        private final Runnable task;

        private final Callback callback;

        CallbackTask(Runnable task, Callback callback) {
            this.task = task;
            this.callback = callback;
        }

        public void run() {
            task.run();
            callback.complete();
        }

    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }

    static final int MY_MINIMUM = 0;

    static final int MY_MAXIMUM = 100;

}