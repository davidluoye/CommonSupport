package com.davidluoye.support.util;

import com.davidluoye.support.log.ILogger;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileUtil {
    private static final ILogger LOGGER = ILogger.logger(FileUtil.class);

    /**
     * Delete a file or directory
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
     * @param path base path
     * @param name target file or directory as child of {@param path}
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
     * Unzip a file to {@param path}
     * @param zip zip file
     * @param path target path
     * @return true if unzip success
     */
    public static final boolean unZip(File zip, File path) {
        ZipInputStream zin = null;
        try {
            zin = new ZipInputStream(new FileInputStream(zip));
            final byte[] buff = new byte[4096];
            ZipEntry entry = null;
            while ((entry = zin.getNextEntry()) != null) {
                String name = entry.getName();
                File child = new File(path, name);

                boolean success = entry.isDirectory() ? (child.exists() || child.mkdirs()) : unZipFile(zin, child, buff);
                if (success) {
                    continue;
                }
                LOGGER.e("fail to unZip to %s", child);
                return false;
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IoUtil.close(zin);
        }
        return false;
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
