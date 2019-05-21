package com.renbo.newsproject;

import android.content.Context;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.renbo.newsproject.tab.fragment.EmptyFragment;
import com.renbo.newsproject.tab.fragment.HomeFragment;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initTabView();
    }

    /*初始化底部tab视图*/
    public void initTabView() {
        // 获取tab的标题（配置在strings.xml中）
        String[] titles = getResources().getStringArray(R.array.tab_title);
        // 获取tab的图标（配置在 xxx_selector.xml文件中）
        int[] icons = new int[]{R.drawable.home_selector,
                R.drawable.video_selector,
                R.drawable.topic_selector,
                R.drawable.mine_selector};
        // 获取fragment数组
        Class[] classes = new Class[]{HomeFragment.class,
                EmptyFragment.class,
                EmptyFragment.class,
                EmptyFragment.class};
        // 1.找到 FragmentTabHost
        FragmentTabHost tabHost = (FragmentTabHost)findViewById(R.id.tab_host);
        // 2.设置绑定fragment显示的容器（containerId：是将要显示的Fragment容器）
        tabHost.setup(this, getSupportFragmentManager(), R.id.tab_container);
        // 3.添加选项卡（tag 即是选项卡的 tabId）
        for (int i = 0; i < titles.length; i++) {
            TabHost.TabSpec tab = tabHost.newTabSpec("" + i);
            tab.setIndicator(getItemView(this, titles, icons, i));
            tabHost.addTab(tab, classes[i], null);
        }

        // 4.监听切换
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {

            }
        });

        // 设置当前选中的tab
        //tabHost.setCurrentTabByTag("1");
        //tabHost.setCurrentTab(1);
    }

    /*获取自定义视图*/
    public View getItemView(Context context, String[] titles, int[] icons, int index) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view_item, null);
        ImageView imageView = (ImageView)view.findViewById(R.id.imageView);
        TextView textView = (TextView)view.findViewById(R.id.textView);
        imageView.setImageResource(icons[index]);
        textView.setText(titles[index]);

        return view;
    }
}
