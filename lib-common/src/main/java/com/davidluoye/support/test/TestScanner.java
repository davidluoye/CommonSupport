package com.davidluoye.support.test;

import android.content.Context;

import com.davidluoye.support.app.AppGlobals;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import dalvik.system.DexFile;
import dalvik.system.PathClassLoader;

/**
 * This is a convenient utils to scan all the test class and test case.
 * Test class is that which has annotation {@link SuitClass}
 * Test case is that which has annotation {@link SuitCase}
 */
public class TestScanner {

    public static List<Class> scanTestClass(String filterPackage) {
        List<Class> classes = new ArrayList<>();
        try {
            Thread currentThread = Thread.currentThread();
            ClassLoader classLoader = currentThread.getContextClassLoader();
            PathClassLoader pathLoader = (PathClassLoader)classLoader;

            Context ctx = AppGlobals.getApplication();
            String resourcePath = ctx.getPackageResourcePath();
            DexFile dex = new DexFile(resourcePath);

            Enumeration<String> entries = dex.entries();
            while (entries.hasMoreElements()) {
                String entryName = entries.nextElement();
                if (entryName.contains(filterPackage)) {
                    Class<?> entryClass = Class.forName(entryName, true, pathLoader);
                    SuitClass annotation = entryClass.getAnnotation(SuitClass.class);
                    if (annotation != null) {
                        classes.add(entryClass);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classes;
    }

    public static List<Method> scanTestCase(Class<?> clazz) {
        List<Method> methodList = new ArrayList<>();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            SuitCase annotation = method.getAnnotation(SuitCase.class);
            if(annotation != null) {
                methodList.add(method);
            }
        }
        return methodList;
    }
}
