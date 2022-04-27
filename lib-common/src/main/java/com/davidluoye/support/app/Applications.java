/******************************************************************************
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
 ********************************************************************************/

package com.davidluoye.support.app;

import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.UserManager;

import com.davidluoye.support.util.Reflect;

import java.lang.reflect.Method;

/**
 * Special private access for certain globals related to a process.
 */
public class Applications {

    /**
     * Return the first Application object made in the process.
     * NOTE: Only works on the main thread.
     */
    public static Application getApplication() {
        Class<?> app = getActivityThreadClass();
        Method currentApplication = Reflect.getStaticMethod(app, "currentApplication", null);
        Application application = Reflect.callStaticMethod(currentApplication, null);
        return application;
    }

    public static boolean isSystem() {
        Class<?> app = getActivityThreadClass();
        Method isSystem = Reflect.getStaticMethod(app, "isSystem", null);
        Boolean system = Reflect.callStaticMethod(isSystem, null);
        return system != null && system;
    }

    /**
     * Return the package name of the first .apk loaded into the process.
     * NOTE: Only works on the main thread.
     */
    public static String getPackageName() {
        Class<?> app = getActivityThreadClass();
        Method currentPackageName = Reflect.getStaticMethod(app, "currentPackageName", null);
        String packageName = Reflect.callStaticMethod(currentPackageName, null);
        return packageName;
    }

    public static String getOpPackageName() {
        Class<?> app = getActivityThreadClass();
        Method currentPackageName = Reflect.getStaticMethod(app, "currentOpPackageName", null);
        String packageName = Reflect.callStaticMethod(currentPackageName, null);
        return packageName;
    }

    /**
     * Return current process name.
     * @return
     */
    public static String getProcessName() {
        Class<?> app = getActivityThreadClass();
        Method currentProcessName = Reflect.getStaticMethod(app, "currentProcessName", null);
        String processName = Reflect.callStaticMethod(currentProcessName, null);
        return processName;
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
        int value = reflect.call("getIntCoreSetting", parameterType, parameter);
        return value;
    }

    public static Instrumentation getInstrumentation() {
        Object activityThreadObject = Applications.getActivityThreadInstance();
        Reflect reflect = new Reflect(activityThreadObject);
        return reflect.getField("mInstrumentation");
    }

    public static boolean setInstrumentation(Instrumentation instrumentation) {
        Object activityThreadObject = Applications.getActivityThreadInstance();
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
        Object appInstance = Reflect.callStaticMethod(currentActivityThread, null);
        return appInstance;
    }

    public static boolean canAccessStorage(Context context) {
        if (context == null) return false;
        if (context.isDeviceProtectedStorage()) return true;
        UserManager ums = context.getSystemService(UserManager.class);
        if (ums != null && ums.isUserUnlocked()) return true;
        return false;
    }
}
