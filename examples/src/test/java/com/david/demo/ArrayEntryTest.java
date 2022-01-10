package com.david.demo;

import com.davidluoye.support.util.list.ArrayEntry;

import org.junit.Assert;
import org.junit.Test;

public class ArrayEntryTest {

    private final ArrayEntry<String, String> entries;
    public ArrayEntryTest() {
        this.entries = new ArrayEntry<>();
    }

    private void fill() {
        entries.add("0", "00");
        entries.add("1", "11");
        entries.add("2", "22");
        entries.add("3", "33");
        entries.add("4", "44");
        entries.add("5", "55");
        entries.add("6", "66");
        entries.add("7", "77");
        entries.add("8", "88");
        entries.add("9", "99");
    }

    @Test
    public void testAll() {
        fill();

        Assert.assertEquals(entries.size(), 10);

        Assert.assertTrue(entries.hasKey("5"));
        Assert.assertTrue(entries.hasKey("6"));

        Assert.assertFalse(entries.hasKey("55"));
        Assert.assertFalse(entries.hasKey("66"));

        Assert.assertTrue(entries.hasValue("55"));
        Assert.assertTrue(entries.hasValue("66"));

        Assert.assertFalse(entries.hasValue("5"));
        Assert.assertFalse(entries.hasValue("6"));

        Assert.assertTrue(entries.isNotEmpty());
        Assert.assertFalse(entries.isEmpty());

        Assert.assertEquals(entries.indexOfKey("5"), 5);
        Assert.assertEquals(entries.indexOfKey("66"), -1);

        String value = null;

        value = entries.add("5", "55");
        Assert.assertEquals(value, "55");
        value = entries.add("6", "66");
        Assert.assertEquals(value, "66");

        Assert.assertEquals(entries.size(), 10);

        Assert.assertEquals(entries.key(5), "5");
        Assert.assertEquals(entries.key(6), "6");

        Assert.assertEquals(entries.value(5), "55");
        Assert.assertEquals(entries.value(6), "66");

        value = entries.add("5", "66");
        Assert.assertEquals(value, "55");
        Assert.assertEquals(entries.value(5), "66");
        Assert.assertEquals(entries.getValue("5"), "66");

        value = entries.add("5", "55");
        Assert.assertEquals(value, "66");
        Assert.assertEquals(entries.value(5), "55");
        Assert.assertEquals(entries.getValue("5"), "55");


    }


}
