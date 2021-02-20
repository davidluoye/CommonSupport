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
package com.davidluoye.support.os;

import com.davidluoye.support.util.Reflect;

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
            s_get = Reflect.getStaticMethod(s_SystemProperties, "get", types);
        }
        Object[] parameters = new Object[]{key, def};
        return Reflect.callStatic(s_get, parameters, String.class);
    }

    public static int getInt(String key, int def) {
        if (s_getInt == null) {
            final Class<String>[] types = new Class[]{String.class, int.class};
            s_getInt = Reflect.getStaticMethod(s_SystemProperties, "getInt", types);
        }
        Object[] parameters = new Object[]{key, def};
        return Reflect.callStatic(s_get, parameters, int.class);
    }


    public static long getLong(String key, long def) {
        if (s_getLong == null) {
            final Class<String>[] types = new Class[]{String.class, long.class};
            s_getLong = Reflect.getStaticMethod(s_SystemProperties, "getLong", types);
        }
        Object[] parameters = new Object[]{key, def};
        return Reflect.callStatic(s_getLong, parameters, long.class);
    }

    public static boolean getBoolean(String key, boolean def) {
        if (s_getBoolean == null) {
            final Class<String>[] types = new Class[]{String.class, boolean.class};
            s_getBoolean = Reflect.getStaticMethod(s_SystemProperties, "getBoolean", types);
        }
        Object[] parameters = new Object[]{key, def};
        return Reflect.callStatic(s_getBoolean, parameters, boolean.class);
    }

    public static void set(String key, String value) {
        if (s_set == null) {
            final Class<String>[] types = new Class[]{String.class, String.class};
            s_set = Reflect.getStaticMethod(s_SystemProperties, "set", types);
        }
        Object[] parameters = new Object[]{key, value};
        Reflect.callStatic(s_set, parameters, String.class);
    }
}
