/*
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
 */

package com.davidluoye.core.os;

import android.util.ArrayMap;

/**
 * This class is used in a similar way as ServiceManager, except the services registered here
 * are not Binder objects and are only available in the same process.
 *
 * Once all services are converted to the SystemService interface, this class can be absorbed
 * into SystemServiceManager.
 */
public final class LocalServices {
    private LocalServices() {}

    private static final ArrayMap<String, Object> sLocalServiceObjects = new ArrayMap<>();

    /**
     * Returns a local service instance that implements the specified interface.
     *
     * @param type The type of service.
     * @return The service object.
     */
    public static <T> T getService(Class<T> type) {
        return (T)getService(type.getName());
    }

    /**
     * Returns a local service instance that implements the specified interface.
     *
     * @param name The name of service.
     * @return The service object.
     */
    public static <T> T getService(String name) {
        synchronized (sLocalServiceObjects) {
            return (T) sLocalServiceObjects.get(name);
        }
    }

    /**
     * Adds a service instance of the specified interface to the global registry of local services.
     */
    public static <T> void addService(Class<T> type, T service) {
        addService(type.getName(), service);
    }

    /**
     * Adds a service instance of the specified interface to the global registry of local services.
     */
    public static <T> void addService(String name, T service) {
        synchronized (sLocalServiceObjects) {
            if (sLocalServiceObjects.containsKey(name)) {
                throw new IllegalStateException("Overriding service registration");
            }
            sLocalServiceObjects.put(name, service);
        }
    }

    public static <T> T remove(Class<T> type) {
        return remove(type.getName());
    }

    public static <T> T remove(String name) {
        synchronized (sLocalServiceObjects) {
            return (T)sLocalServiceObjects.remove(name);
        }
    }

    public static void clean() {
        synchronized (sLocalServiceObjects) {
            sLocalServiceObjects.clear();
        }
    }
}
