package com.davidluoye.support.util;

import java.io.File;

public class FileUtil {

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
}
