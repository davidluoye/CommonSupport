package com.david.demo;

import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;

import com.davidluoye.linker.BaseRemoteManager;

public class DemoManager extends BaseRemoteManager<IDemo> {

    public DemoManager(Context context) {
        super(context, "com.david.demo", "");
    }

    @Override
    protected IDemo convertService(IBinder remote) {
        return IDemo.Stub.asInterface(remote);
    }

    public String action(String message) {
        IDemo demo = getService();
        if (demo != null) {
            try {
                return demo.action(message);
            } catch (RemoteException e) {

            }
        }
        return "not get service";
    }
}
