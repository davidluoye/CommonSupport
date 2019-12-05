
package com.davidluoye.support.component;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.IBinder;

public interface IService {

    IBinder onBind(Intent intent);

    void onRebind(Intent intent);

    boolean onUnbind(Intent intent);

    void onCreate();

    int onStartCommand(Intent intent, int flags, int startId);

    void onDestroy();

    void onConfigurationChanged(Configuration newConfig);

    void onLowMemory();

    void onTrimMemory(int level);
}
