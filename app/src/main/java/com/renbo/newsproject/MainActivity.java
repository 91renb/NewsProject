package com.renbo.newsproject;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.renbo.newsproject.splash.TimeView;

public class MainActivity extends AppCompatActivity {

    TimeView timeView;
    int length = 2 * 1000;
    int space = 100;
    int now = 0;

    Handler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timeView = findViewById(R.id.time);

        // 刷新一个控件
        timeView.setAngle(200);

        int total = length / space;

        // 内存泄漏提示
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                // 接收消息
                if (msg.what == 0) {
                    int now = msg.arg1;
                    if (now <= total) {
                        timeView.setProgress(total, now);
                    } else {
                        // 移除这个任务
                        mHandler.removeCallbacks(refreshTask);
                    }
                }
            }
        };
        // 执行一个任务
        mHandler.post(refreshTask);
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
}
