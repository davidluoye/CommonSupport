/*
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
 */
package com.davidluoye.core.utils;

import android.app.Instrumentation;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.SystemClock;
import android.util.Size;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.davidluoye.core.annotation.RunOnAsyncThread;
import com.davidluoye.core.app.Applications;
import com.davidluoye.core.log.ILogger;
import com.davidluoye.core.thread.Threads;

public class TouchUtils {
    private static final ILogger LOGGER = ILogger.logger(TouchUtils.class);

    private static final int MOTION_EVENT_INJECTION_DELAY_MILLIS = 50;

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
        Instrumentation instrumentation = Applications.getInstrumentation();
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
        Instrumentation instrumentation = Applications.getInstrumentation();

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
        Instrumentation instrumentation = Applications.getInstrumentation();

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

    @RunOnAsyncThread
    public static boolean sendMultiPointerGesture(Point startPoint1, Point startPoint2,
                                                  Point endPoint1, Point endPoint2,
                                                  int duration, boolean ifMove) {
        return sendMultiPointerGesture(startPoint1, startPoint2, endPoint1, endPoint2,
                duration, ifMove, MOTION_EVENT_INJECTION_DELAY_MILLIS);
    }

    @RunOnAsyncThread
    public static boolean sendMultiPointerGesture(Point startPoint1, Point startPoint2,
                                                  Point endPoint1, Point endPoint2,
                                                  int duration, boolean ifMove,
                                                  int motion_event_injection_delay_millis) {
        Instrumentation instrumentation = Applications.getInstrumentation();

        long eventTime = SystemClock.uptimeMillis();;
        long downTime = SystemClock.uptimeMillis();;
        MotionEvent event;
        float eventX1, eventY1, eventX2, eventY2;

        eventX1 = startPoint1.x;
        eventY1 = startPoint1.y;
        eventX2 = startPoint2.x;
        eventY2 = startPoint2.y;

        // specify the property for the two touch points
        MotionEvent.PointerProperties[] properties = new MotionEvent.PointerProperties[2];
        MotionEvent.PointerProperties pp1 = new MotionEvent.PointerProperties();
        pp1.id = 0;
        pp1.toolType = MotionEvent.TOOL_TYPE_FINGER;
        MotionEvent.PointerProperties pp2 = new MotionEvent.PointerProperties();
        pp2.id = 1;
        pp2.toolType = MotionEvent.TOOL_TYPE_FINGER;

        properties[0] = pp1;
        properties[1] = pp2;

        //specify the coordinations of the two touch points
        //NOTE: we MUST set the pressure and size value, or it doesn't work
        MotionEvent.PointerCoords[] pointerCoords = new MotionEvent.PointerCoords[2];
        MotionEvent.PointerCoords pc1 = new MotionEvent.PointerCoords();
        pc1.x = eventX1;
        pc1.y = eventY1;
        pc1.pressure = 1;
        pc1.size = 1;
        MotionEvent.PointerCoords pc2 = new MotionEvent.PointerCoords();
        pc2.x = eventX2;
        pc2.y = eventY2;
        pc2.pressure = 1;
        pc2.size = 1;
        pointerCoords[0] = pc1;
        pointerCoords[1] = pc2;

        //////////////////////////////////////////////////////////////
        // events sequence of zoom gesture
        // 1. send ACTION_DOWN event of one start point
        // 2. send ACTION_POINTER_2_DOWN of two start points
        // 3. send ACTION_MOVE of two middle points
        // 4. repeat step 3 with updated middle points (x,y),
        //      until reach the end points
        // 5. send ACTION_POINTER_2_UP of two end points
        // 6. send ACTION_UP of one end point
        //////////////////////////////////////////////////////////////

        // step 1
        event = MotionEvent.obtain(downTime, eventTime,
                MotionEvent.ACTION_DOWN, 1, properties,
                pointerCoords, 0,  0, 1, 1, 0, 0, 0, 0 );

        instrumentation.sendPointerSync(event);

        //step 2
        event = MotionEvent.obtain(downTime, eventTime,
                MotionEvent.ACTION_POINTER_2_DOWN, 2,
                properties, pointerCoords, 0, 0, 1, 1, 0, 0, 0, 0);

        instrumentation.sendPointerSync(event);

        //step 3, 4
        if (ifMove) {
            int moveEventNumber = 1;
            moveEventNumber = duration / motion_event_injection_delay_millis;

            float stepX1, stepY1, stepX2, stepY2;

            stepX1 = (endPoint1.x - startPoint1.x) / moveEventNumber;
            stepY1 = (endPoint1.y - startPoint1.y) / moveEventNumber;
            stepX2 = (endPoint2.x - startPoint2.x) / moveEventNumber;
            stepY2 = (endPoint2.y - startPoint2.y) / moveEventNumber;

            for (int i = 0; i < moveEventNumber; i++) {
                // update the move events
                eventTime += motion_event_injection_delay_millis;
                eventX1 += stepX1;
                eventY1 += stepY1;
                eventX2 += stepX2;
                eventY2 += stepY2;

                pc1.x = eventX1;
                pc1.y = eventY1;
                pc2.x = eventX2;
                pc2.y = eventY2;

                pointerCoords[0] = pc1;
                pointerCoords[1] = pc2;

                event = MotionEvent.obtain(downTime, eventTime,
                        MotionEvent.ACTION_MOVE, 2, properties,
                        pointerCoords, 0, 0, 1, 1, 0, 0, 0, 0);

                instrumentation.sendPointerSync(event);
            }
        }

        //step 5
        pc1.x = endPoint1.x;
        pc1.y = endPoint1.y;
        pc2.x = endPoint2.x;
        pc2.y = endPoint2.y;
        pointerCoords[0] = pc1;
        pointerCoords[1] = pc2;

        eventTime += motion_event_injection_delay_millis;
        event = MotionEvent.obtain(downTime, eventTime,
                MotionEvent.ACTION_POINTER_2_UP, 2, properties,
                pointerCoords, 0, 0, 1, 1, 0, 0, 0, 0);
        instrumentation.sendPointerSync(event);

        // step 6
        eventTime += motion_event_injection_delay_millis;
        event = MotionEvent.obtain(downTime, eventTime,
                MotionEvent.ACTION_UP, 1, properties,
                pointerCoords, 0, 0, 1, 1, 0, 0, 0, 0 );
        instrumentation.sendPointerSync(event);

        return true;
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
