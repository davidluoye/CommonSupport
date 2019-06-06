package com.davidluoye.support.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.util.List;

public class OsUtil {
    private static final ILogger LOGGER = ILogger.logger(OsUtil.class);

    /** transform unit dp to pixel */
    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    /** transform pixel to dp */
    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    /** get screen width */
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    /** get screen height */
    public static int getScreenHeigth() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    /** get screen width */
    public static int getScreenWidth(Activity activity) {
        DisplayMetrics outMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /** check if current process is the main process. */
    public static boolean mainProcess(Context context, String processName) {
        ActivityManager ams = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        if (ams == null){
            LOGGER.e("mainProcess error, Can not find ams.");
            return false;
        }
        List<ActivityManager.RunningAppProcessInfo> apps = ams.getRunningAppProcesses();
        if (apps == null || apps.isEmpty()){
            LOGGER.e("mainProcess error, Can not find running app.");
            return false;
        }

        final int currentPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo app : apps){
            if(app.pid == currentPid){
                if (processName.equals(app.processName)){
                    return true;
                }
            }
        }
        return false;
    }

}
