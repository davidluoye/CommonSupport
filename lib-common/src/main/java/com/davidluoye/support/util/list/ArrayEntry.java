package com.davidluoye.support.util.list;

import com.davidluoye.support.util.queue.FixQueue;

import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ArrayEntry<KEY, VALUE> {

    private final FixQueue<EntrySet<KEY, VALUE>> entries;

    public ArrayEntry() {
        this.entries = new FixQueue<>();
    }

    public VALUE add(KEY key, VALUE value) {
        EntrySet<KEY, VALUE> entry = entries.findEach(it -> it.key.equals(key));
        if (entry != null) {
            return entry.value.set(value);
        }
        entries.add(EntrySet.obtain(key, value));
        return null;
    }

    public EntrySet<KEY, VALUE> poll() {
        return entries.poll();
    }

    public EntrySet<KEY, VALUE> peek() {
        return entries.peek();
    }

    public EntrySet<KEY, VALUE> pop() {
        return entries.pop();
    }

    public EntrySet<KEY, VALUE> tail() {
        return entries.tail();
    }

    public EntrySet<KEY, VALUE> top() {
        return entries.tail();
    }

    public EntrySet<KEY, VALUE> get(int index) {
        return entries.get(index);
    }

    public KEY key(int index) {
        return entries.get(index).key.find();
    }

    public VALUE value(int index) {
        return entries.get(index).value.find();
    }

    public int indexOfKey(KEY key) {
        return entries.findEach((index, entry) -> entry.key.equals(key));
    }

    public VALUE getValue(KEY key) {
        EntrySet<KEY, VALUE> entry = entries.findEach(it -> it.key.equals(key));
        return entry == null ? null : entry.value.find();
    }

    public VALUE removeKey(KEY key) {
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
        return entries.remove(index);
    }

    public boolean hasKey(KEY key) {
        return entries.stream().anyMatch(it -> it.key.equals(key));
    }

    public boolean hasValue(VALUE value) {
        return entries.stream().anyMatch(it -> it.value.equals(value));
    }

    public int size() {
        return entries.size();
    }

    public boolean isEmpty() {
        return entries.isEmpty();
    }

    public boolean isNotEmpty() {
        return entries.isNotEmpty();
    }

    public void clean() {
        entries.forEach(EntrySet::recycle);
        entries.remove();
    }

    public List<KEY> keys() {
        return entries.stream().map(EntrySet::key).collect(Collectors.toList());
    }

    public List<VALUE> values() {
        return entries.stream().map(EntrySet::value).collect(Collectors.toList());
    }

    public Stream<EntrySet<KEY, VALUE>> stream() {
        return entries.stream();
    }

    public void find(int index, BiConsumer<? extends KEY, ? extends VALUE> action) {
        Objects.requireNonNull(action, "action should not be null.");
        EntrySet<KEY, VALUE> entry = entries.get(index);
        action.accept(entry.key.find(), entry.value.find());
    }

    public void forEach(BiConsumer<? extends KEY, ? extends VALUE> action) {
        Objects.requireNonNull(action, "action should not be null.");
        entries.forEach(it -> action.accept(it.key.find(), it.value.find()));
    }

    public void forEach(BiiConsumer<? extends KEY, ? extends VALUE> action) {
        Objects.requireNonNull(action, "action should not be null.");
        entries.forEach((index, it) -> action.accept(index, it.key.find(), it.value.find()));
    }

    public int findEach(BiiConsumer<KEY, VALUE> action) {
        return entries.findEach((index, it) -> action.accept(index, it.key.find(), it.value.find()));
    }

    @FunctionalInterface
    public interface BiiConsumer<KEY, VALUE> {
        boolean accept(int index, KEY key, VALUE value);
    }
}
