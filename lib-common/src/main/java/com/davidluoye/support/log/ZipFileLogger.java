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

import com.davidluoye.support.app.HookManager;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/* package */ final class ZipFileLogger extends BaseFileLogger {

    private static final String LOG_FILE = "logcat.txt";
    private ZipOutputStream mZipOutStream;
    /* package */ ZipFileLogger(File path, String name) {
        super(path, name, "zip");

        HookManager.hookCrash(new HookManager.CrashHooker() {
            @Override
            protected boolean handleException(Thread t, Throwable e) {
                close();
                return false;
            }
        });
    }

    @Override
    protected BufferedOutputStream handleOpen(File file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        ZipOutputStream zos = new ZipOutputStream(fos);

        // first to create entry file.
        ZipEntry entry = new ZipEntry(LOG_FILE);
        zos.putNextEntry(entry);

        BufferedOutputStream bos = new BufferedOutputStream(zos);
        this.mZipOutStream = zos;
        return bos;
    }

    @Override
    protected boolean handleClose() {
        if (mZipOutStream != null) {
            try {
                mZipOutStream.flush();
                mZipOutStream.closeEntry();
                mZipOutStream.finish();
                mZipOutStream = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
