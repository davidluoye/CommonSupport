package com.davidluoye.support.util;

public class NumberUtil {

    /**
     * Parse a string value which is a long or hex string.
     * @param value
     * @param defValue
     * @return
     */
    public static long parseLong(String value, long defValue) {
        return parseLong(value, defValue, 10);
    }

    /**
     * Parse a string value which is a long or hex string.
     * @param value
     * @param defValue
     * @param radix
     * @return
     */
    public static long parseLong(String value, long defValue, int radix) {
        try {
            if (value == null || value.length() == 0) return defValue;
            if (value.startsWith("0x")) {
                if (value.length() > 2) {
                    String newValue = value.substring(2);
                    return Long.parseLong(newValue, 16);
                }
            }
            return Long.parseLong(value, radix);
        } catch (NumberFormatException e) {
        }
        return defValue;
    }

    /**
     * Parse a string value which is a integer or hex string.
     * @param value
     * @param defValue
     * @return
     */
    public static int parseInt(String value, int defValue) {
        return parseInt(value, defValue, 10);
    }

    /**
     * Parse a string value which is a integer or hex string.
     * @param value
     * @param defValue
     * @param radix
     * @return
     */
    public static int parseInt(String value, int defValue, int radix) {
        try {
            if (value == null || value.length() == 0) return defValue;
            if (value.startsWith("0x")) {
                if (value.length() > 2) {
                    String newValue = value.substring(2);
                    return Integer.parseInt(newValue, 16);
                }
            }
            return Integer.parseInt(value, radix);
        } catch (NumberFormatException e) {
        }
        return defValue;
    }

    public static final String translateIntToHexString(int value) {
        return translateLongToHexString(value);
    }

    /** translate a integer value to hex string. */
    public static final String translateLongToHexString(long value) {
        String hexString = Long.toHexString(value);
        hexString = hexString.toUpperCase();
        if (hexString.length() == 1) {
            return String.format("0%s", hexString);
        }
        return hexString;
    }

    /** translate a byte to integer */
    public static final int parseInt(byte b) {
        return b & 0xFF;
    }

    /** translate a byte array to int array */
    public static final int[] parseInt(byte[] bytes) {
        if (bytes == null) {
            throw new IllegalArgumentException("empty parameter " + bytes);
        }
        int[] value = new int[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            value[i] = parseInt(bytes[i]);
        }
        return value;
    }
}
