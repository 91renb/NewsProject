package com.renbo.newsproject;

import android.content.Context;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;

import com.renbo.newsproject.tab.fragment.EmptyFragment;
import com.renbo.newsproject.tab.fragment.HomeFragment;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1.找到 FragmentTabHost
        FragmentTabHost tabHost = (FragmentTabHost)findViewById(R.id.tab_host);
        // 2.设置绑定fragment显示的容器（containerId：是将要显示的Fragment容器）
        tabHost.setup(this, getSupportFragmentManager(), R.id.tab_container);
        // 3.添加选项卡（tag 即是选项卡的 tabId）
        TabHost.TabSpec one = tabHost.newTabSpec("0").setIndicator(getItemView(this));
        tabHost.addTab(one, HomeFragment.class, null);

        TabHost.TabSpec two = tabHost.newTabSpec("1").setIndicator(getItemView(this));
        tabHost.addTab(two, EmptyFragment.class, null);

        TabHost.TabSpec three = tabHost.newTabSpec("2").setIndicator(getItemView(this));
        tabHost.addTab(three, EmptyFragment.class, null);


        // 4.监听切换
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {

            }
        });
    }

    public View getItemView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View title_view = inflater.inflate(R.layout.view_item, null);
        return title_view;
    }
}
