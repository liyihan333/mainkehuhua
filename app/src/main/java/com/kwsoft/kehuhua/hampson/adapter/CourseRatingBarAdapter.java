package com.kwsoft.kehuhua.hampson.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kwsoft.kehuhua.adcustom.R;
import com.kwsoft.kehuhua.hampson.view.AutoNextLineLinearlayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/8 0008.
 */

public class CourseRatingBarAdapter extends BaseAdapter {
    private Context mContext;
    private List<Map<String, Object>> list = new ArrayList<>();

    private boolean isFristTime = true;

    /**
     * 标签之间的间距 px
     */
    final int itemMargins = 17;

    /**
     * 标签的行间距 px
     */
    final int lineMargins = 10;

    private ViewGroup container = null;



    public CourseRatingBarAdapter(Context mContext, List<Map<String, Object>> list) {
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

        Map<String, Object> map = list.get(i);
        String[] tags = (String[]) map.get("tags");
        if (view == null) {
            //解析布局
            view = LayoutInflater.from(mContext).inflate(R.layout.activity_course_ratingbar_star_list_item, null);
            //创建ViewHolder持有类
            holder = new ViewHolder();
            //将每个控件的对象保存到持有类中
            holder.tv_title = (TextView) view.findViewById(R.id.tv_title);
            holder.tv_teach_name = (TextView) view.findViewById(R.id.tv_teach_name);
            holder.tv_teach_content = (TextView) view.findViewById(R.id.tv_teach_content);
            // holder.ratingbar = (RatingBar) view.findViewById(R.id.ratingbar);
            holder.ll_cb_layout = (AutoNextLineLinearlayout) view.findViewById(R.id.autolayout);
            for (int j =0;j<tags.length;j++){
                CheckBox checkBox = (CheckBox) LayoutInflater.from(mContext).inflate(R.layout.assess_list_cb_item, null);
                Log.e("tag",tags[j]+"?"+tags.length);
                checkBox.setText(tags[j]);
                holder.ll_cb_layout.addView(checkBox);
            }
            //将每个convertView对象中设置这个持有类对象
            view.setTag(holder);
        } else {
            //每次需要使用的时候都会拿到这个持有类
            holder = (ViewHolder) view.getTag();

        }

        //然后可以直接使用这个类中的控件，对控件进行操作，而不用重复去findViewById了
        holder.tv_title.setText(map.get("title")+"");
        holder.tv_teach_name.setText(map.get("teachName")+"");
        holder.tv_teach_content.setText(map.get("teachContent")+"");
      //  holder.ll_cb_layout.removeAllViews();


        return view;
    }

    class ViewHolder {
        TextView tv_title, tv_teach_name, tv_teach_content;
        //RatingBar ratingbar;
        AutoNextLineLinearlayout ll_cb_layout;
    }

}
