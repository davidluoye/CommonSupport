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

package com.davidluoye.core.list;

import com.davidluoye.core.queue.FixQueue;

import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ArrayEntry<KEY, VALUE> {

    private final FixQueue<EntrySet<KEY, VALUE>> entries;
    private final boolean immutable;
    private final boolean equalsAsSameOne;

    public ArrayEntry() {
        this(false, null);
    }

    public ArrayEntry(boolean equalsAsSameOne) {
        this(false, null, equalsAsSameOne);
    }

    protected ArrayEntry(boolean immutable, ArrayEntry<KEY, VALUE> entries) {
        this(immutable, entries, true);
    }

    protected ArrayEntry(boolean immutable, ArrayEntry<KEY, VALUE> entries, boolean equalsAsSameOne) {
        this.immutable = immutable;
        this.entries = entries != null ? entries.entries : new FixQueue<>();
        this.equalsAsSameOne = equalsAsSameOne;
    }

    public void put(ArrayEntry<KEY, VALUE> entries) {
        final int size = entries.size();
        for (int index = 0; index < size; index++) {
            this.put(entries.get(index));
        }
    }

    public void put(EntrySet<KEY, VALUE> entry) {
        this.put(entry.key(), entry.value());
    }

    public VALUE put(KEY key, VALUE value) {
        checkPermission(false);
        EntrySet<KEY, VALUE> entry = entries.findEach(it -> sameKey(it.key(), key));
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
        return entries.findEach((index, entry) -> sameKey(entry.key(), key));
    }

    public VALUE getValue(KEY key) {
        checkPermission(true);
        EntrySet<KEY, VALUE> entry = entries.findEach(it -> sameKey(it.key(), key));
        return entry == null ? null : entry.value.find();
    }

    public VALUE removeKey(KEY key) {
        checkPermission(false);
        for (int index = entries.size() - 1; index >= 0; index--) {
            EntrySet<KEY, VALUE> entry = entries.get(index);
            if (sameKey(entry.key(), key)) {
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
            if (sameValue(entry.value(), value)) {
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
        return entries.stream().anyMatch(it -> sameKey(it.key(), key));
    }

    public boolean hasValue(VALUE value) {
        checkPermission(true);
        return entries.stream().anyMatch(it -> sameValue(it.value(), value));
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

    public boolean sameKey(KEY key, KEY key2) {
        if (this.equalsAsSameOne) return Objects.equals(key, key2);
        return key == key2;
    }

    public boolean sameValue(VALUE value, VALUE value2) {
        if (this.equalsAsSameOne) return Objects.equals(value, value2);
        return value == value2;
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
