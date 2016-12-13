package com.kwsoft.version;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.kwsoft.kehuhua.adcustom.R;
import com.kwsoft.kehuhua.config.Constant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/13 0013.
 *
 */

public class GuideActivity extends Activity implements OnClickListener, OnPageChangeListener{

    private ViewPager vp;
    private GuideAdapter vpAdapter;
    private List<View> views;
   private TextView mBtnStart;
    //引导图片资源
    private static final int[] pics = { R.mipmap.teach_guide1,
            R.mipmap.teach_guide2, R.mipmap.teach_guide3};

    //底部小店图片
    private ImageView[] dots ;

    //记录当前选中位置
    private int currentIndex;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        sPreferences = getSharedPreferences(Constant.proId, MODE_PRIVATE);
        views = new ArrayList<View>();
        getIntenData();
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        //初始化引导图片列表
        for(int i=0; i<pics.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(mParams);
            iv.setImageResource(pics[i]);
            views.add(iv);
        }
        vp = (ViewPager) findViewById(R.id.viewpager);
        //初始化Adapter
        vpAdapter = new GuideAdapter(views);
        vp.setAdapter(vpAdapter);
        //绑定回调
        vp.setOnPageChangeListener(this);

        //初始化底部小点
        initDots();

    }


    String menuData;
    private void getIntenData() {

        Intent intent = getIntent();
        menuData = intent.getStringExtra("jsonArray");
        Log.e(TAG, "getIntenData: menuData "+menuData);
    }

    private void initDots() {
        mBtnStart= (TextView) findViewById(R.id.to_main_page);
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);

        dots = new ImageView[pics.length];

        //循环取得小点图片
        for (int i = 0; i < pics.length; i++) {
            dots[i] = (ImageView) ll.getChildAt(i);
            dots[i].setEnabled(true);//都设为灰色
            dots[i].setOnClickListener(this);
            dots[i].setTag(i);//设置位置tag，方便取出与当前位置对应
        }

        currentIndex = 0;
        dots[currentIndex].setEnabled(false);//设置为白色，即选中状态
    }

    /**
     *设置当前的引导页
     */
    private void setCurView(int position)
    {
        if (position < 0 || position >= pics.length) {
            return;
        }

        vp.setCurrentItem(position);
    }

    /**
     *这只当前引导小点的选中
     */
    private void setCurDot(int positon)
    {
        if (positon < 0 || positon > pics.length - 1 || currentIndex == positon) {
            return;
        }

        dots[positon].setEnabled(false);
        dots[currentIndex].setEnabled(true);

        currentIndex = positon;
    }

    //当滑动状态改变时调用
    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub

    }

    //当当前页面被滑动时调用
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    //当新的页面被选中时调用
    @Override
    public void onPageSelected(int arg0) {
        //设置底部小点选中状态
        Log.e(TAG, "onPageSelected: arg0 "+arg0);
        setCurDot(arg0);
        mBtnStart.setVisibility(arg0 == views.size() - 1 ? View.VISIBLE : View.GONE);
        mBtnStart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "onClick: 是否走点击进入主页");
                toMainPage();
            }
        });
    }

    private static final String TAG = "GuideActivity";
    @Override
    public void onClick(View v) {
        int position = (Integer)v.getTag();
        setCurView(position);
        setCurDot(position);
        Log.e(TAG, "onClick: position "+position);

    }


    private SharedPreferences sPreferences;
    @SuppressWarnings("unchecked")
    public void toMainPage(){
        Log.e(TAG, "toMainPage: menuData "+Constant.menuData);
        try {
            Map<String, Object> menuMap = JSON.parseObject(Constant.menuData,
                    new TypeReference<Map<String, Object>>() {
                    });
            Log.e(TAG, "toMainPage: 监测点1");
            Map<String, Object> loginfo = (Map<String, Object>) menuMap.get("loginInfo");
            String userid = String.valueOf(loginfo.get("USERID"));
            Constant.USERID = String.valueOf(loginfo.get("USERID"));
            sPreferences.edit().putString("userid", userid).apply();
            Log.e(TAG, "toMainPage: 监测点2");
            List<Map<String, Object>> menuListMap1 = (List<Map<String, Object>>) menuMap.get("roleFollowList");
            List<Map<String, Object>> menuListMap2 = (List<Map<String, Object>>) menuMap.get("menuList");
            // List<Map<String, Object>> menuListMap3 = (List<Map<String, Object>>) menuMap.get("hideMenuList");
            List<Map<String, Object>> menuListMap3 = (List<Map<String, Object>>) menuMap.get("personInfoList");//个人资料
            List<Map<String, Object>> menuListMap5 = (List<Map<String, Object>>) menuMap.get("feedbackInfoList");//反馈信息
            List<Map<String, Object>> menuListMap6 = (List<Map<String, Object>>) menuMap.get("homePageList");//今日课表、明日课表
            Log.e(TAG, "toMainPage: 监测点3");
            if (menuMap.containsKey("teaMongoId")) {
                String teaMongoId = menuMap.get("teaMongoId").toString();
                Constant.teaMongoId = teaMongoId;
            }
            Log.e(TAG, "toMainPage: 监测点4");
            Intent intent = new Intent();
            intent.setClass(GuideActivity.this, StuMainActivity.class);
            intent.putExtra("jsonArray", JSON.toJSONString(menuListMap1));
            intent.putExtra("menuDataMap", JSON.toJSONString(menuListMap2));
            intent.putExtra("hideMenuList", JSON.toJSONString(menuListMap3));
            intent.putExtra("feedbackInfoList", JSON.toJSONString(menuListMap5));
            intent.putExtra("homePageList", JSON.toJSONString(menuListMap6));//今明日课表
            startActivity(intent);
            Log.e(TAG, "toMainPage: 监测点5");
            finish();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "toMainPage: 走主页出现异常");
        }

    }
}