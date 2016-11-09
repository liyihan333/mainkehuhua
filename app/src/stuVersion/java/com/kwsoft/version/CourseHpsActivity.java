package com.kwsoft.version;

import com.kwsoft.kehuhua.adcustom.base.BaseActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.kwsoft.kehuhua.adcustom.R;
import com.kwsoft.version.adapter.CourseAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/8 0008.
 */

public class CourseHpsActivity extends BaseActivity {
    public ListView listView;
    private List<Map<String, String>> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        initView();

        Map<String, String> map = new HashMap<>();
        map.put("time", "2015-06-16");
        map.put("title", "少儿一对一英语基础班");
        map.put("homework", "课后内容");
        map.put("teachName", "李小璐");
        map.put("teachContent", "第一阶段第一阶段第一阶段第一阶段第一阶段第一阶段第一阶段第一阶段第一阶段第一阶段第一阶段第一阶段");
        map.put("attence", "考勤");
        list.add(map);
        list.add(map);
        list.add(map);
        list.add(map);
        CourseAdapter adapter = new CourseAdapter(this,list);
        listView.setAdapter(adapter);
    }

    @Override
    public void initView() {
        listView = (ListView) findViewById(R.id.lv_listview);
    }
}
