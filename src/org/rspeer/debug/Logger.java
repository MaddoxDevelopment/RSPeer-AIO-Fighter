package org.rspeer.debug;

import org.rspeer.config.Config;
import org.rspeer.ui.Log;

public class Logger {

    public static void debug(String message) {
        if(Config.getLogLevel() != LogLevel.Debug && Config.getLogLevel() != LogLevel.All)
            return;
        Log.info("DEBUG", message);
    }

    public static void info(String message) {
        Log.info("INFO", message);
    }

    public static void fine(String message) {
        Log.fine(message);
    }

    public static void severe(String message) {
        Log.severe(message);
    }

    public static void trace(String message) {
        if(Config.getLogLevel() != LogLevel.All)
            return;
        Log.info(message);
    }

    public static void exception(Exception e) {
        e.printStackTrace();
    }

}
