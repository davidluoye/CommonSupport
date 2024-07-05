package com.davidluoye.core.utils;

import android.content.Context;

public class Classes {

    public static boolean hasClass(Context context, String name) {
        ClassLoader loader = context.getClassLoader();
        try {
            Class<?> clazz = loader.loadClass(name);
            return clazz != null;
        } catch (ClassNotFoundException e) {}
        return false;
    }

    public static Class<?> loadClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        return null;
    }

    public static Class<?> loadClass(ClassLoader loader, String className) {
        try {
            return loader.loadClass(className);
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        return null;
    }
}
