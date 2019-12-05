package com.davidluoye.support.app;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Binder;

public class Permission {

    public static boolean hasPermission(String permission) {
        PackageManager pm = AppGlobals.getPackageManager();
        String pkg = AppGlobals.getPackageName();
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
        Context context = AppGlobals.getApplication();
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
}
