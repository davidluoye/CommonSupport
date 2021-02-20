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
