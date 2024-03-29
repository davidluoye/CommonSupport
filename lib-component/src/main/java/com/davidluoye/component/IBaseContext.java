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
package com.davidluoye.component;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

public abstract class IBaseContext implements IContext {
    private final Context mContext;
    private final Handler mUiHandler;
    public IBaseContext(Context context) {
        mContext = context;
        if(Looper.myLooper() != getMainLooper()) {
            throw new IllegalStateException("Should only be create in main thread.");
        }
        mUiHandler = new Handler(getMainLooper());
    }

    @Override
    public final Context getContext() {
        return mContext;
    }

    @Override
    public final Context getApplicationContext() {
        return mContext.getApplicationContext();
    }

    @Override
    public final Object getSystemService(String name) {
        return mContext.getSystemService(name);
    }

    @Override
    public final Resources getResources() {
        return mContext.getResources();
    }

    @Override
    public String getPackageName() {
        return mContext.getPackageName();
    }

    @Override
    public final PackageManager getPackageManager() {
        return mContext.getPackageManager();
    }

    @Override
    public final ContentResolver getContentResolver() {
        return mContext.getContentResolver();
    }

    @Override
    public final Looper getMainLooper() {
        return mContext.getMainLooper();
    }

    @Override
    public final CharSequence getText(int resId) {
        return mContext.getText(resId);
    }

    @Override
    public final String getString(int resId) {
        return mContext.getString(resId);
    }

    @Override
    public final String getString(int resId, Object... formatArgs) {
        return mContext.getString(resId, formatArgs);
    }

    @Override
    public String[] getStringArray(int id) {
        return getResources().getStringArray(id);
    }

    @Override
    public final Drawable getDrawable(int id) {
        return mContext.getDrawable(id);
    }

    @Override
    public final void setTheme(int resid) {
        mContext.setTheme(resid);
    }

    @Override
    public void runOnUiThread(Runnable action) {
        mUiHandler.post(action);
    }

    @Override
    public void runOnUiThread(Runnable action, long delayMillis) {
        mUiHandler.postDelayed(action, delayMillis);
    }

    @Override
    public final void startActivity(Intent intent) {
        mContext.startActivity(intent);
    }

    @Override
    public final void startActivity(Intent intent, Bundle options) {
        mContext.startActivity(intent, options);
    }

    @Override
    public final ComponentName startService(Intent service) {
        return mContext.startService(service);
    }

    public final boolean stopService(Intent service) {
        return mContext.stopService(service);
    }

    @Override
    public final boolean bindService(Intent service, ServiceConnection conn, int flags) {
        return mContext.bindService(service, conn, flags);
    }

    @Override
    public final void unbindService(ServiceConnection conn) {
        mContext.unbindService(conn);
    }

    @Override
    public final void sendBroadcast(Intent intent) {
        mContext.sendBroadcast(intent);
    }

    @Override
    public final void sendBroadcast(Intent intent, String receiverPermission) {
        mContext.sendBroadcast(intent, receiverPermission);
    }

    @Override
    public final void sendBroadcast(Intent intent, String receiverPermission, int appOp) {
//        mContext.sendBroadcast(intent, receiverPermission, appOp);
    }

    @Override
    public final Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        return mContext.registerReceiver(receiver, filter);
    }

    @Override
    public final Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter,
            String broadcastPermission, Handler scheduler) {
        return mContext.registerReceiver(receiver, filter, broadcastPermission, scheduler);
    }

    @Override
    public final void unregisterReceiver(BroadcastReceiver receiver) {
        mContext.unregisterReceiver(receiver);
    }
}
