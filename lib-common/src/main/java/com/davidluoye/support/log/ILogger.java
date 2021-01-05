
package com.davidluoye.support.log;

import android.os.Build;
import android.util.Log;

import com.davidluoye.support.util.LogUtil;

/** A logcat utils */
public class ILogger {

    public static final boolean DEBUG = "userdebug".equals(Build.TYPE);

    public static final int DEFULT_LEVEL = DEBUG ? Log.VERBOSE : Log.DEBUG;

    public static ILogger logger() {
        return logger(null, DEFULT_LEVEL);
    }

    public static ILogger logger(String owner) {
        return logger(owner, DEFULT_LEVEL);
    }

    public static ILogger logger(Class<?> owner) {
        return logger(owner.getSimpleName(), DEFULT_LEVEL);
    }

    public static ILogger logger(String owner, int level) {
        ILogger LOG = new ILogger(owner, level);
        return LOG;
    }

    private String owner;
    private int level;

    private ILogger(String owner) {
        this(owner, DEFULT_LEVEL);
    }

    private ILogger(String owner, int level) {
        this.owner = owner;
        this.level = level;
    }

    public int getLogLevel() {
        return level;
    }

    public ILogger setLogLevel(int level) {
        this.level = level;
        return this;
    }

    public boolean canLog(int level) {
        if (this.level <= level) {
            return true;
        } else if (owner != null) {
            return Log.isLoggable(owner, level);
        }
        return false;
    }

    public void v(String msg) {
        if (canLog(Log.VERBOSE)) {
            writeLog(owner, Log.VERBOSE, msg);
        }
    }

    public void v(String format, Object...args) {
        if (canLog(Log.VERBOSE)) {
            writeLog(owner, Log.VERBOSE, format, args);
        }
    }

    public void d(String msg) {
        if (canLog(Log.DEBUG) || DEBUG) {
            writeLog(owner, Log.DEBUG, msg);
        }
    }

    public void d(String format, Object...args) {
        if (canLog(Log.DEBUG) || DEBUG) {
            writeLog(owner, Log.DEBUG, format, args);
        }
    }

    public void w(String msg) {
        if (canLog(Log.WARN) || DEBUG) {
            writeLog(owner, Log.WARN, msg);
        }
    }

    public void w(String format, Object...args) {
        if (canLog(Log.WARN) || DEBUG) {
            writeLog(owner, Log.INFO, format, args);
        }
    }

    public void w(Throwable tr) {
        if (canLog(Log.WARN) || DEBUG) {
            writeLog(owner, Log.INFO, "", tr);
        }
    }

    public void w(Throwable tr, String msg) {
        if (canLog(Log.WARN) || DEBUG) {
            writeLog(owner, Log.WARN, msg, tr);
        }
    }

    public void w(Throwable tr, String format, Object...args) {
        if (canLog(Log.WARN) || DEBUG) {
            writeLog(owner, Log.WARN, format, args);
        }
    }

    public void i(String msg) {
        if (canLog(Log.INFO) || DEBUG) {
            writeLog(owner, Log.INFO, msg);
        }
    }

    public void i(String format, Object...args) {
        if (canLog(Log.INFO) || DEBUG) {
            writeLog(owner, Log.INFO, format, args);
        }
    }

    public void i(Throwable tr, String msg) {
        if (canLog(Log.INFO) || DEBUG) {
            writeLog(owner, Log.INFO, msg, tr);
        }
    }

    public void i(Throwable tr, String format, Object...args) {
        if (canLog(Log.INFO) || DEBUG) {
            writeLog(owner, Log.INFO, format, args, tr);
        }
    }

    public void e(String msg) {
        if (canLog(Log.ERROR) || DEBUG) {
            writeLog(owner, Log.ERROR, msg);
        }
    }

    public void e(String format, Object...args) {
        if (canLog(Log.ERROR) || DEBUG) {
            writeLog(owner, Log.ERROR, format, args);
        }
    }

    public void e(Throwable tr, String msg) {
        if (canLog(Log.ERROR) || DEBUG) {
            writeLog(owner, Log.ERROR, msg, tr);
        }
    }

    public void e(Throwable tr, String format, Object...args) {
        if (canLog(Log.ERROR) || DEBUG) {
            writeLog(owner, Log.ERROR, format, args);
        }
    }


    public static void vv(String tag, String msg) {
        writeLog(tag, Log.VERBOSE, msg);
    }

    public static void vv(String tag, String format, Object...args) {
        writeLog(tag, Log.VERBOSE, format, args);
    }

    public static void dd(String tag, String msg) {
        writeLog(tag, Log.DEBUG, msg);
    }

    public static void dd(String tag, String format, Object...args) {
        writeLog(tag, Log.DEBUG, format, args);
    }

    public static void ii(String tag, String msg) {
        writeLog(tag, Log.INFO, msg);
    }

    public static void ii(String tag, String format, Object...args) {
        writeLog(tag, Log.INFO, format, args);
    }

    public static void ww(String tag, String msg) {
        writeLog(tag, Log.WARN, msg);
    }

    public static void ww(String tag, String format, Object...args) {
        writeLog(tag, Log.WARN, format, args);
    }

    public static void ee(String tag, String msg) {
        writeLog(tag, Log.ERROR, msg);
    }

    public static void ee(String tag, String format, Object...args) {
        writeLog(tag, Log.ERROR, format, args);
    }

    // ========================== internal function ===========================

    private static void writeLog(String tag, int level, String format, Object...args) {
        if (Configuration.canLog(level)) {
            writeLog(tag, level, String.format(format, args));
        }
    }

    private static void writeLog(String tag, int level, String msg, Throwable tr) {
        if (Configuration.canLog(level)) {
            writeLog(tag, level, msg + "\n" + LogUtil.dumpTrace(tr));
        }
    }

    private static void writeLog(String tag, int level, String msg) {
        if (Configuration.canLog(level)) {
            writeLogImpl(tag, level, msg);
        }
    }

    private static void writeLogImpl(String owner, int targetLevel, String msg) {
        Configuration configuration = Configuration.get();

        String appTag = null;
        if (configuration == null || configuration.appTag == null) {
            appTag = Configuration.APP_TAG;
        } else {
            appTag = configuration.appTag;
        }

        String tag = appTag;
        if (owner != null) {
            tag = String.format("%s: [%s]", tag, owner);
        }

        if (configuration != null && configuration.logger != null) {
            IFileLogger logger = configuration.logger;
            logger.write(tag, LogUtil.translateLevel(targetLevel), msg);
            if(!configuration.alwaysPrint) {
                return;
            }
        }

        Log.println(targetLevel, tag, msg);
    }
}
