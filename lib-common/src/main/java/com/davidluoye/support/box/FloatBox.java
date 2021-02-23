package com.davidluoye.support.box;

public class FloatBox {

    public static float parse(String value, float defValue) {
        try {
            if (value == null || value.length() == 0) return defValue;
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
        }
        return defValue;
    }

    public static String toHexString(int value) {
        return Float.toHexString(value);
    }
}
