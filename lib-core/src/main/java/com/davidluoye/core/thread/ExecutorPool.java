/*
 * Copyright 2021 The authors David Yang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.davidluoye.core.thread;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ExecutorPool implements ExecutorService {
    private final BlockingQueue queue;
    private final ExecutorService pools;
    public ExecutorPool(final int size) {
        this(size, String.format("thread-pools-%s", size));
    }

    public ExecutorPool(final int size, final String name) {
        ThreadFactory factory = r -> new Thread(r, name);
        queue = new LinkedBlockingQueue();
        pools = new ThreadPoolExecutor(0, size, 0L, TimeUnit.MILLISECONDS, queue, factory);
    }

    public final ExecutorService getThreadPool() {
        return this.pools;
    }

    @Override
    public void shutdown() {
        this.pools.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        return this.pools.shutdownNow();
    }

    @Override
    public boolean isShutdown() {
        return this.pools.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return this.pools.isTerminated();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return this.pools.awaitTermination(timeout, unit);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return this.pools.submit(task);
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        return this.pools.submit(task, result);
    }

    @Override
    public Future<?> submit(Runnable task) {
        return this.pools.submit(task);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return this.pools.invokeAll(tasks);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        return this.pools.invokeAll(tasks, timeout, unit);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws ExecutionException, InterruptedException {
        return this.pools.invokeAny(tasks);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws ExecutionException, InterruptedException, TimeoutException {
        return this.pools.invokeAny(tasks, timeout, unit);
    }

    @Override
    public void execute(Runnable command) {
        this.pools.execute(command);
    }
}
