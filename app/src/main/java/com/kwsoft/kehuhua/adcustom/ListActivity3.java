package com.kwsoft.kehuhua.adcustom;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.kwsoft.kehuhua.adcustom.base.BaseActivity;
import com.kwsoft.kehuhua.config.Constant;
import com.kwsoft.kehuhua.fragments.ListFragment;
import com.kwsoft.kehuhua.hampson.activity.CourseHpsFragment;
import com.kwsoft.kehuhua.hampson.activity.StageTestFragment;
import com.kwsoft.kehuhua.widget.CommonToolbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;

import static com.kwsoft.kehuhua.config.Constant.buttonSet;
import static com.kwsoft.kehuhua.config.Constant.pageId;
import static com.kwsoft.kehuhua.config.Constant.tableId;

public class ListActivity3 extends BaseActivity {

    public CommonToolbar mToolbar;
    private List<Map<String, Object>> childList = new ArrayList<>();
    private PopupWindow toolListPop, childListPop;

    private FragmentManager manager;
    private List<Fragment> mFragments = new ArrayList<>();
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_avtivity3);
        ButterKnife.bind(this);

        initView();
        getDataIntent();//获取初始化数据
    }

    private static final String TAG = "ListActivity3";
    /**
     * 接收菜单传递过来的模块数据包
     */
    public void getDataIntent() {
        Map<String, Object> itemMap = new HashMap<>();
        Intent intent = getIntent();
        String itemData = intent.getStringExtra("itemData");
        if (intent.getStringExtra("childData") != null) {

            String childData = intent.getStringExtra("childData");
            childList = JSON.parseObject(childData,
                    new TypeReference<List<Map<String, Object>>>() {
                    });
        }
        try {
            itemMap = JSON.parseObject(itemData,
                    new TypeReference<Map<String, Object>>() {
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }

        String titleName;
        if (childList.size() > 0) {
            titleName = childList.get(0).get("menuName") + "";
            for (int i=0;i<childList.size();i++) {

                String fragmentTableId = childList.get(i).get("tableId") + "";
                String fragmentPageId = childList.get(i).get("pageId") + "";
                Map<String,String> paramsMap = new HashMap<>();
                paramsMap.put(tableId, fragmentTableId);
                paramsMap.put(pageId, fragmentPageId);
                paramsMap.put(Constant.timeName, "-1");
                String listFragmentData=JSON.toJSONString(paramsMap);
                Bundle listBundle = new Bundle();
                listBundle.putString("listFragmentData", listFragmentData);

                if (fragmentTableId.equals("100")&&fragmentPageId.equals("3377")) {
                    Fragment stageTestFragment = new StageTestFragment();
                    stageTestFragment.setArguments(listBundle);
                    mFragments.add(stageTestFragment);
                    transaction.add(R.id.fragment_container,stageTestFragment);
                    Log.e(TAG, "refreshPage: 学员端走定制化阶段测评页面");

                }else if (fragmentTableId.equals("102")&&fragmentPageId.equals("2308")) {

                    Fragment courseHpsFragment = new CourseHpsFragment();
                    courseHpsFragment.setArguments(listBundle);
                    mFragments.add(courseHpsFragment);
                    transaction.add(R.id.fragment_container, courseHpsFragment);
                    Log.e(TAG, "refreshPage: 学员端走定制化课堂内容（一对一教学日志）页面");
//评价列表有数据之后在空串后加数字，共2个地方需要修改
//                }else if(fragmentTableId.equals("")&&fragmentPageId.equals("")){
//                    Fragment courseRatingBarFragment = new CourseRatingBarFragment();
//                    courseRatingBarFragment.setArguments(listBundle);
//                    mFragments.add(courseRatingBarFragment);
//                    transaction.add(R.id.fragment_container, courseRatingBarFragment);
//                    Log.e(TAG, "refreshPage: 学员端走定制化评价列表展示页面");

                }else {
                    //Log.e("评价列表：",)
                    Fragment listFragment = new ListFragment();
                    listFragment.setArguments(listBundle);
                    mFragments.add(listFragment);
                    transaction.add(R.id.fragment_container,listFragment);
                }



            }

            transaction.replace(R.id.fragment_container, mFragments.get(0));//把f1的界面替换container
            transaction.commit();

            mToolbar.showChildIv();
            mToolbar.setTextTitleOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    childChose();
                }
            });

        } else {
            String fragmentTableId= itemMap.get("tableId") + "";
            String fragmentPageId = itemMap.get("pageId") + "";
            titleName = itemMap.get("menuName") + "";

            Map<String,String> paramsMap = new HashMap<>();
            paramsMap.put(tableId, fragmentTableId);
            paramsMap.put(pageId, fragmentPageId);
            paramsMap.put(Constant.timeName, "-1");
            String listFragmentData=JSON.toJSONString(paramsMap);
            Bundle listBundle = new Bundle();
            listBundle.putString("listFragmentData", listFragmentData);

            Fragment xFragment;

            if (fragmentTableId.equals("100")&&fragmentPageId.equals("3377")) {
                xFragment = new StageTestFragment();
                Log.e(TAG, "refreshPage: 学员端走定制化阶段测评页面");
            }else if (fragmentTableId.equals("102")&&fragmentPageId.equals("2308")) {
                xFragment = new CourseHpsFragment();
                Log.e(TAG, "refreshPage: 学员端走定制化课堂内容（一对一教学日志）页面");

//评价列表有数据之后在空串后加数字，共2个地方需要修改
//                }else if(fragmentTableId.equals("")&&fragmentPageId.equals("")){
//                    xFragment = new CourseRatingBarFragment();
//                    Log.e(TAG, "refreshPage: 学员端走定制化评价列表展示页面");
            }else {
                xFragment = new ListFragment();
            }
            xFragment.setArguments(listBundle);
            transaction.add(R.id.fragment_container,xFragment);
            transaction.replace(R.id.fragment_container, xFragment);//把f1的界面替换container
            transaction.commit();
        }
        mToolbar.setTitle(titleName);
//        Constant.mainTableIdValue = tableId;
//        Constant.mainPageIdValue = pageId;

    }

    //初始化顶栏
    public void initView() {
        mToolbar = (CommonToolbar) findViewById(R.id.common_toolbar);
        mToolbar.setBackgroundColor(getResources().getColor(Constant.topBarColor));
        mToolbar.setRightButtonIcon(getResources().getDrawable(R.mipmap.often_more)); //右侧pop
        mToolbar.setLeftButtonOnClickListener(new View.OnClickListener() { //左侧返回按钮
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        manager= getSupportFragmentManager() ;
        transaction = manager.beginTransaction() ;

    }

    //顶部展开popwindow 选择子菜单切换
    public void childChose() {
        Log.e("TAG", "展开子菜单popWindow");
        try {
            if (childList.size() > 0) {

                if (childListPop != null && childListPop.isShowing()) {
                    childListPop.dismiss();
                } else {


                    final View toolLayout = getLayoutInflater().inflate(
                            R.layout.activity_list_childlist, null);
                    ListView childListPopView = (ListView) toolLayout
                            .findViewById(R.id.child_menu_List);
                    for (int i = 0; i < childList.size(); i++) {
                        childList.get(i).put("image", R.mipmap.often_drop_curriculum);
                    }

                    final SimpleAdapter adapter = new SimpleAdapter(
                            this,
                            childList,
                            R.layout.activity_list_childlist_item,
                            new String[]{"image", "menuName"},
                            new int[]{R.id.childListItemImg, R.id.childListItemName});
                    childListPopView.setAdapter(adapter);
                    // 点击listview中item的处理
                    childListPopView
                            .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> arg0,
                                                        View arg1, int arg2, long arg3) {
//                                    refreshPage(arg2);
                                   String transTitleName= childList.get(arg2).get("menuName") + "";
                                    mToolbar.setTitle(transTitleName);
                                    FragmentTransaction transaction = manager.beginTransaction();
                                    transaction.replace(R.id.fragment_container, mFragments.get(arg2));//把f1的界面替换container
                                    transaction.commit();
                                    // 隐藏弹出窗口
                                    if (childListPop != null && childListPop.isShowing()) {
                                        childListPop.dismiss();
                                    }
                                }
                            });
                    DisplayMetrics metric = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(metric);
                    int width = metric.widthPixels;     // 屏幕宽度（像素）
                    childListPop = new PopupWindow(toolLayout, (width / 3) * 2,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    //设置半透明
                    WindowManager.LayoutParams params = getWindow().getAttributes();
                    getWindow().setAttributes(params);

                    childListPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            WindowManager.LayoutParams params = getWindow().getAttributes();
                            getWindow().setAttributes(params);
                        }
                    });
                    childListPop.setTouchable(true); // 设置popupwindow可点击
                    childListPop.setOutsideTouchable(true); // 设置popupwindow外部可点击
                    childListPop.setFocusable(true); // 获取焦点
                    childListPop.update();
                    toolLayout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                    int delta = width / 6;
                    childListPop.showAsDropDown(mToolbar, delta, 0);
                    childListPop.setTouchInterceptor(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                                childListPop.dismiss();
                                return true;
                            }
                            return false;
                        }
                    });

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void initPopWindowDropdown(View view) {
        //内容，高度，宽度
        toolListPop = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        //动画效果
        toolListPop.setAnimationStyle(R.style.PopupWindowAnimation);
        //菜单背景色
        ColorDrawable dw = new ColorDrawable(0xffffffff);
        toolListPop.setBackgroundDrawable(dw);
        //显示位置
        toolListPop.showAtLocation(getLayoutInflater().inflate(R.layout.activity_list_avtivity2, null), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
        //设置背景半透明
        backgroundAlpha(0.7f);
        //关闭事件
        toolListPop.setOnDismissListener(new popupDismissListener());
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                /*if( popupWindow!=null && popupWindow.isShowing()){
                    popupWindow.dismiss();
                    popupWindow=null;
                }*/
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
                return false;
            }
        });
    }
    /**
     * 设置添加屏幕的背景透明度
     */
    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    /**
     * 添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
     *
     */
    class popupDismissListener implements PopupWindow.OnDismissListener{

        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }

    }

    public void buttonList() {
        try {
            if (toolListPop != null && toolListPop.isShowing()) {
                toolListPop.dismiss();
            } else {
                final View popInflateView = getLayoutInflater().inflate(
                        R.layout.activity_list_buttonlist, null);
                ListView toolListPopView = (ListView) popInflateView
                        .findViewById(R.id.buttonList);
                TextView tv_dismiss = (TextView) popInflateView.findViewById(R.id.tv_dismiss);
                tv_dismiss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toolListPop.dismiss();
                    }
                });
                final SimpleAdapter adapter = new SimpleAdapter(
                        this,
                        buttonSet,
                        R.layout.activity_list_buttonlist_item,
                        new String[]{"buttonName"},
                        new int[]{R.id.listItem});
                toolListPopView.setAdapter(adapter);
                // 点击listview中item的处理
                toolListPopView
                        .setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> arg0,
                                                    View arg1, int arg2, long arg3) {
                                toPage(arg2);
                                // 隐藏弹出窗口
                                if (toolListPop != null && toolListPop.isShowing()) {
                                    toolListPop.dismiss();
                                }
                            }
                        });
                initPopWindowDropdown(popInflateView);
            }
        } catch (Exception e) {
            Toast.makeText(ListActivity3.this, "无按钮数据", Toast.LENGTH_SHORT).show();
        }
    }

    public void toPage(int position) {
        int buttonType = (int) buttonSet.get(position).get("buttonType");
        Map<String, Object> buttonSetItem = buttonSet.get(position);
        switch (buttonType) {
            case 0://添加页面
                Intent intent = new Intent(mContext, OperateDataActivity.class);
                intent.putExtra("itemSet", JSON.toJSONString(buttonSetItem));
                startActivityForResult(intent, 5);
                break;
            case 3://批量删除操作
//              listAdapter.flag = true;
//              listAdapter.notifyDataSetChanged();
//              setGone();
                break;
        }
    }
    

}
