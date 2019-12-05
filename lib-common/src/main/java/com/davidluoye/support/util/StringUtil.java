package com.davidluoye.support.util;

public class StringUtil {
    private static final String DEFAULT_SEPARATOR = ",";

    public static <T> String join(T... param){
        return join(DEFAULT_SEPARATOR, param);
    }

    public static <T> String join(String separator, T... array) {
        if (array == null || array.length == 0) {
            return null;
        }
        int noOfItems = array.length;
        StringBuilder buf = new StringBuilder(noOfItems * 16);
        for (int i = 0; i < array.length; i++) {
            if (i > 0) {
                buf.append(separator);
            }
            if (array[i] != null) {
                buf.append(String.valueOf(array[i]));
            }
        }
        return buf.toString();
    }
}
