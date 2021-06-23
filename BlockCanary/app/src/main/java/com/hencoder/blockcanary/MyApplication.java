package com.hencoder.blockcanary;

import android.app.Application;
import android.os.Looper;
import android.util.Printer;

import com.github.moduth.blockcanary.BlockCanaryContext;
import com.github.moduth.blockcanary.internal.BlockInfo;
import com.hencoder.blockcanary.source.BlockCanary;

import org.jetbrains.annotations.NotNull;

import timber.log.Timber;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // BlockCanary初始化
        // BlockCanary.install(this, new AppBlockCanaryContext()).start();

        // BlockCanary原理
        BlockCanary.getInstance().start();

        Timber.plant(new Timber.DebugTree() {
            @Override
            protected String createStackElementTag(@NotNull StackTraceElement element) {
                // 通过这种方式可以方便地从Logcat中就跳转具体代码
                return String.format("(%s:%s)", element.getFileName(), element.getLineNumber());
            }
        });
    }

    public static class AppBlockCanaryContext extends BlockCanaryContext {
        /**
         * Config block threshold (in millis), dispatch over this duration is regarded as a BLOCK. You may set it
         * from performance of device.
         *
         * @return threshold in mills
         */
        @Override
        public int provideBlockThreshold() {
            return 1000;
        }

        /**
         * Path to save log, like "/blockcanary/", will save to sdcard if can.
         *
         * @return path of log files
         */
        @Override
        public String providePath() {
            return provideContext().getExternalFilesDir("blockcanary").getAbsolutePath();
        }
    }
}
