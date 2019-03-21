
package com.david.support.model;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;

import java.io.FileDescriptor;
import java.io.PrintWriter;

public interface IActivity {

    void onCreate(Bundle savedInstanceState);

    void onPostCreate(Bundle savedInstanceState);

    void onNewIntent(Intent intent);

    void onStart();

    void onRestart();

    void onResume();

    void onPostResume();

    void onStop();

    void onDestroy();

    void onSaveInstanceState(Bundle outState);

    void onRestoreInstanceState(Bundle savedInstanceState);

    boolean onTouchEvent(MotionEvent event);

    boolean onKeyDown(int keyCode, KeyEvent event);

    boolean onKeyUp(int keyCode, KeyEvent event);

    boolean onBackPressed();

    void finish();

    void finishAndRemoveTask();

    void finishActivity(int requestCode);

    void onActivityResult(int requestCode, int resultCode, Intent data);

    void onAttachFragment(Fragment fragment);

    void onAttachedToWindow();

    void onDetachedFromWindow();

    void onWindowFocusChanged(boolean hasFocus);

    void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args);
}
