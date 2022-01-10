package com.davidluoye.support.except;

import android.util.AndroidRuntimeException;

public class CannotCallerException extends AndroidRuntimeException {
    public CannotCallerException(String name) {
        super(name);
    }

    public CannotCallerException(String name, Throwable cause) {
        super(name, cause);
    }

    public CannotCallerException(Exception cause) {
        super(cause);
    }
}
