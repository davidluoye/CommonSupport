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
package com.davidluoye.support.util;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;

public class RWLock {

    private final ReadWriteLock rwLock;
    public RWLock() {
        this(false);
    }

    public RWLock(boolean fair) {
        rwLock = new ReentrantReadWriteLock(fair);
    }

    public Lock lock(boolean read) {
        Lock lock = read ? rwLock.readLock() : rwLock.writeLock();
        lock.lock();
        return lock;
    }

    public <T> T readLock(Supplier<T> action) {
        Lock lock = this.lock(true);
        try {
            return action.get();
        } finally {
            lock.unlock();
        }
    }

    public <T> T writeLock(Supplier<T> action) {
        Lock lock = this.lock(false);
        try {
            return action.get();
        } finally {
            lock.unlock();
        }
    }
}
