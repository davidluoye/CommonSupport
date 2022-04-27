package com.davidluoye.support.list;

import com.davidluoye.support.util.RWLock;

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
