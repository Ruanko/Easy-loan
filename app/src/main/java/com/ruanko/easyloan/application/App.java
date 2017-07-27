package com.ruanko.easyloan.application;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;

/**
 * Created by deserts on 17/7/27.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AVOSCloud.initialize(this, "BNpgDs8pijTCrki1l50W3W9e-gzGzoHsz",
                "0f9WD97PmD9KtQsOfXHlili1");
        AVOSCloud.setDebugLogEnabled(true);
    }
}
