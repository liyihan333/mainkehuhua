package com.kwsoft.version;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.kwsoft.kehuhua.adcustom.R;
import com.kwsoft.kehuhua.adcustom.base.BaseActivity;
import com.kwsoft.kehuhua.config.Constant;
import com.kwsoft.kehuhua.widget.CommonToolbar;
import com.kwsoft.version.adapter.TodayCourseTabAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/11 0011.
 */

public class TodayCourseTabActivity extends BaseActivity {
    public ListView lv_listview;
    public List<Map<String, String>> list = new ArrayList<>();
    private CommonToolbar mToolbar;
    private String titleName;//顶栏名称

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_course);

        initView();

        initData();


    }

    private void initData() {
        Intent intent = getIntent();
        titleName = intent.getStringExtra("titleName");
        mToolbar.setTitle(titleName);

        Map<String, String> map = new HashMap<>();
        map.put("courseName", "limingfei");
        map.put("courseTime", "2012-10-13");
        list.add(map);
        list.add(map);
        list.add(map);

        TodayCourseTabAdapter adapter = new TodayCourseTabAdapter(list, this);
        lv_listview.setAdapter(adapter);
    }

    @Override
    public void initView() {
        lv_listview = (ListView) findViewById(R.id.lv_listview);

        mToolbar = (CommonToolbar) findViewById(R.id.common_toolbar);
        mToolbar.setBackgroundColor(getResources().getColor(Constant.topBarColor));

        mToolbar.setRightButtonIcon(getResources().getDrawable(R.mipmap.often_more)); //右侧pop
        mToolbar.setLeftButtonOnClickListener(new View.OnClickListener() { //左侧返回按钮
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
