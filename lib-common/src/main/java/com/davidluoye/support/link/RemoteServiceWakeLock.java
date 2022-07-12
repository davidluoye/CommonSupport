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
package com.davidluoye.support.link;

import android.content.ContentResolver;
import android.content.Context;
import android.os.IInterface;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * It is a convenient utils for communicating between two application processes.
 *
 * In normal binder is always be used between two processes communicate and the
 * one is server and another is client. The two processes both can exit in any
 * time in normal, and it may be caused by crash or killed by lmkd or ams and so
 * on.
 *
 * But when client process register a callback into server process in order to
 * listen some states or events, and then server process exit, the client should
 * wake up the server right now and register the callback again. This is what this
 * class utils do.
 *
 * This utils class is to awake server process again.
 *
 * If you want to know how to register the callback cache, see {@link RemoteListenerCache}
 *
 */
public class RemoteServiceWakeLock {

    private static final String TAG = RemoteServiceWakeLock.class.getSimpleName();
    private static final String REMOTE_CLASS = "android.content.IContentProvider";
    private static final String ACQUIRE_NAME = "acquireProvider";
    private static final String RELEASE_NAME = "releaseProvider";

    private final String mProviderAuthor;
    private final Context mContext;
    private Class<?> mRemoteType;
    private RemoteProvider mCaller;

    public RemoteServiceWakeLock(Context context, String author, RemoteProvider caller){
        this.mContext = context.getApplicationContext();
        this.mCaller = caller;
        this.mProviderAuthor = author;
        try {
            mRemoteType = Class.forName(REMOTE_CLASS);
        } catch (ClassNotFoundException e){
            e.printStackTrace();
            mRemoteType = null;
        }
    }

    public IInterface getRemoteService() {
        IInterface mRemote = mCaller.getRemoteService();
        if (mRemote == null) {
            IInterface binder = null;
            try {
                binder = acquireProvider(mContext, mProviderAuthor);
                if (binder != null) {
                    mRemote = mCaller.getRemoteService();
                } else {
                    Log.e(TAG,"Can not find provider " + mProviderAuthor);
                }
            } finally {
                if (binder != null) {
                    releaseProvider(mContext, binder);
                }
            }
        }
        return mRemote;
    }

    private IInterface acquireProvider(Context context, String author){
        ContentResolver cr = context.getContentResolver();
        Class<?>[] pTypes = new Class<?>[]{Context.class, String.class};
        Object[] param = new Object[]{context, author};
        Object result = call(cr, ACQUIRE_NAME, pTypes, param);
        if (result != null && result instanceof IInterface){
            return (IInterface)result;
        }
        return null;
    }

    private boolean releaseProvider(Context context, Object ipc){
        ContentResolver cr = context.getContentResolver();
        Class<?>[] pTypes = new Class<?>[]{mRemoteType};
        Object[] param = new Object[]{ipc};
        Object resultObj = call(cr, RELEASE_NAME, pTypes, param);
        if (resultObj != null && resultObj instanceof Boolean){
            return (Boolean)resultObj;
        }
        return false;
    }

    private Object call(Object obj, String name, Class<?>[] parameterTypes, Object[] parameters) {
        Class<?> clzz = obj.getClass();
        try {
            Method m = clzz.getDeclaredMethod(name, parameterTypes);
            if (m != null) {
                m.setAccessible(true);
                Object r = m.invoke(obj, parameters);
                return r;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public interface RemoteProvider {
        IInterface getRemoteService();
    }
}
