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

package com.davidluoye.support.list;

import com.davidluoye.support.utils.RWLock;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class SynchronizedArrayEntry<KEY, VALUE> extends ArrayEntry<KEY, VALUE> {

    private final RWLock mLock = new RWLock();

    public SynchronizedArrayEntry() {
        this(false, null);
    }

    protected SynchronizedArrayEntry(boolean immutable, ArrayEntry<KEY, VALUE> entries) {
        super(immutable, entries);
    }

    @Override
    public VALUE put(KEY key, VALUE value) {
        return mLock.writeLock(() -> super.put(key, value));
    }

    @Override
    public EntrySet<KEY, VALUE> poll() {
        return mLock.writeLock(super::poll);
    }

    @Override
    public EntrySet<KEY, VALUE> peek() {
        return mLock.readLock(super::peek);
    }

    @Override
    public EntrySet<KEY, VALUE> pop() {
        return mLock.writeLock(super::pop);
    }

    @Override
    public EntrySet<KEY, VALUE> tail() {
        return mLock.readLock(super::tail);
    }

    @Override
    public EntrySet<KEY, VALUE> top() {
        return mLock.readLock(super::top);
    }

    @Override
    public EntrySet<KEY, VALUE> get(int index) {
        return mLock.readLock(() -> super.get(index));
    }

    @Override
    public KEY keyAt(int index) {
        return mLock.readLock(() -> super.keyAt(index));
    }

    @Override
    public VALUE valueAt(int index) {
        return mLock.readLock(() -> super.valueAt(index));
    }

    @Override
    public int indexOfKey(KEY key) {
        return mLock.readLock(() -> super.indexOfKey(key));
    }

    @Override
    public VALUE getValue(KEY key) {
        return mLock.readLock(() -> super.getValue(key));
    }

    @Override
    public VALUE removeKey(KEY key) {
        return mLock.writeLock(() -> super.removeKey(key));
    }

    @Override
    public boolean removeValue(VALUE value) {
        return mLock.writeLock(() -> super.removeValue(value));
    }

    @Override
    public EntrySet<KEY, VALUE> remove(int index) {
        return mLock.writeLock(() -> super.remove(index));
    }

    @Override
    public void clean() {
        mLock.writeLock(() -> { super.clean(); return true; });
    }

    @Override
    public boolean hasKey(KEY key) {
        return mLock.readLock(() -> super.hasKey(key));
    }

    @Override
    public boolean hasValue(VALUE value) {
        return mLock.readLock(() -> super.hasValue(value));
    }

    @Override
    public int size() {
        return mLock.readLock(super::size);
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
    public List<KEY> keys() {
        return mLock.readLock(super::keys);
    }

    @Override
    public List<VALUE> values() {
        return mLock.readLock(super::values);
    }

    @Override
    public Stream<EntrySet<KEY, VALUE>> stream() {
        return mLock.readLock(super::stream);
    }

    @Override
    public void find(int index, BiConsumer<? extends KEY, ? extends VALUE> action) {
        mLock.readLock(() -> { super.find(index, action); return true; });
    }

    @Override
    public void forEach(BiConsumer<? extends KEY, ? extends VALUE> action) {
        mLock.readLock(() -> { super.forEach(action); return true; });
    }

    @Override
    public void forEach(BiiConsumer<? extends KEY, ? extends VALUE> action) {
        mLock.readLock(() -> { super.forEach(action); return true; });
    }

    @Override
    public int findEach(BiiConsumer<KEY, VALUE> action) {
        return mLock.readLock(() -> super.findEach(action));
    }

    @Override
    public ArrayEntry<KEY, VALUE> clone(boolean immutable) {
        return new SynchronizedArrayEntry<>(immutable, this);
    }
}
