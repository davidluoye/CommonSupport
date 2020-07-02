package com.davidluoye.support.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.util.DisplayMetrics;

import com.davidluoye.support.log.ILogger;

import java.util.List;

public class OsUtil {
    private static final ILogger LOGGER = ILogger.logger(OsUtil.class);

    public static final String SCHEME_ANDROID_RESOURCE = "android.resource";

    public static int dp2px(int dp) {
        return dp2px(Resources.getSystem(), dp);
    }

    public static int px2dp(int px) {
        return px2dp(Resources.getSystem(), px);
    }

    public static int px2sp(int px) {
        return px2sp(Resources.getSystem(), px);
    }

    public static int sp2px(int sp) {
        return sp2px(Resources.getSystem(), sp);
    }

    public static int dp2px(Resources res, int dp) {
        return (int) (dp * res.getDisplayMetrics().density + 0.5f);
    }

    public static int px2dp(Resources res, int px) {
        return (int) (px / res.getDisplayMetrics().density + 0.5f);
    }

    public static int px2sp(Resources res, int px) {
        return (int) (px / res.getDisplayMetrics().scaledDensity + 0.5f);
    }

    public static int sp2px(Resources res, int sp) {
        return (int) (sp * res.getDisplayMetrics().scaledDensity + 0.5f);
    }

    /** get screen width */
    public static int getScreenWidth() {
        return getScreenWidth(Resources.getSystem());
    }

    /** get screen height */
    public static int getScreenHeigth() {
        return getScreenHeigth(Resources.getSystem());
    }

    public static int getScreenWidth(Resources res) {
        return res.getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeigth(Resources res) {
        return res.getDisplayMetrics().heightPixels;
    }

    /** get screen width */
    public static int getScreenWidth(Activity activity) {
        DisplayMetrics outMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    public static Uri fromResourceId(String pkg, int resId) {
        Uri.Builder ub = new Uri.Builder();
        ub.scheme(SCHEME_ANDROID_RESOURCE);
        ub.authority(pkg);
        ub.appendPath(String.valueOf(resId));
        return ub.build();
    }

    public static Uri fromResourceName(String pkg, String type, String name) {
        Uri.Builder ub = new Uri.Builder();
        ub.scheme(SCHEME_ANDROID_RESOURCE);
        ub.authority(pkg);
        ub.appendPath(type);
        ub.appendPath(name);
        return ub.build();
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
