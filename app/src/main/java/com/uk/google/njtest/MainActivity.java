package com.uk.google.njtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private TextView mTvAcceptNumber;
    private FrameLayout mFlInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTvAcceptNumber = (TextView) findViewById(R.id.tv_acceptNumber);
        mFlInfo = (FrameLayout) findViewById(R.id.fl_info);
        mTvAcceptNumber.setText(Html.fromHtml("55635粉丝<br/><b><tt>粉丝</tt></b>"));
        HashMap<String, String> kv = new HashMap<>();
        kv.put("出生日期","1993-02-12 ");
        kv.put("城市",". ");
        kv.put("职业","实业浮现 ");
        kv.put("身高","163cm ");
        kv.put("魅力部位",". ");
        kv.put("喜欢的城市",". ");
        kv.put("最喜欢的歌手",". ");
        kv.put("性取向","异性恋 ");
        kv.put("最喜欢的电影",". ");

        MyFlowLayout myFlowLayout = new MyFlowLayout(this);
        Set<String> strings = kv.keySet();
        for (String key:strings){
            TextView textView = new TextView(this);
            textView.setText(Html.fromHtml(key+":<b><tt>"+kv.get(key)+"</tt></b>"));
            myFlowLayout.addView(textView);
        }
        mFlInfo.addView(myFlowLayout);
    }
}
