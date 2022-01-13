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
package com.davidluoye.support.util.queue;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

public class FixQueue<T> implements IQueue<T> {

    private static final int INIT_CAPACITY = 8;

    private final int maxCapacity;

    private int size;
    private Object[] items;
    public FixQueue() {
        this(INIT_CAPACITY, Integer.MAX_VALUE);
    }

    public FixQueue(int maxCapacity) {
        this(INIT_CAPACITY, maxCapacity);
    }

    public FixQueue(T[] array) {
        this(array, Integer.MAX_VALUE);
    }

    public FixQueue(T[] array, int maxCapacity) {
        this(array.length, maxCapacity);
        this.size = items.length;
        System.arraycopy(array, 0, items, 0, size);
    }

    public FixQueue(int initCapacity, int maxCapacity) {
        int capacity = initCapacity;
        if (maxCapacity <= 0) {
            maxCapacity = Integer.MAX_VALUE;
        }
        this.maxCapacity = maxCapacity;
        this.items = new Object[Math.min(capacity, maxCapacity)];
        this.size = 0;
    }

    /**
     * Inserts the specified element into tail of this queue.
     *
     * <node>
     * If the element in this queue has reached max count, then remove the head firstly
     * and insert the new one into tail.
     * </node>
     *
     * @param t the element to add
     * @return {@code true} (as specified by {@link Collection#add})
     */
    @Override
    public boolean add(T t) {
        ensureCapacity(size + 1);
        if (size + 1 <= items.length) {
            items[size++] = t;
            return true;
        }

        System.arraycopy(items, 1, items, 0, size - 1);
        size--;
        items[size++] = t;
        return true;
    }

    /**
     * Inserts the specified elements into tail of this queue.
     *
     * <node>
     * If the element in this queue has reached max count, then remove the head firstly
     * and insert the new one into tail.
     * </node>
     *
     * @param cc the element to add
     * @return {@code true} (as specified by {@link Collection#add})
     */
    @Override
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

    /**
     * Retrieves and removes the head of this queue,
     * or returns {@code null} if this queue is empty.
     *
     * @return the head of this queue, or {@code null} if this queue is empty
     */
    @Override
    public T poll() {
        if (size <= 0) return null;

        T item = (T)items[0];
        System.arraycopy(items, 1, items, 0, size - 1);
        size--;
        return item;
    }

    /**
     * Retrieves, but does not remove, the head of this queue,
     * or returns {@code null} if this queue is empty.
     *
     * @return the head of this queue, or {@code null} if this queue is empty
     */
    @Override
    public T peek() {
        if (size <= 0) return null;
        return (T)items[0];
    }

    /**
     * Retrieves and removes the tail of this queue,
     * or returns {@code null} if this queue is empty.
     *
     * @return the tail of this queue, or {@code null} if this queue is empty
     */
    @Override
    public T pop() {
        if (size <= 0) return null;
        int index = size - 1;
        T item = (T)items[index];
        items[index] = null;
        size--;
        return item;
    }

    /**
     * Retrieves, but does not remove, the tail of this queue,
     * or returns {@code null} if this queue is empty.
     *
     * @return the tail of this queue, or {@code null} if this queue is empty
     */
    @Override
    public T tail() {
        if (size <= 0) return null;
        int index = size - 1;
        return (T)items[index];
    }

    /**
     * Remove the special index element from this queue.
     *
     * @return the special index element
     * @throws IndexOutOfBoundsException if index is not valid
     */
    @Override
    public T remove(int index) {
        if (index >= size) throw new IndexOutOfBoundsException(String.format("index=%s, max=%s", index, size));
        Object obj = items[index];
        System.arraycopy(items, index + 1, items, index, size - 1 - index);
        size--;
        return (T) obj;
    }

    /**
     * Remove all the elements from this queue.
     */
    @Override
    public void remove() {
        Arrays.fill(items, null);
        size = 0;
    }

    /**
     * Retrieves, but does not remove, the special index element of this queue.
     *
     * @return the special index element
     * @throws IndexOutOfBoundsException if index is not valid
     */
    @Override
    public T get(int index) {
        if (index >= size) throw new IndexOutOfBoundsException(String.format("index=%s, max=%s", index, size));
        return (T) items[index];
    }

    /**
     * Returns the index of the first occurrence of the specified element
     * in this list, or -1 if this list does not contain the element.
     * More formally, returns the lowest index <tt>i</tt> such that
     * <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>,
     * or -1 if there is no such index.
     */
    @Override
    public int indexOf(T t) {
        for (int index = 0; index < size; index++) {
            if (Objects.equals(items[index], t)) {
                return index;
            }
        }
        return -1;
    }

    /**
     * Returns the number of elements in this queue.
     * @return the number of elements in this queue
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Returns an array containing all of the elements in this queue
     * in proper sequence (from first to last element).
     *
     * <p>The returned array will be "safe" in that no references to it are
     * maintained by this list.  (In other words, this method must allocate
     * a new array).  The caller is thus free to modify the returned array.
     *
     * <p>This method acts as bridge between array-based and collection-based APIs.
     *
     * @return an array containing all of the elements in this queue in proper sequence
     */
    @Override
    public Object[] toArray() {
        return Arrays.copyOf(items, size);
    }

    /**
     * Returns an array containing all of the elements in this queue
     * in proper sequence (from first to last element).
     *
     * <p>The returned array will be "safe" in that no references to it are
     * maintained by this list.  (In other words, this method must allocate
     * a new array).  The caller is thus free to modify the returned array.
     *
     * <p>This method acts as bridge between array-based and collection-based APIs.
     *
     * @return an array containing all of the elements in this queue in proper sequence
     */
    @Override
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

    /**
     * Returns an array containing the elements of this stream, using the
     * provided {@code generator} function to allocate the returned array, as
     * well as any additional arrays that might be required for a partitioned
     * execution or for resizing.
     *
     * <p>This is a <a href="package-summary.html#StreamOps">terminal
     * operation</a>.
     *
     * The generator function takes an integer, which is the size of the
     * desired array, and produces an array of the desired size.  This can be
     * concisely expressed with an array constructor reference:
     * <pre>{@code
     *     Person[] men = people.stream()
     *                          .filter(p -> p.getGender() == MALE)
     *                          .toArray(Person[]::new);
     * }</pre>
     *
     * @param generator a function which produces a new array of the desired
     *                  type and the provided length
     * @return an array containing the elements in this stream
     * @throws ArrayStoreException if the runtime type of the array returned
     * from the array generator is not a supertype of the runtime type of every
     * element in this stream
     */
    @Override
    public <A> A[] toArray(IntFunction<A[]> generator) {
        return stream(0, size).toArray(generator);
    }

    @Override
    public List<T> getQueue() {
        return stream(0, size).collect(Collectors.toList());
    }

    @Override
    public final FixQueue<T> clone(boolean immutable) {
        return onCloneNewQueue(this.maxCapacity);
    }

    @Override
    public Spliterator<T> spliterator(int startInclusive, int endExclusive) {
        return Spliterators.spliterator(items, startInclusive, endExclusive,
                Spliterator.ORDERED | Spliterator.IMMUTABLE);
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

    protected FixQueue<T> onCloneNewQueue(int maxCapacity) {
        FixQueue<T> queue = new FixQueue<>(maxCapacity);
        forEach(queue::add);
        return queue;
    }

    public static <T> FixQueue<T> of(T... ts) {
        return new FixQueue<>(ts);
    }

    public static <T> FixQueue<T> of(int maxCapacity, T... ts) {
        return new FixQueue<>(ts, maxCapacity);
    }
}
