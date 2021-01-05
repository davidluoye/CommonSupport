package com.davidluoye.support.os;

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
}
