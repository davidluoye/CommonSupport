/*
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
 */

package com.davidluoye.support.box;

import java.util.stream.Stream;

public class Splitter {
    private static final String DEFAULT_SEPARATOR = ",";

    public static String join(int... param) {
        return join(DEFAULT_SEPARATOR, Stream.of(param).map(String::valueOf).toArray(String[]::new));
    }

    public static String join(long... param) {
        return join(DEFAULT_SEPARATOR, Stream.of(param).map(String::valueOf).toArray(String[]::new));
    }

    public static String join(float... param) {
        return join(DEFAULT_SEPARATOR, Stream.of(param).map(String::valueOf).toArray(String[]::new));
    }

    public static String join(double... param) {
        return join(DEFAULT_SEPARATOR, Stream.of(param).map(String::valueOf).toArray(String[]::new));
    }

    public static String join(boolean... param) {
        return join(DEFAULT_SEPARATOR, Stream.of(param).map(String::valueOf).toArray(String[]::new));
    }

    public static String join(byte... param) {
        return join(DEFAULT_SEPARATOR, Stream.of(param).map(String::valueOf).toArray(String[]::new));
    }

    public static String join(char... param) {
        return join(DEFAULT_SEPARATOR, Stream.of(param).map(String::valueOf).toArray(String[]::new));
    }

    public static String join(short... param) {
        return join(DEFAULT_SEPARATOR, Stream.of(param).map(String::valueOf).toArray(String[]::new));
    }

    public static String join(String...param) {
        return join(DEFAULT_SEPARATOR, param);
    }

    public static String join(Object...param) {
        return join(DEFAULT_SEPARATOR, Stream.of(param).map(String::valueOf).toArray(String[]::new));
    }

    public static String join(String separator, String[] array) {
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

    public static String join(String[] keys, Object[] values) {
        return join(DEFAULT_SEPARATOR, keys, values);
    }

    public static String join(String separator, String[] keys, Object[] values) {
        if (keys == null && values == null) return "";
        if (keys != null && values != null) {
            if (keys.length == 0 && values.length == 0) return "";
            if (keys.length != values.length) throw new IllegalArgumentException("error, invalid arguments.");
            StringBuilder sb = new StringBuilder();
            for (int index = 0; index < keys.length; index++) {
                if (index > 0) sb.append(separator).append(" ");
                sb.append(keys[index]).append(" ").append(values[index]);
            }
            return sb.toString();
        }
        throw new IllegalArgumentException("error, invalid null arguments.");
    }
}
