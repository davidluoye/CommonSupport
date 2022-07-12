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

import android.content.Context;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;

import java.lang.reflect.Method;
import java.util.HashMap;

public abstract class BaseRemoteService extends ContentService {
    private static final String TAG = BaseRemoteService.class.getSimpleName();

    private HashMap<String, IBinder> cache = new HashMap<>();

    @Override
    protected void onCreate(Context context) {
    }

    @Override
    protected void onDestroy() {
    }

    @Override
    protected final Bundle call(String serviceName, String callerPackage) {
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

    protected abstract IBinder onBind(String name);

    protected boolean allowBindService(String callerPackage, int callerPid, int callerUid) {
        return true;
    }
}
