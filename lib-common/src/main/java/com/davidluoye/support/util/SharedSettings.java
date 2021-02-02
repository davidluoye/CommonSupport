
package com.davidluoye.support.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

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

    public void registerChangedEvent(Consumer<String> consumer) {
        this.mSharedPreference.registerOnSharedPreferenceChangeListener((sp, key) -> {
            consumer.accept(key);
        });
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