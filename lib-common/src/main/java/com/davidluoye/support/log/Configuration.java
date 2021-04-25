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
package com.davidluoye.support.log;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;

import com.davidluoye.support.app.AppGlobals;
import com.davidluoye.support.app.Permission;
import com.davidluoye.support.util.SharedSettings;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

public class Configuration {

    public static final int DEFAULT_LEVEL = Log.INFO;

    public static final boolean DEFAULT_PERSIST_LOG_LEVEL = true;

    public static final String APP_TAG = "ILib";

    public static final String PATH_BASE = "logcat";

    private static final SimpleDateFormat sTimeFormat = new SimpleDateFormat("yyyyMMdd_HHmmssSSS", Locale.US);

    private static Configuration sInstance;

    public final File directory;
    public final String appTag;
    public final String name;
    public final IFileLogger logger;
    public final boolean alwaysPrint;
    public final boolean alwaysPersist;
    public final boolean persistLogLevel;

    private IntSetting mLogSetting;

    private Configuration(Builder build) {
        if (sInstance != null) {
            throw new IllegalStateException("has already build configuration.");
        }
        sInstance = this;

        this.appTag = build.appTag == null ? APP_TAG : build.appTag;
        this.alwaysPrint = build.alwaysPrint;
        this.alwaysPersist = build.alwaysPersist;
        this.persistLogLevel = build.persistLogLevel;
        this.directory = build.directory != null ? build.directory : getFilePath();
        this.name = sTimeFormat.format(new Date());

        this.mLogSetting = new IntSetting(AppGlobals.getApplication(), persistLogLevel, "logLevel");
        if (!mLogSetting.hasValue()) {
            mLogSetting.setInt(build.logLevel);
        }

        IFileLogger logger = null;
        if (alwaysPersist) {
            if (build.compress) {
                logger = new ZipFileLogger(this.directory, this.name);
            } else {
                logger = new TxtFileLogger(this.directory, this.name);
            }
        }
        this.logger = logger;
    }

    public void setLogLevel(int level) {
        level = Math.max(level, Log.VERBOSE);
        level = Math.min(level, Log.ERROR);
        mLogSetting.setInt(level);
    }

    public int getLogLevel() {
        return mLogSetting.getInt();
    }

    private static class IntSetting {
        private final SharedSettings settings;
        private final AtomicInteger cache;
        private final String name;
        private SharedPreferences.OnSharedPreferenceChangeListener callback;
        private IntSetting(Context context, boolean persistLogLevel, String name) {
            this.name = name;
            this.cache = new AtomicInteger();
            this.settings = context != null && persistLogLevel ? new SharedSettings(context) : null;
            if (settings != null) {
                this.cache.set(settings.getInt(name, cache.intValue()));
                this.callback = (sharedPreferences, key) -> {
                    if (TextUtils.equals(name, key)) {
                        cache.set(settings.getInt(name, cache.intValue()));
                    }
                };
                settings.registerChangedEvent(callback);
            }
        }

        public boolean hasValue() {
            return settings != null && settings.containKey(name);
        }

        public void setInt(int value) {
            int oldValue = cache.getAndSet(value);
            if (settings != null && oldValue != value) {
                settings.setInt(name, value);
            }
        }

        public int getInt() {
            return cache.get();
        }
    }

    public static class Builder {
        private File directory;
        private String appTag;
        private boolean compress;
        private boolean alwaysPrint = false;
        private boolean alwaysPersist = false;
        private boolean persistLogLevel = DEFAULT_PERSIST_LOG_LEVEL;
        private int logLevel = DEFAULT_LEVEL;

        public Builder directory(File directory) {
            this.directory = directory;
            return this;
        }

        public Builder appTag(String appTag) {
            this.appTag = appTag;
            return this;
        }

        public Builder compress() {
            this.compress = true;
            return this;
        }

        public Builder alwaysPrint() {
            this.alwaysPrint = true;
            return this;
        }

        public Builder alwaysPersist() {
            this.alwaysPersist = true;
            return this;
        }

        public Builder logLevel(int level) {
            this.logLevel = level;
            return this;
        }

        public Builder persistLogLevel(boolean persist) {
            this.persistLogLevel = persist;
            return this;
        }

        public Configuration build() {
            return new Configuration(this);
        }
    }

    public static Configuration get() {
        return sInstance;
    }

    /*package*/ static boolean canLog(int level) {
        if (sInstance == null) return DEFAULT_LEVEL <= level;
        return sInstance.getLogLevel() <= level;
    }

    private static File getFilePath() {
        File root = null;
        if (Permission.hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            root = Environment.getExternalStorageDirectory();
        } else if (Process.myUid() == Process.SYSTEM_UID){
            root = Environment.getDataDirectory();
        } else {
            Context context = AppGlobals.getApplication();
            root = context.getCacheDir();
        }
        File logPath = new File(root, PATH_BASE);
        File appLog = new File(logPath, AppGlobals.getPackageName());
        return appLog;
    }
}
