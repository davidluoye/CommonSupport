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

public class StringUtil {
    private static final String DEFAULT_SEPARATOR = ",";

    public static String join(int... param) {
        String[] params = new String[param == null ? 0 : param.length];
        for (int index = 0; index < param.length; index++) {
            params[index] = String.valueOf(param[index]);
        }
        return join(DEFAULT_SEPARATOR, params);
    }

    public static String join(long... param) {
        String[] params = new String[param == null ? 0 : param.length];
        for (int index = 0; index < param.length; index++) {
            params[index] = String.valueOf(param[index]);
        }
        return join(DEFAULT_SEPARATOR, params);
    }

    public static String join(float... param) {
        String[] params = new String[param == null ? 0 : param.length];
        for (int index = 0; index < param.length; index++) {
            params[index] = String.valueOf(param[index]);
        }
        return join(DEFAULT_SEPARATOR, params);
    }

    public static String join(double... param) {
        String[] params = new String[param == null ? 0 : param.length];
        for (int index = 0; index < param.length; index++) {
            params[index] = String.valueOf(param[index]);
        }
        return join(DEFAULT_SEPARATOR, params);
    }

    public static String join(boolean... param) {
        String[] params = new String[param == null ? 0 : param.length];
        for (int index = 0; index < param.length; index++) {
            params[index] = String.valueOf(param[index]);
        }
        return join(DEFAULT_SEPARATOR, params);
    }

    public static String join(byte... param) {
        String[] params = new String[param == null ? 0 : param.length];
        for (int index = 0; index < param.length; index++) {
            params[index] = String.valueOf(param[index]);
        }
        return join(DEFAULT_SEPARATOR, params);
    }

    public static String join(char... param) {
        String[] params = new String[param == null ? 0 : param.length];
        for (int index = 0; index < param.length; index++) {
            params[index] = String.valueOf(param[index]);
        }
        return join(DEFAULT_SEPARATOR, params);
    }

    public static String join(short... param) {
        String[] params = new String[param == null ? 0 : param.length];
        for (int index = 0; index < param.length; index++) {
            params[index] = String.valueOf(param[index]);
        }
        return join(DEFAULT_SEPARATOR, params);
    }

    public static String join(String...param) {
        return join(DEFAULT_SEPARATOR, param);
    }

    public static String join(Object...param) {
        return join(DEFAULT_SEPARATOR, param);
    }

    public static String join(String separator, Object[] array) {
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



    /**
     * <p>Checks if a String is empty ("") or null.</p>
     *
     *  @param value  the String to check, may be null
     * @return <code>true</code> if the String is empty or null
     */
    public static boolean isEmpty(String value) {
        return value == null || value.length() == 0;
    }

    /**
     * <p>Checks if a String is not empty ("") and not null.</p>
     *
     * @param value  the String to check, may be null
     * @return <code>true</code> if the String is not empty and not null
     */
    public static boolean isNotEmpty(String value) {
        return !isEmpty(value);
    }

    /**
     * <p>Checks if a String is whitespace, empty ("") or null.</p>
     *
     * @param value  the String to check, may be null
     * @return <code>true</code> if the String is null, empty or whitespace
     */
    public static boolean isBlank(String value) {
        if (isEmpty(value)) return true;
        for (int i = 0; i < value.length(); i++) {
            if ((!Character.isWhitespace(value.charAt(i)))) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>Checks if a String is not empty (""), not null and not whitespace only.</p>
     *
     * @param value  the String to check, may be null
     * @return <code>true</code> if the String is not empty and not null and not whitespace
     */
    public static boolean isNotBlank(String value) {
        return !isBlank(value);
    }

    public static boolean equals(CharSequence pre, CharSequence next) {
        if (pre == next) return true;
        if (pre == null || next == null) return false;
        if (pre.length() != next.length()) return false;

        int length = pre.length();
        for (int i = 0; i < length; i++) {
            if (pre.charAt(i) != next.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    public static boolean equalsIgnoreCase(CharSequence pre, CharSequence next) {
        if (pre == next) return true;
        if (pre == null || next == null) return false;
        if (pre.length() != next.length()) return false;

        int length = pre.length();
        for (int i = 0; i < length; i++) {
            char one = pre.charAt(i);
            char two = next.charAt(i);
            if (toLowerCase(one) != toLowerCase(two)) {
                return false;
            }
        }
        return true;
    }

    public static char toLowerCase(char a) {
        if (a >= 'A' && a <= 'Z') return (char)(a + ('a' - 'A'));
        return a;
    }

    public static char toUpperCase(char a) {
        if (a >= 'a' && a <= 'b') return (char)(a - ('a' - 'A'));
        return a;
    }
}
