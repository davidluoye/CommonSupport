package com.davidluoye.support.except;

import android.util.AndroidRuntimeException;

public class InvalidThreadCallerException extends AndroidRuntimeException {
    public InvalidThreadCallerException(String name) {
        super(name);
    }

    public InvalidThreadCallerException(String name, Throwable cause) {
        super(name, cause);
    }

    public InvalidThreadCallerException(Exception cause) {
        super(cause);
    }
}
