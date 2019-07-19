
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
            writeLog(Log.DEBUG, msg);
        }
    }

    public void v(String format, Object...args) {
        if (canLog(Log.VERBOSE)) {
            v(String.format(format, args));
        }
    }

    public void d(String msg) {
        if (canLog(Log.DEBUG) || DEBUG) {
            writeLog(Log.DEBUG, msg);
        }
    }

    public void d(String format, Object...args) {
        if (canLog(Log.DEBUG) || DEBUG) {
            d(String.format(format, args));
        }
    }

    public void w(String msg) {
        if (canLog(Log.WARN) || DEBUG) {
            writeLog(Log.WARN, msg);
        }
    }

    public void w(String format, Object...args) {
        if (canLog(Log.WARN) || DEBUG) {
            w(String.format(format, args));
        }
    }

    public void w(Throwable tr) {
        if (canLog(Log.WARN) || DEBUG) {
            writeLog(Log.INFO, "", tr);
        }
    }

    public void w(Throwable tr, String msg) {
        if (canLog(Log.WARN) || DEBUG) {
            writeLog(Log.WARN, msg, tr);
        }
    }

    public void w(Throwable tr, String format, Object...args) {
        if (canLog(Log.WARN) || DEBUG) {
            w(String.format(format, args));
        }
    }

    public void i(String msg) {
        if (canLog(Log.INFO) || DEBUG) {
            writeLog(Log.INFO, msg);
        }
    }

    public void i(String format, Object...args) {
        if (canLog(Log.INFO) || DEBUG) {
            i(String.format(format, args));
        }
    }

    public void i(Throwable tr, String msg) {
        if (canLog(Log.INFO) || DEBUG) {
            writeLog(Log.INFO, msg, tr);
        }
    }

    public void i(Throwable tr, String format, Object...args) {
        if (canLog(Log.INFO) || DEBUG) {
            i(tr, String.format(format, args));
        }
    }

    public void e(String msg) {
        if (canLog(Log.ERROR) || DEBUG) {
            writeLog(Log.ERROR, msg);
        }
    }

    public void e(String format, Object...args) {
        if (canLog(Log.ERROR) || DEBUG) {
            e(String.format(format, args));
        }
    }

    public void e(Throwable tr, String msg) {
        if (canLog(Log.ERROR) || DEBUG) {
            writeLog(Log.ERROR, msg, tr);
        }
    }

    public void e(Throwable tr, String format, Object...args) {
        if (canLog(Log.ERROR) || DEBUG) {
            e(tr, String.format(format, args));
        }
    }

    private void writeLog(int targetLevel, String msg, Throwable tr) {
        writeLog(targetLevel,msg + "\n" + LogUtil.getStackTraceString(tr));
    }

    private void writeLog(int targetLevel, String msg) {
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
