package com.davidluoye.support.util;

import java.lang.reflect.Method;

public class CloseGuard {

    private final Object instance;

    public CloseGuard() {
        Object object = null;
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Class<?> clazz = classLoader.loadClass("dalvik.system.CloseGuard");
            Method m = Reflect.getStaticMethod(clazz, "get", null);
            object = Reflect.callStaticMethod(m, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.instance = object;
    }

    public void open(String closer) {
        if (instance != null) {
            Reflect reflect = new Reflect(instance);
            reflect.call("open", new Class[]{String.class}, new Object[] {closer});
        }
    }

    public void close() {
        if (instance != null) {
            Reflect reflect = new Reflect(instance);
            reflect.call("close", null, null);
        }
    }

    public void warnIfOpen() {
        if (instance != null) {
            Reflect reflect = new Reflect(instance);
            reflect.call("warnIfOpen", null, null);
        }
    }
}
