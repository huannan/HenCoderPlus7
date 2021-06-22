package com.hencoder.blockcanary;

import android.app.Application;

import com.github.moduth.blockcanary.BlockCanary;
import com.github.moduth.blockcanary.BlockCanaryContext;

import org.jetbrains.annotations.NotNull;

import timber.log.Timber;

public class TestApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        BlockCanary.install(this, new BlockCanaryContext()).start();
        Timber.plant(new Timber.DebugTree() {
            @Override
            protected String createStackElementTag(@NotNull StackTraceElement element) {
                return String.format(" (%s:%s)", element.getFileName(), element.getLineNumber());
            }
        });
    }
}
