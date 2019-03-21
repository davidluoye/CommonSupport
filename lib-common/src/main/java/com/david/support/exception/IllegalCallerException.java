
package com.david.support.exception;

public class IllegalCallerException extends RuntimeException {

    public IllegalCallerException() {
        super();
    }

    public IllegalCallerException(String detailMessage) {
        super(detailMessage);
    }

    public IllegalCallerException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalCallerException(Throwable cause) {
        super((cause == null ? null : cause.toString()), cause);
    }
}
