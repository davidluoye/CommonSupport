package com.davidluoye.support.app;

import android.app.Instrumentation;

import com.davidluoye.support.log.ILogger;

import java.lang.Thread.UncaughtExceptionHandler;

public class HookManager {
    private static final ILogger LOGGER = ILogger.logger("HookManager");

    public static void hookCrash(CrashHooker hooker) {
        UncaughtExceptionHandler oldHandler = Thread.getDefaultUncaughtExceptionHandler();
        hooker.attachHandler(oldHandler);
        Thread.setDefaultUncaughtExceptionHandler(hooker);
    }

    public static void hookInstrumentation(InstrumentationHooker hooker) {
        Instrumentation oldInstrumentation = AppGlobals.getInstrumentation();
        hooker.attachInstrumentation(oldInstrumentation);
        AppGlobals.setInstrumentation(hooker);
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
