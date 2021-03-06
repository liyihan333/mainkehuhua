package com.kwsoft.version.fragment;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.facebook.drawee.view.SimpleDraweeView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.kwsoft.kehuhua.adcustom.BlankActivity;
import com.kwsoft.kehuhua.adcustom.ChartActivity;
import com.kwsoft.kehuhua.adcustom.CourseActivity;
import com.kwsoft.kehuhua.adcustom.ListActivity4;
import com.kwsoft.kehuhua.adcustom.MessagAlertActivity;
import com.kwsoft.kehuhua.adcustom.R;
import com.kwsoft.kehuhua.adcustom.base.BaseActivity;
import com.kwsoft.kehuhua.config.Constant;
import com.kwsoft.kehuhua.hampson.activity.KanBanLRActivity;
import com.kwsoft.kehuhua.urlCnn.EdusStringCallback;
import com.kwsoft.kehuhua.urlCnn.ErrorToast;
import com.kwsoft.kehuhua.utils.DataProcess;
import com.kwsoft.kehuhua.zxing.TestScanActivity;
import com.kwsoft.version.StuInfoActivity;
import com.kwsoft.version.StuLoginActivity;
import com.kwsoft.version.TodayCourseTableActivity;
import com.kwsoft.version.androidRomType.AndtoidRomUtil;
import com.kwsoft.version.view.StudyGridView;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/9/6 0006.
 */
public class StudyFragment extends Fragment implements View.OnClickListener {

    private TextView stuName;
    private StudyGridView homeGridView;
    private List<Map<String, Object>> parentList = new ArrayList<>();
    private int[] image = {R.mipmap.edus_see_today, R.mipmap.edus_see_tomorrow,
            R.mipmap.edus_see_vip, R.mipmap.edus_see_group};
    private int[] imgs2 = {R.mipmap.k1, R.mipmap.k2,
            R.mipmap.k3, R.mipmap.k4, R.mipmap.k5,
            R.mipmap.k6, R.mipmap.k7, R.mipmap.k8,
            R.mipmap.k9, R.mipmap.k10, R.mipmap.k11,
            R.mipmap.k12, R.mipmap.k13, R.mipmap.k14,
            R.mipmap.k15, R.mipmap.k16, R.mipmap.k17,
            R.mipmap.k18, R.mipmap.k19, R.mipmap.k20};
    private GridView gridView;
    private List<Map<String, Object>> menuListAll = new ArrayList<>();
    private List<Map<String, Object>> menuListMap = new ArrayList<>();
    private PullToRefreshScrollView pull_refresh_scrollview;
    private SharedPreferences sPreferences;
    private TextView tvUserrole, tvMonth, tvDay; //角色、日期、星期
    private String monthstr, daystr, enDaystr;
    private Boolean isLogin = false;
    public String arrStr;
    public Bundle arrBundle;
    public String teachUrl, homePageListstr;
    private String todayPageId, tomorrowPageId, todayTableid, tomorrowTableId;//金明日课表page/table
    private List<Map<String, Object>> homePagelistMap = new ArrayList<>();
    SimpleDraweeView stuHeadImage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_study, container, false);
        teachUrl = Constant.sysUrl + Constant.projectLoginUrl;
        Log.e("studyfrg", "studyfrg");
        initView(view);

        ButterKnife.bind(this, view);
        return view;
    }

    public void initView(View view) {
        stuName = (TextView) view.findViewById(R.id.stu_name);
        tvUserrole = (TextView) view.findViewById(R.id.tv_userrole);
        tvMonth = (TextView) view.findViewById(R.id.tv_month);
        tvDay = (TextView) view.findViewById(R.id.tv_day);
//设置首页头像
        stuHeadImage = (SimpleDraweeView) view.findViewById(R.id.stu_head_image);
        String urlStr = Constant.sysUrl + Constant.downLoadFileStr + Constant.teaMongoId;
        updateImage(urlStr);

        try {
            String username = Constant.loginName;
            stuName.setText(username);
            String roleNames = Constant.roleNamesTeach;
            String spStr[] = roleNames.split(",");
            tvUserrole.setText(spStr[0]);
            initDateWeek();
            tvMonth.setText(monthstr);
            tvDay.setText(enDaystr);

        } catch (Exception e) {
            e.printStackTrace();
        }

//        homeGridView = (KanbanGridView) view.findViewById(R.id.home_grid);
        homeGridView = (StudyGridView) view.findViewById(R.id.home_grid);
        homeGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, String> itemData = new HashMap<>();
                String tableId = String.valueOf(parentList.get(position).get("tableId"));
                String pageId = String.valueOf(parentList.get(position).get("penetratePageId"));
                itemData.put("tableId", String.valueOf(parentList.get(position).get("tableId")));
                itemData.put("pageId", String.valueOf(parentList.get(position).get("penetratePageId")));
                itemData.put("menuName", String.valueOf(parentList.get(position).get("cnName")));
                Constant.stu_index = String.valueOf(parentList.get(position).get("ctType"));
                Constant.stu_homeSetId = String.valueOf(parentList.get(position).get("SourceDataId"));
                try {
                    Log.e(TAG, "onItemClick: wyl" + tableId + "//" + pageId + "//" + String.valueOf(parentList.get(position).get("cnName")));
//                    switch (tableId) {
//                        case "19":
//                        case "70":
//                        case "71":
//                            Intent intent = new Intent(getActivity(), KanBanLRActivity.class);
//                            intent.putExtra("itemData", JSON.toJSONString(itemData));
//                            Log.e("itemtdastudy", JSON.toJSONString(itemData));
//                            startActivity(intent);
//                            break;
//                        default:
                    Intent  intent = new Intent();
                            intent.setClass(getActivity(), ListActivity4.class);
                            intent.putExtra("itemData", JSON.toJSONString(itemData));
                            Log.e("itemtdastudy", JSON.toJSONString(itemData));
                            startActivity(intent);
//                            break;
//                    }
//                    Intent intent = new Intent();
//                    intent.setClass(getActivity(), ListActivity4.class);
//                    intent.putExtra("itemData", JSON.toJSONString(itemData));
//                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        gridView = (GridView) view.findViewById(R.id.gridView);
        gridView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        pull_refresh_scrollview = (PullToRefreshScrollView) view.findViewById(R.id.pull_refresh_scrollview);
        pull_refresh_scrollview.getLoadingLayoutProxy().setLastUpdatedLabel(getResources().getString(R.string.data_refresh));
        pull_refresh_scrollview.getLoadingLayoutProxy().setPullLabel(getResources().getString(R.string.Pull_down_refresh));
        pull_refresh_scrollview.getLoadingLayoutProxy().setRefreshingLabel(getResources().getString(R.string.data_refreshing));
        pull_refresh_scrollview.getLoadingLayoutProxy().setReleaseLabel(getResources().getString(R.string.data_will_refresh));
        //上拉、下拉设定
//        pull_refresh_scrollview.setMode(PullToRefreshBase.Mode.BOTH);
        //上拉监听函数
        pull_refresh_scrollview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                //执行刷新函数

                getLoginData(teachUrl);
            }
        });
        //获取ScrollView布局，此文中用不到
        //mScrollView = mPullRefreshScrollView.getRefreshableView();
        getData();
        //菜单列表中的gridview数据
        setMenuModel();
        // initData();
        //  initModel();


        //设置广播
        IntentFilter filter = new IntentFilter(MeFragment.action);
        getActivity().registerReceiver(broadcastReceiver, filter);
    }

    private void setMenuModel() {
        //菜单列表中的gridview数据
        if ((homePageListstr != null) && (homePageListstr.length() > 0)) {
            homePagelistMap = JSON.parseObject(homePageListstr,
                    new TypeReference<List<Map<String, Object>>>() {
                    });
            if ((homePagelistMap != null) && (homePagelistMap.size() > 0)) {
                menuListAll.clear();
                String menuName;
                for (int i = 0; i < homePagelistMap.size(); i++) {
                    Map<String, Object> map = homePagelistMap.get(i);
                    menuName = map.get("menuName").toString();

//                    if ((map.get("menuName").toString()).contains("手机端")) {
                    map.put("menuName", map.get("menuName").toString().replace("手机端", ""));
//                    } else {
//                        map.put("menuName", map.get("menuName").toString());
//                    }
                    map.put("image", image[i]);
                    menuListAll.add(map);
                    Log.e(TAG, "setMenuModel: menuName1" + menuName);
                    if (menuName.contains("Today")) {
                        todayPageId = map.get("pageId").toString();
                        todayTableid = map.get("tableId").toString();
                        continue;
                    } else if (menuName.contains("Tomorrow")) {
                        tomorrowPageId = map.get("pageId").toString();
                        tomorrowTableId = map.get("tableId").toString();
                        continue;
                    }
                }
            } else {
                initModel();
            }
        } else {
            initModel();

        }
        setMenuAdapter(menuListAll);
    }

    private void initDateWeek() {
        long time = System.currentTimeMillis();
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒 EEEE");
        format = new SimpleDateFormat("yyyy/MM/dd");
        monthstr = format.format(date).substring(5);
        format = new SimpleDateFormat("EEEE");
        daystr = format.format(date);
        enDaystr = daystr;
//        switch (daystr) {
//            case "星期一":
//                enDaystr = "Monday";
//                break;
//            case "星期二":
//                enDaystr = "Tuesday";
//                break;
//            case "星期三":
//                enDaystr = "Wednesday";
//                break;
//            case "星期四":
//                enDaystr = "Thursday";
//                break;
//            case "星期五":
//                enDaystr = "Friday";
//                break;
//            case "星期六":
//                enDaystr = "Saturday";
//                break;
//            case "星期日":
//                enDaystr = "Sunday";
//                break;
//            default:
//                break;
//        }
    }

    public int isResume = 0;

    @Override
    public void onResume() {
        isResume = 1;
        super.onResume();
        if (!isLogin) {
            isLogin = arrBundle.getBoolean("isLogin");
            initData();
        } else {
            getLoginData(teachUrl);

        }

    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            updateImage(intent.getExtras().getString("uriStr"));
        }
    };

    public void updateImage(String uriStr) {
        Uri uri = Uri.parse(uriStr);
        stuHeadImage.setImageURI(uri);
    }

    public void initData() {
        //设置看板数据
        parentList = getkanbanData(arrStr);
        setKanbanAdapter(parentList);
    }

//    private void getTodyTomorTP() {
//        //菜单列表中的gridview数据
//        if ((homePageListstr != null) && (homePageListstr.length() > 0)) {
//            List<Map<String, Object>> listMap = JSON.parseObject(homePageListstr,
//                    new TypeReference<List<Map<String, Object>>>() {
//                    });
//            if ((listMap != null) && (listMap.size() > 0)) {
//                String menuName;
//                for (int i = 0; i < listMap.size(); i++) {
//                    Map<String, Object> map = listMap.get(i);
//                    menuName = map.get("menuName").toString();
//                    if (menuName.contains("今日课表")) {
//                        todayPageId = map.get("pageId").toString();
//                        todayTableid = map.get("tableId").toString();
//                        continue;
//                    } else if (menuName.contains("明日课表")) {
//                        tomorrowPageId = map.get("pageId").toString();
//                        tomorrowTableId = map.get("tableId").toString();
//                        continue;
//                    }
//                }
//                Log.e("tda", todayPageId + "/" + todayTableid + "/" + tomorrowPageId + "/" + tomorrowTableId);
//            }
//        } else {
//            Toast.makeText(getActivity(), "无今明日课表", Toast.LENGTH_SHORT).show();
//
//        }
//    }

    private void initModel() {
        Map<String, Object> map = new HashMap<>();
        map.put("menuName", "扫码考勤");
        map.put("image", image[0]);
        menuListAll.add(map);
        map = new HashMap<>();
        map.put("menuName", "报表管理");
        map.put("image", image[1]);
        menuListAll.add(map);
        map = new HashMap<>();
        map.put("menuName", "消息提醒");
        map.put("image", image[2]);
        menuListAll.add(map);
        map = new HashMap<>();
        map.put("menuName", "系统设置");
        map.put("image", image[3]);
        menuListAll.add(map);
    }

    private void getData() {
        arrBundle = getArguments();
        arrStr = arrBundle.getString("arrStr");
        homePageListstr = arrBundle.getString("homePageList");
        Log.e("homePageListstr", homePageListstr);
        //getTodyTomorTP();
    }

    public void setMenuAdapter(final List<Map<String, Object>> menuListMaps) {
        final SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), menuListMaps,
                R.layout.fragment_study_gridview_item, new String[]{"image", "menuName"},
                new int[]{R.id.itemImage, R.id.itemName});
        gridView.setAdapter(simpleAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                if (homePagelistMap != null && homePagelistMap.size() > 0) {
                                                    Map<String, Object> map = menuListMaps.get(i);
                                                    String menuName = map.get("menuName") + "";
                                                    Log.e(TAG, "onItemClick: menuName2" + menuName+"//"+map.toString());
                                                    if (menuName.contains("Today")) {
                                                        //今日课表
                                                        Intent intent = new Intent(getActivity(), TodayCourseTableActivity.class);
                                                        intent.putExtra("todayPageId", todayPageId);
                                                        intent.putExtra("todayTableid", todayTableid);
                                                        intent.putExtra("isToday", "1");
                                                        intent.putExtra("titleName", String.valueOf(menuListMaps.get(i).get("menuName")));
                                                        startActivity(intent);
                                                    } else if (menuName.contains("Tomorrow")) {
                                                        //明日课表
                                                        Intent intent = new Intent(getActivity(), TodayCourseTableActivity.class);
                                                        intent.putExtra("tomorrowPageId", tomorrowPageId);
                                                        intent.putExtra("tomorrowTableId", tomorrowTableId);
                                                        intent.putExtra("isToday", "2");
                                                        intent.putExtra("titleName", String.valueOf(menuListMaps.get(i).get("menuName")));
                                                        startActivity(intent);
                                                    } else {
                                                        DataProcess.toList(getActivity(), menuListMaps.get(i));
                                                    }
                                                } else {
                                                    if (i == 0) {
                                                        PermissionGen.needPermission(StudyFragment.this, 106,
                                                                new String[]{
                                                                        Manifest.permission.CAMERA,
                                                                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                                                                }
                                                        );
                                                    } else if (i == 1) {
                                                        Intent intent = new Intent(getActivity(), ChartActivity.class);
                                                        intent.putExtra("titleName", String.valueOf(menuListMaps.get(i).get("menuName")));
                                                        startActivity(intent);
                                                    } else if (i == 2) {
                                                        Intent intent = new Intent(getActivity(), MessagAlertActivity.class);
                                                        startActivity(intent);
                                                    } else if (i == 3) {
                                                        Intent intent = new Intent(getActivity(), BlankActivity.class);
                                                        intent.putExtra("titleName", String.valueOf(menuListMaps.get(i).get("menuName")));
                                                        startActivity(intent);
                                                    }
                                                }
                                            }
                                        }

        );
    }

    private static final String TAG = "StudyFragment";

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @PermissionSuccess(requestCode = 106)
    public void doCapture() {
        Intent intent = new Intent(getActivity(), TestScanActivity.class);
        startActivity(intent);
    }

    @PermissionFail(requestCode = 106)
    public void doFailedCapture() {
        Toast.makeText(getActivity(), getResources().getString(R.string.permission_Access_failed), Toast.LENGTH_SHORT).show();
    }

    public void setKanbanAdapter(List<Map<String, Object>> parentLists) {
        if ((parentLists.size()) % 2 == 1) {
            Map<String, Object> map = new HashMap<>();
            map.put("image", R.color.white);
            map.put("cnName", "");
            map.put("name", "");
            parentLists.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), parentLists,
                R.layout.activity_stu_study_item, new String[]{"image", "cnName", "name"},
                new int[]{R.id.iv_item, R.id.text1, R.id.text2});
        homeGridView.setAdapter(adapter);
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getkanbanData(String arrStr) {
        List<Map<String, Object>> parentLists = new ArrayList<>();
        try {
            List<Map<String, Object>> listMap = JSON.parseObject(arrStr,
                    new TypeReference<List<Map<String, Object>>>() {
                    });
            String cnName;
            for (int i = 0; i < listMap.size(); i++) {
                Map<String, Object> map = new HashMap<>();
                cnName = String.valueOf(listMap.get(i).get("cnName"));
                map.put("ctType", "3");
                map.put("cnName", cnName);
                // int j = i % 4;
                map.put("image", imgs2[i]);
                map.put("SourceDataId", listMap.get(i).get("homeSetId") + "_" + listMap.get(i).get("index"));
                map.put("penetratePageId", listMap.get(i).get("phonePageId"));
                map.put("tableId", listMap.get(i).get("tableId"));
                List<Map<String, Object>> listMap1 = (List<Map<String, Object>>) listMap.get(i).get("valueMap");
                String name = 0 + "";
                if (listMap1.size() > 0) {
                    if (listMap1.get(0) != null && listMap1.get(0).size() > 0) {
                        name = String.valueOf(listMap1.get(0).get("name"));
                    }
                }
                map.put("name", name);
                parentLists.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parentLists;
    }

    //获取父类菜单数据，并取不大于7个
    public List<Map<String, Object>> getMenuListData(List<Map<String, Object>> menuListAlls) {
        List<Map<String, Object>> menuListMaps = new ArrayList<>();
        menuListMap = DataProcess.toParentList(menuListAlls);
        //大于7个的情况
        if (menuListMap.size() > 7) {
            for (int k = 0; k < 7; k++) {
                menuListMaps.add(menuListMap.get(k));
            }
        } else {
            //小于7个的情况
            menuListMaps.addAll(menuListMap);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("menuName", "全部");
        map.put("image", R.drawable.stu_see_all);
        menuListMaps.add(map);
        Log.e("TAG", "parentList去掉手机端 " + menuListMap.toString());
        return menuListMaps;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                boolean emui = AndtoidRomUtil.isEMUI();
                boolean miui = AndtoidRomUtil.isMIUI();
                boolean flyme = AndtoidRomUtil.isFlyme();

                if (emui) {
                    //华为
//                    PackageManager pm = getActivity().getPackageManager();
//                    //MediaStore.ACTION_IMAGE_CAPTURE android.permission.RECORD_AUDIO
//                    boolean permission = (PackageManager.PERMISSION_GRANTED ==
//                            pm.checkPermission("MediaStore.ACTION_IMAGE_CAPTURE", "packageName"));
//                    if (permission) {
//                        Intent intent = new Intent(getActivity(), CaptureActivity.class);
//                        startActivityForResult(intent, 1);
//                    } else {
//                        Constant.goHuaWeiSetting(getActivity());
//                    }
                    Intent intent = new Intent(getActivity(), TestScanActivity.class);
                    startActivityForResult(intent, 1);
                } else if (miui) {
                    //小米
                    Intent intent = new Intent(getActivity(), TestScanActivity.class);
                    startActivityForResult(intent, 1);
                } else if (flyme) {
                    //魅族rom
                    Intent intent = new Intent(getActivity(), TestScanActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    Intent intent = new Intent(getActivity(), TestScanActivity.class);
                    startActivityForResult(intent, 1);
                }
                break;
            case R.id.layout_1:
                Intent intent2 = new Intent(getActivity(), MessagAlertActivity.class);
                startActivity(intent2);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(broadcastReceiver);
        ButterKnife.unbind(this);

    }

    @OnClick(R.id.stu_homepage_info)
    public void onClick() {

        Intent intent = new Intent(getActivity(), StuInfoActivity.class);
        startActivity(intent);


    }

//    private class TestNormalAdapter extends StaticPagerAdapter {
//        private int[] imgs = {
//                R.drawable.stu_see_banner,
//                R.drawable.img2,
//                R.drawable.img3,
//                R.drawable.img4,
//        };
//
//
//        @Override
//        public View getView(ViewGroup container, int position) {
//            ImageView view = new ImageView(container.getContext());
//            view.setImageResource(imgs[position]);
//            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//            return view;
//        }
//
//
//        @Override
//        public int getCount() {
//            return imgs.length;
//        }
//    }

    public void toItem(int menuId, Map<String, Object> itemData) {

        //获取子列表
        List<Map<String, Object>> childList = new ArrayList<>();
        for (int i = 0; i < menuListAll.size(); i++) {
            if (Integer.valueOf(String.valueOf(menuListAll.get(i).get("parent_menuId"))) == menuId) {
                childList.add(menuListAll.get(i));
            }
        }

        if (childList.size() > 0) {
            childList = DataProcess.toImgList(childList);

        }
        //转换整项为字符串准备发送
        String itemDataString = JSONArray.toJSONString(itemData);

        //转换子列表对象为字符串准备发送
        String childString = JSONArray.toJSONString(childList);
        Intent intent = new Intent();
        if (itemData.get("menuPageUrl") == null) {
            intent.setClass(getActivity(), ListActivity4.class);
        } else {
            intent.setClass(getActivity(), CourseActivity.class);
        }
        intent.putExtra("itemData", itemDataString);
        intent.putExtra("childData", childString);
        startActivity(intent);
    }

    public void getLoginData(String volleyUrl) {
        if (((BaseActivity) getActivity()).hasInternetConnected()) {

            //参数
            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put(Constant.USER_NAME, Constant.USERNAME_ALL);
            paramsMap.put(Constant.PASSWORD, Constant.PASSWORD_ALL);
            paramsMap.put(Constant.proIdName, Constant.proId);
            paramsMap.put(Constant.timeName, Constant.menuAlterTime);
            paramsMap.put(Constant.sourceName, Constant.sourceInt);
            //请求
            OkHttpUtils
                    .post()
                    .params(paramsMap)
                    .url(volleyUrl)
                    .build()
                    .execute(new EdusStringCallback(getActivity()) {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            ErrorToast.errorToast(mContext, e);
                            pull_refresh_scrollview.onRefreshComplete();
                        }


                        @Override
                        public void onResponse(String response, int id) {
                            Log.e(TAG, "onResponse: " + "  id  " + id);
                            mainPage(response);
                        }
                    });
        } else {
            pull_refresh_scrollview.onRefreshComplete();
            Toast.makeText(getActivity(), getResources().getString(R.string.no_network), Toast.LENGTH_SHORT).show();

        }
    }

    //此方法传递菜单JSON数据
    @SuppressWarnings("unchecked")
    private void mainPage(String menuData) {
        try {
            Map<String, Object> menuMap = JSON.parseObject(menuData,
                    new TypeReference<Map<String, Object>>() {
                    });

            Map<String, Object> loginfo = (Map<String, Object>) menuMap.get("loginInfo");
            Constant.USERID = String.valueOf(loginfo.get("USERID"));
            if (String.valueOf(loginfo.get("sessionId")).equals(Constant.sessionId)) {
            List<Map<String, Object>> menuListMap1 = (List<Map<String, Object>>) menuMap.get("roleFollowList");
            // List<Map<String, Object>> menuListMap2 = (List<Map<String, Object>>) menuMap.get("menuList");
//看板模块数据
            String arrStr = JSON.toJSONString(menuListMap1);
            parentList.clear();
            parentList = getkanbanData(arrStr);
            setKanbanAdapter(parentList);

            //在更新UI后，无需其它Refresh操作，系统会自己加载新的listView
            pull_refresh_scrollview.onRefreshComplete();
            pull_refresh_scrollview.onRefreshComplete();
            if (isResume == 0) {
                Toast.makeText(getActivity(), getResources().getString(R.string.data_refresh), Toast.LENGTH_SHORT).show();
            }

            isResume = 0;
            } else {
                Toast.makeText(getActivity(), "登陆失效，已退出登陆！", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), StuLoginActivity.class);
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}















