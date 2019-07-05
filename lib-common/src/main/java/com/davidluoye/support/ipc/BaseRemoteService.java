package com.davidluoye.support.ipc;

import android.content.Context;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.BinderThread;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.reflect.Method;
import java.util.HashMap;

public abstract class BaseRemoteService extends ContentService {
    private static final String TAG = BaseRemoteService.class.getSimpleName();

    private HashMap<String, IBinder> cache = new HashMap<>();

    @CallSuper
    @Override
    protected void onCreate(Context context) {
    }

    @CallSuper
    @Override
    protected void onDestroy() {
    }

    @Nullable
    @Override
    public final Bundle call(@NonNull String serviceName, @Nullable String callerPackage, @Nullable Bundle extras) {
        final int callerPid = Binder.getCallingPid();
        final int callerUid = Binder.getCallingUid();
        final long identity = Binder.clearCallingIdentity();
        try {
            boolean allow = allowBindService(callerPackage, callerPid, callerUid);
            if (allow) {
                return call(serviceName);
            }
        } finally {
            Binder.restoreCallingIdentity(identity);
        }
        return null;
    }

    @Nullable
    private final Bundle call(@NonNull String name) {
        IBinder service = null;
        synchronized (cache) {
            service = cache.get(name);
            if (service == null) {
                service = onBind(name);
                if (service != null) {
                    cache.put(name, service);
                }
            }
        }

        Bundle result = new Bundle();
        if (service != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                result.putBinder(ContentService.KEY_REMOTE, service);
            } else {
                try {
                    Class<?> clzz = result.getClass();
                    Method putIBinder = clzz.getDeclaredMethod("putIBinder", String.class, IBinder.class);
                    if (putIBinder != null) {
                        putIBinder.invoke(result, ContentService.KEY_REMOTE, service);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    @BinderThread
    public abstract IBinder onBind(@NonNull String name);

    public boolean allowBindService(String callerPackage, int callerPid, int callerUid) {
        return true;
    }
}
