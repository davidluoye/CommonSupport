package com.davidluoye.support.log;

public interface IFileLogger {

    String getPath();

    String getName();

    void write(String tag, String level, String msg);

    boolean close();
}
