
package com.davidluoye.support.model;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.davidluoye.support.util.ILogger;

import java.io.FileDescriptor;
import java.io.PrintWriter;

public abstract class IBaseActivity extends IBaseContext implements IActivity {
    private static final ILogger LOGGER = ILogger.logger(IBaseActivity.class);

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
