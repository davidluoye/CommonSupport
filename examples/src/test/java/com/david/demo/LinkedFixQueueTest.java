package com.david.demo;

import com.davidluoye.core.queue.ArrayFixQueue;
import com.davidluoye.core.queue.LinkedFixQueue;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class LinkedFixQueueTest {

    private static final int MAX_QUEUE = 10;

    private final LinkedFixQueue<String> queue;
    public LinkedFixQueueTest() {
        this.queue = new LinkedFixQueue(MAX_QUEUE);
    }

    private void fill(LinkedFixQueue<String> queue, int min, int count) {
        for (int index = min; index < count + min; index++) {
            queue.add(String.valueOf(index));
        }
    }

    @Test
    public void testPreTail() {
        queue.add("1");
        System.out.println("testAdd 1: " + queue.dump());

        queue.add("2");
        System.out.println("testAdd 2: " + queue.dump());

        queue.add("3");
        System.out.println("testAdd 3: " + queue.dump());

        queue.add("4");
        System.out.println("testAdd 4: " + queue.dump());

        fill(queue, 1, 13);
        System.out.println("testAdd fill: " + queue.dump());
    }

    @Test
    public void testAdd() {
        queue.remove();
        queue.add("1");
        queue.add("2");
        queue.add("3");
        queue.add("4");
        queue.add("5");
        Assert.assertEquals(queue.size(), 5);

        String[] items = new String[] {"6", "7", "8", "9", "10"};
        queue.add(Arrays.asList(items));
        Assert.assertEquals(queue.size(), 10);

        queue.add("11");
        queue.add("12");
        queue.add("13");
        Assert.assertEquals(queue.size(), 10);

        System.out.println("testAdd: " + queue.dump());
    }

    @Test
    public void testPollPeek() {
        queue.remove();
        queue.add("1");
        queue.add("2");
        queue.add("3");
        queue.add("4");
        queue.add("5");

        String item = null;
        item = queue.peek();
        Assert.assertEquals(item, "1");
        Assert.assertEquals(queue.size(), 5);

        item = queue.poll();
        Assert.assertEquals(item, "1");
        Assert.assertEquals(queue.size(), 4);

        item = queue.poll();
        Assert.assertEquals(item, "2");
        Assert.assertEquals(queue.size(), 3);

        item = queue.poll();
        Assert.assertEquals(item, "3");
        Assert.assertEquals(queue.size(), 2);

        item = queue.poll();
        Assert.assertEquals(item, "4");
        Assert.assertEquals(queue.size(), 1);

        item = queue.poll();
        Assert.assertEquals(item, "5");
        Assert.assertEquals(queue.size(), 0);


        fill(queue, 1, 13);
        Assert.assertEquals(queue.size(), 10);

        item = queue.peek();
        Assert.assertEquals(item, "4");
        Assert.assertEquals(queue.size(), 10);

        item = queue.poll();
        Assert.assertEquals(item, "4");
        Assert.assertEquals(queue.size(), 9);

        item = queue.poll();
        Assert.assertEquals(item, "5");
        Assert.assertEquals(queue.size(), 8);

        item = queue.poll();
        Assert.assertEquals(item, "6");
        Assert.assertEquals(queue.size(), 7);

        item = queue.poll(); // 7
        item = queue.poll(); // 8
        item = queue.poll(); // 9
        item = queue.poll(); // 10
        item = queue.poll(); // 11
        item = queue.poll(); // 12
        item = queue.poll(); // 13
        Assert.assertEquals(item, "13");
        Assert.assertEquals(queue.size(), 0);

        item = queue.peek();
        Assert.assertNull(item);
        Assert.assertEquals(queue.size(), 0);

        item = queue.poll();
        Assert.assertNull(item);
        Assert.assertEquals(queue.size(), 0);

        System.out.println("testPollPeek: " + queue.dump());
    }

    @Test
    public void testPopTail() {
        queue.remove();
        fill(queue, 1, 13);

        String item = null;
        item = queue.tail();
        Assert.assertEquals(item, "13");
        Assert.assertEquals(queue.size(), 10);

        item = queue.tail();
        Assert.assertEquals(item, "13");
        Assert.assertEquals(queue.size(), 10);

        Object[] q = queue.toArray();
        Assert.assertArrayEquals(q, new String[]{"4", "5", "6", "7", "8", "9", "10", "11", "12", "13"});

        item = queue.pop();
        Assert.assertEquals(item, "13");
        Assert.assertEquals(queue.size(), 9);

        q = queue.toArray();
        Assert.assertArrayEquals(q, new String[]{"4", "5", "6", "7", "8", "9", "10", "11", "12"});

        item = queue.pop();
        Assert.assertEquals(item, "12");
        Assert.assertEquals(queue.size(), 8);
    }

    @Test
    public void testRemoveGet() {
        queue.remove();
        fill(queue, 1, 13);

        String item = null;
        item = queue.remove(0);
        Assert.assertEquals(item, "4");
        Assert.assertEquals(queue.size(), 9);

        item = queue.remove(0);
        Assert.assertEquals(item, "5");
        Assert.assertEquals(queue.size(), 8);

        item = queue.peek();
        Assert.assertEquals(item, "6");
        Assert.assertEquals(queue.size(), 8);

        item = queue.peek();
        Assert.assertEquals(item, "6");
        Assert.assertEquals(queue.size(), 8);

        item = queue.remove(7);
        Assert.assertEquals(item, "13");
        Assert.assertEquals(queue.size(), 7);

        item = queue.tail();
        Assert.assertEquals(item, "12");
        Assert.assertEquals(queue.size(), 7);

        item = queue.peek();
        Assert.assertEquals(item, "6");
        Assert.assertEquals(queue.size(), 7);

        item = queue.remove(1);
        Assert.assertEquals(item, "7");
        Assert.assertEquals(queue.size(), 6);

        item = queue.peek();
        Assert.assertEquals(item, "6");
        Assert.assertEquals(queue.size(), 6);

        item = queue.get(0);
        Assert.assertEquals(item, "6");
        Assert.assertEquals(queue.size(), 6);

        item = queue.get(1);
        Assert.assertEquals(item, "8");
        Assert.assertEquals(queue.size(), 6);

        item = queue.get(2);
        Assert.assertEquals(item, "9");
        Assert.assertEquals(queue.size(), 6);

        String[] expect = new String[] {"6", "8", "9", "10", "11", "12"};

        Object[] array = queue.toArray();
        Assert.assertArrayEquals(expect, array);

        String[] array2 = queue.toArray(new String[0]);
        Assert.assertArrayEquals(expect, array2);

        String[] array3 = queue.toArray(String[]::new);
        Assert.assertArrayEquals(expect, array3);

        String[] array4 = queue.getQueue().toArray(new String[0]);
        Assert.assertArrayEquals(expect, array4);

        int index = queue.indexOf("8");
        Assert.assertEquals(index, 1);

        index = queue.indexOf("6");
        Assert.assertEquals(index, 0);

        index = queue.indexOf("7");
        Assert.assertEquals(index, -1);

        Assert.assertFalse(queue.isEmpty());
        Assert.assertTrue(queue.isNotEmpty());

        queue.remove();
        Assert.assertEquals(queue.size(), 0);
        Assert.assertTrue(queue.isEmpty());
        Assert.assertFalse(queue.isNotEmpty());
    }

    @Test
    public void testList() {
        queue.remove();
        fill(queue, 1, 13);

        final AtomicInteger index = new AtomicInteger(0);
        final String[] array = queue.toArray(String[]::new);

        String[] array1 = new String[queue.size()];
        queue.forEach(it -> {
            array1[index.getAndIncrement()] = it;
        });
        Assert.assertArrayEquals(array, array1);

        String[] array2 = new String[queue.size()];
        queue.forEach((i, it) -> {
            array2[i] = it;
        });
        Assert.assertArrayEquals(array, array2);

        index.set(0);
        String[] array3 = new String[queue.size()];
        for (String item : queue) {
            array3[index.getAndIncrement()] = item;
        }
        Assert.assertArrayEquals(array, array3);
    }

    @Test
    public void testCreate() {
        queue.remove();
        ArrayFixQueue<String> queue = null;
        String[] array = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
        String[] array1 = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};

        queue = ArrayFixQueue.of(array);
        Assert.assertArrayEquals(queue.toArray(String[]::new), array);

        queue = ArrayFixQueue.of(10, array);
        Assert.assertEquals(queue.size(), 10);
        Assert.assertArrayEquals(queue.toArray(String[]::new), array1);
        Assert.assertEquals(queue.peek(), "1");
        Assert.assertEquals(queue.tail(), "10");

        queue = new ArrayFixQueue<>();
        queue.add(array);
        Assert.assertEquals(queue.size(), 12);
        Assert.assertEquals(queue.peek(), "1");
        Assert.assertEquals(queue.tail(), "12");

        queue = new ArrayFixQueue<>(10);
        queue.add(array);
        Assert.assertEquals(queue.size(), 10);
        Assert.assertEquals(queue.peek(), "3");
        Assert.assertEquals(queue.tail(), "12");
    }
}
