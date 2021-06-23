package com.hencoder.blockcanary.source;

import android.os.Looper;

public final class BlockCanary {

    private BlockCanary() {
    }

    private static final class Holder {
        static final BlockCanary INSTANCE = new BlockCanary();
    }

    public static BlockCanary getInstance() {
        return Holder.INSTANCE;
    }

    public void start() {
        Looper.getMainLooper().setMessageLogging(new Monitor());
    }

}
