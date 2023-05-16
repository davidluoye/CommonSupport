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
package com.davidluoye.linker;

import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.RemoteException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
 * This utils class is to register callback cache again into server which died and
 * start up again.
 *
 * If you want to know how to awake server process, see {@link RemoteServiceWakeLock}
 *
 * @param <T>
 */
public class RemoteListenerCache<T>{
    private static final String TAG = RemoteListenerCache.class.getSimpleName();

    private final List<T> mCacheCallBackList = new ArrayList<>();
    private final ProcessMonitor mMonitor;
    private final Handler mHandler;
    private BinderMonitor mBinderMonitor;
    public RemoteListenerCache(ProcessMonitor callBack){
        mMonitor = callBack;
        mHandler = new Handler(Looper.getMainLooper());
    }

    public void register(IInterface service, T callback){
        synchronized (mCacheCallBackList) {
            if (mBinderMonitor == null){
                try {
                    mBinderMonitor = new BinderMonitor(service);
                    mBinderMonitor.acquire();
                } catch (RemoteException e){
                    e.printStackTrace();
                    mBinderMonitor = null;
                }
            }
            if (!mCacheCallBackList.contains(callback)) {
                mCacheCallBackList.add(callback);
            }
        }
    }

    public void unRegister(T callback){
        synchronized (mCacheCallBackList) {
            mCacheCallBackList.remove(callback);
            if (mCacheCallBackList.isEmpty() && mBinderMonitor != null){
                mBinderMonitor.release();
                mBinderMonitor = null;
            }
        }
    }

    public List<T> getCacheCallBackList(){
        synchronized (mCacheCallBackList){
            return Collections.unmodifiableList(mCacheCallBackList);
        }
    }

    private class BinderMonitor implements IBinder.DeathRecipient {
        private final IInterface service;
        public BinderMonitor(IInterface service) {
            this.service = service;
        }

        public void acquire() throws RemoteException {
            this.service.asBinder().linkToDeath(this, 0);
        }

        public void release(){
            this.service.asBinder().unlinkToDeath(this, 0);
        }

        @Override
        public void binderDied() {
            handleProcessDied();
        }
    }

    private void handleProcessDied(){
        synchronized (mCacheCallBackList) {
            if (mBinderMonitor != null) {
                mBinderMonitor.release();
                mBinderMonitor = null;
            }
            mHandler.removeCallbacks(mBinderMonitorRunnable);
            mHandler.postDelayed(mBinderMonitorRunnable, 1000);
        }
    }

    private Runnable mBinderMonitorRunnable = new Runnable() {
        @Override
        public void run() {
            synchronized (mCacheCallBackList) {
                if (mMonitor != null) {
                    mMonitor.onProcessDied();
                }
            }
        }
    };

    public interface ProcessMonitor {
        void onProcessDied();
    }
}
