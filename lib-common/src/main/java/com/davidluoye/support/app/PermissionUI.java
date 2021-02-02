package com.davidluoye.support.app;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.davidluoye.support.log.ILogger;

public class PermissionUI extends Activity {
    private static final ILogger LOGGER = ILogger.logger(PermissionUI.class);

    private static final int REQUEST_PERMISSION_CODE = 0x0010;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null) {
            String[] permissions = intent.getStringArrayExtra(Permission.KEY_EXTRA);
            if (permissions != null && permissions.length > 0) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(permissions, REQUEST_PERMISSION_CODE);
                    return;
                }
            }
        }
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (int index = 0; index < permissions.length; index++) {
                if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                    LOGGER.e("request %s fail.", permissions[index]);
                }
            }
        }
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
