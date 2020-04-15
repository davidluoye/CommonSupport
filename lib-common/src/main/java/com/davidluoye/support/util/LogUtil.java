
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

    public static String translateLevel(int level) {
        switch (level) {
            case Log.VERBOSE:
                return "V";
            case Log.DEBUG:
                return "D";
            case Log.INFO:
                return "I";
            case Log.WARN:
                return "W";
            case Log.ERROR:
                return "E";
            case Log.ASSERT:
                return "A";
        }
        return "N";
    }
}
