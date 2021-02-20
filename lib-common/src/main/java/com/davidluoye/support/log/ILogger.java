/******************************************************************************
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
 ********************************************************************************/
package com.davidluoye.support.log;

import android.util.Log;

import com.davidluoye.support.util.LogUtil;

/** A logcat utils */
public class ILogger {

    public static final int INVALID = -1;

    public static ILogger logger(Class<?> owner) {
        return logger(owner.getSimpleName(), INVALID);
    }

    public static ILogger logger(Class<?> owner, int level) {
        return logger(owner.getSimpleName(), level);
    }

    public static ILogger logger(String owner, int level) {
        ILogger LOG = new ILogger(owner, level);
        return LOG;
    }

    private String owner;
    private int level;

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
            writeLog(owner, Log.VERBOSE, true, msg);
        }
    }

    public void v(String format, Object...args) {
        if (canLog(Log.VERBOSE)) {
            writeLog(owner, Log.VERBOSE, true, format, args);
        }
    }

    public void d(String msg) {
        if (canLog(Log.DEBUG)) {
            writeLog(owner, Log.DEBUG, true, msg);
        }
    }

    public void d(String format, Object...args) {
        if (canLog(Log.DEBUG)) {
            writeLog(owner, Log.DEBUG, true, format, args);
        }
    }

    public void w(String msg) {
        if (canLog(Log.WARN)) {
            writeLog(owner, Log.WARN, true, msg);
        }
    }

    public void w(String format, Object...args) {
        if (canLog(Log.WARN)) {
            writeLog(owner, Log.INFO, true, format, args);
        }
    }

    public void w(Throwable tr) {
        if (canLog(Log.WARN)) {
            writeLog(owner, Log.INFO, true, "", tr);
        }
    }

    public void w(Throwable tr, String msg) {
        if (canLog(Log.WARN)) {
            writeLog(owner, Log.WARN, true, msg, tr);
        }
    }

    public void w(Throwable tr, String format, Object...args) {
        if (canLog(Log.WARN)) {
            writeLog(owner, Log.WARN, true, format, args);
        }
    }

    public void i(String msg) {
        if (canLog(Log.INFO)) {
            writeLog(owner, Log.INFO, true, msg);
        }
    }

    public void i(String format, Object...args) {
        if (canLog(Log.INFO)) {
            writeLog(owner, Log.INFO, true, format, args);
        }
    }

    public void i(Throwable tr, String msg) {
        if (canLog(Log.INFO)) {
            writeLog(owner, Log.INFO, true, msg, tr);
        }
    }

    public void i(Throwable tr, String format, Object...args) {
        if (canLog(Log.INFO)) {
            writeLog(owner, Log.INFO, true, format, args, tr);
        }
    }

    public void e(String msg) {
        if (canLog(Log.ERROR)) {
            writeLog(owner, Log.ERROR, true, msg);
        }
    }

    public void e(String format, Object...args) {
        if (canLog(Log.ERROR)) {
            writeLog(owner, Log.ERROR, true, format, args);
        }
    }

    public void e(Throwable tr, String msg) {
        if (canLog(Log.ERROR)) {
            writeLog(owner, Log.ERROR, true, msg, tr);
        }
    }

    public void e(Throwable tr, String format, Object...args) {
        if (canLog(Log.ERROR)) {
            writeLog(owner, Log.ERROR, true, format, args);
        }
    }


    public static void vv(String tag, String msg) {
        writeLog(tag, Log.VERBOSE, false, msg);
    }

    public static void vv(String tag, String format, Object...args) {
        writeLog(tag, Log.VERBOSE, false, format, args);
    }

    public static void dd(String tag, String msg) {
        writeLog(tag, Log.DEBUG, false, msg);
    }

    public static void dd(String tag, String format, Object...args) {
        writeLog(tag, Log.DEBUG, false, format, args);
    }

    public static void ii(String tag, String msg) {
        writeLog(tag, Log.INFO, false, msg);
    }

    public static void ii(String tag, String format, Object...args) {
        writeLog(tag, Log.INFO, false, format, args);
    }

    public static void ww(String tag, String msg) {
        writeLog(tag, Log.WARN, false, msg);
    }

    public static void ww(String tag, String format, Object...args) {
        writeLog(tag, Log.WARN, false, format, args);
    }

    public static void ee(String tag, String msg) {
        writeLog(tag, Log.ERROR, false, msg);
    }

    public static void ee(String tag, String format, Object...args) {
        writeLog(tag, Log.ERROR, false, format, args);
    }

    // ========================== internal function ===========================

    private static void writeLog(String tag, int level, boolean allowed, String format, Object...args) {
        if (allowed || Configuration.canLog(level)) {
            writeLog(tag, level, allowed, String.format(format, args));
        }
    }

    private static void writeLog(String tag, int level, boolean allowed, String msg, Throwable tr) {
        if (allowed || Configuration.canLog(level)) {
            writeLog(tag, level, allowed, msg + "\n" + LogUtil.dumpTrace(tr));
        }
    }

    private static void writeLog(String tag, int level, boolean allowed, String msg) {
        if (allowed || Configuration.canLog(level)) {
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
