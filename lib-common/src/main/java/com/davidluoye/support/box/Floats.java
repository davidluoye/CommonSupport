package com.davidluoye.support.box;

public class Floats {

    public static float parse(String value, float defValue) {
        try {
            if (value == null || value.length() == 0) return defValue;
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
        }
        return defValue;
    }

    public static String toHexString(float value) {
        return IBox.HEX + Float.toHexString(value);
    }
}
