/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author Axioo Pongo
 */

import java.util.prefs.Preferences;

public class ConfigHandler {
    private static final String PORT_KEY = "port";
    private static final String WEB_DIR_KEY = "webDir";
    private static final String LOG_DIR_KEY = "logDir";
    
    // Menyimpan konfigurasi ke dalam Preferences
    public static void savePreferences(int port, String webDir, String logDir) {
        Preferences prefs = Preferences.userNodeForPackage(ConfigHandler.class);
        prefs.putInt(PORT_KEY, port);
        prefs.put(WEB_DIR_KEY, webDir);
        prefs.put(LOG_DIR_KEY, logDir);
    }
    
    // Mendapatkan nilai port dari Preferences
    public static int getPortPreference() {
        Preferences prefs = Preferences.userNodeForPackage(ConfigHandler.class);
        return prefs.getInt(PORT_KEY, 8080); // Default port 8080
    }
    
    // Mendapatkan nilai web directory dari Preferences
    public static String getWebDirPreference() {
        Preferences prefs = Preferences.userNodeForPackage(ConfigHandler.class);
        return prefs.get(WEB_DIR_KEY, "D://"); // Default web directory D://
    }
    
    // Mendapatkan nilai log directory dari Preferences
    public static String getLogDirPreference() {
        Preferences prefs = Preferences.userNodeForPackage(ConfigHandler.class);
        return prefs.get(LOG_DIR_KEY, "D://"); // Default log directory D://
    }
}