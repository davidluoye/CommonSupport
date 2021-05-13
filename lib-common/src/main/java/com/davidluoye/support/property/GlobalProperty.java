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
package com.davidluoye.support.property;

import android.content.Context;

import com.davidluoye.support.app.AppGlobals;
import com.davidluoye.support.box.Floats;
import com.davidluoye.support.box.Ints;
import com.davidluoye.support.box.Longs;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GlobalProperty {

    public static boolean set(String key, String value) {
        ReadWriteOptions options = getOptions();
        return options.put(key, value);
    }

    public static boolean set(String key, String...values) {
        ReadWriteOptions options = getOptions();
        return options.put(key, String.join(separator, values));
    }

    public static boolean set(String key, int value) {
        ReadWriteOptions options = getOptions();
        return options.put(key, String.valueOf(value));
    }

    public static boolean set(String key, char value) {
        ReadWriteOptions options = getOptions();
        return options.put(key, String.valueOf(value));
    }

    public static boolean set(String key, float value) {
        ReadWriteOptions options = getOptions();
        return options.put(key, String.valueOf(value));
    }

    public static boolean set(String key, long value) {
        ReadWriteOptions options = getOptions();
        return options.put(key, String.valueOf(value));
    }

    public static boolean set(String key, boolean value) {
        ReadWriteOptions options = getOptions();
        return options.put(key, String.valueOf(value));
    }

    public static String getString(String key) {
        ReadWriteOptions options = getOptions();
        String value = options.get(key);
        return value;
    }

    public static String[] getStringArray(String key) {
        ReadWriteOptions options = getOptions();
        String value = options.get(key);
        if (value != null) {
            return value.split(separator);
        }
        return null;
    }

    public static int getInt(String key, int defValue) {
        ReadWriteOptions options = getOptions();
        String value = options.get(key);
        return Ints.parse(value, defValue);
    }

    public static char getChar(String key, char defValue) {
        ReadWriteOptions options = getOptions();
        String value = options.get(key);
        return (value != null && value.length() == 1) ? value.charAt(0) : defValue;
    }

    public static float getFloat(String key, float defValue) {
        ReadWriteOptions options = getOptions();
        String value = options.get(key);
        return Floats.parse(value, defValue);
    }

    public static long getLong(String key, long defValue) {
        ReadWriteOptions options = getOptions();
        String value = options.get(key);
        return Longs.parse(value, defValue);
    }

    public static boolean getBoolean(String key, boolean defValue) {
        ReadWriteOptions options = getOptions();
        String value = options.get(key);
        return value == null ? defValue : Boolean.parseBoolean(value);
    }

    private static final String separator = ",";
    private static final String name = "properties.ini";
    private static ReadWriteOptions options = null;

    private static ReadWriteOptions getOptions() {
        if (options == null) {
            Context app = AppGlobals.getApplication();
            File filePath = app.getFilesDir();
            Path path = Paths.get(filePath.getAbsolutePath(), name);
            options = new ReadWriteOptions(path);
        }
        return options;
    }
}
