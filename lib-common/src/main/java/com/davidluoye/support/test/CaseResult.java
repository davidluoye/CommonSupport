package com.davidluoye.support.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class CaseResult {
    private final List<TestClass> classes;
    public CaseResult() {
        this.classes = new ArrayList<>();
    }

    public void add(TestClass testClass) {
        this.classes.add(testClass);
    }

    public List<TestClass> getList() {
        return Collections.unmodifiableList(classes);
    }

    public int success() {
        int count = 0;
        for (TestClass clazz : classes) {
            if (clazz.fail() == 0) {
                count = count + 1;
            }
        }
        return count;
    }

    public int fail() {
        int count = 0;
        for (TestClass clazz : classes) {
            if (clazz.fail() > 0) {
                count = count + 1;
            }
        }
        return count;
    }

    public static final class TestClass {
        public final String name;
        public final List<TestCase> cases;

        public TestClass(String name) {
            this.name = name;
            this.cases = new ArrayList<>();
        }

        public void add(TestCase testCase) {
            this.cases.add(testCase);
        }

        public int success() {
            int count = 0;
            for (TestCase tCase : cases) {
                if (tCase.success) {
                    count = count + 1;
                }
            }
            return count;
        }

        public int fail() {
            int count = 0;
            for (TestCase tCase : cases) {
                if (!tCase.success) {
                    count = count + 1;
                }
            }
            return count;
        }
    }

    public static final class TestCase {
        public final String parent;
        public final String name;
        private boolean success;

        public TestCase(String parent, String name) {
            this.parent = parent;
            this.name = name;
            this.success = false;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }
    }
}
