/**
 * Copyright (C) 2012 The Android Open Source Project
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

package com.davidluoye.support.thread;

import android.os.Handler;
import android.os.HandlerThread;

import com.davidluoye.support.list.ArrayEntry;

import java.util.List;

public class WorkTask extends HandlerThread {
    private static ArrayEntry<String, WorkTask> sInstances = new ArrayEntry<>();
    private static ArrayEntry<String, Handler> sHandlers = new ArrayEntry<>();
    private static int index;

    private WorkTask(String name) {
        super(String.format("WorkTask-%s-%s", name, index));
    }

    private static void ensureThreadLocked(String name) {
        WorkTask thread = sInstances.getValue(name);
        if (thread == null) {
            thread = new WorkTask(name);
            thread.start();
            sInstances.put(name, thread);
            sHandlers.put(name, new Handler(thread.getLooper()));
            index++;
        }
    }

    public static WorkTask get(String name) {
        synchronized (WorkTask.class) {
            ensureThreadLocked(name);
            return sInstances.getValue(name);
        }
    }

    public static Handler getHandler(String name) {
        synchronized (WorkTask.class) {
            ensureThreadLocked(name);
            return sHandlers.getValue(name);
        }
    }

    public static void release(String name) {
        synchronized (WorkTask.class) {
            sHandlers.removeKey(name);
            WorkTask thread = sInstances.removeKey(name);
            if (thread != null) {
                thread.quitSafely();
                index--;
            }
        }
    }

    public static void release() {
        synchronized (WorkTask.class) {
            List<WorkTask> threads = sInstances.values();
            threads.forEach(HandlerThread::quitSafely);
            sInstances.clean();
            sHandlers.clean();
            index -= threads.size();
        }
    }
}
