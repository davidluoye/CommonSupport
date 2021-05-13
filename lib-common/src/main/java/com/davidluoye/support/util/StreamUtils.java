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
package com.davidluoye.support.util;

import android.os.ParcelFileDescriptor;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class StreamUtils {

    public static final Charset DEFAULT = StandardCharsets.UTF_8;

    @FunctionalInterface
    public interface NameFilter {
        String getName(File file);
    }

    public static boolean compress(File zip, NameFilter filter, File... files) {
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(new FileOutputStream(zip), DEFAULT);
            return compress(zos, filter, files);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(zos);
        }
        return false;
    }

    public static boolean compress(File zip, NameFilter filter, File file) throws IOException {
        FileInputStream fis = null;
        try {
            String name = filter == null ? file.getName() : filter.getName(file);
            fis = new FileInputStream(file);
            return compress(zip, name, fis);
        } finally {
            close(fis);
        }
    }

    public static boolean compress(File zip, String entryName, InputStream is) throws IOException {
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(new FileOutputStream(zip), DEFAULT);
            return compress(zos, is, entryName);
        } finally {
            close(zos);
        }
    }

    public static boolean compress(ZipOutputStream zos, NameFilter filter, File... files) {
        boolean success = true;
        for (File file : files) {
            String name = filter == null ? file.getName() : filter.getName(file);
            try {
                success &= compress(zos, file, name);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return success;
    }

    public static boolean compress(ZipOutputStream zos, File file, String entryName) throws IOException {
        FileInputStream fis = null;
        try {
            zos.putNextEntry(new ZipEntry(entryName));
            fis = new FileInputStream(file);
            return copyStream(fis, zos);
        } finally {
            zos.closeEntry();
            close(fis);
        }
    }

    public static boolean compress(ZipOutputStream zos, FileDescriptor fd, String entryName) throws IOException {
        InputStream is = null;
        try {
            zos.putNextEntry(new ZipEntry(entryName));
            is = new FileInputStream(fd);
            return copyStream(is, zos);
        } finally {
            zos.closeEntry();
            close(is);
        }
    }

    public static boolean compress(ZipOutputStream zos, InputStream is, String entryName) throws IOException {
        try {
            zos.putNextEntry(new ZipEntry(entryName));
            return copyStream(is, zos);
        } finally {
            zos.closeEntry();
        }
    }

    public static boolean unCompress(File zip, File path) throws IOException {
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(zip);
            return unCompress(stream, path);
        } finally {
            close(stream);
        }
    }

    public static boolean unCompress(FileDescriptor fd, File path) throws IOException {
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(fd);
            return unCompress(stream, path);
        } finally {
            close(stream);
        }
    }

    public static boolean unCompress(InputStream is, File path) throws IOException {
        ZipInputStream zin = new ZipInputStream(is);
        final byte[] buff = new byte[4096];
        ZipEntry entry = null;
        while ((entry = zin.getNextEntry()) != null) {
            String name = entry.getName();
            File child = new File(path, name);
            if (entry.isDirectory() && (child.exists() || child.mkdirs())) {
                continue;
            }

            if (!entry.isDirectory()) {
                FileOutputStream os = null;
                try {
                    os = new FileOutputStream(child);
                    if (copyStream(zin, os)) {
                        continue;
                    }
                } finally {
                    close(os);
                }
            }
            return false;
        }
        return true;
    }

    public static boolean copyStream(File file, OutputStream os) throws IOException {
        ParcelFileDescriptor fd = null;
        try {
            fd = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
            return copyStream(fd, os);
        } finally {
            close(fd);
        }
    }

    public static boolean copyStream(ParcelFileDescriptor fd, OutputStream os) throws IOException {
        return copyStream(fd, os);
    }

    public static boolean copyStream(FileDescriptor fd, OutputStream os) throws IOException {
        InputStream is = null;
        try {
            is = new FileInputStream(fd);
            return copyStream(is, os);
        } finally {
            close(is);
        }
    }

    public static boolean copyStream(InputStream is, OutputStream os) throws IOException {
        byte[] buff = new byte[1024];
        int length = 0;
        while ((length = is.read(buff)) != -1) {
            os.write(buff, 0, length);
        }
        os.flush();
        return true;
    }

    public static boolean copyStream(File oldPath, File newPath, String name) throws IOException {
        return copyStream(new File(oldPath, name), new File(newPath, name));
    }

    public static boolean copyStream(File input, File output) throws IOException {
        FileInputStream is = null;
        FileOutputStream os = null;
        try {
            is = new FileInputStream(input);
            os = new FileOutputStream(output);
            return copyStream(is, os);
        } finally {
            StreamUtils.close(is);
            StreamUtils.close(os);
        }
    }

    public static boolean copyStream(FileDescriptor input, FileDescriptor output) throws IOException {
        FileInputStream is = null;
        FileOutputStream os = null;
        try {
            is = new FileInputStream(input);
            os = new FileOutputStream(output);
            return copyStream(is, os);
        } finally {
            StreamUtils.close(is);
            StreamUtils.close(os);
        }
    }

    public static final boolean copyStream(FileInputStream inputStream, FileOutputStream outputStream) throws IOException {
        FileChannel in = null;
        FileChannel out = null;
        try {
            in = inputStream.getChannel();
            out = outputStream.getChannel();
            return copyStream(in, out);
        } finally {
            StreamUtils.close(in);
            StreamUtils.close(out);
        }
    }

    public static final boolean copyStream(FileChannel inputStream, FileChannel outputStream) throws IOException {
        outputStream.transferFrom(inputStream, 0, inputStream.size());
        return true;
    }

    public static void close(AutoCloseable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (Exception e) {}
    }

    public static void close(ZipOutputStream closeable) {
        try {
            if (closeable != null) {
                closeable.closeEntry();
                closeable.close();
            }
        } catch (Exception e) {}
    }

    public static void flush(Flushable flushable) {
        try {
            if (flushable != null) {
                flushable.flush();
            }
        } catch (Exception e) {}
    }
}
