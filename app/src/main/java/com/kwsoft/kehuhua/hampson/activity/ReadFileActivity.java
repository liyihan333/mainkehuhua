package com.kwsoft.kehuhua.hampson.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.kwsoft.kehuhua.adcustom.R;
import com.kwsoft.kehuhua.adcustom.base.BaseActivity;
import com.kwsoft.kehuhua.config.Constant;
import com.kwsoft.kehuhua.hampson.adapter.ZuoYeGridViewAdapter;
import com.kwsoft.kehuhua.wechatPicture.andio.Mp3ListAdapter;
import com.kwsoft.kehuhua.widget.CommonToolbar;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

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

    private Mp3ListAdapter  mAdapter;
    private List<String> mDatas = new ArrayList<>();
    private List<File> mFiles = new ArrayList<>();

    public  ListView mlistview;

    private String downLoadId;
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

    private void getIntentData() {
        Intent intent = getIntent();
        String positionStr = intent.getStringExtra("position");
        position = Integer.valueOf(positionStr);
        String fieldSetStr = intent.getStringExtra("fieldSet");
        downLoadId = intent.getStringExtra("downLoadId");
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
//        String downLoadId = "";
//        for (int k = 0; k < fieldSet.size(); k++) {
//
//            String fieldCnName = fieldSet.get(k).get("fieldCnName");
//
//            if (fieldCnName.equals("mongodbId")) {
//                downLoadId = fieldSet.get(k).get("fieldCnName2");
//            }
//
//        }

            String[] downLoadIdArr = downLoadId.split(",");
            for (int m = 0; m < downLoadIdArr.length; m++) {
                mongoIds.add(downLoadIdArr[m]);
            }

        List<String> fileNames = new ArrayList<>();

        if (!value.equals("")) {
            String[] valueArr = value.split(",");
            for (int m = 0; m < valueArr.length; m++) {
                fileNames.add(valueArr[m]);
            }
        }

        if (mongoIds.size() > 0) {
            Log.e(TAG, "showImage: mongoIds "+mongoIds.toString() );

            imageDatas=new ArrayList<>();
            ImageFileNames=new ArrayList<>();



            musicDatas=new ArrayList<>();
            musicFileNames=new ArrayList<>();

            //将mongodb分类
            for (int k=0;k<fileNames.size();k++) {
                String url= Constant.sysUrl+Constant.downLoadFileStr+mongoIds.get(k);
                String filename=fileNames.get(k);
                Log.e(TAG, "showImage: url "+url);
                Log.e(TAG, "showImage: filename "+filename);
                if (!filename.endsWith(".MP3")&&!filename.endsWith(".mp3")) {
                    imageDatas.add(url);
                    ImageFileNames.add(filename);

                }else{
                    Log.e(TAG, "showImage: mp3   "+filename);
                    musicDatas.add(url);
                    musicFileNames.add(filename);

                }
            }




            ZuoYeGridViewAdapter gridViewAdapter = new ZuoYeGridViewAdapter(this, imageDatas, ImageFileNames);
            zuoYeImageGridView.setAdapter(gridViewAdapter);


            if (musicDatas.size()>0) {
                Log.e(TAG, "showImage: 开始下载mp3");
//                downLoadMp3();
            }


//            mAdapter = new RecorderAdapter(ReadFileActivity.this, mDatas);
//            mlistview.setAdapter(mAdapter);



        }

    }
int num=0;
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
    String path= Environment.getExternalStorageDirectory().getPath()+"/hampsonDownloadVoice/";
    public void downLoadMp3(){
        Log.e(TAG, "downLoadMp3: musicDatas.get(num) "+musicDatas.get(num));
        Log.e(TAG, "downLoadMp3: path "+path);
        Log.e(TAG, "downLoadMp3: musicFileNames.get(num) "+musicFileNames.get(num));

        OkHttpUtils.get()//
                .url(musicDatas.get(num))
                .tag(this)//
                .build()
                .execute(new FileCallBack(path, musicFileNames.get(num)) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(ReadFileActivity.this, "mp3语音下载失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        Log.e(TAG, "onResponse: "+response);
                        mFiles.add(response);
                        num++;
                        if (num<musicDatas.size()) {
                            downLoadMp3();
                        }else{
                            try {
                                mp32Media();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }
                });

    }

    public void mp32Media() throws IOException {
        Log.e(TAG, "mp32Media: 开始适配音频文件列表");
        mAdapter = new Mp3ListAdapter(ReadFileActivity.this, mFiles);
        mlistview.setAdapter(mAdapter);
        Log.e(TAG, "mp32Media: 适配音频列表完毕");

    }


}
