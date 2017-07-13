package cz.bublik.scripthon;

import java.util.prefs.Preferences;

public class Settings {

    public static final String JAVA_SOURCES_DIR_PATH = "javaSourcesDirPath";
    public static final String DEPENDENCIES_DIR_PATH = "jarsDirPath";
    public static final String SCRIPTHON_SOURCES = "scripthonSources";
    private static Preferences prefs = Preferences.userRoot().node("Settings");

    public static void savePreference(String key, String value) {
        prefs.put(key, value);
    }

    public static String getValue(String key) {
        String value = prefs.get(key, null);
        return value;
    }
}