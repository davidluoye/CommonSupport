package com.davidluoye.support.list;

import com.davidluoye.support.queue.FixQueue;

import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ArrayEntry<KEY, VALUE> {

    private final FixQueue<EntrySet<KEY, VALUE>> entries;
    private final boolean immutable;

    public ArrayEntry() {
        this(false, null);
    }

    protected ArrayEntry(boolean immutable, ArrayEntry<KEY, VALUE> entries) {
        this.immutable = immutable;
        this.entries = entries != null ? entries.entries : new FixQueue<>();
    }

    public VALUE put(KEY key, VALUE value) {
        checkPermission(false);
        EntrySet<KEY, VALUE> entry = entries.findEach(it -> it.key.equals(key));
        if (entry != null) {
            return entry.value.set(value);
        }
        entries.add(EntrySet.obtain(key, value));
        return null;
    }

    public EntrySet<KEY, VALUE> poll() {
        checkPermission(false);
        return entries.poll();
    }

    public EntrySet<KEY, VALUE> peek() {
        checkPermission(true);
        return entries.peek();
    }

    public EntrySet<KEY, VALUE> pop() {
        checkPermission(false);
        return entries.pop();
    }

    public EntrySet<KEY, VALUE> tail() {
        checkPermission(true);
        return entries.tail();
    }

    public EntrySet<KEY, VALUE> top() {
        checkPermission(true);
        return entries.tail();
    }

    public EntrySet<KEY, VALUE> get(int index) {
        checkPermission(true);
        return entries.get(index);
    }

    public KEY keyAt(int index) {
        checkPermission(true);
        return entries.get(index).key.find();
    }

    public VALUE valueAt(int index) {
        checkPermission(true);
        return entries.get(index).value.find();
    }

    public int indexOfKey(KEY key) {
        checkPermission(true);
        return entries.findEach((index, entry) -> entry.key.equals(key));
    }

    public VALUE getValue(KEY key) {
        checkPermission(true);
        EntrySet<KEY, VALUE> entry = entries.findEach(it -> it.key.equals(key));
        return entry == null ? null : entry.value.find();
    }

    public VALUE removeKey(KEY key) {
        checkPermission(false);
        for (int index = entries.size() - 1; index >= 0; index--) {
            EntrySet<KEY, VALUE> entry = entries.get(index);
            if (entry.key.equals(key)) {
                entries.remove(index);
                VALUE value = entry.value.find();
                entry.recycle();
                return value;
            }
        }
        return null;
    }

    public boolean removeValue(VALUE value) {
        checkPermission(false);
        for (int index = entries.size() - 1; index >= 0; index--) {
            EntrySet<KEY, VALUE> entry = entries.get(index);
            if (entry.value.equals(value)) {
                entry.recycle();
                entries.remove(index);
                return true;
            }
        }
        return false;
    }

    public EntrySet<KEY, VALUE> remove(int index) {
        checkPermission(false);
        return entries.remove(index);
    }

    public void clean() {
        checkPermission(false);
        entries.forEach(EntrySet::recycle);
        entries.remove();
    }

    public boolean hasKey(KEY key) {
        checkPermission(true);
        return entries.stream().anyMatch(it -> it.key.equals(key));
    }

    public boolean hasValue(VALUE value) {
        checkPermission(true);
        return entries.stream().anyMatch(it -> it.value.equals(value));
    }

    public int size() {
        checkPermission(true);
        return entries.size();
    }

    public boolean isEmpty() {
        checkPermission(true);
        return entries.isEmpty();
    }

    public boolean isNotEmpty() {
        checkPermission(true);
        return entries.isNotEmpty();
    }

    public List<KEY> keys() {
        checkPermission(true);
        return entries.stream().map(EntrySet::key).collect(Collectors.toList());
    }

    public List<VALUE> values() {
        checkPermission(true);
        return entries.stream().map(EntrySet::value).collect(Collectors.toList());
    }

    public Stream<EntrySet<KEY, VALUE>> stream() {
        checkPermission(true);
        return entries.stream();
    }

    public void find(int index, BiConsumer<? extends KEY, ? extends VALUE> action) {
        checkPermission(true);
        Objects.requireNonNull(action, "action should not be null.");
        EntrySet<KEY, VALUE> entry = entries.get(index);
        action.accept(entry.key.find(), entry.value.find());
    }

    public void forEach(BiConsumer<? extends KEY, ? extends VALUE> action) {
        checkPermission(true);
        Objects.requireNonNull(action, "action should not be null.");
        entries.forEach(it -> action.accept(it.key.find(), it.value.find()));
    }

    public void forEach(BiiConsumer<? extends KEY, ? extends VALUE> action) {
        checkPermission(true);
        Objects.requireNonNull(action, "action should not be null.");
        entries.forEach((index, it) -> action.accept(index, it.key.find(), it.value.find()));
    }

    public int findEach(BiiConsumer<KEY, VALUE> action) {
        checkPermission(true);
        return entries.findEach((index, it) -> action.accept(index, it.key.find(), it.value.find()));
    }

    public ArrayEntry<KEY, VALUE> clone(boolean immutable) {
        return new ArrayEntry<>(immutable, this);
    }

    private void checkPermission(boolean readOption) {
        if (this.immutable && !readOption) {
            throw new UnsupportedOperationException("read only permission, can not write this instance.");
        }
    }

    @FunctionalInterface
    public interface BiiConsumer<KEY, VALUE> {
        boolean accept(int index, KEY key, VALUE value);
    }
}
