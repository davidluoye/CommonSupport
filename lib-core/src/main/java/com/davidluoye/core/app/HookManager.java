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
package com.davidluoye.core.app;

import android.app.Instrumentation;
import android.os.Process;

import java.lang.Thread.UncaughtExceptionHandler;

public class HookManager {

    public static UncaughtExceptionHandler hookCrash() {
        return hookCrash(new CrashHooker() {
            @Override
            protected boolean handleException(Thread t, Throwable e) {
                Process.killProcess(Process.myPid());
                System.exit(10);
                return true;
            }
        });
    }

    public static UncaughtExceptionHandler hookCrash(CrashHooker hooker) {
        UncaughtExceptionHandler oldHandler = Thread.getDefaultUncaughtExceptionHandler();
        hooker.attachHandler(oldHandler);
        Thread.setDefaultUncaughtExceptionHandler(hooker);
        return oldHandler;
    }

    public static Instrumentation hookInstrumentation(InstrumentationHooker hooker) {
        Instrumentation oldInstrumentation = Applications.getInstrumentation();
        hooker.attachInstrumentation(oldInstrumentation);
        Applications.setInstrumentation(hooker);
        return oldInstrumentation;
    }

    public static abstract class CrashHooker implements UncaughtExceptionHandler {
        private UncaughtExceptionHandler mOldUncaughtExceptionHandler;

        private final void attachHandler(UncaughtExceptionHandler handler) {
            this.mOldUncaughtExceptionHandler = handler;
        }

        @Override
        public final void uncaughtException(Thread t, Throwable e) {
            boolean handle = handleException(t, e);
            if (!handle && mOldUncaughtExceptionHandler != null) {
                mOldUncaughtExceptionHandler.uncaughtException(t, e);
            }
        }

        protected abstract boolean handleException(Thread t, Throwable e);
    }

    public static abstract class InstrumentationHooker extends Instrumentation {
        private Instrumentation mOldInstrumentation;
        private final void attachInstrumentation(Instrumentation instrumentation) {
            this.mOldInstrumentation = instrumentation;
        }

        protected final Instrumentation getInstrumentation() {
            return mOldInstrumentation;
        }
    }
}
