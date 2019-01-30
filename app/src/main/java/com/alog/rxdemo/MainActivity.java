package com.alog.rxdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.alog.netlibrary.RxManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RxManager.getInstance().getNet();
    }
}
