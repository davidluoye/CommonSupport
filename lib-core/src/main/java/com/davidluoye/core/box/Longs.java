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

package com.davidluoye.core.box;

import java.util.stream.LongStream;
import java.util.stream.Stream;

public class Longs {

    public static long parse(String value, long defValue) {
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

    public static long parse(String value, int radix, long defValue) {
        try {
            return Long.parseLong(value, radix);
        } catch (NumberFormatException e) {
            e.fillInStackTrace();
        }
        return defValue;
    }

    public static Long[] boxed(long[] array) {
        return LongStream.of(array).boxed().toArray(Long[]::new);
    }

    public static long[] unBoxed(Long[] array) {
        return Stream.of(array).mapToLong(it -> it != null ? it : 0).toArray();
    }

    public static String toHexString(long value) {
        return IBox.HEX + Long.toHexString(value);
    }
}
