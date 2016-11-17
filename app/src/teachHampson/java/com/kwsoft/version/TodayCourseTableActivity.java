package com.kwsoft.version;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.kwsoft.kehuhua.adcustom.ListActivity2;
import com.kwsoft.kehuhua.adcustom.base.BaseActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.kwsoft.kehuhua.adcustom.R;
import com.kwsoft.kehuhua.config.Constant;
import com.kwsoft.kehuhua.urlCnn.EdusStringCallback;
import com.kwsoft.kehuhua.urlCnn.ErrorToast;
import com.kwsoft.kehuhua.widget.CommonToolbar;
import com.kwsoft.version.adapter.TodayCourseTabAdapter;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/11/14 0014.
 */

public class TodayCourseTableActivity extends BaseActivity {
    public ListView lv_listview;
    public List<Map<String, String>> list = null;
    private CommonToolbar mToolbar;
    private String titleName;//顶栏名称

    private String todayPageId, tomorrowPageId, todayTableid, tomorrowTableId, isToday;//金明日课表page/table

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_course);

        initView();

        initData();


    }

    private void initData() {
        Intent intent = getIntent();
        titleName = intent.getStringExtra("titleName");
        isToday = intent.getStringExtra("isToday");
        if (isToday.equals("1")) {
            todayPageId = intent.getStringExtra("todayPageId");
            todayTableid = intent.getStringExtra("todayTableid");

            getTData(todayPageId, todayTableid);
        } else {
            tomorrowPageId = intent.getStringExtra("tomorrowPageId");
            tomorrowTableId = intent.getStringExtra("tomorrowTableId");
            getTData(tomorrowPageId, tomorrowTableId);
        }
        mToolbar.setTitle(titleName);

    }

    @Override
    public void initView() {
        lv_listview = (ListView) findViewById(R.id.lv_listview);

        mToolbar = (CommonToolbar) findViewById(R.id.common_toolbar);
        mToolbar.setBackgroundColor(getResources().getColor(Constant.topBarColor));

        mToolbar.setRightButtonIcon(getResources().getDrawable(R.mipmap.often_more)); //右侧pop
        mToolbar.setLeftButtonOnClickListener(new View.OnClickListener() { //左侧返回按钮
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void getTData(String pageid, String tableid) {
        if (hasInternetConnected()) {
            //地址
            String volleyUrl = Constant.sysUrl + Constant.requestListSet;
            Log.e("TAG", "列表请求地址：" + volleyUrl);
            Map<String, String> paramsMap = new HashMap<>();
            //参数
            paramsMap.put("tableId", tableid);
            paramsMap.put("pageId", pageid);

//请求
            OkHttpUtils
                    .post()
                    .params(paramsMap)
                    .url(volleyUrl)
                    .build()
                    .execute(new EdusStringCallback(TodayCourseTableActivity.this) {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            ErrorToast.errorToast(mContext, e);
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            parseData(response);

                        }


                    });


        } else {
            Toast.makeText(TodayCourseTableActivity.this, "请连接网络", Toast.LENGTH_SHORT).show();
        }
    }
    private void parseData(String response) {
        Map<String, Object> menuMap = JSON.parseObject(response,
                new TypeReference<Map<String, Object>>() {
                });
        List<Map<String, Object>> dataList = (List<Map<String, Object>>) menuMap.get("dataList");
        Map<String, Object> pageSet = (Map<String, Object>) menuMap.get("pageSet");
        List<Map<String, Object>> fieldSet = (List<Map<String, Object>>) pageSet.get("fieldSet");
        String fieldCnName, teacherfieldAliasName = "", classfieldAliasName = "";
        list = new ArrayList<>();
        if (fieldSet != null && dataList != null && (fieldSet.size() > 0) && (dataList.size() > 0)) {
            for (int i = 0; i < fieldSet.size(); i++) {
                Map<String, Object> map = fieldSet.get(i);
                fieldCnName = map.get("fieldCnName") + "";

                if (fieldCnName.contains("教师")) {
                    teacherfieldAliasName = map.get("fieldAliasName") + "";
                    continue;
                } else if (fieldCnName.contains("时段")) {
                    classfieldAliasName = map.get("fieldAliasName") + "";
                    Log.e("fdsf",classfieldAliasName);
                    continue;
                }
            }
            Log.e("fdsf",classfieldAliasName);
            for (int j = 0; j < dataList.size(); j++) {
                Map<String, Object> dataListmap = dataList.get(j);
                Map<String, String> map = new HashMap<String, String>();
                if (dataListmap.containsKey(teacherfieldAliasName)) {
                    map.put("courseName", dataListmap.get(teacherfieldAliasName) + "");
                }
                if (dataListmap.containsKey(classfieldAliasName)) {
                    map.put("courseTime", dataListmap.get(classfieldAliasName) + "");
                }
                list.add(map);
            }
        }else {
            Toast.makeText(TodayCourseTableActivity.this,"暂时无课表数据",Toast.LENGTH_SHORT).show();
        }
        TodayCourseTabAdapter adapter = new TodayCourseTabAdapter(list, this);
        lv_listview.setAdapter(adapter);
    }

}
