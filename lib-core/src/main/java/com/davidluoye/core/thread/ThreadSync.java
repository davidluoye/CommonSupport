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

import android.os.Looper;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Supplier;

public class ThreadSync {

    public enum SyncState {
        NONE, WAITED, NOTIFIED
    }

    public static AtomicReference<SyncState> getSyncState() {
        return new AtomicReference<>(SyncState.NONE);
    }

    public static void waitSyncState(AtomicReference<SyncState> syncState) {
        synchronized (syncState) {
            SyncState state = syncState.get();
            if (state == SyncState.NOTIFIED) {
                syncState.set(SyncState.NONE);
                return;
            }

            try {
                syncState.set(SyncState.WAITED);
                syncState.wait();
            } catch (Exception var4) {
            }
        }
    }

    public static void notifySyncState(AtomicReference<SyncState> syncState) {
        synchronized (syncState) {
            SyncState state = syncState.get();
            if (state == SyncState.WAITED) {
                try {
                    syncState.notify();
                } catch (Exception var4) {
                }
            }
            syncState.set(SyncState.NOTIFIED);
        }
    }

    public static <R> R waitForSync(Supplier<R> supplier) {
        Looper executor = Looper.getMainLooper();
        if (executor == Looper.myLooper()) {
            throw new IllegalStateException("Can not be called in main thread.");
        }

        final AtomicReference<ThreadSync.SyncState> state = ThreadSync.getSyncState();
        final AtomicReference<R> result = new AtomicReference<>();
        Threads.post(() -> {
            result.set(supplier.get());
            ThreadSync.notifySyncState(state);
        }, 10);
        ThreadSync.waitSyncState(state);
        return result.get();
    }

    public static <P, R> R waitForSync(P params, Function<P, R> function) {
        Looper executor = Looper.getMainLooper();
        if (executor == Looper.myLooper()) {
            throw new IllegalStateException("Can not be called in main thread.");
        }

        final AtomicReference<ThreadSync.SyncState> state = ThreadSync.getSyncState();
        final AtomicReference<R> result = new AtomicReference<>();
        Threads.post(() -> {
            result.set(function.apply(params));
            ThreadSync.notifySyncState(state);
        }, 10);
        ThreadSync.waitSyncState(state);
        return result.get();
    }
}
