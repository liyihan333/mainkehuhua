package com.kwsoft.kehuhua.hampson.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.kwsoft.kehuhua.adcustom.R;
import com.kwsoft.kehuhua.hampson.adapter.StageTestAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**

 */
public class StageTestFragment extends Fragment {
    @Bind(R.id.tv_year)
    TextView tv_year;

    @Bind(R.id.lv_listview)
    ListView listView;
    private List<Map<String, String>> list = new ArrayList<>();
    private static final String TAG = "CourseHpsActivity";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_stage_test_list, container, false);
        ButterKnife.bind(this, view);
        Map<String, String> map = new HashMap<>();
        map.put("month", "12月");
        map.put("day", "31日");
        map.put("title", "第二阶段模拟测试");
        map.put("score", "120");
        map.put("contentTitle", "成绩描述");
        map.put("content", "第一阶段第一阶段第一阶段第一阶段第一阶段第一阶段第一阶段第一阶段第一阶段第一阶段第一阶段第一阶段");
        list.add(map);
        list.add(map);
        list.add(map);
        list.add(map);
        StageTestAdapter adapter = new StageTestAdapter(getActivity(),list);
        listView.setAdapter(adapter);
        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
