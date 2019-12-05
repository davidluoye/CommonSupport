package com.davidluoye.support.util;

import java.io.ByteArrayOutputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IoUtil {
    private static final int IO_BUF_SIZE = 1024 * 16; // 16KB

    public static final void close(AutoCloseable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static final void flush(Flushable flushable) {
        if (flushable != null) {
            try {
                flushable.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /** read all bytes from input stream */
    public static byte[] readAllBytes(InputStream is) throws IOException {
        ByteArrayOutputStream bytesBuf = new ByteArrayOutputStream(1024);
        int length;
        byte[] buf = new byte[1024 * 8];
        while ((length = is.read(buf, 0, buf.length)) != -1) {
            bytesBuf.write(buf, 0, length);
        }
        return bytesBuf.toByteArray();
    }

    /**
     * Copy data from the input stream to the output stream.
     * Note: This method will not close the input stream and output stream.
     */
    public static void copyStream(InputStream is, OutputStream os)
            throws IOException {
        byte[] buffer = new byte[IO_BUF_SIZE];
        int len;
        while ((len = is.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }
        os.flush();
    }
}
