package com.davidluoye.support.box;

import java.util.stream.Stream;

public class Splitter {
    private static final String DEFAULT_SEPARATOR = ",";

    public static String join(int... param) {
        return join(DEFAULT_SEPARATOR, Stream.of(param).map(String::valueOf).toArray(String[]::new));
    }

    public static String join(long... param) {
        return join(DEFAULT_SEPARATOR, Stream.of(param).map(String::valueOf).toArray(String[]::new));
    }

    public static String join(float... param) {
        return join(DEFAULT_SEPARATOR, Stream.of(param).map(String::valueOf).toArray(String[]::new));
    }

    public static String join(double... param) {
        return join(DEFAULT_SEPARATOR, Stream.of(param).map(String::valueOf).toArray(String[]::new));
    }

    public static String join(boolean... param) {
        return join(DEFAULT_SEPARATOR, Stream.of(param).map(String::valueOf).toArray(String[]::new));
    }

    public static String join(byte... param) {
        return join(DEFAULT_SEPARATOR, Stream.of(param).map(String::valueOf).toArray(String[]::new));
    }

    public static String join(char... param) {
        return join(DEFAULT_SEPARATOR, Stream.of(param).map(String::valueOf).toArray(String[]::new));
    }

    public static String join(short... param) {
        return join(DEFAULT_SEPARATOR, Stream.of(param).map(String::valueOf).toArray(String[]::new));
    }

    public static String join(String...param) {
        return join(DEFAULT_SEPARATOR, param);
    }

    public static String join(Object...param) {
        return join(DEFAULT_SEPARATOR, Stream.of(param).map(String::valueOf).toArray(String[]::new));
    }

    public static String join(String separator, String[] array) {
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
