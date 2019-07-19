package com.davidluoye.support.test;

/** The parent of all the test class
 *
 *
 * For example:
 *
 * @SuitClass
 * public class TestThreadUtil extends TestUnit {
 *
 *    @SuitCase
 *    public void testPost() {
 *        ... ... ...
 *    }
 *
 *    @SuitCase
 *    public void testPostDelay() {
 *          ... ... ...
 *    }
 * }
 *
 * */
public abstract class TestUnit {

    /**
     * start test class callback.
     * When we find a test class, all the test case function will be called,
     * this will be called before all the test case.
     */
    public void start() {

    }

    /**
     * stop test class callback.
     * When we test all the test case in the test class, this will be called.
     */
    public void stop() {

    }
}
