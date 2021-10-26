package com.davidluoye.support.box;

public class Strings {

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

    public static <T> boolean isEmpty(T[] values) {
        return  values == null || values.length == 0;
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

    public static boolean contains(CharSequence raw, CharSequence... items) {
        if (items == null) return raw == null;
        for (CharSequence cs : items) {
            if (equals(raw, cs)) return true;
        }
        return false;
    }

    public static boolean containsIgnoreCase(CharSequence raw, CharSequence... items) {
        if (items == null) return raw == null;
        for (CharSequence cs : items) {
            if (equalsIgnoreCase(raw, cs)) return true;
        }
        return false;
    }
}
