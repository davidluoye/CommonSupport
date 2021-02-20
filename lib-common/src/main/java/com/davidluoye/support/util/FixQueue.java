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

import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class FixQueue<T> implements Iterable<T> {

    private static final int INIT_CAPACITY = 8;

    private final int maxCapacity;

    private int size;
    private Object[] items;

    public FixQueue() {
        this(Integer.MAX_VALUE);
    }

    public FixQueue(int maxCapacity) {
        int capacity = INIT_CAPACITY;
        if (maxCapacity <= 0) {
            maxCapacity = Integer.MAX_VALUE;
        }
        this.maxCapacity = maxCapacity;
        this.items = new Object[Math.min(capacity, maxCapacity)];
    }

    public boolean add(T t) {
        ensureCapacity(size + 1);
        if (size + 1 <= items.length) {
            items[size++] = t;
            return true;
        }
        System.arraycopy(items, 1, items, 0, size - 1);
        items[size++] = t;
        return true;
    }

    public boolean add(Collection<T> cc) {
        ensureCapacity(size + cc.size());
        final Object[] objs = cc.toArray();

        if (objs.length >= items.length) {
            System.arraycopy(objs, objs.length - items.length, items, 0, items.length);
            size = items.length;
            return true;
        }

        int delta = size + objs.length - items.length;
        if (delta > 0) {
            System.arraycopy(items, delta, items, 0, items.length - delta);
            size = size - delta;
        }

        System.arraycopy(objs, 0, items, size, objs.length);
        size = size + objs.length;

        return true;
    }

    public T remove(int index) {
        if (index < size) {
            Object obj = items[index];
            System.arraycopy(items, index + 1, items, index, size - 1 - index);
            size--;
            return (T) obj;
        }
        throw new IndexOutOfBoundsException(String.format("index=%s, max=%s", index, size));
    }

    public void removeAll() {
        Arrays.fill(items, null);
        size = 0;
    }

    public T get(int index) {
        if (index < size) {
            return (T) items[index];
        }
        throw new IndexOutOfBoundsException(String.format("index=%s, max=%s", index, size));
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public Object[] toArray() {
        Object[] obj = new Object[size];
        System.arraycopy(items, 0, obj, 0, size);
        return obj;
    }

    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            return (T[]) Arrays.copyOf(items, size, a.getClass());
        }
        System.arraycopy(items, 0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int index = 0; index < size; index++) {
            if (index > 0) {
                sb.append(",");
            }
            sb.append(items[index].toString());
        }
        return sb.toString();
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity > items.length) {
            int capacity = items.length * 3 / 2 + 1;
            capacity = Math.max(capacity, minCapacity);
            capacity = Math.min(capacity, maxCapacity);
            if (capacity > items.length) {
                Object[] tmp = new Object[capacity];
                System.arraycopy(items, 0, tmp, 0, items.length);
                items = tmp;
            }
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new Itr();
    }

    private class Itr implements Iterator<T> {
        private int max;
        private int cursor;

        private Itr() {
            this.max = FixQueue.this.size;
            this.cursor = 0;
        }

        @Override
        public boolean hasNext() {
            return cursor < max;
        }

        @Override
        public T next() {
            if (max != FixQueue.this.size) {
                throw new ConcurrentModificationException();
            }

            int i = cursor;
            if (i >= max) {
                throw new NoSuchElementException();
            }

            cursor = i + 1;
            return (T) items[i];
        }
    }
}
