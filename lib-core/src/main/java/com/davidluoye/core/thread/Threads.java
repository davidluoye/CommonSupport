/*
 * Copyright 2021 The authors David Yang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.davidluoye.core.thread;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

import java.util.concurrent.ExecutorService;

public class Threads {

    public static void wait(Object obj, long milliseconds) {
        synchronized (obj) {
            try {
                obj.wait(milliseconds);
            } catch (Exception e) {}
        }
    }

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
}
