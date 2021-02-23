package com.davidluoye.support.box;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class IntBox {

    public static int parse(String value, int defValue) {
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

    public static int parse(String value, int radix, int defValue) {
        try {
            return Integer.parseInt(value, radix);
        } catch (NumberFormatException e) {
            e.fillInStackTrace();
        }
        return defValue;
    }

    public static Integer[] box(int[] array) {
        return IntStream.of(array).boxed().toArray(Integer[]::new);
    }

    public static int[] unBox(Integer[] array) {
        return Stream.of(array).mapToInt(it -> it != null ? it : 0).toArray();
    }

    public static String toHexString(int value) {
        return "0x" + Integer.toHexString(value);
    }
}
