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

import android.os.Process;
import android.os.SystemClock;
import android.util.Log;

import com.davidluoye.support.util.StreamUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * A class to persist log info.
 * This may be helpful to position some special issues.
 *
 * If you want to call {@link #log(String, String, String)}, you should
 * call {@link #start()} firstly, and finish with {@link #stop()}
 */
public class SceneZipMaker {
    private static final String TAG = "SceneZipMaker";

    private static final SimpleDateFormat sDayTimeFormat = new SimpleDateFormat("MM-dd HH:mm:ss:SSS", Locale.US);
    private final String lineSeparator;
    private final File path;
    private final String name;

    private ZipOutputStream mStream;
    public SceneZipMaker(File path, String name) {
        if (!path.exists()) {
            path.mkdirs();
        }
        this.path = path;
        this.name = String.format("%s.%s", name, "zip");
        this.lineSeparator = System.getProperty("line.separator");
    }

    public final synchronized boolean start() {
        if (mStream != null) {
            Log.d(TAG, "should call stop firstly to finish last logger file.");
            return false;
        }

        // get time since boot, include time spend in sleep.
        final long bootTime = SystemClock.elapsedRealtime();
        final String name = String.format("boot_%s", bootTime);

        File file = new File(path, name);
        try {
            FileOutputStream fos = new FileOutputStream(file, true);
            mStream = new ZipOutputStream(fos);

            // first to create entry file.
            ZipEntry entry = new ZipEntry(name);
            mStream.putNextEntry(entry);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public final synchronized boolean stop() {
        if (mStream != null) {
            try {
                mStream.flush();
                mStream.closeEntry();
                mStream.finish();
            } catch (IOException e) {
                e.printStackTrace();
            }
            StreamUtils.close(mStream);
            mStream = null;
            return true;
        }
        return false;
    }

    public final synchronized void log(String tag, String level, String msg) {
        if (mStream == null) {
            Log.d(TAG, "should call start firstly.");
            return;
        }

        try {
            write(format(tag, level, msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final void write(String line) throws IOException {
        if (mStream != null) {
            byte[] bytes = line.getBytes("UTF-8");
            mStream.write(bytes, 0, bytes.length);

            byte[] lineBytes = lineSeparator.getBytes("UTF-8");
            mStream.write(lineBytes, 0, lineBytes.length);

            mStream.flush();
        }
    }

    private final String format(String tag, String level, String msg) {
        StringBuilder sb = new StringBuilder();
        sb.append(sDayTimeFormat.format(new Date()));
        sb.append("  ");
        sb.append(Process.myPid());
        sb.append("  ");
        sb.append(Process.myTid());
        sb.append(" ");
        sb.append(level);
        sb.append(" ");
        sb.append(tag);
        sb.append(": ");
        sb.append(msg);

        String line = sb.toString();
        return line;
    }
}
