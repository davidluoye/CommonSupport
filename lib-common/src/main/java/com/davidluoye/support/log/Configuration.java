package com.davidluoye.support.log;

import android.Manifest;
import android.content.Context;
import android.os.Environment;
import android.os.Process;
import android.util.Log;

import com.davidluoye.support.app.AppGlobals;
import com.davidluoye.support.app.Permission;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Configuration {

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
    public final int logLevel;

    private Configuration(Builder build) {
        if (sInstance != null) {
            throw new IllegalStateException("has already build configuration.");
        }
        sInstance = this;

        this.appTag = build.appTag == null ? APP_TAG : build.appTag;
        this.alwaysPrint = build.alwaysPrint;
        this.alwaysPersist = build.alwaysPersist;
        this.directory = build.directory != null ? build.directory : getFilePath();
        this.name = sTimeFormat.format(new Date());
        this.logLevel = build.logLevel;

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

    public static class Builder {
        private File directory;
        private String appTag;
        private boolean compress;
        private boolean alwaysPrint = false;
        private boolean alwaysPersist = false;
        private int logLevel = ILogger.DEBUG ? Log.VERBOSE : Log.DEBUG;

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

        public Configuration build() {
            return new Configuration(this);
        }
    }

    public static Configuration get() {
        return sInstance;
    }

    /*package*/ static boolean canLog(int level) {
        Configuration configuration = Configuration.get();
        if (configuration != null) {
            return configuration.logLevel <= level;
        }
        return true;
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
