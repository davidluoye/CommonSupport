
package com.davidluoye.support.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.concurrent.locks.Lock;

public class ISettings {

    private static ISettings sInstance;
    private Context mContext;
    private SharedPreferences mSharedPerference;

    private final RWLock mLock = new RWLock();

    private ISettings(Context context) {
        Preconditions.checkNotNull(context, "Context must not be null");
        mContext = context;
        mSharedPerference = context.getSharedPreferences("setting", Context.MODE_PRIVATE);
    }

    public static synchronized ISettings init(Context context) {
        if (sInstance == null) {
            sInstance = new ISettings(context);
        }
        return sInstance;
    }

    private static void checkState() {
        if (sInstance == null) {
            throw new IllegalStateException("LogSettings should be initial first");
        }
    }

    /** Set a string value in the setting preferences */
    public static boolean setString(String key, String value) {
        checkState();
        Lock lock = sInstance.mLock.lock(false);
        try {
            SharedPreferences sp = sInstance.mSharedPerference;
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(key, value);
            return editor.commit();
        } finally {
            lock.unlock();
        }
    }

    /** Set a integer value in the setting preferences */
    public static void setInt(String key, int value) {
        checkState();
        Lock lock = sInstance.mLock.lock(false);
        try {
            SharedPreferences sp = sInstance.mSharedPerference;
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt(key, value);
            editor.commit();
        } finally {
            lock.unlock();
        }
    }

    /** Set a boolean value in the setting preferences */
    public static boolean setBoolean(String key, boolean value) {
        checkState();
        Lock lock = sInstance.mLock.lock(false);
        try {
            SharedPreferences sp = sInstance.mSharedPerference;
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean(key, value);
            return editor.commit();
        } finally {
            lock.unlock();
        }
    }

    /** Set a float value in the setting preferences */
    public static boolean setFloat(String key, float value) {
        checkState();
        Lock lock = sInstance.mLock.lock(false);
        try {
            SharedPreferences sp = sInstance.mSharedPerference;
            SharedPreferences.Editor editor = sp.edit();
            editor.putFloat(key, value);
            return editor.commit();
        } finally {
            lock.unlock();
        }
    }

    /** Set a long value in the setting preferences */
    public static boolean setLong(String key, long value) {
        checkState();
        Lock lock = sInstance.mLock.lock(false);
        try {
            SharedPreferences sp = sInstance.mSharedPerference;
            SharedPreferences.Editor editor = sp.edit();
            editor.putLong(key, value);
            return editor.commit();
        } finally {
            lock.unlock();
        }
    }

    /** Retrieve a string value from the preferences. */
    public static String getString(String key, String defValue) {
        checkState();
        Lock lock = sInstance.mLock.lock(true);
        try {
            SharedPreferences sp = sInstance.mSharedPerference;
            return sp.getString(key, defValue);
        } finally {
            lock.unlock();
        }
    }

    /** Retrieve a integer value from the preferences. */
    public static int getInt(String key, int defValue) {
        checkState();
        Lock lock = sInstance.mLock.lock(true);
        try {
            SharedPreferences sp = sInstance.mSharedPerference;
            return sp.getInt(key, defValue);
        } finally {
            lock.unlock();
        }
    }

    /** Retrieve a boolean value from the preferences. */
    public static boolean getBoolean(String key, boolean defValue) {
        checkState();
        Lock lock = sInstance.mLock.lock(true);
        try {
            SharedPreferences sp = sInstance.mSharedPerference;
            return sp.getBoolean(key, defValue);
        } finally {
            lock.unlock();
        }
    }

    /** Retrieve a float value from the preferences. */
    public static float getFloat(String key, float defValue) {
        checkState();
        Lock lock = sInstance.mLock.lock(true);
        try {
            SharedPreferences sp = sInstance.mSharedPerference;
            return sp.getFloat(key, defValue);
        } finally {
            lock.unlock();
        }
    }

    /** Retrieve a long value from the preferences. */
    public static long getLong(String key, long defValue) {
        checkState();
        Lock lock = sInstance.mLock.lock(true);
        try {
            SharedPreferences sp = sInstance.mSharedPerference;
            return sp.getLong(key, defValue);
        } finally {
            lock.unlock();
        }
    }
}