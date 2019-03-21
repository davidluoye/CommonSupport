
package com.david.support.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class IType {
    private Object mInstance;

    public IType(Object instance) {
        mInstance = instance;
    }

    /** reflect a field and get the value. */
    public <T> T getField(String name, Class<T> type) {
        try {
            Field f = mInstance.getClass().getDeclaredField(name);
            if (f != null) {
                f.setAccessible(true);
                Object value = f.get(mInstance);
                return translate(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /** set a class member by reflect */
    public boolean setField(String name, Object value) {
        try {
            Field f = mInstance.getClass().getDeclaredField(name);
            if (f != null) {
                f.setAccessible(true);
                f.set(mInstance, value);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /** reflect a function and get the return value. */
    public <T> T call(String name, Class<?>[] parameterTypes, Object[] parameters, Class<T> returnType) {
        Class<?> clzz = mInstance.getClass();
        try {
            Method m = clzz.getDeclaredMethod(name, parameterTypes);
            if (m != null) {
                m.setAccessible(true);
                Object value = m.invoke(mInstance, parameters);
                return translate(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /** reflect an annotation. */
    public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
        Class<?> clazz = mInstance.getClass();
        Object value = clazz.getAnnotation(annotationType);
        return translate(value);
    }

    /** reflect a static field. */
    public static <T> T getStaticField(Class<?> clzz, String name, Class<T> type){
        try {
            Field f = clzz.getDeclaredField(name);
            if (f != null){
                f.setAccessible(true);
                return translate(f.get(null));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /** for translate object translate a special type. */
    public static <T extends Object> T translate(Object obj) {
        if (obj == null) {
            return null;
        }
        return (T)obj;
    }
}
