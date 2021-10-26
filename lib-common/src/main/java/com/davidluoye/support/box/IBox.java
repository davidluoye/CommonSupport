package com.davidluoye.support.box;
/* package */interface IBox {
    String HEX = "0x";
    String BINARY = "0b";
    String OCTAL = "0";

    static boolean isValidNumber(String value) {
        if (value == null || value.length() == 0) return false;
        if (isHexNumber(value)) return true;
        if (isOctalNumber(value)) return true;
        if (isBinaryNumber(value)) return true;
        if (isDecimalNumber(value)) return true;
        return false;
    }

    static boolean isHexNumber(String value) {
        if (value == null || value.length() == 0) return false;
        if (value.length() <= 2) return false;
        if (!value.startsWith(HEX)) return false;
        for (int index = 2; index < value.length(); index++) {
            char ch = value.charAt(index);
            if (ch >= '0' && ch <= '9' || ch >= 'a' && ch <= 'f' || ch >= 'A' && ch <= 'F') {
                continue;
            }
            return false;
        }
        return true;
    }

    static boolean isOctalNumber(String value) {
        if (value == null || value.length() == 0) return false;
        if (value.length() <= 1) return false;
        if (!value.startsWith(OCTAL)) return false;
        for (int index = 1; index < value.length(); index++) {
            char ch = value.charAt(index);
            if (ch >= '0' && ch <= '7') {
                continue;
            }
            return false;
        }
        return true;
    }

    static boolean isBinaryNumber(String value) {
        if (value == null || value.length() == 0) return false;
        if (value.length() <= 2) return false;
        if (!value.startsWith(BINARY)) return false;
        for (int index = 2; index < value.length(); index++) {
            char ch = value.charAt(index);
            if (ch >= '0' && ch <= '1') {
                continue;
            }
            return false;
        }
        return true;
    }

    static boolean isDecimalNumber(String value) {
        if (value == null || value.length() == 0) return false;
        for (int index = 0; index < value.length(); index++) {
            char ch = value.charAt(index);
            if (index == 0 && ch == '0') return false;
            if (ch >= '0' && ch <= '9') {
                continue;
            }
            return false;
        }
        return true;
    }
}
