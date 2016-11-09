package com.kwsoft.kehuhua.hampson.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kwsoft.kehuhua.adcustom.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/7 0007.
 */

public class StageTestAdapter extends BaseAdapter {
    private Context mContext;
    private List<Map<String, String>> list = new ArrayList<>();

    public StageTestAdapter(Context mContext, List<Map<String, String>> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            //解析布局
            view = LayoutInflater.from(mContext).inflate(R.layout.activity_stage_test_list_item, null);
            //创建ViewHolder持有类
            holder = new ViewHolder();
            //将每个控件的对象保存到持有类中
            holder.tv_month = (TextView) view.findViewById(R.id.tv_month);
            holder.tv_day = (TextView) view.findViewById(R.id.tv_day);
            holder.tv_title = (TextView) view.findViewById(R.id.tv_title);
            holder.tv_score = (TextView) view.findViewById(R.id.tv_score);
            holder.tv_content_title = (TextView) view.findViewById(R.id.tv_content_title);
            holder.tv_content = (TextView) view.findViewById(R.id.tv_content);
            //将每个convertView对象中设置这个持有类对象
            view.setTag(holder);
        }
        //每次需要使用的时候都会拿到这个持有类
        holder = (ViewHolder) view.getTag();
        Map<String, String> map = list.get(i);
        //然后可以直接使用这个类中的控件，对控件进行操作，而不用重复去findViewById了
        holder.tv_month.setText(map.get("month"));
        holder.tv_day.setText(map.get("day"));
        holder.tv_title.setText(map.get("title"));
        holder.tv_score.setText(map.get("score"));
        holder.tv_content_title.setText(map.get("contentTitle"));
        holder.tv_content.setText(map.get("content"));
        return view;
    }

    class ViewHolder {
        TextView tv_month, tv_day, tv_title, tv_score, tv_content_title, tv_content;
    }
}
