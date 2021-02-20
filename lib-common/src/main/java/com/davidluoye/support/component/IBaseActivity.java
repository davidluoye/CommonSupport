/******************************************************************************
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
 ********************************************************************************/
package com.davidluoye.support.component;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.io.FileDescriptor;
import java.io.PrintWriter;

public abstract class IBaseActivity extends IBaseContext implements IActivity {

    protected final Activity mActivity;
    public IBaseActivity(Activity activity) {
        super(activity);
        mActivity = activity;
    }

    /* ================================================================== */
    /* ========== activity function and should not be override ========== */
    /* ================================================================== */

    public final void setContentView(int layoutResID) {
        mActivity.setContentView(layoutResID);
    }

    public final void setContentView(View view) {
        mActivity.setContentView(view);
    }

    public final void setContentView(View view, ViewGroup.LayoutParams params) {
        mActivity.setContentView(view, params);
    }

    public final View findViewById(int id) {
        return mActivity.findViewById(id);
    }

    public final Intent getIntent() {
        return mActivity.getIntent();
    }

    public final void startActivityForResult(Intent intent, int requestCode) {
        mActivity.startActivityForResult(intent, requestCode);
    }

    /* ================================================================== */
    /* ========== activity child function and can be override ========== */
    /* ================================================================== */

    @Override
    public void finish() {
        mActivity.finish();
    }

    @Override
    public void finishAndRemoveTask() {
        mActivity.finishAndRemoveTask();
    }

    @Override
    public void finishActivity(int requestCode) {
        mActivity.finishActivity(requestCode);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
    }

    @Override
    public void onRestart() {
        // TODO Auto-generated method stub
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
    }

    @Override
    public void onPostResume() {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean onBackPressed() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onAttachedToWindow() {
        // TODO Auto-generated method stub
    }

    @Override
    public void onDetachedFromWindow() {
        // TODO Auto-generated method stub
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
    }

    @Override
    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        // TODO Auto-generated method stub
    }
}
