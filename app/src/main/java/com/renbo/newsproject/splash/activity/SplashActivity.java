package com.renbo.newsproject.splash.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.renbo.newsproject.MainActivity;
import com.renbo.newsproject.R;
import com.renbo.newsproject.splash.OnTimeClickListener;
import com.renbo.newsproject.splash.TimeView;
import com.renbo.newsproject.splash.bean.Action;
import com.renbo.newsproject.splash.bean.Ads;
import com.renbo.newsproject.splash.bean.AdsDetail;
import com.renbo.newsproject.util.Constant;
import com.renbo.newsproject.util.JsonUtil;

import org.w3c.dom.Text;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SplashActivity extends AppCompatActivity {
    ImageView adsImageView;
    TimeView timeView;
    int length = 3 * 1000;
    int space = 100;
    int now = 0;

    MyHandler mHandler;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置全屏显示（隐藏系统状态栏），支持Android2.0以上
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // 沉浸式(全屏显示)，支持Android4.4以上
        //View decorView = getWindow().getDecorView();
        //decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE);

        setContentView(R.layout.activity_splash);

        adsImageView = (ImageView) findViewById(R.id.ads);
        timeView = findViewById(R.id.time);
        // 刷新一个控件
        timeView.setAngle(200);
        // 按钮点击事件
        timeView.setListener(new OnTimeClickListener() {
            @Override
            public void onClickSkip(View view) {
                // 移除这个定时任务
                mHandler.removeCallbacks(refreshTask);
                gotoMain();
            }
        });

        getAds();

        // 内存泄漏提示
//        mHandler = new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
//                // 接收消息
//                if (msg.what == 0) {
//                    int now = msg.arg1;
//                    if (now <= total) {
//                        timeView.setProgress(total, now);
//                    } else {
//                        // 移除这个定时任务
//                        mHandler.removeCallbacks(refreshTask);
//                        gotoMain();
//                    }
//                }
//            }
//        };

        mHandler = new MyHandler(this);
        // 执行一个任务
        mHandler.post(refreshTask);
    }

    // 使用静态内部类切断访问activity，避免内存泄漏
    static class MyHandler extends Handler {
        // 弱引用（jvm是无法保证它的存活）
        WeakReference<SplashActivity> activity;
        public MyHandler(SplashActivity activity) {
            this.activity = new WeakReference<SplashActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            SplashActivity act = activity.get();
            if (null == act) {
                return;
            }
            int total = act.length / act.space;
            // 接收消息
            if (msg.what == 0) {
                int now = msg.arg1;
                if (now <= total) {
                    act.timeView.setProgress(total, now);
                } else {
                    // 移除这个定时任务
                    this.removeCallbacks(act.refreshTask);
                    act.gotoMain();
                }
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // 移除这个定时任务
        mHandler.removeCallbacks(refreshTask);
    }

    private void gotoMain() {
        // 点击直接进入首页
        Intent intent = new Intent();
        intent.setClass(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    Runnable refreshTask = new Runnable() {
        @Override
        public void run() {
            // 发送消息
            // 重新新建 message
            // Message msg = new Message();
            // 消息池中复用 message
            Message msg = mHandler.obtainMessage(0);
            msg.arg1 = now;
            mHandler.sendMessage(msg);

            // space 毫秒后重新执行这个任务
            mHandler.postDelayed(this, space);
            now ++;
        }
    };

    /* 获取广告数据 */
    public void getAds() {
        // 1.创建 OkHttpClient 实例对象
        final OkHttpClient client = new OkHttpClient();
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
                        if (null != detail) {
                            // 切换到主线程
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // 在主线程中更新UI
                                    showImage(detail);
                                }
                            });
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

    public void showImage(AdsDetail detail) {
        List<String> res_url = detail.getRes_url();
        if (null != res_url) {
            String imageUrl = res_url.get(0);
            if (!TextUtils.isEmpty(imageUrl)) {

                // 显示网络图片
                Glide.with(this).load(imageUrl).into(adsImageView);
            }
        }

        Action action = detail.getAction_params();
        if (null != action) {
            String pageUrl = action.getLink_url();
            if (!TextUtils.isEmpty(pageUrl)) {

                // 给图片设置点击事件
                adsImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 跳转新的页面
                        Intent intent = new Intent();
                        intent.setClass(SplashActivity.this, WebViewActivity.class);
                        intent.putExtra(WebViewActivity.PAGE_URL, pageUrl);
                        startActivity(intent);
                    }
                });

            }
        }
    }
}
