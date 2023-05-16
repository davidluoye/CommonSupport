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
