package com.hencoder.pluginablehotfix;

import android.app.Application;
import android.content.Context;

public class HotfixApp extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        HotfixUtils.hotfix(this);
    }
}
