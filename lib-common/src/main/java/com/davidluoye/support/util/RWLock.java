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
