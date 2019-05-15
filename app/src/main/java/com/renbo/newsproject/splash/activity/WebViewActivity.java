package com.renbo.newsproject.splash.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.renbo.newsproject.R;

public class WebViewActivity extends AppCompatActivity {

    public static final String PAGE_URL = "pageUrl";
    WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 接收上个页面传过来的参数
        String pageUrl = (String) getIntent().getSerializableExtra(PAGE_URL);
        setContentView(R.layout.activity_webview);

        webView = (WebView) findViewById(R.id.webview);
        // 启动 JavaScript
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(pageUrl);

        // 处理跳转（url重定向），不要抛到系统浏览器上
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return true;
            }
        });
    }

    /* 处理回退按键 */
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
            return;
        }
        super.onBackPressed();
    }
}
