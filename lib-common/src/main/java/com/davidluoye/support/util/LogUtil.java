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
package com.davidluoye.support.util;

import android.util.Log;

public class LogUtil {

    public static String dumpTrace(String msg) {
        return dumpTrace(msg, 0);
    }

    public static String dumpTrace(Throwable throwable) {
        return dumpTrace(throwable, 0);
    }

    public static String dumpTrace(String msg, int deep) {
        Throwable throwable = new Throwable(msg);
        return dumpTrace(throwable, deep);
    }

    public static String dumpTrace(Throwable throwable, int deep) {
        StackTraceElement[] traces = throwable.getStackTrace();
        StringBuilder sb = new StringBuilder();
        sb.append("dump trace with reason: " + throwable.getMessage());
        int size = deep > 0 ? Math.min(deep, traces.length) : traces.length;
        for (int i = 0; i < size; i++) {
            StackTraceElement element = traces[i];
            sb.append("\n");
            sb.append("  => " + element.toString());
        }
        return sb.toString();
    }

    public static String logLevelToChar(int level) {
        switch (level) {
            case Log.VERBOSE:   return "V";
            case Log.DEBUG:     return "D";
            case Log.INFO:      return "I";
            case Log.WARN:      return "W";
            case Log.ERROR:     return "E";
            case Log.ASSERT:    return "A";
        }
        return "N";
    }

    public static String logLevelToString(int logLevel) {
        switch (logLevel) {
            case Log.VERBOSE:   return "Verbose";
            case Log.DEBUG:     return "Debug";
            case Log.INFO:      return "Info";
            case Log.WARN:      return "Warn";
            case Log.ERROR:     return "Error";
            case Log.ASSERT:    return "Assert";
        }
        return null;
    }

    public static int logStringToLevel(String logString) {
        String lowerCase = logString.toLowerCase();
        switch (lowerCase) {
            case "2" : case "v": case "verbose":   return Log.VERBOSE;
            case "3" : case "d": case "debug":     return Log.DEBUG;
            case "4" : case "i": case "info":      return Log.INFO;
            case "5" : case "w": case "warn":      return Log.WARN;
            case "6" : case "e": case "error":     return Log.ERROR;
            case "7" : case "a": case "assert":    return Log.ASSERT;
        }
        return 0;
    }
}
