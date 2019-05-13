package com.renbo.newsproject.splash.activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.renbo.newsproject.R;
import com.renbo.newsproject.splash.bean.Ads;
import com.renbo.newsproject.splash.util.Constant;
import com.renbo.newsproject.splash.util.JsonUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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

        getAds();

    }

    /* 获取广告数据 */
    public void getAds() {
        // 1.创建 OkHttpClient 实例对象
        OkHttpClient client = new OkHttpClient();
        // 2.创建 Request 对象
        Request request = new Request.Builder()
                .url(Constant.ADS_URL)
                .get()
                .build();
        // 3.执行 Request 请求（异步执行）
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 请求失败
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // 获取接口的数据
                    String data = response.body().string();
                    // 请求成功
                    Log.i("okHttp的请求结果：", data);
                    // Json字符串转对象
                    Ads ads = JsonUtil.parseJson(data, Ads.class);
                    if (null != ads) {
                        // 数据解析成功
                        Log.i("数据解析成功", ads.toString());
                    }
                } else {
                    // 请求失败
                    Log.i("okHttp的请求失败：", null);
                }
            }
        });
    }
}
