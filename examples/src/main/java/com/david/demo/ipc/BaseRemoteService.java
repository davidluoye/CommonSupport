package com.david.demo.ipc;

import android.content.Context;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;

import com.davidluoye.support.annotation.CallSuper;

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

    @Override
    public final Bundle call(String serviceName, String callerPackage, Bundle extras) {
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

    private final Bundle call(String name) {
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

    public abstract IBinder onBind(String name);

    public boolean allowBindService(String callerPackage, int callerPid, int callerUid) {
        return true;
    }
}
