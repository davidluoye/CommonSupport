package com.davidluoye.support.box;

import java.util.stream.LongStream;
import java.util.stream.Stream;

public class Longs {

    public static long parse(String value, long defValue) {
        if (value == null || value.length() == 0) return defValue;
        if (IBox.isBinaryNumber(value)) {
            return parse(value.substring(2), 2, defValue);
        } else if (IBox.isOctalNumber(value)) {
            return parse(value.substring(1), 8, defValue);
        } else if (IBox.isDecimalNumber(value)) {
            return parse(value.substring(0), 10, defValue);
        } else if (IBox.isHexNumber(value)) {
            return parse(value.substring(2), 16, defValue);
        }

        boolean hex = false;
        for (int index = 0; index < value.length(); index++) {
            char ch = value.charAt(index);
            if (ch >= 'a' && ch <= 'f' || ch >= 'A' && ch <= 'F') {
                hex = true;
                continue;
            }

            if (ch >= '0' && ch <= '9') {
                continue;
            }
            return defValue;
        }
        return parse(value, hex ? 16 : 10, defValue);
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

    public static String toHexString(long value) {
        return IBox.HEX + Long.toHexString(value);
    }
}
