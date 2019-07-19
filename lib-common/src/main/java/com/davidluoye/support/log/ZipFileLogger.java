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
