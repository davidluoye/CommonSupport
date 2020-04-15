package com.davidluoye.support.util;

public class StringUtil {
    private static final String DEFAULT_SEPARATOR = ",";

    public static String join(int... param) {
        String[] params = new String[param == null ? 0 : param.length];
        for (int index = 0; index < param.length; index++) {
            params[index] = String.valueOf(param[index]);
        }
        return join(DEFAULT_SEPARATOR, params);
    }

    public static String join(long... param) {
        String[] params = new String[param == null ? 0 : param.length];
        for (int index = 0; index < param.length; index++) {
            params[index] = String.valueOf(param[index]);
        }
        return join(DEFAULT_SEPARATOR, params);
    }

    public static String join(float... param) {
        String[] params = new String[param == null ? 0 : param.length];
        for (int index = 0; index < param.length; index++) {
            params[index] = String.valueOf(param[index]);
        }
        return join(DEFAULT_SEPARATOR, params);
    }

    public static String join(double... param) {
        String[] params = new String[param == null ? 0 : param.length];
        for (int index = 0; index < param.length; index++) {
            params[index] = String.valueOf(param[index]);
        }
        return join(DEFAULT_SEPARATOR, params);
    }

    public static String join(boolean... param) {
        String[] params = new String[param == null ? 0 : param.length];
        for (int index = 0; index < param.length; index++) {
            params[index] = String.valueOf(param[index]);
        }
        return join(DEFAULT_SEPARATOR, params);
    }

    public static String join(byte... param) {
        String[] params = new String[param == null ? 0 : param.length];
        for (int index = 0; index < param.length; index++) {
            params[index] = String.valueOf(param[index]);
        }
        return join(DEFAULT_SEPARATOR, params);
    }

    public static String join(char... param) {
        String[] params = new String[param == null ? 0 : param.length];
        for (int index = 0; index < param.length; index++) {
            params[index] = String.valueOf(param[index]);
        }
        return join(DEFAULT_SEPARATOR, params);
    }

    public static String join(short... param) {
        String[] params = new String[param == null ? 0 : param.length];
        for (int index = 0; index < param.length; index++) {
            params[index] = String.valueOf(param[index]);
        }
        return join(DEFAULT_SEPARATOR, params);
    }

    public static String join(String...param) {
        return join(DEFAULT_SEPARATOR, param);
    }

    public static String join(Object...param) {
        return join(DEFAULT_SEPARATOR, param);
    }

    public static String join(String separator, Object[] array) {
        StringBuilder buf = new StringBuilder(array.length);
        for (int i = 0; i < array.length; i++) {
            if (i > 0) {
                buf.append(separator);
            }
            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }
}
