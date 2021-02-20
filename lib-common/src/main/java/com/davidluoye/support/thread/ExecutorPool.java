/******************************************************************************
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
 ********************************************************************************/
package com.davidluoye.support.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ExecutorPool {
    private final BlockingQueue queue;
    private final ExecutorService pools;
    public ExecutorPool(final int size) {
        this(size, String.format("thread-pools-%s", size));
    }

    public ExecutorPool(final int size, final String name) {
        ThreadFactory factory = new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, name);
            }
        };
        queue = new LinkedBlockingQueue();
        pools = new ThreadPoolExecutor(0, size, 0L, TimeUnit.MILLISECONDS, queue, factory);
    }

    public final ExecutorService getThreadPool() {
        return this.pools;
    }
}
