package com.davidluoye.support.util;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.MessageQueue;

public class MessageUtil {

    /**
     * Wait util the application's main thread goes idle.
     */
    public void waitForIdle() {
        waitForIdle(null);
    }

    /**
     * Schedule a callback for when the application's main thread goes idle.
     *
     * @param callback Called when the thread's message queue is idle.
     */
    public static void waitForIdle(Runnable callback) {
        Looper looper = Looper.getMainLooper();
        if (looper == Looper.myLooper()) {
            throw new IllegalStateException("Can not be called in main thread.");
        }
        waitForIdle(looper, callback);
    }

    /**
     * Schedule a callback for when the target thread goes idle.
     *
     * @param handler Handler in target thread.
     * @param callback Called when the thread's message queue is idle.
     */
    public static void waitForIdle(Handler handler, Runnable callback) {
        Preconditions.checkNotNull(handler, "Handler should not be null.");
        Looper looper = handler.getLooper();
        waitForIdle(looper, callback);
    }

    /**
     * Schedule a callback for when the target thread goes idle.
     *
     * @param looper Looper in target thread.
     * @param callback Called when the thread's message queue is idle.
     */
    public static void waitForIdle(Looper looper, Runnable callback) {
        Preconditions.checkNotNull(looper, "Looper should not be null.");
        final ILooper iLooper = new ILooper(looper);
        final MessageQueue queue = iLooper.getQueue();
        waitForIdle(queue, callback);
    }

    /**
     * Schedule a callback for when the target thread goes idle.
     *
     * @param queue MessageQueue in target thread.
     * @param callback Called when the thread's message queue is idle.
     */
    public static void waitForIdle(MessageQueue queue, Runnable callback) {
        Preconditions.checkNotNull(queue, "MessageQueue should not be null.");
        final MessageQueue currentQueue = ILooper.myMessageQueue();
        if (currentQueue == queue) {
            throw new IllegalStateException("Can not call this function in same thread.");
        }

        Idler idler = new Idler(callback);
        queue.addIdleHandler(idler);
        idler.waitForIdle();
    }

    private static final class Idler implements MessageQueue.IdleHandler {
        private final Runnable mCallback;
        private boolean mIdle;

        public Idler(Runnable callback) {
            mCallback = callback;
            mIdle = false;
        }

        public final boolean queueIdle() {
            if (mCallback != null) {
                mCallback.run();
            }
            synchronized (this) {
                mIdle = true;
                notifyAll();
            }
            return false;
        }

        public void waitForIdle() {
            synchronized (this) {
                while (!mIdle) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }

    public static final class ILooper {
        private Looper looper;

        public ILooper(Looper looper) {
            this.looper = looper;
        }

        public MessageQueue getQueue() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return looper.getQueue();
            }

            Reflect type = new Reflect(looper);
            MessageQueue queue = type.getField("mQueue");
            return queue;
        }

        public static MessageQueue myMessageQueue() {
            Looper looper = Looper.myLooper();
            if (looper != null) {
                ILooper iLooper = new ILooper(looper);
                return iLooper.getQueue();
            }
            return null;
        }
    }
}
