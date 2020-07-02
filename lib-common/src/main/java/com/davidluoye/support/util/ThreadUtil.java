package com.davidluoye.support.util;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadUtil {

    public static void wait(Object obj) {
        synchronized (obj) {
            try {
                obj.wait();
            } catch (Exception e) {}
        }
    }

    public static void notify(Object obj) {
        synchronized (obj) {
            try {
                obj.notifyAll();
            } catch (Exception e) {}
        }
    }

    public static void sleep(long ms) {
        SystemClock.sleep(ms);
    }

    public static boolean isUiThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    /** post an runnable action in ui thread */
    public static void post(Runnable action) {
        ThreadHolder.sUiThreadHandler.post(action);
    }

    /** post an delay runnable action in ui thread */
    public static void post(Runnable action, long delay) {
        ThreadHolder.sUiThreadHandler.postDelayed(action, delay);
    }

    /** remove an runnable action from ui thread */
    public static void postRemove(Runnable action) {
        ThreadHolder.sUiThreadHandler.removeCallbacks(action);
    }

    /** execute an runnable action in non-ui-thread */
    public static void execute(Runnable action) {
        ThreadHolder.sBgThreadHandler.post(action);
    }

    /** execute an delay runnable action in non-ui-thread */
    public static void execute(Runnable action, long delay) {
        ThreadHolder.sBgThreadHandler.postDelayed(action, delay);
    }

    /** remove an runnable action from non-ui-thread */
    public static void executeRemove(Runnable action) {
        ThreadHolder.sBgThreadHandler.removeCallbacks(action);
    }

    /** execute an runnable action in non-ui-thread in parallel */
    public static void addThreadPool(Runnable action) {
        ThreadHolder.sThreadPool.execute(action);
    }

    private static class ThreadHolder {
        private static final Handler sUiThreadHandler = new Handler(Looper.getMainLooper());
        private static final Handler sBgThreadHandler = BgThread.getHandler();
        private static final ExecutorService sThreadPool = new ExecutorPool(5).getThreadPool();
    }

    private static class ExecutorPool {
        private final BlockingQueue queue;
        private final ExecutorService pools;
        private ExecutorPool(final int size) {
            ThreadFactory factory = new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, String.format("thread-pools-%s", size));
                }
            };
            queue = new LinkedBlockingQueue();
            pools = new ThreadPoolExecutor(0, size, 0L, TimeUnit.MILLISECONDS, queue, factory);
        }

        public final ExecutorService getThreadPool() {
            return this.pools;
        }
    }

}
