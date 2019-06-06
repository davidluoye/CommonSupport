package com.davidluoye.support.util;

public class StringUtil {
    private static final ILogger LOGGER = ILogger.logger(StringUtil.class);
    private static final String DEFAULT_SEPARATOR = ",";

    public static <T extends Object> String join(T[] array){
        return join(array, DEFAULT_SEPARATOR);
    }

    public static <T extends Object> String join(T[] array, String separator) {
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
