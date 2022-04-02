package com.davidluoye.support.thread;

import android.os.Looper;

import com.davidluoye.support.except.InvalidThreadCallerException;

public class Validation {

    public static void checkMainThread() {
        checkMainThread("Cannot be called in async thread.");
    }

    public static void checkNotMainThread() {
        checkNotMainThread("Cannot be called in main thread.");
    }

    public static void checkMainThread(String message) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new InvalidThreadCallerException(message);
        }
    }

    public static void checkNotMainThread(String message) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new InvalidThreadCallerException(message);
        }
    }
}
