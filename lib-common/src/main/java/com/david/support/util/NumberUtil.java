package com.david.support.util;

import java.nio.charset.Charset;

public class NumberUtil {

    /** translate a integer value to hex string. */
    public static final String toHexString(Integer value) {
        if (value == null) {
            return null;
        }
        String hexString = Integer.toHexString(value);
        hexString = hexString.toUpperCase();
        if (hexString.length() == 1) {
            return String.format("0%s", hexString);
        }
        return hexString;
    }

    /** translate a string value which is a integer or hex string to integer. */
    public static final int parseInt(String value){
        if (value == null || value.length() == 0){
            throw new NumberFormatException("empty parameter " + value);
        }
        if (value.startsWith("0x")){
            if (value.length() > 2){
                String newValue = value.substring(2);
                return Integer.parseInt(newValue, 16);
            }
        }
        return Integer.parseInt(value);
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

    /** translate a string value with special charset to int array */
    public static final int[] parseInt(String value, Charset charset) {
        byte[] value_charset_byte = value.getBytes(charset);
        int[] value_charset_int = parseInt(value_charset_byte);
        return value_charset_int;
    }

    /** translate a string value to byte */
    public static final byte parseByte(String value){
        int v = parseInt(value);
        if (v >= 256){
            throw new NumberFormatException("overflow byte" + value);
        }
        return (byte)v;
    }
}
