package com.davidluoye.core.except;

import android.util.AndroidRuntimeException;

public class NotSupportCallerException extends AndroidRuntimeException {
    public NotSupportCallerException(String name) {
        super(name);
    }

    public NotSupportCallerException(String name, Throwable cause) {
        super(name, cause);
    }

    public NotSupportCallerException(Exception cause) {
        super(cause);
    }
}
