package com.example.wuyuxi.webcam.util;

import android.util.Log;

import com.example.wuyuxi.webcam.BuildConfig;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class Logging {
    public static final class LEVELS {
        public static final int DEBUG = 0;
        public static final int INFO = 1;
        public static final int ERROR = 2;
        public static final int FATAL = 3;
        public static final int DISABLED = 4;
    }

    private static int sLevel = BuildConfig.DEBUG ? LEVELS.DEBUG : LEVELS.DISABLED;

    public static String getTag() {
        StackTraceElement[] cause = Thread.currentThread().getStackTrace();
        String tag = "Logging";
        for (int i = 1; i < cause.length; i++) {
            if (cause[i - 1].getClassName().equals(Logging.class.getName())
                    && !cause[i].getClassName().equals(Logging.class.getName())) {
                tag = cause[i].getClassName().replaceAll("^.*\\.", "");
                break;
            }
        }
        return tag;
    }

    /**
     * Debug level logger
     */
    public static void d(String s) {
        if (isLevelLogging(LEVELS.DEBUG)) {
            Log.d(getTag(), s);
        }
    }

    public static void d(Throwable e) {
        if (isLevelLogging(LEVELS.DEBUG) && e != null) {
            List<String> error = new ArrayList<>();
            error.add(e.getMessage());
            for (StackTraceElement element : e.getStackTrace()) {
                error.add(element.toString());
            }
            for (String line : error) {
                Log.d(getTag(), line);
            }
        }
    }

    /**
     * Info level logger
     */
    public static void i(String s) {
        if (isLevelLogging(LEVELS.INFO)) {
            Log.i(getTag(), s);
        }
    }

    public static void i(Throwable e) {
        if (isLevelLogging(LEVELS.INFO) && e != null) {
            List<String> error = new ArrayList<>();
            error.add(e.getMessage());
            for (StackTraceElement element : e.getStackTrace()) {
                error.add(element.toString());
            }
            for (String line : error) {
                Log.d(getTag(), line);
            }
        }
    }

    /**
     * Error level logger
     */
    public static void e(String s) {
        if (isLevelLogging(LEVELS.ERROR)) {
            Log.e(getTag(), s);
        }
    }

    public static void e(Throwable e) {
        if (isLevelLogging(LEVELS.ERROR) && e != null) {
            List<String> error = new ArrayList<>();
            error.add(e.getMessage());
            for (StackTraceElement element : e.getStackTrace()) {
                error.add(element.toString());
            }
            for (String line : error) {
                Log.d(getTag(), line);
            }
        }
    }

    public static void setLogLevel(int level) {
        sLevel = level;
    }

    public static boolean isLevelLogging(int level) {
        return sLevel <= level;
    }

    public static void logStackTrace(Throwable throwable) {
        if (isLevelLogging(LEVELS.ERROR)) {
            final StringWriter result = new StringWriter();
            final PrintWriter printWriter = new PrintWriter(result);
            throwable.printStackTrace(printWriter);
            result.flush();
            e(result.toString());
        }
    }
}
