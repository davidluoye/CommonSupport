package com.davidluoye.support.util;

import android.hardware.input.InputManager;
import android.os.Looper;
import android.os.SystemClock;
import android.view.InputDevice;
import android.view.InputEvent;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.MotionEvent;

import java.lang.reflect.Method;

public class EventSender {

    /**
     * Input Event Injection Synchronization Mode: None.
     * Never blocks.  Injection is asynchronous and is assumed always to be successful.
     */
    // see InputDispatcher.h
    public static final int INJECT_INPUT_EVENT_MODE_ASYNC =
            Reflect.getStaticField(InputManager.class, "INJECT_INPUT_EVENT_MODE_ASYNC");

    /**
     * Input Event Injection Synchronization Mode: Wait for result.
     * Waits for previous events to be dispatched so that the input dispatcher can
     * determine whether input event injection will be permitted based on the current
     * input focus.  Does not wait for the input event to finish being handled
     * by the application.
     */
    // see InputDispatcher.h
    public static final int INJECT_INPUT_EVENT_MODE_WAIT_FOR_RESULT =
            Reflect.getStaticField(InputManager.class, "INJECT_INPUT_EVENT_MODE_WAIT_FOR_RESULT");

    /**
     * Input Event Injection Synchronization Mode: Wait for finish.
     * Waits for the event to be delivered to the application and handled.
     */
    // see InputDispatcher.h
    public static final int INJECT_INPUT_EVENT_MODE_WAIT_FOR_FINISH =
            Reflect.getStaticField(InputManager.class, "INJECT_INPUT_EVENT_MODE_WAIT_FOR_FINISH");

    /**
     * Sends the key events corresponding to the text to the app being
     * instrumented.
     *
     * @param text The text to be sent.
     */
    public static void sendStringSync(String text) {
        if (text == null) {
            return;
        }
        KeyCharacterMap keyCharacterMap = KeyCharacterMap.load(KeyCharacterMap.VIRTUAL_KEYBOARD);

        KeyEvent[] events = keyCharacterMap.getEvents(text.toCharArray());

        if (events != null) {
            for (int i = 0; i < events.length; i++) {
                // We have to change the time of an event before injecting it because
                // all KeyEvents returned by KeyCharacterMap.getEvents() have the same
                // time stamp and the system rejects too old events. Hence, it is
                // possible for an event to become stale before it is injected if it
                // takes too long to inject the preceding ones.
                sendKeySync(KeyEvent.changeTimeRepeat(events[i], SystemClock.uptimeMillis(), 0));
            }
        }
    }

    /**
     * Send a key event to the currently focused window/view and wait for it to
     * be processed.  Finished at some point after the recipient has returned
     * from its event processing, though it may <em>not</em> have completely
     * finished reacting from the event -- for example, if it needs to update
     * its display as a result, it may still be in the process of doing that.
     *
     * @param event The event to send to the current focus.
     */
    public static void sendKeySync(KeyEvent event) {
        validateNotAppThread();

        long downTime = event.getDownTime();
        long eventTime = event.getEventTime();
        int action = event.getAction();
        int code = event.getKeyCode();
        int repeatCount = event.getRepeatCount();
        int metaState = event.getMetaState();
        int deviceId = event.getDeviceId();
        int scancode = event.getScanCode();
        int source = event.getSource();
        int flags = event.getFlags();
        if (source == InputDevice.SOURCE_UNKNOWN) {
            source = InputDevice.SOURCE_KEYBOARD;
        }
        if (eventTime == 0) {
            eventTime = SystemClock.uptimeMillis();
        }
        if (downTime == 0) {
            downTime = eventTime;
        }
        KeyEvent newEvent = new KeyEvent(downTime, eventTime, action, code, repeatCount, metaState,
                deviceId, scancode, flags | KeyEvent.FLAG_FROM_SYSTEM, source);
        injectInputEvent(newEvent, INJECT_INPUT_EVENT_MODE_WAIT_FOR_FINISH);
    }

    /**
     * Sends an up and down key event sync to the currently focused window.
     *
     * @param key The integer keycode for the event.
     */
    public static void sendKeyDownUpSync(int key) {
        sendKeySync(new KeyEvent(KeyEvent.ACTION_DOWN, key));
        sendKeySync(new KeyEvent(KeyEvent.ACTION_UP, key));
    }

    /**
     * Higher-level method for sending both the down and up key events for a
     * particular character key code.  Equivalent to creating both KeyEvent
     * objects by hand and calling {@link #sendKeySync}.  The event appears
     * as if it came from keyboard 0, the built in one.
     *
     * @param keyCode The key code of the character to send.
     */
    public static void sendCharacterSync(int keyCode) {
        sendKeySync(new KeyEvent(KeyEvent.ACTION_DOWN, keyCode));
        sendKeySync(new KeyEvent(KeyEvent.ACTION_UP, keyCode));
    }

    /**
     * Dispatch a pointer event. Finished at some point after the recipient has
     * returned from its event processing, though it may <em>not</em> have
     * completely finished reacting from the event -- for example, if it needs
     * to update its display as a result, it may still be in the process of
     * doing that.
     *
     * @param event A motion event describing the pointer action.  (As noted in
     * {@link MotionEvent#obtain(long, long, int, float, float, int)}, be sure to use
     * {@link SystemClock#uptimeMillis()} as the timebase.
     */
    public static void sendPointerSync(MotionEvent event) {
        validateNotAppThread();
        if ((event.getSource() & InputDevice.SOURCE_CLASS_POINTER) == 0) {
            event.setSource(InputDevice.SOURCE_TOUCHSCREEN);
        }
        injectInputEvent(event, INJECT_INPUT_EVENT_MODE_WAIT_FOR_FINISH);
    }

    private static boolean injectInputEvent(InputEvent event, int mode) {
        Method method = Reflect.getStaticMethod(InputManager.class, "injectInputEvent", new Class[]{InputEvent.class, int.class});
        if (method != null) {
            return Reflect.callStaticMethod(method, new Object[]{event, mode});
        }
        return false;
    }

    private static final void validateNotAppThread() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new RuntimeException("This method can not be called from the main application thread");
        }
    }
}
