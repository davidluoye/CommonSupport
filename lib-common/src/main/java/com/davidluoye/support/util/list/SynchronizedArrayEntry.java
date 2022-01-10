package com.davidluoye.support.util.list;

import com.davidluoye.support.util.RWLock;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class SynchronizedArrayEntry<KEY, VALUE> extends ArrayEntry<KEY, VALUE> {

    private final RWLock mLock = new RWLock();

    public SynchronizedArrayEntry() {
        super();
    }

    public VALUE add(KEY key, VALUE value) {
        return mLock.writeLock(() -> super.add(key, value));
    }

    public EntrySet<KEY, VALUE> poll() {
        return mLock.writeLock(super::poll);
    }

    public EntrySet<KEY, VALUE> peek() {
        return mLock.readLock(super::peek);
    }

    public EntrySet<KEY, VALUE> pop() {
        return mLock.writeLock(super::pop);
    }

    public EntrySet<KEY, VALUE> tail() {
        return mLock.readLock(super::tail);
    }

    public EntrySet<KEY, VALUE> top() {
        return mLock.readLock(super::top);
    }

    public EntrySet<KEY, VALUE> get(int index) {
        return mLock.readLock(() -> super.get(index));
    }

    public KEY key(int index) {
        return mLock.readLock(() -> super.key(index));
    }

    public VALUE value(int index) {
        return mLock.readLock(() -> super.value(index));
    }

    public int indexOfKey(KEY key) {
        return mLock.readLock(() -> super.indexOfKey(key));
    }

    public VALUE getValue(KEY key) {
        return mLock.readLock(() -> super.getValue(key));
    }

    public VALUE removeKey(KEY key) {
        return mLock.writeLock(() -> super.removeKey(key));
    }

    public boolean removeValue(VALUE value) {
        return mLock.writeLock(() -> super.removeValue(value));
    }

    public EntrySet<KEY, VALUE> remove(int index) {
        return mLock.writeLock(() -> super.remove(index));
    }

    public boolean hasKey(KEY key) {
        return mLock.readLock(() -> super.hasKey(key));
    }

    public boolean hasValue(VALUE value) {
        return mLock.readLock(() -> super.hasValue(value));
    }

    public int size() {
        return mLock.readLock(super::size);
    }

    public boolean isEmpty() {
        return mLock.readLock(super::isEmpty);
    }

    public boolean isNotEmpty() {
        return mLock.readLock(super::isNotEmpty);
    }

    public void clean() {
        mLock.writeLock(() -> { super.clean(); return true; });
    }

    public List<KEY> keys() {
        return mLock.readLock(super::keys);
    }

    public List<VALUE> values() {
        return mLock.readLock(super::values);
    }

    public Stream<EntrySet<KEY, VALUE>> stream() {
        return mLock.readLock(super::stream);
    }

    public void find(int index, BiConsumer<? extends KEY, ? extends VALUE> action) {
        mLock.readLock(() -> { super.find(index, action); return true; });
    }

    public void forEach(BiConsumer<? extends KEY, ? extends VALUE> action) {
        mLock.readLock(() -> { super.forEach(action); return true; });
    }

    public void forEach(BiiConsumer<? extends KEY, ? extends VALUE> action) {
        mLock.readLock(() -> { super.forEach(action); return true; });
    }

    public int findEach(BiiConsumer<KEY, VALUE> action) {
        return mLock.readLock(() -> super.findEach(action));
    }
}
