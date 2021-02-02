package com.davidluoye.support.app;

import android.app.Application;
import android.app.Instrumentation;
import android.content.pm.PackageManager;

import com.davidluoye.support.util.Reflect;

import java.lang.reflect.Method;

/**
 * Special private access for certain globals related to a process.
 */
public class AppGlobals {

    /**
     * Return the first Application object made in the process.
     * NOTE: Only works on the main thread.
     */
    public static Application getApplication() {
        Class<?> app = getActivityThreadClass();
        Method currentApplication = Reflect.getStaticMethod(app, "currentApplication", null);
        Application application = Reflect.callStatic(currentApplication, null, Application.class);
        return application;
    }

    /**
     * Return the package name of the first .apk loaded into the process.
     * NOTE: Only works on the main thread.
     */
    public static String getPackageName() {
        Class<?> app = getActivityThreadClass();
        Method currentPackageName = Reflect.getStaticMethod(app, "currentPackageName", null);
        String packageName = Reflect.callStatic(currentPackageName, null, String.class);
        return packageName;
    }

    /**
     * Return the raw interface to the package manager.
     * @return The package manager.
     */
    public static PackageManager getPackageManager() {
        Application app = getApplication();
        if (app != null) {
            return app.getPackageManager();
        }
        return null;
    }

    /**
     * Gets the value of an integer core setting.
     *
     * @param key The setting key.
     * @param defaultValue The setting default value.
     * @return The core settings.
     */
    public static int getIntCoreSetting(String key, int defaultValue) {
        Object appInstance = getActivityThreadInstance();
        if (appInstance == null) {
            return defaultValue;
        }

        Class<?>[] parameterType = new Class[] {String.class, int.class};
        Object[] parameter = new Object[]{key, defaultValue};

        Reflect reflect = new Reflect(appInstance);
        int value = reflect.call("getIntCoreSetting", parameterType, parameter, int.class);
        return value;
    }

    public static Instrumentation getInstrumentation() {
        Object activityThreadObject = AppGlobals.getActivityThreadInstance();
        Reflect reflect = new Reflect(activityThreadObject);
        return reflect.getField("mInstrumentation");
    }

    public static boolean setInstrumentation(Instrumentation instrumentation) {
        Object activityThreadObject = AppGlobals.getActivityThreadInstance();
        Reflect reflect = new Reflect(activityThreadObject);
        return reflect.setField("mInstrumentation", instrumentation);
    }

    public static Class<?> getActivityThreadClass() {
        try {
             return Class.forName("android.app.ActivityThread");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getActivityThreadInstance() {
        Class<?> app = getActivityThreadClass();
        Method currentActivityThread = Reflect.getStaticMethod(app, "currentActivityThread", null);
        Object appInstance = Reflect.callStatic(currentActivityThread, null, app);
        return appInstance;
    }
}
