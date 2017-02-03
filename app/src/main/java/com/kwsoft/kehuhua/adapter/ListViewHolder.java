package com.kwsoft.kehuhua.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kwsoft.kehuhua.adcustom.R;

/**
 * Created by Administrator on 2016/10/21 0021.
 */

public class ListViewHolder extends RecyclerView.ViewHolder {
    final View mView;
    final TextView studentName,
            left1, left2, left3, left4, //left5, left6,
            right1, right2, right3, right4; //right5, right6,
    //    ,click_open_btn;
    LinearLayout click_open;
    LinearLayout dash_ll;
    LinearLayout list_opera_layout;

    Button list_opera0, list_opera1, list_opera2;
    TextView list_opera0_tv, list_opera1_tv, list_opera2_tv;

    View line_view1,line_view2;

    ListViewHolder(View view) {
        super(view);
        mView = view;
        studentName = (TextView) view.findViewById(R.id.stu_name);
        left1 = (TextView) view.findViewById(R.id.left1);
        left2 = (TextView) view.findViewById(R.id.left2);
        left3 = (TextView) view.findViewById(R.id.left3);
        left4 = (TextView) view.findViewById(R.id.left4);
//        left5 = (TextView) view.findViewById(left5);
//        left6 = (TextView) view.findViewById(left6);

        right1 = (TextView) view.findViewById(R.id.right1);
        right2 = (TextView) view.findViewById(R.id.right2);
        right3 = (TextView) view.findViewById(R.id.right3);
        right4 = (TextView) view.findViewById(R.id.right4);
//        right5 = (TextView) view.findViewById(right5);
//        right6 = (TextView) view.findViewById(right6);

        click_open = (LinearLayout) view.findViewById(R.id.click_open);
        //click_open_btn = (TextView) view.findViewById(R.id.click_open_btn);

        dash_ll = (LinearLayout) view.findViewById(R.id.dash_ll);
        //初始化三个按钮
        list_opera_layout = (LinearLayout) view.findViewById(R.id.list_opera_layout);
        list_opera0 = (Button) view.findViewById(R.id.list_opera0);
        list_opera1 = (Button) view.findViewById(R.id.list_opera1);
        list_opera2 = (Button) view.findViewById(R.id.list_opera2);
        // list_opera2=(LinearLayout)view.findViewById(R.id.list_opera2);
        list_opera0_tv= (TextView) view.findViewById(R.id.list_opera0_tv);
        list_opera1_tv= (TextView) view.findViewById(R.id.list_opera1_tv);
        list_opera2_tv= (TextView) view.findViewById(R.id.list_opera2_tv);
        line_view1 = view.findViewById(R.id.line_view1);
        line_view2 = view.findViewById(R.id.line_view2);
    }
}
