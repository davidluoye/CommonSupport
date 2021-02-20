/******************************************************************************
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
 ********************************************************************************/
package com.davidluoye.support.component;

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

public interface IContext {

    Context getContext();

    Context getApplicationContext();

    Object getSystemService(String name);

    Resources getResources();

    String getPackageName();

    PackageManager getPackageManager();

    ContentResolver getContentResolver();

    Looper getMainLooper();

    CharSequence getText(int resId);

    String getString(int resId);

    String getString(int resId, Object... formatArgs);

    String[] getStringArray(int id);

    Drawable getDrawable(int id);

    void setTheme(int resid);

    void runOnUiThread(Runnable action);

    void runOnUiThread(Runnable action, long delayMillis);

    void runOnBgThread(Runnable action);

    void runOnBgThread(Runnable action, long delayMillis);

    void startActivity(Intent intent);

    void startActivity(Intent intent, Bundle options);

    ComponentName startService(Intent service);

    boolean stopService(Intent service);

    boolean bindService(Intent service, ServiceConnection conn, int flags);

    void unbindService(ServiceConnection conn);

    void sendBroadcast(Intent intent);

    void sendBroadcast(Intent intent, String receiverPermission);

    void sendBroadcast(Intent intent, String receiverPermission, int appOp);

    Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter);

    Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter,
                            String broadcastPermission, Handler scheduler);

    void unregisterReceiver(BroadcastReceiver receiver);
}
