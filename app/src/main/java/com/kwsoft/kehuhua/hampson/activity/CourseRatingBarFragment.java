package com.kwsoft.kehuhua.hampson.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.kwsoft.kehuhua.adcustom.R;
import com.kwsoft.kehuhua.hampson.adapter.CourseRatingBarAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**

 */
public class CourseRatingBarFragment extends Fragment {
    @Bind(R.id.lv_listview)
    ListView listView;
    private List<Map<String, String>> list = new ArrayList<>();
    private static final String TAG = "CourseHpsActivity";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_course_ratingbar_star_list, container, false);
        ButterKnife.bind(this, view);
        Map<String, String> map = new HashMap<>();
        map.put("title", "少儿一对一英语基础课程");
        map.put("teachName", "李明福");
        map.put("teachContent", "第一阶段第一阶段第一阶段第一阶段第一阶段第一阶段");
        list.add(map);
        list.add(map);
        list.add(map);
        list.add(map);
        CourseRatingBarAdapter adapter = new CourseRatingBarAdapter(getActivity(),list);
        listView.setAdapter(adapter);
        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
