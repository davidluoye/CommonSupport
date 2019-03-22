package com.david.support.os;

import com.david.support.util.IType;

import java.lang.reflect.Method;

public class SystemProperties {
    private static Class<?> s_SystemProperties;

    private static Method s_get;
    private static Method s_getInt;
    private static Method s_getLong;
    private static Method s_getBoolean;

    private static Method s_set;

    static {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            s_SystemProperties = Class.forName("android.os.SystemProperties", false, classLoader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private SystemProperties() {
        // nothing to do
    }

    public static String get(String key, String def) {
        if (s_get == null) {
            final Class<String>[] types = new Class[]{String.class, String.class};
            s_get = IType.getStaticMethod(s_SystemProperties, "get", types);
        }
        Object[] parameters = new Object[]{key, def};
        return IType.callStatic(s_get, parameters, String.class);
    }

    public static int getInt(String key, int def) {
        if (s_getInt == null) {
            final Class<String>[] types = new Class[]{String.class, int.class};
            s_getInt = IType.getStaticMethod(s_SystemProperties, "getInt", types);
        }
        Object[] parameters = new Object[]{key, def};
        return IType.callStatic(s_get, parameters, int.class);
    }


    public static long getLong(String key, long def) {
        if (s_getLong == null) {
            final Class<String>[] types = new Class[]{String.class, long.class};
            s_getLong = IType.getStaticMethod(s_SystemProperties, "getLong", types);
        }
        Object[] parameters = new Object[]{key, def};
        return IType.callStatic(s_getLong, parameters, long.class);
    }

    public static boolean getBoolean(String key, boolean def) {
        if (s_getBoolean == null) {
            final Class<String>[] types = new Class[]{String.class, boolean.class};
            s_getBoolean = IType.getStaticMethod(s_SystemProperties, "getBoolean", types);
        }
        Object[] parameters = new Object[]{key, def};
        return IType.callStatic(s_getBoolean, parameters, boolean.class);
    }

    public static void set(String key, String value) {
        if (s_set == null) {
            final Class<String>[] types = new Class[]{String.class, String.class};
            s_set = IType.getStaticMethod(s_SystemProperties, "set", types);
        }
        Object[] parameters = new Object[]{key, value};
        IType.callStatic(s_set, parameters, String.class);
    }
}
