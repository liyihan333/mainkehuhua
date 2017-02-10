package com.kwsoft.kehuhua.hampson.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.kwsoft.kehuhua.adcustom.R;
import com.kwsoft.kehuhua.config.Constant;
import com.kwsoft.kehuhua.hampson.view.FlexBoxLayout;
import com.kwsoft.version.StuPra;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2016/11/8 0008.
 */

public class CourseRatingBarAdapter extends BaseAdapter {
    private Context mContext;
    private List<List<Map<String, String>>> list = new ArrayList<>();

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


    public CourseRatingBarAdapter(Context mContext, List<List<Map<String, String>>> list) {
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
//        ViewHolder holder = null;

        List<Map<String, String>> map = list.get(i);
        view = LayoutInflater.from(mContext).inflate(R.layout.activity_course_ratingbar_star_list_item, null);

        Log.e(TAG, "getView: list " + map.toString());
        try {

            TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
            TextView my_ping_jia = (TextView) view.findViewById(R.id.my_ping_jia);

            TextView tv_teach_content = (TextView) view.findViewById(R.id.tv_teach_content);
            TextView tv_teach_name = (TextView) view.findViewById(R.id.tv_teach_name);
            TextView teach_name_title = (TextView) view.findViewById(R.id.teach_name_title);
            View star0 = view.findViewById(R.id.star0);
            View star1 = view.findViewById(R.id.star1);
            View star2 = view.findViewById(R.id.star2);
            View star3 = view.findViewById(R.id.star3);
            View star4 = view.findViewById(R.id.star4);
            FlexBoxLayout ll_cb_layout = (FlexBoxLayout) view.findViewById(R.id.autolayout);
            //满意度，字段选择
//        FlexBoxLayout ll_cb_layout = (FlexBoxLayout) view.findViewById(R.id.autolayout);
//            ll_cb_layout.setHorizontalSpace(17);
//            ll_cb_layout.setVerticalSpace(10);
            ll_cb_layout.setHorizontalSpace(10);
            ll_cb_layout.setVerticalSpace(10);
            ll_cb_layout.removeAllViews();
            //然后可以直接使用这个类中的控件，对控件进行操作，而不用重复去findViewById了

            String xingJi = "", pingJiaMiaoShu , pingjiaMiaoShuSec;
            String allItemData = map.get(0).get("allItemData");
            Map<String, Object> allItemDataMap = JSON.parseObject(allItemData,
                    new TypeReference<Map<String, Object>>() {
                    });
            String DIC_AFM_10 = String.valueOf(allItemDataMap.get("DIC_AFM_10"));
            Log.e(TAG, "getView: DIC_AFM_10 "+DIC_AFM_10 );
            if (DIC_AFM_10.contains("一对一")) {
                //一对一
                //课程名称
                //   tv_title.setText(map.get(1).get("fieldCnName2"));
                tv_title.setText(map.get(6).get("fieldCnName2"));
                //我的评价
               //my_ping_jia.setText(map.get(2).get("fieldCnName"));
                my_ping_jia.setText(map.get(4).get("fieldCnName"));
                //老师标题和老师名字
                teach_name_title.setText(map.get(0).get("fieldCnName"));
                tv_teach_name.setText(map.get(0).get("fieldCnName2"));
                pingJiaMiaoShu = map.get(7).get("fieldCnName2");
                // String pingJiaMiaoShu = map.get(4).get("fieldCnName2");
                Log.e(TAG, "getView: map " + map.toString());
                Log.e(TAG, "getView: 总数 " + (map.size() - 1) + " 现在执行到 pingJiaMiaoShu");

                //获取星级数字
                xingJi = map.get(2).get("fieldCnName2");
                //获取最底层评价描述
                pingjiaMiaoShuSec = String.valueOf(map.get(4).get("fieldCnName2"));
            } else {
                //班课
                //课程名称
                //tv_title.setText(map.get(1).get("fieldCnName2"));
                tv_title.setText(map.get(0).get("fieldCnName2"));
                //我的评价
                //my_ping_jia.setText(map.get(2).get("fieldCnName"));
                my_ping_jia.setText(map.get(4).get("fieldCnName"));
                //老师标题和老师名字
                teach_name_title.setText(map.get(0).get("fieldCnName"));
                tv_teach_name.setText(map.get(0).get("fieldCnName2"));
                pingJiaMiaoShu = map.get(6).get("fieldCnName2");
                // String pingJiaMiaoShu = map.get(4).get("fieldCnName2");
                Log.e(TAG, "getView: map " + map.toString());
                Log.e(TAG, "getView: 总数 " + (map.size() - 1) + " 现在执行到 pingJiaMiaoShu");

                //获取星级数字
                xingJi = map.get(2).get("fieldCnName2");
                //获取最底层评价描述
                pingjiaMiaoShuSec = String.valueOf(map.get(4).get("fieldCnName2"));
            }

            if (pingJiaMiaoShu != null && pingJiaMiaoShu.length() > 0) {
                String[] tags = pingJiaMiaoShu.split(",");
                Log.e(TAG, "getView: pingJiaMiaoShu " + pingJiaMiaoShu);
//                ll_cb_layout.removeAllViews();
                for (String tag : tags) {
                    TextView textview = (TextView) LayoutInflater.from(mContext).inflate(R.layout.assess_list_cb_item, null);
                    Log.e("tag", tag + "?" + tags.length);
                    textview.setText(tag);
                    ll_cb_layout.addView(textview);
                }
            }
            ll_cb_layout.setVisibility(View.VISIBLE);
            switch (xingJi) {
                case "五星":
                    star0.setBackgroundResource(R.mipmap.star_ratingbar_full);
                    star1.setBackgroundResource(R.mipmap.star_ratingbar_full);
                    star2.setBackgroundResource(R.mipmap.star_ratingbar_full);
                    star3.setBackgroundResource(R.mipmap.star_ratingbar_full);
                    star4.setBackgroundResource(R.mipmap.star_ratingbar_full);
                    break;
                case "四星":
                    star0.setBackgroundResource(R.mipmap.star_ratingbar_full);
                    star1.setBackgroundResource(R.mipmap.star_ratingbar_full);
                    star2.setBackgroundResource(R.mipmap.star_ratingbar_full);
                    star3.setBackgroundResource(R.mipmap.star_ratingbar_full);
                    star4.setBackgroundResource(R.mipmap.star_ratingbar_empty);

                    break;
                case "三星":
                    star0.setBackgroundResource(R.mipmap.star_ratingbar_full);
                    star1.setBackgroundResource(R.mipmap.star_ratingbar_full);
                    star2.setBackgroundResource(R.mipmap.star_ratingbar_full);
                    star3.setBackgroundResource(R.mipmap.star_ratingbar_empty);
                    star4.setBackgroundResource(R.mipmap.star_ratingbar_empty);
                    break;
                case "两星":
                    star0.setBackgroundResource(R.mipmap.star_ratingbar_full);
                    star1.setBackgroundResource(R.mipmap.star_ratingbar_full);
                    star2.setBackgroundResource(R.mipmap.star_ratingbar_empty);
                    star3.setBackgroundResource(R.mipmap.star_ratingbar_empty);
                    star4.setBackgroundResource(R.mipmap.star_ratingbar_empty);
                    break;
                case "一星":
                    star0.setBackgroundResource(R.mipmap.star_ratingbar_full);
                    star1.setBackgroundResource(R.mipmap.star_ratingbar_empty);
                    star2.setBackgroundResource(R.mipmap.star_ratingbar_empty);
                    star3.setBackgroundResource(R.mipmap.star_ratingbar_empty);
                    star4.setBackgroundResource(R.mipmap.star_ratingbar_empty);
                    break;
                default:
                    star0.setBackgroundResource(R.mipmap.star_ratingbar_empty);
                    star1.setBackgroundResource(R.mipmap.star_ratingbar_empty);
                    star2.setBackgroundResource(R.mipmap.star_ratingbar_empty);
                    star3.setBackgroundResource(R.mipmap.star_ratingbar_empty);
                    star4.setBackgroundResource(R.mipmap.star_ratingbar_empty);
                    break;

            }

            if (!pingjiaMiaoShuSec.equals("") || !pingjiaMiaoShuSec.equals("null")) {

                tv_teach_content.setText(pingjiaMiaoShuSec);
            } else {
                tv_teach_content.setText(R.string.no_eval_content);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    private static final String TAG = "CourseRatingBarAdapter";

//    class ViewHolder {
//        TextView tv_title, tv_teach_name, tv_teach_content, teach_name_title, my_ping_jia;
//        View star0, star1, star2, star3, star4;
////        FlexBoxLayout ll_cb_layout;
//        //RatingBar ratingbar;
//        //AutoNextLineLinearlayout ll_cb_layout;
//    }

    public void clear() {
        list.removeAll(list);
        notifyDataSetChanged();
    }

    public void addData(List<List<Map<String, String>>> addData) {
        list.addAll(addData);
        notifyDataSetChanged();
    }

    public List<List<Map<String, String>>> getDatas() {
        return list;

    }
}
