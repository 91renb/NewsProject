package com.renbo.newsproject.splash.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.renbo.newsproject.R;
import com.renbo.newsproject.splash.bean.Action;
import com.renbo.newsproject.splash.bean.Ads;
import com.renbo.newsproject.splash.bean.AdsDetail;
import com.renbo.newsproject.util.Constant;
import com.renbo.newsproject.util.JsonUtil;

import java.io.IOException;
import java.util.List;
import java.util.Random;

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
                        List<AdsDetail> list = ads.getAds();
                        Random random = new Random();
                        int index = random.nextInt(list.size());
                        AdsDetail detail = list.get(index);
                        List<String> res_url = detail.getRes_url();
                        if (null != res_url) {
                            String imageUrl = res_url.get(0);
                            if (!TextUtils.isEmpty(imageUrl)) {
                                Log.i("显示图片路径：", imageUrl);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // 在主线程中更新UI
                                        showImage(imageUrl);

                                    }
                                });


                                // 给图片设置点击事件
                                adsImageView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Action action = detail.getAction_params();
                                        String pageUrl = action.getLink_url();
                                        if (null != action && !TextUtils.isEmpty(pageUrl)) {
                                            // 跳转新的页面
                                            Intent intent = new Intent();
                                            intent.setClass(SplashActivity.this, WebViewActivity.class);
                                            intent.putExtra(WebViewActivity.PAGE_URL, pageUrl);
                                            startActivity(intent);
                                        }
                                    }
                                });
                            }
                        }
                    } else {
                        Log.i("数据解析失败", null);
                    }
                } else {
                    // 请求失败
                    Log.i("okHttp的请求失败：", null);
                }
            }
        });
    }

    public void showImage(String imageUrl) {
        Glide.with(this).load(imageUrl).placeholder(R.drawable.biz_ad_slogan).into(adsImageView);
    }
}
