/*
 * Copyright 2021 The authors David Yang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.davidluoye.support.log;

import android.util.Log;

/** A logcat utils */
public class ILogger {

    public static final int INVALID = -1;

    public static ILogger logger(Class<?> owner) {
        return logger(owner.getSimpleName(), INVALID);
    }

    public static ILogger logger(Class<?> owner, int level) {
        return logger(owner.getSimpleName(), level);
    }

    public static ILogger logger(String owner) {
        return logger(owner, INVALID);
    }

    public static ILogger logger(String owner, int level) {
        ILogger LOG = new ILogger(owner, level);
        return LOG;
    }

    private final String owner;
    private int level;

    private ILogger(String owner, int level) {
        this.owner = owner;
        this.level = level;
    }

    public ILogger level(int level) {
        this.level = level;
        return this;
    }

    public int level() {
        return level;
    }

    public String tag() {
        return formatTag(appTag(), owner);
    }

    public String owner() {
        return this.owner;
    }

    public boolean canLog(int level) {
        if (this.level <= 0) {
            return Configuration.canLog(level);
        } else if (this.level <= level) {
            return true;
        } else if (owner != null) {
            return Log.isLoggable(owner, level);
        }
        return false;
    }

    public void v(String msg) {
        if (canLog(Log.VERBOSE)) {
            writeLog(owner, Log.VERBOSE, true, null, msg);
        }
    }

    public void v(String format, Object...args) {
        if (canLog(Log.VERBOSE)) {
            writeLog(owner, Log.VERBOSE, true, null, format, args);
        }
    }

    public void d(String msg) {
        if (canLog(Log.DEBUG)) {
            writeLog(owner, Log.DEBUG, true, null, msg);
        }
    }

    public void d(String format, Object...args) {
        if (canLog(Log.DEBUG)) {
            writeLog(owner, Log.DEBUG, true, null, format, args);
        }
    }

    public void w(String msg) {
        if (canLog(Log.WARN)) {
            writeLog(owner, Log.WARN, true, null, msg);
        }
    }

    public void w(String format, Object...args) {
        if (canLog(Log.WARN)) {
            writeLog(owner, Log.INFO, true, null, format, args);
        }
    }

    public void w(Throwable tr) {
        if (canLog(Log.WARN)) {
            writeLog(owner, Log.INFO, true, tr, null);
        }
    }

    public void w(Throwable tr, String msg) {
        if (canLog(Log.WARN)) {
            writeLog(owner, Log.WARN, true, tr, msg);
        }
    }

    public void w(Throwable tr, String format, Object...args) {
        if (canLog(Log.WARN)) {
            writeLog(owner, Log.WARN, true, tr, format, args);
        }
    }

    public void i(String msg) {
        if (canLog(Log.INFO)) {
            writeLog(owner, Log.INFO, true, null, msg);
        }
    }

    public void i(String format, Object...args) {
        if (canLog(Log.INFO)) {
            writeLog(owner, Log.INFO, true, null, format, args);
        }
    }

    public void i(Throwable tr, String msg) {
        if (canLog(Log.INFO)) {
            writeLog(owner, Log.INFO, true, tr, msg);
        }
    }

    public void i(Throwable tr, String format, Object...args) {
        if (canLog(Log.INFO)) {
            writeLog(owner, Log.INFO, true, tr, format, args);
        }
    }

    public void e(String msg) {
        if (canLog(Log.ERROR)) {
            writeLog(owner, Log.ERROR, true, null, msg);
        }
    }

    public void e(String format, Object...args) {
        if (canLog(Log.ERROR)) {
            writeLog(owner, Log.ERROR, true, null, format, args);
        }
    }

    public void e(Throwable tr, String msg) {
        if (canLog(Log.ERROR)) {
            writeLog(owner, Log.ERROR, true, tr, msg);
        }
    }

    public void e(Throwable tr, String format, Object...args) {
        if (canLog(Log.ERROR)) {
            writeLog(owner, Log.ERROR, true, tr, format, args);
        }
    }


    public static void vv(String tag, String msg) {
        writeLog(tag, Log.VERBOSE, false, null, msg);
    }

    public static void vv(String tag, String format, Object...args) {
        writeLog(tag, Log.VERBOSE, false, null, format, args);
    }

    public static void dd(String tag, String msg) {
        writeLog(tag, Log.DEBUG, false, null, msg);
    }

    public static void dd(String tag, String format, Object...args) {
        writeLog(tag, Log.DEBUG, false, null, format, args);
    }

    public static void ii(String tag, String msg) {
        writeLog(tag, Log.INFO, false, null, msg);
    }

    public static void ii(String tag, String format, Object...args) {
        writeLog(tag, Log.INFO, false, null, format, args);
    }

    public static void ww(String tag, String msg) {
        writeLog(tag, Log.WARN, false, null, msg);
    }

    public static void ww(String tag, String format, Object...args) {
        writeLog(tag, Log.WARN, false, null, format, args);
    }

    public static void ee(String tag, String msg) {
        writeLog(tag, Log.ERROR, false, null, msg);
    }

    public static void ee(String tag, String format, Object...args) {
        writeLog(tag, Log.ERROR, false, null, format, args);
    }

    public static String appTag() {
        Configuration configuration = Configuration.get();
        String appTag = null;
        if (configuration == null || configuration.appTag() == null) {
            appTag = Configuration.APP_TAG;
        } else {
            appTag = configuration.appTag();
        }
        return appTag;
    }

    private static String formatTag(String appTag, String owner) {
        if (owner != null) {
            return String.format("%s: [%s]", appTag, owner);
        }
        return appTag;
    }

    // ========================== internal function ===========================

    private static void writeLog(String tag, int level, boolean allowed, Throwable tr, String format, Object...args) {
        if (allowed || Configuration.canLog(level)) {
            writeLog(tag, level, allowed, tr, String.format(format, args));
        }
    }

    private static void writeLog(String tag, int level, boolean allowed, Throwable tr, String msg) {
        if (allowed || Configuration.canLog(level)) {
            if (tr != null && msg != null) {
                writeLogImpl(tag, level, msg + "\n" + LogUtils.dumpTrace(tr));
            } else if (msg != null) {
                writeLogImpl(tag, level, msg);
            } else if (tr != null) {
                writeLogImpl(tag, level, LogUtils.dumpTrace(tr));
            }
        }
    }

    private static void writeLogImpl(String owner, int targetLevel, String msg) {
        String tag = formatTag(appTag(), owner);
        Log.println(targetLevel, tag, msg);
    }
}
