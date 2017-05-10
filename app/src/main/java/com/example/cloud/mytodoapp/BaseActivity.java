package com.example.cloud.mytodoapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setContentView(int layoutResId) {
        super.setContentView(layoutResId);
    }

    protected abstract void initView();

    protected abstract void initDate();

    protected abstract void initViewDate();

    protected abstract void initListener();
}
