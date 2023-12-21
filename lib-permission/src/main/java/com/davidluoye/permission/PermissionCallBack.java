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
package com.davidluoye.permission;

import java.util.ArrayList;
import java.util.List;

public abstract class PermissionCallBack extends IPermissionCallBack.Stub {
    public final String[] permissions;
    public PermissionCallBack(String[] permissions) {
        this.permissions = permissions;
    }

    @Override
    public final void onCallBack(List<String> grantedPermission) {
        List<String> deniedPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (grantedPermission.contains(permission)) continue;
            deniedPermissions.add(permission);
        }
        onGranted(grantedPermission.toArray(new String[0]), deniedPermissions.toArray(new String[0]));
    }

    public void onGranted(String[] grantedPermissions, String[] deniedPermissions) {

    }
}
