package com.davidluoye.support.box;

import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class LongBox {

    public static long parse(String value, long defValue) {
        if (value == null || value.length() == 0) return defValue;
        final int radix = IBox.getRadix(value);

        String newValue;
        if (radix == IBox.RADIX_OCTAL) {
            newValue = value.substring(1);
        } else {
            newValue = value.substring(2);
        }
        return parse(newValue, radix, defValue);
    }

    public static long parse(String value, int radix, long defValue) {
        try {
            return Long.parseLong(value, radix);
        } catch (NumberFormatException e) {
            e.fillInStackTrace();
        }
        return defValue;
    }

    public static Long[] boxed(long[] array) {
        return LongStream.of(array).boxed().toArray(Long[]::new);
    }

    public static long[] unBoxed(Long[] array) {
        return Stream.of(array).mapToLong(it -> it != null ? it : 0).toArray();
    }

    public static String toHexString(int value) {
        return "0x" + Long.toHexString(value);
    }
}
