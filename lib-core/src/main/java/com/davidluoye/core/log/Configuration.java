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
package com.davidluoye.core.log;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.davidluoye.core.app.Applications;
import com.davidluoye.core.box.Strings;
import com.davidluoye.core.utils.SharedSettings;

import java.util.concurrent.atomic.AtomicInteger;

public class Configuration {

    public static final int DEFAULT_LEVEL = Log.INFO;

    public static String APP_TAG = "ILib";

    private static Configuration sInstance;

    public final boolean persistLogLevel;

    private final IntSetting mLogSetting;
    private String appTag;
    private Configuration(Builder build) {
        if (sInstance != null) {
            throw new IllegalStateException("has already build configuration.");
        }
        sInstance = this;

        this.appTag = Strings.isBlank(build.appTag) ? APP_TAG : build.appTag;
        this.persistLogLevel = build.persistLogLevel;

        this.mLogSetting = new IntSetting(Applications.getApplication(), persistLogLevel, "logLevel");
        if (!mLogSetting.hasValue()) {
            mLogSetting.setInt(build.logLevel);
        }
    }

    public void setLogLevel(int level) {
        level = Math.max(level, Log.VERBOSE);
        level = Math.min(level, Log.ERROR);
        mLogSetting.setInt(level);
    }

    public int getLogLevel() {
        return mLogSetting.getInt();
    }

    public void appTag(String appTag) {
        this.appTag = appTag;
    }

    public String appTag() {
        return this.appTag;
    }

    private static class IntSetting {
        private final SharedSettings settings;
        private final AtomicInteger cache;
        private final String name;
        private IntSetting(Context context, boolean persistLogLevel, String name) {
            this.name = name;
            this.cache = new AtomicInteger();
            this.settings = persistLogLevel && Applications.canAccessStorage(context) ? new SharedSettings(context) : null;
            if (settings != null) {
                this.cache.set(settings.getInt(name, cache.intValue()));
                settings.registerChangedEvent((key) -> {
                    if (TextUtils.equals(name, key)) {
                        cache.set(settings.getInt(name, cache.intValue()));
                    }
                });
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
        private String appTag;
        private boolean persistLogLevel = false;
        private int logLevel = DEFAULT_LEVEL;

        public Builder appTag(String appTag) {
            this.appTag = appTag;
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
            Configuration configuration = Configuration.get();
            if (configuration != null) {
                configuration.appTag(this.appTag);
                configuration.setLogLevel(this.logLevel);
                return configuration;
            }
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
}
