
package com.david.support.util;

import android.os.Handler;
import android.os.HandlerThread;

public class BgThread extends HandlerThread {
    private static BgThread sInstance;
    private static Handler sHandler;

    private static int index;
    private BgThread() {
        super(String.format("BgThread-%s", ++index));
    }

    private static void ensureThreadLocked() {
        if (sInstance == null) {
            sInstance = new BgThread();
            sInstance.start();
            sHandler = new Handler(sInstance.getLooper());
        }
    }

    public static BgThread get() {
        synchronized (BgThread.class) {
            ensureThreadLocked();
            return sInstance;
        }
    }

    public static Handler getHandler() {
        synchronized (BgThread.class) {
            ensureThreadLocked();
            return sHandler;
        }
    }

    public static void release() {
        synchronized (BgThread.class) {
            sInstance.quitSafely();
            sInstance = null;
            --index;
            sHandler = null;
        }
    }
}
