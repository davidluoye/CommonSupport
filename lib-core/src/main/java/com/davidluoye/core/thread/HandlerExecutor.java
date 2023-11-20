package com.davidluoye.core.thread;

import android.os.Handler;
import android.os.Looper;

import com.davidluoye.core.utils.Preconditions;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;

public class HandlerExecutor implements Executor {
    private final Handler mHandler;

    public HandlerExecutor(Looper looper) {
        mHandler = Preconditions.checkNotNull(new Handler(looper));
    }

    public HandlerExecutor(Handler handler) {
        mHandler = Preconditions.checkNotNull(handler);
    }

    @Override
    public void execute(Runnable command) {
        if (!mHandler.post(command)) {
            throw new RejectedExecutionException(mHandler + " is shutting down");
        }
    }
}
