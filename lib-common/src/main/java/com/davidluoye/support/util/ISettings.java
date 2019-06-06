
package com.davidluoye.support.util;

import android.content.Context;
import android.content.SharedPreferences;

public class ISettings {

    private static ISettings sInstance;
    private Context mContext;
    private SharedPreferences mSharedPerference;

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
    public static void setString(String key, String value) {
        checkState();
        SharedPreferences sp = sInstance.mSharedPerference;
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /** Set a integer value in the setting preferences */
    public static void setInt(String key, int value) {
        checkState();
        SharedPreferences sp = sInstance.mSharedPerference;
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /** Set a boolean value in the setting preferences */
    public static void setBoolean(String key, boolean value) {
        checkState();
        SharedPreferences sp = sInstance.mSharedPerference;
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /** Set a float value in the setting preferences */
    public static void setFloat(String key, float value) {
        checkState();
        SharedPreferences sp = sInstance.mSharedPerference;
        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    /** Set a long value in the setting preferences */
    public static void setLong(String key, long value) {
        checkState();
        SharedPreferences sp = sInstance.mSharedPerference;
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    /** Retrieve a string value from the preferences. */
    public static String getString(String key, String defValue) {
        checkState();
        SharedPreferences sp = sInstance.mSharedPerference;
        return sp.getString(key, defValue);
    }

    /** Retrieve a integer value from the preferences. */
    public static int getInt(String key, int defValue) {
        checkState();
        SharedPreferences sp = sInstance.mSharedPerference;
        return sp.getInt(key, defValue);
    }

    /** Retrieve a boolean value from the preferences. */
    public static boolean getBoolean(String key, boolean defValue) {
        checkState();
        SharedPreferences sp = sInstance.mSharedPerference;
        return sp.getBoolean(key, defValue);
    }

    /** Retrieve a float value from the preferences. */
    public static float getFloat(String key, float defValue) {
        checkState();
        SharedPreferences sp = sInstance.mSharedPerference;
        return sp.getFloat(key, defValue);
    }

    /** Retrieve a long value from the preferences. */
    public static long getLong(String key, long defValue) {
        checkState();
        SharedPreferences sp = sInstance.mSharedPerference;
        return sp.getLong(key, defValue);
    }
}