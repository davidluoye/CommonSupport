package com.davidluoye.support.property;

import android.content.Context;
import android.util.ArrayMap;

import com.davidluoye.support.app.AppGlobals;
import com.davidluoye.support.util.IoUtil;
import com.davidluoye.support.util.NumberUtil;
import com.davidluoye.support.util.StringUtil;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

public class GlobalProperty {

    public static boolean set(String key, String value) {
        ReadWriteOptions options = getOptions();
        return options.put(key, value);
    }

    public static boolean set(String key, String...values) {
        ReadWriteOptions options = getOptions();
        return options.put(key, StringUtil.join(separator, values));
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
        return NumberUtil.parseInt(value, defValue);
    }

    public static char getChar(String key, char defValue) {
        ReadWriteOptions options = getOptions();
        String value = options.get(key);
        return (value != null && value.length() == 1) ? value.charAt(0) : defValue;
    }

    public static float getFloat(String key, float defValue) {
        ReadWriteOptions options = getOptions();
        String value = options.get(key);
        return NumberUtil.parseFloat(value, defValue);
    }

    public static long getLong(String key, long defValue) {
        ReadWriteOptions options = getOptions();
        String value = options.get(key);
        return NumberUtil.parseLong(value, defValue);
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
            options = new ReadWriteOptions();
            options.read();
        }
        return options;
    }

    private static class ReadWriteOptions {
        private static final char SPLITTER = '=';

        private final ArrayMap<String, String> cache;
        private final PropertyReader mReader;
        private final PropertyWriter mWriter;
        private ReadWriteOptions() {
            this.mReader = new PropertyReader(SPLITTER);
            this.mWriter = new PropertyWriter(SPLITTER);
            this.cache = new ArrayMap<>();
        }

        public boolean put(String key, String value) {
            cache.put(key, value);
            return write();
        }

        public String get(String key) {
            return cache.get(key);
        }

        private synchronized boolean write() {
            Context app = AppGlobals.getApplication();
            OutputStream os = null;
            try {
                os = app.openFileOutput(name, Context.MODE_PRIVATE);
                return mWriter.write(os, cache);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                IoUtil.flush(os);
                IoUtil.close(os);
            }
            return false;
        }

        private synchronized boolean read() {
            Context app = AppGlobals.getApplication();
            InputStream is = null;
            try {
                is = app.openFileInput(name);
                boolean success = mReader.read(is);
                cache.putAll(mReader.getProperties());
                return success;
            } catch (FileNotFoundException e) {
            } finally {
                IoUtil.close(is);
            }
            return false;
        }
    }
}
