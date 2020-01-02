package com.davidluoye.support.util;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

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
}
