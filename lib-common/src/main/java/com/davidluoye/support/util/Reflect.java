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
package com.davidluoye.support.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Reflect {
    private Object mInstance;

    public Reflect(Object instance) {
        mInstance = instance;
    }

    public <T> T getField(String name) {
        return getField(mInstance.getClass(), name);
    }

    public boolean setField(String name, Object value) {
        return setField(mInstance.getClass(), name, value);
    }

    /** reflect a field and get the value. */
    public <T> T getField(Class<?> ownerClazz, String name) {
        try {
            Field f = ownerClazz.getDeclaredField(name);
            f.setAccessible(true);
            Object value = f.get(mInstance);
            return translate(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /** set a class member by reflect */
    public boolean setField(Class<?> ownerClazz, String name, Object value) {
        try {
            Field f = ownerClazz.getDeclaredField(name);
            f.setAccessible(true);
            f.set(mInstance, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /** reflect a function and get the return value. */
    public <T> T call(String name, Class<?>[] parameterTypes, Object[] parameters) {
        Class<?> ownerClazz = mInstance.getClass();
        return call(ownerClazz, name, parameterTypes, parameters);
    }

    /** reflect a function and get the return value. */
    public <T> T call(Class<?> ownerClazz, String name, Class<?>[] parameterTypes, Object[] parameters) {
        try {
            Method m = ownerClazz.getDeclaredMethod(name, parameterTypes);
            m.setAccessible(true);
            Object value = m.invoke(mInstance, parameters);
            return translate(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /** reflect an annotation. */
    public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
        return getAnnotation(mInstance.getClass(), annotationType);
    }

    public <T extends Annotation> T getAnnotation(Class<?> ownerClazz, Class<T> annotationType) {
        Object value = ownerClazz.getAnnotation(annotationType);
        return translate(value);
    }

    /** reflect a static field. */
    public static <T> T getStaticField(Class<?> ownerClazz, String name){
        try {
            Field f = ownerClazz.getDeclaredField(name);
            f.setAccessible(true);
            return translate(f.get(null));
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static Method getStaticMethod(Class<?> ownerClazz, String name, Class<?>[] parameterTypes) {
        try {
            Method m = ownerClazz.getDeclaredMethod(name, parameterTypes);
            m.setAccessible(true);
            return m;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /** reflect a static function and get the return value. */
    public static <T> T callStaticMethod(Method method, Object[] parameters) {
        try {
            if (method != null) {
                method.setAccessible(true);
                Object value = method.invoke(null, parameters);
                return translate(value);
            }
        } catch (Exception e) {
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
