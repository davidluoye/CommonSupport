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
package com.davidluoye.component;

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
