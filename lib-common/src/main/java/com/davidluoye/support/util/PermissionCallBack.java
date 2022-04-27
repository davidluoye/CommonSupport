package com.davidluoye.support.util;

import com.davidluoye.support.IPermissionCallBack;

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
