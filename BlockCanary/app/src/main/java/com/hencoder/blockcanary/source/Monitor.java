package com.hencoder.blockcanary.source;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Printer;

import com.github.moduth.blockcanary.internal.BlockInfo;

import timber.log.Timber;

public class Monitor implements Printer {

    private static final long BLOCK_THRESHOLD = 1000L;
    private long mStartTimeMillis;
    private boolean mPrintingStarted = false;
    private final HandlerThread mHandlerThread;
    private final Handler mHandler;
    private final Runnable mDumpStackRunnable;
    private String mStackTrace;
    private final Object LOCK = new Object();

    public Monitor() {
        mHandlerThread = new HandlerThread("BlockCanary");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());
        mDumpStackRunnable = new Runnable() {
            @Override
            public void run() {
                // 抓取堆栈
                StringBuilder stringBuilder = new StringBuilder();
                for (StackTraceElement stackTraceElement : Looper.getMainLooper().getThread().getStackTrace()) {
                    stringBuilder
                            .append(stackTraceElement.toString())
                            .append(BlockInfo.SEPARATOR);
                }
                synchronized (LOCK) {
                    mStackTrace = stringBuilder.toString();
                }
            }
        };
    }

    @Override
    public void println(String x) {
        if (!mPrintingStarted) {
            mStartTimeMillis = System.currentTimeMillis();

            mHandler.removeCallbacks(mDumpStackRunnable);
            mHandler.postDelayed(mDumpStackRunnable, (long) (BLOCK_THRESHOLD * 0.8));

            mPrintingStarted = true;
        } else {
            if (isBlock()) {
                synchronized (LOCK) {
                    Timber.e("出现卡顿，堆栈信息: %s", mStackTrace);
                }
            }

            mHandler.removeCallbacks(mDumpStackRunnable);

            mPrintingStarted = false;
        }
    }

    private boolean isBlock() {
        return System.currentTimeMillis() - mStartTimeMillis > BLOCK_THRESHOLD;
    }
}
