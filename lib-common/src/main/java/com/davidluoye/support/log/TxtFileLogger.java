package com.davidluoye.support.log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/* package */ final class TxtFileLogger extends BaseFileLogger {

    /* package */ TxtFileLogger(File path, String name) {
        super(path, name, "txt");
    }

    @Override
    protected BufferedOutputStream handleOpen(File file) throws IOException {
        return new BufferedOutputStream(new FileOutputStream(file));
    }
}
