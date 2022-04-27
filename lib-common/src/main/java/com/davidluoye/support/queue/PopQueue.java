package com.davidluoye.support.queue;

import com.davidluoye.support.annotation.RunOnAsyncThread;
import com.davidluoye.support.thread.Threads;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class PopQueue<KEY, VALUE> {

    public interface OnPopupEvent<KEY, VALUE> {
        @RunOnAsyncThread void onPopup(KEY key, VALUE value);
        @RunOnAsyncThread void finish();
    }

    private final List<Node<KEY, VALUE>> mCaches;
    private final NodePools<KEY, VALUE> mRecycles;
    private final AtomicBoolean mStopped;
    private final AtomicReference<OnPopupEvent<KEY, VALUE>> mCallBack;
    private final Thread mThread;
    public PopQueue(boolean auto) {
        this.mCaches = new LinkedList<>();
        this.mRecycles = new NodePools<>();
        this.mStopped = new AtomicBoolean(true);
        this.mCallBack = new AtomicReference<>();

        this.mThread = new Thread(new PopUpTask<>(this));
        this.mThread.setName("pop-thread");
        this.mThread.start();
    }

    public void add(KEY key, VALUE value) {
        synchronized (mCaches) {
            Node<KEY, VALUE> node = mRecycles.pop();
            mCaches.add(node.set(key, value));
        }
    }

    public void add(List<VALUE> values) {
        synchronized (mCaches) {
            values.forEach(it -> {
                Node<KEY, VALUE> node = mRecycles.pop();
                node.set(null, it);
                mCaches.add(node);
            });
        }
    }

    public void reset() {
        synchronized (mCaches) {
            while (!mCaches.isEmpty()) {
                Node<KEY, VALUE> node = mCaches.remove(0);
                mRecycles.push(node);
            }
        }
    }

    public void start() {
        synchronized (mStopped) {
            if (mStopped.get()) {
                mStopped.set(false);
            }
            Threads.notify(mStopped);
        }
    }

    public void stop(boolean reset) {
        synchronized (mStopped) {
            mStopped.set(true);
            if (reset) reset();
        }
    }

    public void pop() {
        synchronized (mStopped) {
            Threads.notify(mStopped);
        }
    }

    public boolean hasStop() {
        synchronized (mStopped) {
            return mStopped.get();
        }
    }

    public void setOnPopupEvent(OnPopupEvent<KEY, VALUE> callback) {
        mCallBack.set(callback);
    }

    private void performPopupEvent(KEY key, VALUE value) {
        OnPopupEvent<KEY, VALUE> callback = mCallBack.get();
        if (callback != null) {
            callback.onPopup(key, value);
        }
    }

    private void performPopupFinish() {
        OnPopupEvent<KEY, VALUE> callback = mCallBack.get();
        if (callback != null) {
            callback.finish();
        }
    }

    private static class PopUpTask<KEY, VALUE> implements Runnable {
        private final PopQueue<KEY, VALUE> queue;
        public PopUpTask(PopQueue<KEY, VALUE> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            while (true) {
                synchronized (queue.mStopped) {
                    if (queue.mStopped.get()) {
                        Threads.wait(queue.mStopped);
                    }
                }

                Node<KEY, VALUE> node = null;
                synchronized (queue.mCaches) {
                    if (queue.mCaches.size() > 0) {
                        node = queue.mCaches.remove(0);
                    }
                }

                if (node != null) {
                    queue.performPopupEvent(node.key, node.value);
                    queue.mRecycles.push(node);
                } else {
                    queue.performPopupFinish();
                    synchronized (queue.mStopped) {
                        queue.mStopped.set(true);
                    }
                }
            }
        }
    }

    private static class Node<KEY, VALUE> {
        public KEY key;
        public VALUE value;
        public Node<KEY, VALUE> set(KEY key, VALUE value) {
            this.key = key;
            this.value = value;
            return this;
        }
    }

    private static final class NodePools<KEY, VALUE> {
        private final Stack<Node<KEY, VALUE>> pools = new Stack<>();

        public Node<KEY, VALUE> pop() {
            synchronized (pools) {
                if (pools.isEmpty()) {
                    return new Node<>();
                }
                return pools.pop();
            }
        }

        public void push(Node<KEY, VALUE> node) {
            synchronized (pools) {
                node.key = null;
                node.value = null;
                pools.push(node);
            }
        }
    }

    @Override
    public String toString() {
        List<VALUE> cache = mCaches.stream().map(it -> it.value).collect(Collectors.toList());
        return String.format("mStopped = %s\n  mCaches: %s", mStopped, cache);
    }
}
