package com.davidluoye.support.box;

/* package */interface IBox {

    String BINARY = "0b";
    String OCTAL = "0";
    String HEX = "0x";

    int RADIX_BINARY = 2;
    int RADIX_OCTAL = 8;
    int RADIX_DECIMAL = 10;
    int RADIX_HEX = 16;

    static boolean hasHeader(String value) {
        if (value == null || value.length() == 0) return false;
        if (value.length() == 1) return false;
        final char firstChar = value.charAt(0);
        if (firstChar != '0') return false;
        return true;
    }

    static String getHeader(String value) {
        if (value == null || value.length() == 0) return null;
        if (value.length() == 1) return null;
        final char firstChar = value.charAt(0);
        if (firstChar != '0') return null;

        final char secondChar = value.charAt(1);
        if (secondChar == 'b' || secondChar == 'B') {
            return BINARY;
        }

        if (secondChar == 'x' || secondChar == 'X') {
            return HEX;
        }

        if (secondChar >= '1' && secondChar <= '7') {
            return OCTAL;
        }
        return null;
    }

    static int getRadix(String value) {
        String head = getHeader(value);
        if (head == BINARY) return 2;
        if (head == OCTAL) return 8;
        if (head == HEX) return 16;
        return 10;
    }
}
