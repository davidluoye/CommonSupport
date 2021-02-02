package com.davidluoye.support.log;

import android.os.Process;

import com.davidluoye.support.util.StreamUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public abstract class BaseFileLogger implements IFileLogger {

    protected static final SimpleDateFormat sDayTimeFormat = new SimpleDateFormat("MM-dd HH:mm:ss:SSS", Locale.US);
    protected final File path;
    protected final String name;

    private final String lineSeparator;

    private BufferedOutputStream mStream;
    private int lines;
    public BaseFileLogger(File path, String name, String suffix) {
        if (!path.exists()) {
            path.mkdirs();
        }
        this.path = path;
        this.name = String.format("%s.%s", name, suffix);
        this.lines = 0;
        this.lineSeparator = System.getProperty("line.separator");
    }

    @Override
    public final String getPath() {
        return path.getAbsolutePath();
    }

    @Override
    public final String getName() {
        return name;
    }

    @Override
    public final void write(String tag, String level, String msg) {
        if (msg == null) {
            return;
        }

        try {
            if (mStream == null) {
                mStream = handleOpen(new File(path, name));
            }

            write(format(tag, level, msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected final void write(String line) throws IOException {
        if (mStream != null) {
            byte[] bytes = line.getBytes("UTF-8");
            mStream.write(bytes, 0, bytes.length);

            byte[] lineBytes = lineSeparator.getBytes("UTF-8");
            mStream.write(lineBytes, 0, lineBytes.length);

            mStream.flush();
        }
    }

    protected final String format(String tag, String level, String msg) {
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

    @Override
    public final boolean close() {
        if (mStream != null) {
            handleClose();
            StreamUtils.flush(mStream);
            StreamUtils.close(mStream);
            mStream = null;
        }
        return true;
    }

    protected abstract BufferedOutputStream handleOpen(File file) throws IOException;
    protected boolean handleClose() {return true;}
}
