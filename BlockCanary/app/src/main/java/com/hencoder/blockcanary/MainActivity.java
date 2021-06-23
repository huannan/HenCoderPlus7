package com.hencoder.blockcanary;

import android.Manifest;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.tbruyelle.rxpermissions3.RxPermissions;

import hugo.weaving.DebugLog;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Consumer;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

    @DebugLog
    public void c() {
        SystemClock.sleep(500);
    }
}