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
package com.davidluoye.core.app;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Bundle;

import com.davidluoye.core.utils.PermissionCallBack;

import java.util.ArrayList;
import java.util.List;

public class Permission {

    public static final String KEY_EXTRA = "intent-extra";
    public static final String KEY_PERMISSION = "key-permission";
    public static final String KEY_CALLBACK = "key-callback";

    public static boolean hasPermission(String permission) {
        PackageManager pm = Applications.getPackageManager();
        String pkg = Applications.getPackageName();
        int gain = pm.checkPermission(permission, pkg);
        return gain == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Determine whether the given permission is allowed for a particular
     * process and user ID running in the system.
     *
     * @param permission The name of the permission being checked.
     * @param pid The process ID being checked against.  Must be > 0.
     * @param uid The user ID being checked against.
     * <p> A uid of 0 is the root user, which will pass every permission check. </p>
     *
     * @return {@link PackageManager#PERMISSION_GRANTED} if the given
     *       pid/uid is allowed that permission, or
     *      {@link PackageManager#PERMISSION_DENIED} if it is not.
     */
    public static int checkPermission(String permission, int pid, int uid) {
        Context context = Applications.getApplication();
        return context.checkPermission(permission, pid, uid);
    }

    /**
     * Determine whether the calling process of an IPC you are handling has been
     * granted a particular permission.  This is basically the same as calling
     * {@link #checkPermission(String, int, int)} with the pid and uid returned
     * by {@link android.os.Binder#getCallingPid} and
     * {@link android.os.Binder#getCallingUid}.  One important difference
     * is that if you are not currently processing an IPC, this function
     * will always fail.  This is done to protect against accidentally
     * leaking permissions;
     *
     * @param permission The name of the permission being checked.
     *
     * @return {@link PackageManager#PERMISSION_GRANTED} if the calling
     * pid/uid is allowed that permission, or
     * {@link PackageManager#PERMISSION_DENIED} if it is not.
     *
     * @see PackageManager#checkPermission(String, String)
     * @see #checkPermission
     */
    public static int checkCallingPermission(String permission) {
        final int pid = Binder.getCallingPid();
        final int uid = Binder.getCallingUid();
        return checkPermission(permission, pid, uid);
    }

    /**
     * Determine whether <em>you</em> have been granted a particular permission.
     *
     * @param permission The name of the permission being checked.
     *
     * @return {@link PackageManager#PERMISSION_GRANTED} if you have the
     * permission, or {@link PackageManager#PERMISSION_DENIED} if not.
     *
     * @see PackageManager#checkPermission(String, String)
     * @see #checkCallingPermission(String)
     */
    public static int checkSelfPermission(String permission) {
        final int myPid = android.os.Process.myPid();
        final int myUid = android.os.Process.myUid();
        return checkPermission(permission, myPid, myUid);
    }

    /**
     * Get the denied permissions
     *
     * @param permissions The name of the permissions being checked.
     * @return an array of permissions which has been denied.
     */
    public static String[] getDeniedPermissions(String[] permissions) {
        List<String> needRequest = new ArrayList<>();
        for (int index = 0; index < permissions.length; index++) {
            int gain = checkSelfPermission(permissions[index]);
            if (gain != PackageManager.PERMISSION_GRANTED) {
                needRequest.add(permissions[index]);
            }
        }
        return needRequest.toArray(new String[]{});
    }

    /**
     * Start a permission activity
     * @param context
     * @param permissions request permission array.
     */
    public static void requestPermission(Context context, String[] permissions) {
        requestPermission(context, new PermissionCallBack(permissions){});
    }

    /**
     * Start a permission activity
     * @param context
     * @param callback permission state changed callback
     */
    public static void requestPermission(Context context, PermissionCallBack callback) {
        String[] permissions = getDeniedPermissions(callback.permissions);
        if (permissions.length <= 0) {
            callback.onGranted(new String[0], permissions);
            return;
        }

        Intent intent = new Intent(context, PermissionUI.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle extra = new Bundle();
        extra.putStringArray(KEY_PERMISSION, permissions);
        extra.putBinder(KEY_CALLBACK, callback);
        intent.putExtra(KEY_EXTRA, extra);
        context.startActivity(intent);
    }
}
