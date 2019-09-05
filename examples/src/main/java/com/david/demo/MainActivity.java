package com.david.demo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.davidluoye.support.log.Configuration;
import com.davidluoye.support.log.ILogger;
import com.davidluoye.support.test.CaseResult;
import com.davidluoye.support.test.TestRunner;

public class MainActivity extends Activity {
    private static final ILogger LOGGER = ILogger.logger();

    private TextView mResultView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mResultView = findViewById(R.id.test_result);

        Configuration.Builder builder = new Configuration.Builder();
        builder.alwaysPrint();
//        builder.compress();
        builder.appTag("TestRunner");
        builder.directory(getFilesDir());
        builder.build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        CaseResult result = new CaseResult();
        TestRunner.execute(getPackageName(), result);
    }
}
