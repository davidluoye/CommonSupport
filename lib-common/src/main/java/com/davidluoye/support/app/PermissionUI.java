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
package com.davidluoye.support.app;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;

import com.davidluoye.support.IPermissionCallBack;
import com.davidluoye.support.log.ILogger;

import java.util.ArrayList;
import java.util.List;

public class PermissionUI extends Activity {
    private static final ILogger LOGGER = ILogger.logger(PermissionUI.class);

    private static final int REQUEST_PERMISSION_CODE = 0x0010;

    private IPermissionCallBack mCallBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null) {
            Bundle extra = intent.getBundleExtra(Permission.KEY_EXTRA);
            if (extra != null) {
                String[] permissions = extra.getStringArray(Permission.KEY_PERMISSION);
                IBinder callback = extra.getBinder(Permission.KEY_CALLBACK);
                mCallBack = IPermissionCallBack.Stub.asInterface(callback);
                if (permissions != null && permissions.length > 0) {
                    requestPermissions(permissions, REQUEST_PERMISSION_CODE);
                    return;
                }
            }
        }
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_CODE) {
            List<String> grantedPermission = new ArrayList<>();
            for (int index = 0; index < permissions.length; index++) {
                if (grantResults[index] == PackageManager.PERMISSION_GRANTED) {
                    grantedPermission.add(permissions[index]);
                } else {
                    LOGGER.e("request %s fail.", permissions[index]);
                }
            }

            if (mCallBack != null) {
                try {
                    mCallBack.onCallBack(grantedPermission);
                } catch (Exception e){}
            }
        }
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
