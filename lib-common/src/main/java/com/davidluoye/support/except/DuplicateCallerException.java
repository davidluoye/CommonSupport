package com.davidluoye.support.except;

import android.util.AndroidRuntimeException;

public class DuplicateCallerException extends AndroidRuntimeException {
    public DuplicateCallerException(String name) {
        super(name);
    }

    public DuplicateCallerException(String name, Throwable cause) {
        super(name, cause);
    }

    public DuplicateCallerException(Exception cause) {
        super(cause);
    }
}
