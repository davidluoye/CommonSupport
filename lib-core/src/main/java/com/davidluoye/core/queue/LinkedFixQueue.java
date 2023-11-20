package com.davidluoye.core.queue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntFunction;

public class LinkedFixQueue<T> implements IQueue<T> {

    private static class Node<E> {
        volatile E item;
        volatile Node<E> next;
        volatile Node<E> pre;

        @Override
        public String toString() {
            return this.item != null ? this.item.toString() : null;
        }
    }

    private volatile Node<T> head;
    private volatile Node<T> tail;
    private int size;

    private final int capacity;
    private final boolean immutable;
    public LinkedFixQueue(int capacity) {
        this(capacity, false);
    }

    public LinkedFixQueue(LinkedFixQueue<T> queue, boolean immutable) {
        this(queue.capacity, immutable);
        for (int index = 0; index < queue.size; index++) {
            add(queue.get(index));
        }
    }

    private LinkedFixQueue(int capacity, boolean immutable) {
        this.capacity = capacity;
        this.head = null;
        this.tail = null;
        this.size = 0;
        this.immutable = immutable;
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
        checkPermission(false);

        Node<T> node = new Node<>();
        node.item = t;
        node.pre = null;
        node.next = null;

        if (tail != null) {
            node.pre = tail;
            tail.next = node;
        }
        tail = node;

        if (head == null) {
            head = tail;
            head.pre = null; // pre in head always is null.
        }
        this.size++;

        if (size > capacity) {
            head = head.next;
            head.pre = null;  // pre in head always is null.
            this.size--;
        }
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
        checkPermission(false);
        cc.forEach(this::add);
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
        checkPermission(false);
        if (this.head == null) return null;
        T item = head.item;
        this.head = head.next;
        if (this.head != null) {
            this.head.pre = null; // pre in head always is null.
        }
        this.size--;
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
        checkPermission(true);
        if (this.head == null) return null;
        return head.item;
    }

    /**
     * Retrieves and removes the tail of this queue,
     * or returns {@code null} if this queue is empty.
     *
     * @return the tail of this queue, or {@code null} if this queue is empty
     */
    @Override
    public T pop() {
        checkPermission(false);
        if (this.size == 0) return null;

        T value = tail.item;
        if (tail.pre == null) {
            head = tail = null;
        } else {
            tail = tail.pre;
            tail.next = null;
        }
        this.size--;
        return value;
    }

    /**
     * Retrieves, but does not remove, the tail of this queue,
     * or returns {@code null} if this queue is empty.
     *
     * @return the tail of this queue, or {@code null} if this queue is empty
     */
    @Override
    public T tail() {
        checkPermission(true);
        if (this.tail == null) return null;
        return tail.item;
    }

    /**
     * Remove the special index element from this queue.
     *
     * @return the special index element
     * @throws IndexOutOfBoundsException if index is not valid
     */
    @Override
    public T remove(int index) {
        checkPermission(false);
        if (index >= size) {
            throw new IndexOutOfBoundsException(String.format("size=%s, but index=%s", size, index));
        }

        if (index == 0) {
            return poll();
        }

        if (index + 1 == this.size) {
            return pop();
        }

        Node<T> n = getNodeByIndex(index);
        n.pre.next = n.next;
        n.next.pre = n.pre;
        this.size--;
        return n.item;
    }

    /**
     * Remove all the elements from this queue.
     */
    @Override
    public void remove() {
        checkPermission(false);
        head = tail = null;
        this.size = 0;
    }

    /**
     * Retrieves, but does not remove, the special index element of this queue.
     *
     * @return the special index element
     * @throws IndexOutOfBoundsException if index is not valid
     */
    @Override
    public T get(int index) {
        checkPermission(true);
        if (index >= size) {
            throw new IndexOutOfBoundsException(String.format("size=%s, but index=%s", size, index));
        }

        Node<T> n = getNodeByIndex(index);
        return n.item;
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
        checkPermission(true);
        Node<T> n = head;
        for (int i = 0; i < this.size && n != null; i++) {
            if (Objects.equals(n.item, t)) return i;
            n = n.next;
        }
        return -1;
    }

    /**
     * Returns the number of elements in this queue.
     * @return the number of elements in this queue
     */
    @Override
    public int size() { return this.size; }

    /**
     * Returns {@code true} if this list contains no elements.
     *
     * @return {@code true} if this list contains no elements
     */
    @Override
    public boolean isEmpty() {
        return this.size <= 0;
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
        Object[] obs = new Object[this.size];
        Node<T> n = head;
        for (int i = 0; i < this.size && n != null; i++) {
            obs[i] = n.item;
            n = n.next;
        }
        return obs;
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
    public <E> E[] toArray(E[] a) {
        Object[] obs = toArray();
        if (a.length < size) {
            return (E[]) Arrays.copyOf(obs, size, a.getClass());
        }

        System.arraycopy(obs, 0, a, 0, size);
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
        checkPermission(false);
        List<T> obs = new ArrayList<>();
        Node<T> n = head;
        for (int i = 0; i < this.size && n != null; i++) {
            obs.add(n.item);
            n = n.next;
        }
        return obs;
    }

    @Override
    public IQueue<T> clone(boolean immutable) {
        return new LinkedFixQueue<>(this, immutable);
    }

    @Override
    public Spliterator<T> spliterator(int startInclusive, int endExclusive) {
        return Spliterators.spliterator(toArray(), startInclusive, endExclusive,
                Spliterator.ORDERED | Spliterator.IMMUTABLE);
    }

    private Node<T> getNodeByIndex(int index) {
        int i = 0;
        Node<T> n = head;
        while (n.next != null && i < index) {
            n = n.next;
            i++;
        }
        return n;
    }

    private void checkPermission(boolean readOption) {
        if (this.immutable && !readOption) {
            throw new UnsupportedOperationException("read only permission, can not write this instance.");
        }
    }

    /** only for test queue information */
    public String dump() {
        StringBuilder sb = new StringBuilder();
        Node<T> n = head;
        for (int i = 0; i < this.size && n != null; i++) {
            sb.append(String.format("[%s, %s, %s]", n.pre, n.item, n.next));
            n = n.next;
        }
        return sb.toString();
    }
}
