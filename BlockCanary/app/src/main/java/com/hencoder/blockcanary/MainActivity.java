package com.hencoder.blockcanary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;

import hugo.weaving.DebugLog;
import timber.log.Timber;

@DebugLog
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void click(View view) {
        a();
        b();
        c();

        Timber.d("aaa");
    }

    public void a() {
        SystemClock.sleep(700);
    }

    public void b() {
        SystemClock.sleep(100);
    }

    public void c() {
        SystemClock.sleep(500);
    }
}