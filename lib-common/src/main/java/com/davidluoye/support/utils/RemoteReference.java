package com.davidluoye.support.utils;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

public final class RemoteReference<T extends IInterface> {

    private T value;
    private IBinder binder;

    private final Died mDied;
    public RemoteReference() {
        this(null);
    }

    public RemoteReference(T value) {
        this.mDied = new Died();
        set(value);
    }

    public void set(T value) {
        synchronized (this) {
            if (this.value != null) {
                this.binder.unlinkToDeath(mDied, 0);
                this.binder = null;
                this.value = null;
            }

            if (value != null) {
                this.binder = linkDeath(value);
                if (this.binder != null) {
                    this.value = value;
                }
            }
        }
    }

    public T get() {
        return this.value;
    }

    private IBinder linkDeath(T value) {
        IBinder binder = value.asBinder();
        try {
            binder.linkToDeath(mDied, 0);
            return binder;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class Died implements IBinder.DeathRecipient {
        @Override
        public void binderDied() {
            synchronized (this) {
                RemoteReference.this.value = null;
                RemoteReference.this.binder = null;
            }
        }
    }
}
