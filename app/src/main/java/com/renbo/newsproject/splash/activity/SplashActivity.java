package com.renbo.newsproject.splash.activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.renbo.newsproject.R;

public class SplashActivity extends AppCompatActivity {
    ImageView adsImageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置全屏显示（隐藏系统状态栏），支持Android2.0以上
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // 沉浸式，支持Android4.4以上

        setContentView(R.layout.activity_splash);

        adsImageView = (ImageView) findViewById(R.id.ads);

    }
}
