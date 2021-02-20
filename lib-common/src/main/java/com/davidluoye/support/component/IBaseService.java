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

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.IBinder;

public abstract class IBaseService extends IBaseContext implements IService {
    protected final Service mService;

    public IBaseService(Service service) {
        super(service);
        mService = service;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onRebind(Intent intent) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onLowMemory() {
        // TODO Auto-generated method stub
    }

    @Override
    public void onTrimMemory(int level) {
        // TODO Auto-generated method stub
    }

}
