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

package com.davidluoye.core.queue;

import com.davidluoye.core.utils.RWLock;

import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.stream.Stream;

public class SynchronizedFixQueue<T> extends FixQueue<T> {

    private final RWLock mLock = new RWLock();

    public SynchronizedFixQueue() {
        super();
    }

    public SynchronizedFixQueue(int maxCapacity) {
        super(maxCapacity);
    }

    public SynchronizedFixQueue(T[] array) {
        super(array);
    }

    public SynchronizedFixQueue(T[] array, int maxCapacity) {
        super(array, maxCapacity);
    }

    public SynchronizedFixQueue(int initCapacity, int maxCapacity) {
        super(initCapacity, maxCapacity);
    }

    @Override
    public boolean add(T t) {
        return mLock.writeLock(() -> super.add(t));
    }

    @Override
    public boolean add(T[] cc) {
        return mLock.writeLock(() -> super.add(cc));
    }

    @Override
    public boolean add(Collection<T> cc) {
        return mLock.writeLock(() -> super.add(cc));
    }

    @Override
    public T poll() {
        return mLock.writeLock(super::poll);
    }

    @Override
    public T peek() {
        return mLock.readLock(super::peek);
    }

    @Override
    public T pop() {
        return mLock.writeLock(super::pop);
    }

    @Override
    public T tail() {
        return mLock.readLock(super::tail);
    }

    @Override
    public T remove(int index) {
        return mLock.writeLock(() -> super.remove(index));
    }

    @Override
    public void remove() {
        mLock.writeLock(() -> {
            super.remove();
            return true;
        });
    }

    @Override
    public T get(int index) {
        return mLock.readLock(() -> super.get(index));
    }

    @Override
    public int indexOf(T t) {
        return mLock.readLock(() -> super.indexOf(t));
    }

    @Override
    public boolean isEmpty() {
        return mLock.readLock(super::isEmpty);
    }

    @Override
    public boolean isNotEmpty() {
        return mLock.readLock(super::isNotEmpty);
    }

    @Override
    public int size() {
        return mLock.readLock(super::size);
    }

    @Override
    public Object[] toArray() {
        return mLock.readLock(super::toArray);
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return mLock.readLock(() -> super.toArray(a));
    }

    @Override
    public <A> A[] toArray(IntFunction<A[]> generator) {
        return mLock.readLock(() -> super.toArray(generator));
    }

    @Override
    public List<T> getQueue() {
        return mLock.readLock(super::getQueue);
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        mLock.readLock(() -> {
            super.forEach(action);
            return true;
        });
    }

    @Override
    public void forEach(BiConsumer<Integer, ? super T> action) {
        mLock.readLock(() -> {
            super.forEach(action);
            return true;
        });
    }

    @Override
    public Stream<T> stream() {
        return mLock.readLock(() -> super.stream());
    }

    @Override
    public Stream<T> stream(int startInclusive, int endExclusive) {
        return mLock.readLock(() -> super.stream(startInclusive, endExclusive));
    }

    @Override
    protected FixQueue<T> onCloneNewQueue(int maxCapacity) {
        FixQueue<T> queue = new SynchronizedFixQueue<>(maxCapacity);
        forEach(queue::add);
        return queue;
    }

    public static <T> SynchronizedFixQueue<T> of(T... ts) {
        return new SynchronizedFixQueue<>(ts);
    }

    public static <T> SynchronizedFixQueue<T> of(int maxCapacity, T... ts) {
        return new SynchronizedFixQueue<>(ts, maxCapacity);
    }
}
