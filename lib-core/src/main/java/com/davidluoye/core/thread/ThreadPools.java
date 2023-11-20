package com.davidluoye.core.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPools {
    private final BlockingQueue<Runnable> queue;
    private final ExecutorService pools;
    public ThreadPools() {
        this(Runtime.getRuntime().availableProcessors() * 2 + 1);
    }

    public ThreadPools(final int size) {
        this(size, String.format("thread-pools-%s", size));
    }

    public ThreadPools(final int size, final String name) {
        ThreadFactory factory = r -> new Thread(r, name);
        this.queue = new LinkedBlockingQueue<>();
        this.pools = new ThreadPoolExecutor(size, size * 10, 10L, TimeUnit.MINUTES, queue, factory);
    }

    public final ExecutorService getPools() {
        return this.pools;
    }

    public final void execute(Runnable command) { this.pools.execute(command); }
}
