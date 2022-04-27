package com.david.demo;

import com.davidluoye.support.list.ArrayEntry;

import org.junit.Assert;
import org.junit.Test;

public class ArrayEntryTest {

    private final ArrayEntry<String, String> entries;
    public ArrayEntryTest() {
        this.entries = new ArrayEntry<>();
    }

    private void fill() {
        entries.put("0", "00");
        entries.put("1", "11");
        entries.put("2", "22");
        entries.put("3", "33");
        entries.put("4", "44");
        entries.put("5", "55");
        entries.put("6", "66");
        entries.put("7", "77");
        entries.put("8", "88");
        entries.put("9", "99");
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

        value = entries.put("5", "55");
        Assert.assertEquals(value, "55");
        value = entries.put("6", "66");
        Assert.assertEquals(value, "66");

        Assert.assertEquals(entries.size(), 10);

        Assert.assertEquals(entries.keyAt(5), "5");
        Assert.assertEquals(entries.keyAt(6), "6");

        Assert.assertEquals(entries.valueAt(5), "55");
        Assert.assertEquals(entries.valueAt(6), "66");

        value = entries.put("5", "66");
        Assert.assertEquals(value, "55");
        Assert.assertEquals(entries.valueAt(5), "66");
        Assert.assertEquals(entries.getValue("5"), "66");

        value = entries.put("5", "55");
        Assert.assertEquals(value, "66");
        Assert.assertEquals(entries.valueAt(5), "55");
        Assert.assertEquals(entries.getValue("5"), "55");


    }


}
