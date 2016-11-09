package com.kwsoft.kehuhua.hampson.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kwsoft.kehuhua.adcustom.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/8 0008.
 */

public class CourseAdapter extends BaseAdapter {
    private Context mContext;
    private List<Map<String, String>> list = new ArrayList<>();

    public CourseAdapter(Context mContext, List<Map<String, String>> list) {
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
            view = LayoutInflater.from(mContext).inflate(R.layout.activity_course_list_item, null);
            //创建ViewHolder持有类
            holder = new ViewHolder();
            //将每个控件的对象保存到持有类中
            holder.tv_time = (TextView) view.findViewById(R.id.tv_time);
            holder.tv_title = (TextView) view.findViewById(R.id.tv_title);
            holder.tv_homework = (TextView) view.findViewById(R.id.tv_homework);
            holder.tv_teach_name = (TextView) view.findViewById(R.id.tv_teach_name);
            holder.tv_teach_content = (TextView) view.findViewById(R.id.tv_teach_content);
            holder.tv_attence = (TextView) view.findViewById(R.id.tv_attence);
            holder.iv_attence = (ImageView) view.findViewById(R.id.iv_attence);
            holder.iv_homework = (ImageView) view.findViewById(R.id.iv_homework);
            //将每个convertView对象中设置这个持有类对象
            view.setTag(holder);
        }
        //每次需要使用的时候都会拿到这个持有类
        holder = (ViewHolder) view.getTag();
        Map<String, String> map = list.get(i);
        //然后可以直接使用这个类中的控件，对控件进行操作，而不用重复去findViewById了
        holder.tv_time.setText(map.get("time"));
        holder.tv_title.setText(map.get("title"));
        holder.tv_homework.setText(map.get("homework"));
        holder.tv_teach_name.setText(map.get("teachName"));
        holder.tv_teach_content.setText(map.get("teachContent"));
        holder.tv_attence.setText(map.get("attence"));

        return view;
    }

    class ViewHolder {
        TextView tv_time, tv_title,tv_teach_name , tv_teach_content, tv_attence,tv_homework;
        ImageView iv_attence,iv_homework;
    }
}
