package com.kwsoft.kehuhua.hampson.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.kwsoft.kehuhua.adcustom.R;
import com.kwsoft.kehuhua.adcustom.base.BaseActivity;
import com.kwsoft.kehuhua.config.Constant;
import com.kwsoft.kehuhua.hampson.adapter.ZuoYeGridViewAdapter;
import com.kwsoft.kehuhua.wechatPicture.andio.Recorder;
import com.kwsoft.kehuhua.wechatPicture.andio.RecorderAdapter;
import com.kwsoft.kehuhua.widget.CommonToolbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.kwsoft.kehuhua.config.Constant.topBarColor;

/**
 * Created by Administrator on 2016/11/15 0015.
 *
 */

public class ReadFileActivity extends BaseActivity {
    private CommonToolbar mToolbar;
    ZuoYeImageGridView zuoYeImageGridView;


    List<String> imageDatas,ImageFileNames;
    List<String> musicDatas,musicFileNames;

    private ArrayAdapter<Recorder> mAdapter;
    private List<Recorder> mDatas = new ArrayList<Recorder>();


    public  ListView mlistview;


    private static final String TAG = "ReadFileActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hampson_activity_read_file_layout);
        initView();
        getIntentData();
    }

    private List<Map<String, String>> fieldSet;
    private int position;
    String prefix;

    private void getIntentData() {
        Intent intent = getIntent();
        String positionStr = intent.getStringExtra("position");
        position = Integer.valueOf(positionStr);
        String fieldSetStr = intent.getStringExtra("fieldSet");

        prefix= intent.getStringExtra("fileEnd");
        fieldSet = JSON.parseObject(fieldSetStr,
                new TypeReference<List<Map<String, String>>>() {
                });

        showImage();


    }

    private void showImage() {
        List<String> mongoIds = new ArrayList<>();
//获取mongoDB字符串

        Map<String, String> itemMap = fieldSet.get(position);
        String name = itemMap.get("fieldCnName");
        String value = itemMap.get("fieldCnName2");
        String downLoadId = "";
        for (int k = 0; k < fieldSet.size(); k++) {

            String fieldCnName = fieldSet.get(k).get("fieldCnName");

            if (fieldCnName.equals("mongodbId")) {
                downLoadId = fieldSet.get(k).get("fieldCnName2");
            }

        }
        if (!downLoadId.equals("")) {
            String[] downLoadIdArr = downLoadId.split(",");
            for (int m = 0; m < downLoadIdArr.length; m++) {
                mongoIds.add(downLoadIdArr[m]);
            }
        }
        List<String> fileNames = new ArrayList<>();

        if (!value.equals("")) {
            String[] valueArr = value.split(",");
            for (int m = 0; m < valueArr.length; m++) {
                fileNames.add(valueArr[m]);
            }
        }

        if (mongoIds.size() > 0) {
            Log.e(TAG, "getView: mongoIds " + mongoIds);

            imageDatas=new ArrayList<>();
            ImageFileNames=new ArrayList<>();



            musicDatas=new ArrayList<>();
            musicFileNames=new ArrayList<>();

            //将mongodb分类
            for (int k=0;k<fileNames.size();k++) {
                String url= Constant.sysUrl+Constant.downLoadFileStr+mongoIds.get(k);
                String filename=fileNames.get(k);
                if (!filename.endsWith(".MP3")||!filename.endsWith(".mp3")) {
                    String prefix=filename.substring(filename.lastIndexOf(".")+1);
                    imageDatas.add(url);
                    ImageFileNames.add(filename);

                }else{

                }
            }
            ZuoYeGridViewAdapter gridViewAdapter = new ZuoYeGridViewAdapter(this, imageDatas, ImageFileNames);
            zuoYeImageGridView.setAdapter(gridViewAdapter);


            mAdapter = new RecorderAdapter(this, mDatas);
            mlistview.setAdapter(mAdapter);



        }

    }

    @Override
    public void initView() {
        mToolbar = (CommonToolbar) findViewById(R.id.common_toolbar);
        mToolbar.setTitle("查看附件");
        mToolbar.setBackgroundColor(getResources().getColor(topBarColor));
        //左侧返回按钮
        mToolbar.setLeftButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        zuoYeImageGridView = (ZuoYeImageGridView) findViewById(R.id.zuoYeImageGridView);
        mlistview = (ListView) findViewById(R.id.listview);

    }


}
