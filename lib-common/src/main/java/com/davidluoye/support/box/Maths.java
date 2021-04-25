package com.davidluoye.support.box;

public class Maths {

    public static int measure(int value, int min, int max) {
        return Math.min(Math.max(value, min), max);
    }
}
