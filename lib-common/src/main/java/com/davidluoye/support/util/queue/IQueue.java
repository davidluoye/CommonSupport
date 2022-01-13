package com.davidluoye.support.util.queue;

import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface IQueue<T> extends Iterable<T> {

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
    public boolean add(T t);

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
    public boolean add(Collection<T> cc);

    default boolean add(T[] cc) {
        return add(Arrays.asList(cc));
    }

    /**
     * Retrieves and removes the head of this queue,
     * or returns {@code null} if this queue is empty.
     *
     * @return the head of this queue, or {@code null} if this queue is empty
     */
    public T poll();

    /**
     * Retrieves, but does not remove, the head of this queue,
     * or returns {@code null} if this queue is empty.
     *
     * @return the head of this queue, or {@code null} if this queue is empty
     */
    public T peek();

    /**
     * Retrieves and removes the tail of this queue,
     * or returns {@code null} if this queue is empty.
     *
     * @return the tail of this queue, or {@code null} if this queue is empty
     */
    public T pop();

    /**
     * Retrieves, but does not remove, the tail of this queue,
     * or returns {@code null} if this queue is empty.
     *
     * @return the tail of this queue, or {@code null} if this queue is empty
     */
    public T tail();

    /**
     * Remove the special index element from this queue.
     *
     * @return the special index element
     * @throws IndexOutOfBoundsException if index is not valid
     */
    public T remove(int index);

    /**
     * Remove all the elements from this queue.
     */
    public void remove();

    /**
     * Retrieves, but does not remove, the special index element of this queue.
     *
     * @return the special index element
     * @throws IndexOutOfBoundsException if index is not valid
     */
    public T get(int index);

    /**
     * Returns the index of the first occurrence of the specified element
     * in this list, or -1 if this list does not contain the element.
     * More formally, returns the lowest index <tt>i</tt> such that
     * <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>,
     * or -1 if there is no such index.
     */
    public int indexOf(T t);

    /**
     * Returns the number of elements in this queue.
     * @return the number of elements in this queue
     */
    public int size();

    /**
     * Returns <tt>true</tt> if this queue contains the specified element.
     * More formally, returns <tt>true</tt> if and only if this queue
     * contains at least one element <tt>e</tt> such that
     * <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>.
     *
     * @param o element whose presence in this queue is to be tested
     * @return <tt>true</tt> if this queue contains the specified
     *         element
     * @throws ClassCastException if the type of the specified element
     *         is incompatible with this queue
     *         (<a href="#optional-restrictions">optional</a>)
     */
    default boolean contains(Object o) {
        return stream().anyMatch(it -> it.equals(o));
    }

    /**
     * Returns <tt>true</tt> if this queue contains no elements.
     * @return <tt>true</tt> if this queue contains no elements
     */
    default boolean isEmpty() {
        return size() <= 0;
    }

    /**
     * Returns <tt>true</tt> if this queue contains elements.
     * @return <tt>true</tt> if this queue contains elements
     */
    default boolean isNotEmpty() {
        return size() > 0;
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
    public Object[] toArray();

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
    public <T> T[] toArray(T[] a);

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
    public <A> A[] toArray(IntFunction<A[]> generator);

    public List<T> getQueue();

    public IQueue<T> clone(boolean immutable);

    /**
     * Creates a {@code Spliterator} covering a range of elements of a given
     * array, using a customized set of spliterator characteristics.
     *
     * <p>This method is provided as an implementation convenience for
     * Spliterators which store portions of their elements in arrays, and need
     * fine control over Spliterator characteristics.  Most other situations in
     * which a Spliterator for an array is needed should use
     * {@link Arrays#spliterator(Object[])}.
     *
     * <p>The returned spliterator always reports the characteristics
     * {@code SIZED} and {@code SUBSIZED}.  The caller may provide additional
     * characteristics for the spliterator to report; it is common to
     * additionally specify {@code IMMUTABLE} and {@code ORDERED}.
     *
     * @param startInclusive The least index (inclusive) to cover
     * @param endExclusive One past the greatest index to cover
     * @return A spliterator for an array
     * @throws NullPointerException if the given array is {@code null}
     * @throws ArrayIndexOutOfBoundsException if {@code fromIndex} is negative,
     *         {@code toIndex} is less than {@code fromIndex}, or
     *         {@code toIndex} is greater than the array size
     * @see Arrays#spliterator(Object[], int, int)
     */
    public Spliterator<T> spliterator(int startInclusive, int endExclusive);

    /**
     * Performs the given action for each element of the {@code Iterable}
     * until all elements have been processed or the action throws an
     * exception.  Unless otherwise specified by the implementing class,
     * actions are performed in the order of iteration (if an iteration order
     * is specified).  Exceptions thrown by the action are relayed to the
     * caller.
     *
     * <p>The default implementation behaves as if:
     * <pre>{@code
     *     for (T t : this)
     *         action.accept(t);
     * }</pre>
     *
     * @param action The action to be performed for each element
     * @throws NullPointerException if the specified action is null
     */
    default void forEach(Consumer<? super T> action) {
        Objects.requireNonNull(action, "action should not be null.");
        for (T t : this) {
            action.accept(t);
        }
    }

    /**
     * Performs the given action for each element of the {@code Iterable}
     * until all elements have been processed or the action throws an
     * exception.  Unless otherwise specified by the implementing class,
     * actions are performed in the order of iteration (if an iteration order
     * is specified).  Exceptions thrown by the action are relayed to the
     * caller.
     *
     * @param action The action to be performed for each element
     * @throws NullPointerException if the specified action is null
     */
    default void forEach(BiConsumer<Integer, ? super T> action) {
        Objects.requireNonNull(action, "action should not be null.");
        int size = size();
        for (int index = 0; index < size; index++) {
            action.accept(index, get(index));
        }
    }

    default T findEach(Function<? super T, Boolean> action) {
        Objects.requireNonNull(action, "action should not be null.");
        for (T t : this) {
            boolean find = action.apply(t);
            if (find) return t;
        }
        return null;
    }

    default int findEach(BiFunction<Integer, ? super T, Boolean> action) {
        Objects.requireNonNull(action, "action should not be null.");
        int size = size();
        for (int index = 0; index < size; index++) {
            boolean find = action.apply(index, get(index));
            if (find) return index;
        }
        return -1;
    }

    /**
     * Returns a sequential {@code Stream} with this queue as its source.
     *
     * <p>This method should be overridden when the {@link #spliterator()}
     * method cannot return a spliterator that is {@code IMMUTABLE},
     * {@code CONCURRENT}, or <em>late-binding</em>. (See {@link #spliterator()}
     * for details.)
     *
     * The default implementation creates a sequential {@code Stream} from the
     * queue's {@code Spliterator}.
     *
     * @return a sequential {@code Stream} over the elements in this collection
     */
    default Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    /**
     * Returns a sequential {@link Stream} with the specified range of the
     * specified array as its source.
     *
     * @param startInclusive the first index to cover, inclusive
     * @param endExclusive index immediately past the last index to cover
     * @return a {@code Stream} for the array range
     * @throws ArrayIndexOutOfBoundsException if {@code startInclusive} is
     *         negative, {@code endExclusive} is less than
     *         {@code startInclusive}, or {@code endExclusive} is greater than
     *         the array size
     */
    default Stream<T> stream(int startInclusive, int endExclusive) {
        return StreamSupport.stream(spliterator(startInclusive, endExclusive), false);
    }

    @Override
    default Iterator<T> iterator() {
        return new Itr<T>(this::size, this::get);
    }

    class Itr<T> implements Iterator<T> {
        private final Supplier<Integer> size;
        private final Function<Integer, T> item;
        private final int max;
        private int cursor;

        protected Itr(Supplier<Integer> size, Function<Integer, T> item) {
            this.size = size;
            this.item = item;

            this.max = size.get();
            this.cursor = 0;
        }

        @Override
        public boolean hasNext() {
            return cursor < max;
        }

        @Override
        public T next() {
            if (max != size.get()) {
                throw new ConcurrentModificationException();
            }

            int i = cursor;
            if (i >= max) {
                throw new NoSuchElementException();
            }

            cursor = i + 1;
            return item.apply(i);
        }
    }
}
