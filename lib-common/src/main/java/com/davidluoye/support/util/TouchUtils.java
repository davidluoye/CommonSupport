package com.davidluoye.support.util;

import android.app.Instrumentation;
import android.graphics.PointF;
import android.os.SystemClock;
import android.util.Size;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.davidluoye.support.annotation.RunOnAsyncThread;
import com.davidluoye.support.app.Applications;
import com.davidluoye.support.log.ILogger;
import com.davidluoye.support.thread.Threads;

public class TouchUtils {
    private static final ILogger LOGGER = ILogger.logger(TouchUtils.class);

    /**
     * Touching a specific location and dragging to a new location.
     *
     * @param fromX X coordinate of the initial touch, in screen coordinates
     * @param toX X coordinate of the drag destination, in screen coordinates
     * @param fromY X coordinate of the initial touch, in screen coordinates
     * @param toY Y coordinate of the drag destination, in screen coordinates
     * @param stepCount How many move steps to include in the drag
     */
    public static void drag(final float fromX, final float toX, final float fromY, final float toY, final int stepCount) {
        Threads.execute(() -> dragSync(fromX, toX, fromY, toY, 5));
    }

    /**
     * Touch and click a location.
     * @param x X coordinate of the initial touch, in screen coordinates
     * @param y Y coordinate of the initial touch, in screen coordinates
     */
    public static void sendClickEvent(float x, float y) {
        Threads.execute(() -> sendClickEventSync(x, y));
    }

    /**
     * Touch and long click a location.
     * @param x X coordinate of the initial touch, in screen coordinates
     * @param y Y coordinate of the initial touch, in screen coordinates
     */
    public static void sendLongClickEvent(float x, float y) {
        Threads.execute(() -> sendLongClickEventSync(x, y));
    }

    @RunOnAsyncThread
    public static boolean dragSync(float fromX, float toX, float fromY, float toY, int stepCount) {
        Instrumentation instrumentation = getInstrumentation();
        if (instrumentation == null) {
            LOGGER.d("can not find instrumentation");
            return false;
        }

        LOGGER.d("from [%s, %s] to [%s, %s]", fromX, fromY, toX, toY);

        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis();

        float y = fromY;
        float x = fromX;

        float yStep = (toY - fromY) / stepCount;
        float xStep = (toX - fromX) / stepCount;

        MotionEvent event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, x, y, 0);
        instrumentation.sendPointerSync(event);
        for (int i = 0; i < stepCount; ++i) {
            y += yStep;
            x += xStep;
            eventTime = SystemClock.uptimeMillis();
            event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_MOVE, x, y, 0);
            instrumentation.sendPointerSync(event);
        }

        eventTime = SystemClock.uptimeMillis();
        event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, x, y, 0);
        instrumentation.sendPointerSync(event);
        return true;
    }

    @RunOnAsyncThread
    public static boolean sendClickEventSync(float x, float y) {
        Instrumentation instrumentation = getInstrumentation();

        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis();

        MotionEvent event = null;
        event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, x, y, 0);
        instrumentation.sendPointerSync(event);

        eventTime = SystemClock.uptimeMillis();
        event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, x, y, 0);
        instrumentation.sendPointerSync(event);
        return true;
    }

    @RunOnAsyncThread
    public static boolean sendLongClickEventSync(float x, float y) {
        Instrumentation instrumentation = getInstrumentation();

        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis();

        MotionEvent event = null;
        event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, x, y, 0);
        instrumentation.sendPointerSync(event);

        eventTime = SystemClock.uptimeMillis();
        final int touchSlop = getViewConfiguration().getScaledTouchSlop();
        final float delta = touchSlop / 2.0f;
        event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_MOVE, x + delta, y + delta, 0);
        instrumentation.sendPointerSync(event);

        Threads.sleep((long) (ViewConfiguration.getLongPressTimeout() * 1.1f));

        eventTime = SystemClock.uptimeMillis();
        event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, x, y, 0);
        instrumentation.sendPointerSync(event);
        return true;
    }

    public static final Instrumentation getInstrumentation() {
        return Applications.getInstrumentation();
    }

    public static final ViewConfiguration getViewConfiguration() {
        return ViewConfiguration.get(Applications.getApplication());
    }

    public static final PointF getLocation(View view) {
        final int[] pos = new int[2];
        view.getLocationOnScreen(pos);

        final int width = view.getWidth();
        final int height = view.getHeight();

        float x = pos[0] + (width / 2.0f);
        float y = pos[1] + (height / 2.0f);
        return new PointF(x, y);
    }

    public static final Size getSize(View view) {
        return new Size(view.getWidth(), view.getHeight());
    }
}