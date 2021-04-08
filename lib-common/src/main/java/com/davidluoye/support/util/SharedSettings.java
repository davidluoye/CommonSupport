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

import java.util.HashSet;
import java.util.Set;

public class SharedSettings {

    private final RWLock mLock = new RWLock();
    private final SharedPreferences mSharedPreference;

    public SharedSettings(Context context) {
        this(context, "setting");
    }

    public SharedSettings(Context context, String name) {
        this(context.getSharedPreferences(name, Context.MODE_PRIVATE));
    }

    public SharedSettings(SharedPreferences sp) {
        this.mSharedPreference = sp;
    }

    public void registerChangedEvent(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        this.mSharedPreference.registerOnSharedPreferenceChangeListener(listener);
    }

    public void unRegisterChangedEvent(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        this.mSharedPreference.unregisterOnSharedPreferenceChangeListener(listener);
    }

    public boolean containKey(String key) {
        return mLock.readLock(() -> mSharedPreference.contains(key));
    }

    public int getInt(String key, int defValue) {
        return mLock.readLock(() -> mSharedPreference.getInt(key, defValue));
    }

    public float getFloat(String key, float defValue) {
        return mLock.readLock(() -> mSharedPreference.getFloat(key, defValue));
    }

    public long getLong(String key, long defValue) {
        return mLock.readLock(() -> mSharedPreference.getLong(key, defValue));
    }

    public boolean getBoolean(String key, boolean defValue) {
        return mLock.readLock(() -> mSharedPreference.getBoolean(key, defValue));
    }

    public String getString(String key, String defValue) {
        return mLock.readLock(() -> mSharedPreference.getString(key, defValue));
    }

    public Set<String> getStringSet(String key) {
        return mLock.readLock(() -> mSharedPreference.getStringSet(key, new HashSet<>()));
    }

    public boolean setInt(String key, int value) {
        return mLock.writeLock(() -> {
            SharedPreferences.Editor editor = mSharedPreference.edit();
            editor.putInt(key, value);
            return editor.commit();
        });
    }

    public boolean setFloat(String key, float value) {
        return mLock.writeLock(() -> {
            SharedPreferences.Editor editor = mSharedPreference.edit();
            editor.putFloat(key, value);
            return editor.commit();
        });
    }

    public boolean setLong(String key, long value) {
        return mLock.writeLock(() -> {
            SharedPreferences.Editor editor = mSharedPreference.edit();
            editor.putLong(key, value);
            return editor.commit();
        });
    }

    public boolean setBoolean(String key, boolean value) {
        return mLock.writeLock(() -> {
            SharedPreferences.Editor editor = mSharedPreference.edit();
            editor.putBoolean(key, value);
            return editor.commit();
        });
    }

    public boolean setString(String key, String value) {
        return mLock.writeLock(() -> {
            SharedPreferences.Editor editor = mSharedPreference.edit();
            editor.putString(key, value);
            return editor.commit();
        });
    }

    public boolean setStringSet(String key, Set<String> value) {
        return mLock.writeLock(() -> {
            SharedPreferences.Editor editor = mSharedPreference.edit();
            editor.putStringSet(key, value);
            return editor.commit();
        });
    }
}