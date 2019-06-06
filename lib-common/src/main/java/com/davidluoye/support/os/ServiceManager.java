package com.davidluoye.support.os;

import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.davidluoye.support.util.IType;

import java.lang.reflect.Method;

public class ServiceManager {

    private static Class<?> sServiceManager = null;
    private static Method s_getService = null;
    private static Method s_addService = null;
    private static Method s_checkService = null;
    private static Method s_listServices = null;

    static {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            sServiceManager = Class.forName("android.os.ServiceManager", false, classLoader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ServiceManager(){}


    /**
     * Returns a reference to a service with the given name.
     *
     * <p>Important: May block the calling thread!</p>
     * @param name the name of the service to get
     * @return a reference to the service, or <code>null</code> if the service doesn't exist
     */
    @Nullable
    public static IBinder getService(@NonNull String name) {
        if (s_getService == null) {
            final String functionName = "getService";
            final Class<String>[] types = new Class[]{String.class};
            s_getService = IType.getStaticMethod(sServiceManager, functionName, types);
        }
        Object[] parameters = new Object[]{name};
        return IType.callStatic(s_getService, parameters, IBinder.class);
    }

    /**
     * Retrieve an existing service called @a name from the
     * service manager.  Non-blocking.
     */
    @Nullable
    public static IBinder checkService(@NonNull String name) {
        if (s_checkService == null) {
            final String functionName = "checkService";
            final Class<String>[] types = new Class[]{String.class};
            s_checkService = IType.getStaticMethod(sServiceManager, functionName, types);
        }
        Object[] parameters = new Object[]{name};
        return IType.callStatic(s_checkService, parameters, IBinder.class);
    }


    /**
     * Place a new @a service called @a name into the service
     * manager.
     *
     * @param name the name of the new service
     * @param service the service object
     */
    public static void addService(@NonNull String name, @NonNull IBinder service) {
        if (s_addService == null) {
            final String functionName = "checkService";
            final Class<String>[] types = new Class[]{String.class, IBinder.class};
            s_addService = IType.getStaticMethod(sServiceManager, functionName, types);
        }
        Object[] parameters = new Object[]{name, service};
        IType.callStatic(s_addService, parameters, Object.class);
    }

    /**
     * Return a list of all currently running services.
     */
    @Nullable
    public static String[] listServices() {
        if (s_listServices == null) {
            final String functionName = "listServices";
            s_listServices = IType.getStaticMethod(sServiceManager, functionName, null);
        }
        return IType.callStatic(s_listServices, null, String[].class);
    }
}
