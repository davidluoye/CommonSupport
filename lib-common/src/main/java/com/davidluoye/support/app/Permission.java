package com.davidluoye.support.app;

import android.content.pm.PackageManager;

public class Permission {

    public static boolean hasPermission(String permission) {
        PackageManager pm = AppGlobals.getPackageManager();
        String pkg = AppGlobals.getPackageName();
        int gain = pm.checkPermission(permission, pkg);
        return gain == PackageManager.PERMISSION_GRANTED;
    }
}
