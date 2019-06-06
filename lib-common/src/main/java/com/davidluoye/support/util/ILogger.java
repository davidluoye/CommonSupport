
package com.davidluoye.support.util;

import android.os.Build;
import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;

/** A logcat utils */
public class ILogger {
    private static final String APP = "ILib";

    public static final boolean DEBUG = "userdebug".equals(Build.TYPE);

    public static final int DEFULT_LEVEL = DEBUG ? Log.VERBOSE : Log.DEBUG;

    public static ILogger logger(Object owner) {
        return logger(owner.getClass());
    }

    public static ILogger logger(Class<?> owner) {
        return logger(owner.getSimpleName());
    }

    public static ILogger logger(String owner) {
        return logger(owner, DEFULT_LEVEL);
    }

    public static ILogger logger(Object owner, int level) {
        return logger(owner.getClass(), level);
    }

    public static ILogger logger(Class<?> owner, int level) {
        return logger(owner.getSimpleName(), level);
    }

    public static ILogger logger(String owner, int level) {
        ILogger LOG = new ILogger(owner, level);
        return LOG;
    }

    private String mTag;
    private int mLogLevel = DEFULT_LEVEL;

    public ILogger(String owner) {
        this(owner, DEFULT_LEVEL);
    }

    public ILogger(String owner, int level) {
        mLogLevel = level;
        if (owner == null) {
            mTag = APP;
        } else {
            mTag = String.format("%s[%s]", APP, owner);
        }
    }

    public int getLogLevel() {
        return mLogLevel;
    }

    public void setLogLevel(int level) {
        mLogLevel = level;
    }

    public boolean canLog(int level) {
        return mLogLevel <= level;
    }

    public void v(String msg) {
        if (canLog(Log.VERBOSE)) {
            Log.d(mTag, msg);
        }
    }

    public void v(String format, Object...args) {
        if (canLog(Log.VERBOSE)) {
            v(String.format(format, args));
        }
    }

    public void d(String msg) {
        if (canLog(Log.DEBUG) || DEBUG) {
            Log.d(mTag, msg);
        }
    }

    public void d(String format, Object...args) {
        if (canLog(Log.DEBUG) || DEBUG) {
            d(String.format(format, args));
        }
    }

    public void w(String msg) {
        if (canLog(Log.WARN) || DEBUG) {
            Log.w(mTag, msg);
        }
    }

    public void w(String format, Object...args) {
        if (canLog(Log.WARN) || DEBUG) {
            w(String.format(format, args));
        }
    }

    public void w(Throwable tr) {
        if (canLog(Log.WARN) || DEBUG) {
            Log.w(mTag, tr);
        }
    }

    public void w(Throwable tr, String msg) {
        if (canLog(Log.WARN) || DEBUG) {
            Log.w(mTag, msg, tr);
        }
    }

    public void w(Throwable tr, String format, Object...args) {
        if (canLog(Log.WARN) || DEBUG) {
            w(String.format(format, args));
        }
    }

    public void i(String msg) {
        if (canLog(Log.INFO) || DEBUG) {
            Log.i(mTag, msg);
        }
    }

    public void i(String format, Object...args) {
        if (canLog(Log.INFO) || DEBUG) {
            i(String.format(format, args));
        }
    }

    public void i(Throwable tr, String msg) {
        if (canLog(Log.INFO) || DEBUG) {
            Log.i(mTag, msg, tr);
        }
    }

    public void i(Throwable tr, String format, Object...args) {
        if (canLog(Log.INFO) || DEBUG) {
            i(tr, String.format(format, args));
        }
    }

    public void e(String msg) {
        if (canLog(Log.ERROR) || DEBUG) {
            Log.e(mTag, msg);
        }
    }

    public void e(String format, Object...args) {
        if (canLog(Log.ERROR) || DEBUG) {
            e(String.format(format, args));
        }
    }

    public void e(Throwable tr, String msg) {
        if (canLog(Log.ERROR) || DEBUG) {
            Log.e(mTag, msg, tr);
        }
    }

    public void e(Throwable tr, String format, Object...args) {
        if (canLog(Log.ERROR) || DEBUG) {
            e(tr, String.format(format, args));
        }
    }

    /** dump caller trace */
    public static String dumpTrace(String msg) {
        Throwable throwable = new Throwable(msg);
        StringWriter sw = new StringWriter();
        throwable.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
}
