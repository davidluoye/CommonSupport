package com.david.remoteservice;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.util.Log;

import java.lang.ref.SoftReference;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

public abstract class BaseRemoteManager<T extends IInterface> {
    private static final String TAG = BaseRemoteManager.class.getSimpleName();

    private final RemoteLinker remoteLiker;
    private final Context context;
    private final String authority;
    private final String name;
    private T service;

    protected BaseRemoteManager(@NonNull String authority) {
        this(null, authority, null);
    }

    /**
     * @param authority the authority declare in AndroidManifest.xml
     * @param name the name of service if it has, null default
     */
    protected BaseRemoteManager(@NonNull String authority, String name) {
        this(null, authority, name);
    }

    /**
     * @param context
     * @param authority the authority declare in AndroidManifest.xml
     * @param name the name of service service
     */
    protected BaseRemoteManager(Context context, @NonNull String authority, String name) {
        if (context == null) {
            Application app = invoke("currentApplication");
            context = app;
        }
        this.context = context.getApplicationContext();
        this.authority = authority;
        this.name = name;
        this.remoteLiker = new RemoteLinker(this);
    }

    private static <T extends Type> T invoke(String function) {
        try {
            Class<?> activityThread = Class.forName("android.app.ActivityThread");
            Method method = activityThread.getDeclaredMethod(function);
            method.setAccessible(true);
            return (T)method.invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class RemoteLinker implements IBinder.DeathRecipient {
        private SoftReference<BaseRemoteManager> manager;
        RemoteLinker(BaseRemoteManager manager) {
            this.manager = new SoftReference<>(manager);
        }

        public void binderLink(IBinder remote){
            try {
                remote.linkToDeath(this, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        public void binderUnlink(IBinder remote){
            remote.unlinkToDeath(this, 0);
        }

        @Override
        public void binderDied() {
            BaseRemoteManager m = manager.get();
            if (m != null) {
                m.remoteServiceDied();
            }
        }
    }

    private final void remoteServiceDied() {
        synchronized (this) {
            if (service != null) {
                Log.d(TAG, String.format("service{%s,%s} has died.", authority, name));
                IBinder remote = service.asBinder();
                remoteLiker.binderUnlink(remote);
                service = null;
            }
        }
    }

    /**
     * Get remote service binder form other application no matter remote process
     * alive or died. If remote process does not exist, this caller can start it.
     *
     * @return
     */
    private final IBinder bindService() {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.parse("content://" + authority);
        Bundle bundle = null;

        try {
            String method = name == null ? "" : name;
            bundle = cr.call(uri, method, null, null);
        } catch (IllegalArgumentException e) {
            final String msg = "Can not find authority:" + authority;
            if (!handleAuthorityNotFoundException(msg)) {
                throw new IllegalArgumentException(msg);
            }
        }

        if (bundle != null) {
            Object remote = bundle.get(ContentService.KEY_REMOTE);
            if (remote == null) {
                final String msg = "Can not find service:" + name;
                if (!handleServiceNotFoundException(msg)) {
                    throw new IllegalArgumentException(msg);
                }
            }
            return (IBinder) remote;
        }
        return null;
    }

    /**
     * Get service binder form other application.
     * @return
     */
    public final T getService() {
        synchronized (this) {
            if (service == null) {
                IBinder remote = bindService();
                if (remote != null) {
                    remoteLiker.binderLink(remote);
                    service = convertService(remote);
                } else {
                    Log.e(TAG, String.format("can not find RemoteService by authority %s, " +
                            "have you register it in AndroidManifest ?", authority));
                }
            }
            return service;
        }
    }

    protected abstract T convertService(IBinder remote);

    protected boolean handleAuthorityNotFoundException(String msg) {
        return true;
    }

    protected boolean handleServiceNotFoundException(String msg) {
        return true;
    }
}
