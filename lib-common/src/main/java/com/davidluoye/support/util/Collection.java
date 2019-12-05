package com.davidluoye.support.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Collection {

    public static ArrayList newArrayList() {
        return new ArrayList();
    }

    public static LinkedList newLinkedList() {
        return new LinkedList();
    }

    public static List unmodifiableList(List list) {
        return Collections.unmodifiableList(list);
    }
}
