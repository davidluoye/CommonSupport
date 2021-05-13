package com.davidluoye.support.property;

import android.util.ArrayMap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.EnumSet;
import java.util.Set;

public class ReadWriteOptions {
    private static class PosixPermissions {
        static final FileAttribute<Set<PosixFilePermission>> filePermissions =
                PosixFilePermissions.asFileAttribute(EnumSet.of(
                                PosixFilePermission.OWNER_READ,
                                PosixFilePermission.OWNER_WRITE)
                );
    }

    private static final char SPLITTER = '=';

    private final ArrayMap<String, String> mCache;
    private final PropertyReader mReader;
    private final PropertyWriter mWriter;
    public ReadWriteOptions(Path path) {
        if (!Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
            try {
                Files.createFile(path, PosixPermissions.filePermissions);
            } catch (IOException e) {
                this.mReader = null;
                this.mWriter = null;
                this.mCache = null;
                return;
            }
        }

        this.mWriter = new PropertyWriter(SPLITTER, path);
        this.mReader = new PropertyReader(SPLITTER, path);
        this.mReader.read();

        this.mCache = new ArrayMap<>();
        this.mCache.putAll(mReader.getProperties());

    }

    public boolean put(String key, String value) {
        if (mCache != null) {
            String oldValue = mCache.put(key, value);
            boolean success = mWriter.write(mCache);
            if (!success) mCache.put(key, oldValue);
            return success;
        }
        return false;
    }

    public String get(String key) {
        return mCache != null ? mCache.get(key) : null;
    }
}
