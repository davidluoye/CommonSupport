/******************************************************************************
 * Copyright 2021 The authors David Yang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ********************************************************************************/
package com.davidluoye.support.util;

public class NumberUtil {

    /**
     * Parse a string value which is a float or hex string.
     * @param value float string value
     * @param defValue default value if has a number format exception
     * @return float value
     */
    public static float parseFloat(String value, float defValue) {
        try {
            if (value == null || value.length() == 0) return defValue;
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
        }
        return defValue;
    }

    /**
     * Parse a string value which is a long or hex string.
     * @param value long string value
     * @param defValue default value if has a number format exception
     * @return long value
     */
    public static long parseLong(String value, long defValue) {
        try {
            if (value == null || value.length() == 0) return defValue;
            if (value.startsWith("0x")) {
                if (value.length() > 2) {
                    String newValue = value.substring(2);
                    return Long.parseLong(newValue, 16);
                }
            }
            return Long.parseLong(value, 10);
        } catch (NumberFormatException e) {
        }
        return defValue;
    }

    /**
     * Parse a string value which is a integer or hex string.
     * @param value int string value
     * @param defValue default value if has a number format exception
     * @return int value
     */
    public static int parseInt(String value, int defValue) {
        try {
            if (value == null || value.length() == 0) return defValue;
            if (value.startsWith("0x")) {
                if (value.length() > 2) {
                    String newValue = value.substring(2);
                    return Integer.parseInt(newValue, 16);
                }
            }
            return Integer.parseInt(value, 10);
        } catch (NumberFormatException e) {
        }
        return defValue;
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

    public static String toHexString(long value) {
        String hexString = Long.toHexString(value);
        return formatHexString(hexString);
    }

    public static String toHexString(int value) {
        String hexString = Integer.toHexString(value);
        return formatHexString(hexString);
    }

    private static String formatHexString(String hex) {
        hex = hex.toUpperCase();
        if (hex.length() == 1) {
            return String.format("0%s", hex);
        }
        return hex;
    }
}
