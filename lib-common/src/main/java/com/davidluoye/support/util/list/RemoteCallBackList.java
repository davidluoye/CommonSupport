package com.davidluoye.support.util.list;

import android.os.IInterface;
import android.os.RemoteCallbackList;

import java.util.function.BiConsumer;

public class RemoteCallBackList<CALLBACK extends IInterface, COOKIE> {

    private final RemoteCallbackList<CALLBACK> cache;
    public RemoteCallBackList() {
        this.cache = new RemoteCallbackList<>();
    }

    public boolean register(CALLBACK callBack) {
        return register(callBack, null);
    }

    public boolean register(CALLBACK callBack, COOKIE cookie) {
        synchronized (this) {
            return cache.register(callBack, cookie);
        }
    }

    public boolean unRegister(CALLBACK callback) {
        synchronized (this) {
            return cache.unregister(callback);
        }
    }

    public void broadcast(BiConsumer<CALLBACK, COOKIE> consumer) {
        synchronized (this) {
            int count = cache.beginBroadcast();
            try {
                for (int index = count - 1; index >= 0; index--) {
                    CALLBACK callback = cache.getBroadcastItem(index);
                    COOKIE cookie = (COOKIE)cache.getBroadcastCookie(index);
                    consumer.accept(callback, cookie);
                }
            } finally {
                cache.finishBroadcast();
            }
        }
    }

    public boolean hasCookie(COOKIE cookie) {
        synchronized (this) {
            int count = cache.beginBroadcast();
            try {
                for (int index = count - 1; index >= 0; index--) {
                    COOKIE c = (COOKIE)(cache.getBroadcastCookie(index));
                    if (c.equals(cookie)) return true;
                }
            } finally {
                cache.finishBroadcast();
            }
        }
        return false;
    }

    public CALLBACK getCallBackFromCookie(COOKIE cookie) {
        synchronized (this) {
            int count = cache.beginBroadcast();
            try {
                for (int index = count - 1; index >= 0; index--) {
                    COOKIE c = (COOKIE)(cache.getBroadcastCookie(index));
                    if (c.equals(cookie)) return cache.getBroadcastItem(index);
                }
            } finally {
                cache.finishBroadcast();
            }
        }
        return null;
    }
}
