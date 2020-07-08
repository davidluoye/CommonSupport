package com.davidluoye.support.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

import com.davidluoye.support.app.AppGlobals;
import com.davidluoye.support.log.ILogger;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileUtil {
    private static final ILogger LOGGER = ILogger.logger(FileUtil.class);

    /**
     * Delete a file or directory
     *
     * @param file target file or directory
     * @return true if delete success
     */
    public static final boolean delete(File file) {
        if (!file.exists()) {
            return true;
        }

        if (file.isFile()) {
            return file.delete();
        }

        boolean success = true;
        File[] files = file.listFiles();
        for (File child : files) {
            success &= delete(child);
        }

        success &= file.delete();
        return success;
    }

    /**
     * Create a file or directory
     *
     * @param path   base path
     * @param name   target file or directory as child of {@code path}
     * @param isFile true if target is file, and false if target is directory
     * @return true if create success.
     */
    public static final File create(File path, String name, boolean isFile) {
        if (!path.exists()) {
            boolean success = path.mkdirs();
            if (!success) {
                return null;
            }
        }

        try {
            File file = new File(path, name);
            boolean success = isFile ? file.createNewFile() : file.mkdirs();
            return success ? file : null;
        } catch (IOException e) {
        }
        return null;
    }

    /**
     * Unzip a file to {@code path}
     *
     * @param file zip file
     * @param path target path
     * @return true if unzip success
     */
    public static final boolean unZip(File file, File path) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            return unZip(fis, path);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IoUtil.close(fis);
        }
        return false;
    }

    /**
     * Unzip a file to {@code path}
     *
     * @param uri  zip file uri
     * @param path target path
     * @return true if unzip success
     */
    public static final boolean unZip(Uri uri, File path) {
        Context app = AppGlobals.getApplication();
        ContentResolver cr = app.getContentResolver();
        ParcelFileDescriptor pfd = null;
        try {
            pfd = cr.openFileDescriptor(uri, "r");
            if (pfd != null) {
                FileDescriptor fd = pfd.getFileDescriptor();
                return unZip(fd, path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IoUtil.close(pfd);
        }
        return false;
    }

    /**
     * Unzip a file to {@code path}
     *
     * @param am    asset manager
     * @param child zip file in asset directory
     * @param path  target path
     * @return true if unzip success
     */
    public static final boolean unZip(AssetManager am, String child, File path) {
        InputStream is = null;
        try {
            is = am.open(child);
            return unZip(is, path);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IoUtil.close(is);
        }
        return false;
    }

    /**
     * Unzip a file to {@code path}
     *
     * @param fd   zip file description
     * @param path target path
     * @return true if unzip success
     */
    public static final boolean unZip(FileDescriptor fd, File path) throws IOException {
        return unZip(new FileInputStream(fd), path);
    }

    /**
     * Unzip a file to {@code path}
     *
     * @param is   input stream
     * @param path target path
     * @return true if unzip success
     */
    public static final boolean unZip(InputStream is, File path) throws IOException {
        ZipInputStream zin = new ZipInputStream(is);
        final byte[] buff = new byte[4096];
        ZipEntry entry = null;
        while ((entry = zin.getNextEntry()) != null) {
            boolean directory = entry.isDirectory();
            String name = entry.getName();
            File child = new File(path, name);
            boolean success = directory ? (child.exists() || child.mkdirs()) : unZipFile(zin, child, buff);
            if (!success) {
                return false;
            }
        }
        return true;
    }

    private static final boolean unZipFile(ZipInputStream zin, File file, byte[] buffer) {
        BufferedOutputStream out = null;
        try {
            out = new BufferedOutputStream(new FileOutputStream(file));
            int count;
            while ((count = zin.read(buffer)) != -1) {
                out.write(buffer, 0, count);
            }
            IoUtil.flush(out);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IoUtil.close(out);
        }
        return false;
    }
}
