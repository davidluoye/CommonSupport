package com.davidluoye.support.box;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Ints {

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

    public static int fromBytes(byte b1, byte b2, byte b3, byte b4) {
        return b1 << 24 | (b2 & 0xFF) << 16 | (b3 & 0xFF) << 8 | (b4 & 0xFF);
    }

    public static byte[] toBytes(int value, boolean bigEndian) {
        byte[] bytes = new byte[4];
        if (bigEndian) {
            bytes[0] = (byte)(value >> 24 & 0xFF);
            bytes[1] = (byte)(value >> 16 & 0xFF);
            bytes[2] = (byte)(value >> 8 & 0xFF);
            bytes[3] = (byte)(value & 0xFF);
        } else {
            bytes[3] = (byte)(value >> 24 & 0xFF);
            bytes[2] = (byte)(value >> 16 & 0xFF);
            bytes[1] = (byte)(value >> 8 & 0xFF);
            bytes[0] = (byte)(value & 0xFF);
        }
        return bytes;
    }

    public static Integer[] box(int[] array) {
        return IntStream.of(array).boxed().toArray(Integer[]::new);
    }

    public static int[] unBox(Integer[] array) {
        return Stream.of(array).mapToInt(it -> it != null ? it : 0).toArray();
    }

    public static String toHexString(int value) {
        return IBox.HEX + Integer.toHexString(value);
    }
}
