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
package com.davidluoye.support.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

public class StaticSharedSettings {

    public static synchronized StaticSharedSettings init(Context context) {
        return init(context, "setting");
    }

    public static synchronized StaticSharedSettings init(Context context, String name) {
        return init(context.getSharedPreferences(name, Context.MODE_PRIVATE));
    }

    public static synchronized StaticSharedSettings init(SharedPreferences sp) {
        if (sInstance != null) {
            sInstance = new StaticSharedSettings(sp);
        }
        return sInstance;
    }

    private static StaticSharedSettings sInstance;
    private SharedSettings mSetting;
    private StaticSharedSettings(SharedPreferences sp) {
        this.mSetting = new SharedSettings(sp);
    }

    private static void checkState() {
        if (sInstance == null) {
            throw new IllegalStateException("SharedStaticSettings should be initial first");
        }
    }

    /** Set a integer value in the setting preferences */
    public static boolean setInt(String key, int value) {
        checkState();
        return sInstance.mSetting.setInt(key, value);
    }

    /** Set a float value in the setting preferences */
    public static boolean setFloat(String key, float value) {
        checkState();
        return sInstance.mSetting.setFloat(key, value);
    }

    /** Set a long value in the setting preferences */
    public static boolean setLong(String key, long value) {
        checkState();
        return sInstance.mSetting.setLong(key, value);
    }

    /** Set a boolean value in the setting preferences */
    public static boolean setBoolean(String key, boolean value) {
        checkState();
        return sInstance.mSetting.setBoolean(key, value);
    }

    /** Set a string value in the setting preferences */
    public static boolean setString(String key, String value) {
        checkState();
        return sInstance.mSetting.setString(key, value);
    }

    /** Set a string list value in the setting preferences */
    public static boolean setStringSet(String key, Set<String> value) {
        checkState();
        return sInstance.mSetting.setStringSet(key, value);
    }

    /** Retrieve a integer value from the preferences. */
    public static int getInt(String key, int defValue) {
        checkState();
        return sInstance.mSetting.getInt(key, defValue);
    }

    /** Retrieve a float value from the preferences. */
    public static float getFloat(String key, float defValue) {
        checkState();
        return sInstance.mSetting.getFloat(key, defValue);
    }

    /** Retrieve a long value from the preferences. */
    public static long getLong(String key, long defValue) {
        checkState();
        return sInstance.mSetting.getLong(key, defValue);
    }

    /** Retrieve a boolean value from the preferences. */
    public static boolean getBoolean(String key, boolean defValue) {
        checkState();
        return sInstance.mSetting.getBoolean(key, defValue);
    }

    /** Retrieve a string value from the preferences. */
    public static String getString(String key, String defValue) {
        checkState();
        return sInstance.mSetting.getString(key, defValue);
    }

    /** Retrieve a string list value from the preferences. */
    public static Set<String> getStringSet(String key) {
        checkState();
        return sInstance.mSetting.getStringSet(key);
    }
}