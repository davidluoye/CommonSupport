
package com.davidluoye.support.util;

import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;


public class LogUtil {

    public static String dumpTrace(String msg) {
        Throwable throwable = new Throwable(msg);
        StringWriter sw = new StringWriter();
        throwable.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }


    public static String getStackTraceString(Throwable tr) {
        if (tr != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            tr.printStackTrace(pw);
            pw.flush();
            return sw.toString();
        }
        return null;
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
