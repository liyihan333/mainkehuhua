package com.kwsoft.kehuhua.hampson.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.kwsoft.kehuhua.adcustom.R;
import com.kwsoft.kehuhua.hampson.adapter.CourseAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**

 */
public class CourseHpsFragment extends Fragment {
    @Bind(R.id.lv_listview)
    ListView listView;
    private List<Map<String, String>> list = new ArrayList<>();
    private static final String TAG = "CourseHpsActivity";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_course_list, container, false);
        ButterKnife.bind(this, view);
        Log.e(TAG, "onCreate: initView()结束" );
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
        CourseAdapter adapter = new CourseAdapter(getActivity(),list);
        Log.e(TAG, "onCreate: adapter初始化结束" );
        listView.setAdapter(adapter);
        Log.e(TAG, "onCreate: setAdapter结束");
        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
