package com.david.demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.davidluoye.support.log.ILogger;

public class MainActivity extends Activity {
    private static final ILogger LOGGER = ILogger.logger();

    private TextView mResultView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mResultView = findViewById(R.id.test_result);

        DemoManager manager = new DemoManager(this);
        String result = manager.action("aaaaa");
        Log.d("david-UI", result);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
