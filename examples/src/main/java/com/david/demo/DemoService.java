package com.david.demo;

import android.content.Context;
import android.os.IBinder;
import android.util.Log;

import com.davidluoye.support.link.BaseRemoteService;

public class DemoService extends BaseRemoteService {

    @Override
    protected void onCreate(Context context) {
        super.onCreate(context);
    }

    @Override
    public IBinder onBind(String name) {
        return mBinder;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private IDemo.Stub mBinder = new IDemo.Stub() {
        @Override
        public String action(String message) {
            Log.d("david", "Service has received message:" + message);
            return "We had already receiver message: " + message;
        }
    };
}
