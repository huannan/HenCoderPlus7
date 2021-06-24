package com.hencoder.blockcanary;

import android.Manifest;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Trace;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.tbruyelle.rxpermissions3.RxPermissions;

import hugo.weaving.DebugLog;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Consumer;
import timber.log.Timber;

/**
 * https://ui.perfetto.dev/#!/
 */
public class MainActivity extends AppCompatActivity {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TraceView的使用
        // Debug.startMethodTracing();

        // SysTrace的使用
        Trace.beginSection("onCreate");

        final RxPermissions rxPermissions = new RxPermissions(this);
        compositeDisposable.add(rxPermissions
                .request(Manifest.permission.READ_PHONE_STATE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Throwable {
                        if (granted) {
                            setContentView(R.layout.activity_main);
                        }
                    }
                }));

        // Debug.stopMethodTracing();

        Trace.endSection();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }

    public void click(View view) {
        // BlockCanary由于延时不一定准确，但是能大致定位卡顿问题
        a();
        b();
        c();

        Timber.d("我是测试Log");
    }

    public void a() {
        SystemClock.sleep(700);
    }

    public void b() {
        SystemClock.sleep(100);
    }

    /**
     * 原理AOP
     */
    @DebugLog
    public void c() {
        SystemClock.sleep(500);
    }
}