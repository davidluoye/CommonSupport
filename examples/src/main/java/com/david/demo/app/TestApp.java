package com.david.demo.app;

import android.app.Application;
import android.app.Instrumentation;

import com.davidluoye.support.app.AppGlobals;
import com.davidluoye.support.app.HookManager;
import com.davidluoye.support.log.ILogger;
import com.davidluoye.support.test.SuitCase;
import com.davidluoye.support.test.SuitClass;
import com.davidluoye.support.test.TestUnit;
import com.davidluoye.support.util.Preconditions;
import com.davidluoye.support.util.ThreadUtil;

@SuitClass
public class TestApp extends TestUnit {
    private static final ILogger LOGGER = ILogger.logger(TestApp.class);

    private Thread.UncaughtExceptionHandler oldOne;
    private String oneMsg;

    @SuitCase
    public void testAppGlobals() {
        Application app = AppGlobals.getApplication();
        Preconditions.checkNotNull(app, "application should not be null.");

        String pkg = AppGlobals.getPackageName();
        Preconditions.checkNotNull(pkg, "can not get package name.");

        String name = app.getPackageName();
        Preconditions.checkArgument(name.equals(pkg), "package not equals.");

        Instrumentation instrumentation = AppGlobals.getInstrumentation();
        Preconditions.checkNotNull(instrumentation, "can not get instrumentation.");
    }

    @SuitCase
    public void testHookManager() {
        HookManager.InstrumentationHooker hookInstrumention = new HookManager.InstrumentationHooker() {
        };
        Instrumentation oldIns = HookManager.hookInstrumentation(hookInstrumention);
        Preconditions.checkNotNull(oldIns, "instrumentation should not be null.");

        AppGlobals.setInstrumentation(oldIns);

        final String message = "test hooker";
        final HookManager.CrashHooker hookerCrash = new HookManager.CrashHooker() {
            @Override
            protected boolean handleException(Thread t, Throwable e) {
                oneMsg = e.getMessage();
                return true;
            }
        };

        ThreadUtil.post(new Runnable() {
            @Override
            public void run() {
                oldOne = HookManager.hookCrash(hookerCrash);
            }
        });

        ThreadUtil.post(new Runnable() {
            @Override
            public void run() {
                throw new NullPointerException(message);
            }
        }, 500);

        ThreadUtil.post(new Runnable() {
            @Override
            public void run() {
                Thread.setDefaultUncaughtExceptionHandler(oldOne);
            }
        }, 1000);

        ThreadUtil.sleep(2000);
        Preconditions.checkArgument(message.equals(oneMsg), "can not hook crash.");
    }
}
