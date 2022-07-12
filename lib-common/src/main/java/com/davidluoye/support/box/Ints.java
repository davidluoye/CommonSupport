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

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Ints {

    public static int parse(String value, int defValue) {
        if (value == null || value.length() == 0) return defValue;
        if (IBox.isBinaryNumber(value)) {
            return parse(value.substring(2), 2, defValue);
        } else if (IBox.isOctalNumber(value)) {
            return parse(value.substring(1), 8, defValue);
        } else if (IBox.isDecimalNumber(value)) {
            return parse(value.substring(0), 10, defValue);
        } else if (IBox.isHexNumber(value)) {
            return parse(value.substring(2), 16, defValue);
        }

        boolean hex = false;
        for (int index = 0; index < value.length(); index++) {
            char ch = value.charAt(index);
            if (ch >= 'a' && ch <= 'f' || ch >= 'A' && ch <= 'F') {
                hex = true;
                continue;
            }

            if (ch >= '0' && ch <= '9') {
                continue;
            }
            return defValue;
        }
        return parse(value, hex ? 16 : 10, defValue);
    }

    public static int parse(String value, int radix, int defValue) {
        try {
            return Integer.parseInt(value, radix);
        } catch (NumberFormatException e) {
            e.fillInStackTrace();
        }
        return defValue;
    }

    public static int fromBytes(byte b1, byte b2, byte b3, byte b4) {
        return b1 << 24 | (b2 & 0xFF) << 16 | (b3 & 0xFF) << 8 | (b4 & 0xFF);
    }

    public static byte[] toBytes(int value, boolean bigEndian) {
        byte[] bytes = new byte[4];
        if (bigEndian) {
            bytes[0] = (byte)(value >> 24 & 0xFF);
            bytes[1] = (byte)(value >> 16 & 0xFF);
            bytes[2] = (byte)(value >> 8 & 0xFF);
            bytes[3] = (byte)(value & 0xFF);
        } else {
            bytes[3] = (byte)(value >> 24 & 0xFF);
            bytes[2] = (byte)(value >> 16 & 0xFF);
            bytes[1] = (byte)(value >> 8 & 0xFF);
            bytes[0] = (byte)(value & 0xFF);
        }
        return bytes;
    }

    public static Integer[] box(int[] array) {
        return IntStream.of(array).boxed().toArray(Integer[]::new);
    }

    public static int[] unBox(Integer[] array) {
        return Stream.of(array).mapToInt(it -> it != null ? it : 0).toArray();
    }

    public static String toHexString(int value) {
        return IBox.HEX + Integer.toHexString(value);
    }
}
