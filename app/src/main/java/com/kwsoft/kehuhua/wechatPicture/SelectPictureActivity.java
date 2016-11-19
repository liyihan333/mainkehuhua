package com.kwsoft.kehuhua.wechatPicture;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.kwsoft.kehuhua.adcustom.OperateDataActivity;
import com.kwsoft.kehuhua.adcustom.R;
import com.kwsoft.kehuhua.adcustom.base.BaseActivity;
import com.kwsoft.kehuhua.audiorecorder.AudioRecorder2Mp3Util;
import com.kwsoft.kehuhua.audiorecorder.IRecordButton;
import com.kwsoft.kehuhua.audiorecorder.RecordButton;
import com.kwsoft.kehuhua.urlCnn.EdusStringCallback;
import com.kwsoft.kehuhua.urlCnn.ErrorToast;
import com.kwsoft.kehuhua.utils.DataProcess;
import com.kwsoft.kehuhua.wechatPicture.andio.MediaManager;
import com.kwsoft.kehuhua.wechatPicture.andio.Recorder;
import com.kwsoft.kehuhua.widget.CommonToolbar;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;
import me.iwf.photopicker.PhotoPicker;
import okhttp3.Call;

import static com.kwsoft.kehuhua.config.Constant.img_Paths;
import static com.kwsoft.kehuhua.config.Constant.pictureUrl;
import static com.kwsoft.kehuhua.config.Constant.sysUrl;
import static com.kwsoft.kehuhua.config.Constant.topBarColor;

/**
 * Created by Administrator on 2016/10/13 0013.
 */

public class SelectPictureActivity extends BaseActivity implements View.OnClickListener {
    private CommonToolbar mToolbar;
    String position, fieldRole;
    @Bind(R.id.gridView)
    GridView gridView;
    private ArrayList<String> imgPaths = new ArrayList<>();
    private PhotoPickerAdapter adapter;

    String codeListStr = "";
    private static final String TAG = "SelectPictureActivity";
    private WaterWaveProgress waveProgress;

    //录音按钮
    private RecordButton voiceButton;
    //文件保存路径
    private String BasePath = Environment.getExternalStorageDirectory().toString() + "/voicerecord";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_picture_layout);
        ButterKnife.bind(this);
        initView();
        getIntentData();
//        initData();
//        setListener();
        //展示音频
        initAudioView();
    }

    private void getIntentData() {


    }

    public void initView() {
        Intent intent = getIntent();
        position = intent.getStringExtra("position");
        fieldRole = intent.getStringExtra("fieldRole");


        mToolbar = (CommonToolbar) findViewById(R.id.common_toolbar);
        if (fieldRole.equals("18")) {
            mToolbar.setTitle("单文件选择");
        } else if (fieldRole.equals("19")) {
            mToolbar.setTitle("多文件选择");
        }


        mToolbar.setBackgroundColor(getResources().getColor(topBarColor));
        //左侧返回按钮
        mToolbar.setRightButtonIcon(getResources().getDrawable(R.mipmap.nav_scan_file));
        mToolbar.setLeftButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mToolbar.showRightImageButton();

        //右侧下拉按钮
        mToolbar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFile();//收集文件
                Log.e("fieldRoale", fieldRole);
                if (fieldRole.equals("18")) {
                    if (myFile.size() == 1) {
                        uploadMethod();
                    } else if (myFile.size() == 0) {
                        Toast.makeText(SelectPictureActivity.this, "请选择一个文件", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SelectPictureActivity.this, "最多只允许上传一个文件", Toast.LENGTH_SHORT).show();
                    }
                } else if (fieldRole.equals("19")) {
                    if (myFile.size() > 0) {
//                        uploadMethod(myFile);

                        uploadMethod();

                    } else {
                        Toast.makeText(SelectPictureActivity.this, "请至少选择一个文件", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });

        waveProgress = (WaterWaveProgress) findViewById(R.id.waterWaveProgress1);

        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        //  int height = wm.getDefaultDisplay().getHeight();
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) waveProgress.getLayoutParams();
        //layoutParams.setMargins(width/4,12,10,5);//4个参数按顺序分别是左上右下
        layoutParams.setMarginStart(width / 3);
        waveProgress.setLayoutParams(layoutParams); //mView是控件
        adapter = new PhotoPickerAdapter(imgPaths);

        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == imgPaths.size()) {
                    PermissionGen.with(SelectPictureActivity.this)
                            .addRequestCode(100)
                            .permissions(
                                    Manifest.permission.CAMERA,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .request();
//                    PhotoPicker.builder()
//                            .setPhotoCount(9)
//                            .setShowCamera(true)
//                            .setSelected(imgPaths)
//                            .setShowGif(true)
//                            .setPreviewEnabled(true)
//                            .start(SelectPictureActivity.this, PhotoPicker.REQUEST_CODE);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("imgPaths", imgPaths);
                    bundle.putInt("position", position);
                    goToActivityForResult(SelectPictureActivity.this, EnlargePicActivity.class, bundle, position);
                }
            }
        });


    }

    private void initAudioView() {
        voiceButton = (RecordButton) findViewById(R.id.record);
        mlistview = (ListView) findViewById(R.id.listview);
        // 录音事件监听
        voiceButton.setAudioRecord(new IRecordButton() {
            private String fileName;
            private AudioRecorder2Mp3Util audioRecoder;
            private boolean canClean = false;

            /**
             * 释放资源
             */
            @Override
            public void stop() {
                Log.d("gmyboy", "------------stop-------------");
                audioRecoder.stopRecordingAndConvertFile();
                audioRecoder.cleanFile(AudioRecorder2Mp3Util.RAW);
                audioRecoder.close();
                audioRecoder = null;
            }

            /**
             * 开始录音
             */
            @Override
            public void start() {
                Log.d("gmyboy", "------------start-------------");
                if (canClean) {
                    audioRecoder.cleanFile(AudioRecorder2Mp3Util.MP3
                            | AudioRecorder2Mp3Util.RAW);
                }
                audioRecoder.startRecording();
                canClean = true;
            }

            /**
             * 准备工作
             */
            @Override
            public void ready() {
                Log.d("gmyboy", "------------ready-------------");
                File file = new File(BasePath);
                if (!file.exists()) {
                    file.mkdir();
                }
                fileName = getCurrentDate();
                if (audioRecoder == null) {
                    audioRecoder = new AudioRecorder2Mp3Util(null,
                            getFilePath() + fileName + ".raw", getFilePath()
                            + fileName + ".mp3");
                }

            }

            /**
             * 获取保存路径
             */
            @Override
            public String getFilePath() {
                return BasePath + "/";
            }

            @Override
            public double getAmplitude() {
                //这里就放了一个随机数
                return Math.random() * 20000;
            }

            /**
             * 删除本地保存文件
             */
            @Override
            public void deleteOldFile() {
                Log.d("gmyboy", "------------deleteOldFile-------------");
                File file = new File(getFilePath() + fileName + ".mp3");
                if (file.exists())
                    file.delete();
            }

            /**
             * 录音完成，执行后面操作（发送）
             */
            @Override
            public void complite(float time) {
                Log.d("gmyboy", "------------complite-------------");
                Toast.makeText(SelectPictureActivity.this, "voicePath = " + getFilePath() + fileName + ".mp3" + "\n" + "voiceTime = " + String.valueOf((int) time), Toast.LENGTH_LONG).show();
                String path=getFilePath();
                String fileName1=fileName + ".mp3";
                String voiceSecond=String.valueOf((int) time);
                showListView(path,fileName1,voiceSecond);

            }
        });
        // 以当前时间作为录音文件名

    }
    /**
     * 获取当前系统时间作为音频文件名
     * @return
     */
    private String getCurrentDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HHmmss");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String str = formatter.format(curDate);
        return str;
    }
    public  ListView mlistview;
    private ArrayAdapter<Recorder> mAdapter;
    private List<Recorder> mDatas = new ArrayList<Recorder>();
//显示并展现列表
//        public  void showListView(String path,String fileName,String voiceSecond) {
//        Log.e("hidel", "hidelistview");
//        voiceButton.setVisibility(View.GONE);
//        mlistview.setVisibility(View.GONE);
//    }



//
//    public static void hideListView() {
//        Log.e("hidel", "hidelistview");
//        button.setVisibility(View.VISIBLE);
//        mlistview.setVisibility(View.GONE);
//    }

//    /**
//     * 上传音频一
//     */
//    private void uploadAudioFirst() {
//
//        Recorder recorder = mDatas.get(0);
//        String path = recorder.getFilePathString();
//        uploadAudio(path);
//    }

    /**
     * 上传音频
     */
    private void uploadAudio(String path) {
        File audioFile = new File(path);
        Log.e("audioFile=", audioFile.getPath());
        String url = sysUrl + pictureUrl;

        OkHttpUtils.post()//
                .addFile("audiofile", audioFile.getName(), audioFile)
                .url(url)
                .build()
                .execute(new EdusStringCallback(SelectPictureActivity.this) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ErrorToast.errorToast(mContext, e);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("response", response);
                        getAutoFileCode(response);
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @PermissionSuccess(requestCode = 106)
    public void doSomething() {
        Toast.makeText(this, "打开权限成功", Toast.LENGTH_SHORT).show();
    }

    @PermissionFail(requestCode = 106)
    public void doFailSomething() {
        Toast.makeText(this, "Contact permission is not granted", Toast.LENGTH_SHORT).show();
    }


    @PermissionSuccess(requestCode = 100)
    public void doCapture() {
        PhotoPicker.builder()
                .setPhotoCount(9)
                .setShowCamera(true)
                .setSelected(imgPaths)
                .setShowGif(true)
                .setPreviewEnabled(true)
                .start(SelectPictureActivity.this, PhotoPicker.REQUEST_CODE);
    }

    @PermissionFail(requestCode = 100)
    public void doFailedCapture() {
        Toast.makeText(SelectPictureActivity.this, "获取权限失败", Toast.LENGTH_SHORT).show();
    }

    public void goToActivityForResult(Context context, Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent(context, cls);
        if (bundle == null) {
            bundle = new Bundle();
        }
        intent.putExtras(bundle);
        startActivityForResult(intent, requestCode);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            if (data != null) {
                imgPaths.clear();
                ArrayList<String> photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                imgPaths.addAll(photos);
                adapter.notifyDataSetChanged();
            }
        }

        if (resultCode == RESULT_OK && requestCode >= 0 && requestCode <= 8) {
            imgPaths.remove(requestCode);
            adapter.notifyDataSetChanged();
        }
        img_Paths.clear();
        img_Paths.addAll(imgPaths);

    }

    List<File> myFile = new ArrayList<>();


    //上传文件
    public void getFile() {
        waveProgress.setVisibility(View.VISIBLE);
        waveProgress.setProgress(0);

        //待上传的两个文件

        //图片
        if (img_Paths.size() >= 0) {
            for (int i = 0; i < img_Paths.size(); i++) {
                File file = new File(img_Paths.get(i));

                myFile.add(file);

            }
        }
        //音频
//        if (mDatas != null && mDatas.size() > 0) {
//            Recorder recorder = mDatas.get(0);
//            String path = recorder.getFilePathString();
//            File audioFile = new File(path);
//            myFile.add(audioFile);
//        }
//
//
//        Log.e(TAG, "uploadMethod: 开始上传文件 " + mDatas.size() + "/" + myFile.toString());
        //上传文件


//        else {
//            Toast.makeText(SelectPictureActivity.this, "您尚未选择图片", Toast.LENGTH_SHORT).show();
//          //  parseAudo();
//            waveProgress.setVisibility(View.GONE);
//
//        }

    }

    int num = 0;

    public void uploadMethod() {

        String url = sysUrl + pictureUrl;
        Log.e(TAG, "uploadMethod: 开始上传文件" + myFile.get(num).toString());
//        if (files.size() > 0) {
        OkHttpUtils.post()//
                .addFile("myFile", myFile.get(num).getName(), myFile.get(num))
                .url(url)
                .build()
                .execute(new EdusStringCallback(SelectPictureActivity.this) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ErrorToast.errorToast(mContext, e);
                        waveProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            if (num + 1 < myFile.size()) {//一直请求到最后一个
                                num++;
                                Log.e(TAG, "onResponse: 上传成功" + (num + 1) + "个");
                                getFileCode(response);
                                uploadMethod();
                            } else {//已达上限，返回关联添加页面
                                Log.e(TAG, "onResponse: 已达上限，返回关联添加页面");
                                getFileCode(response);
                                jump2Activity();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e(TAG, "onResponse: 上传成功" + (num + 1) + "个");
                        }


//                            getFileCode(response);
                        //解析音频
                        // parseAudo();
                    }

                    @Override
                    public void inProgress(float progress, long total, int id) {

                    }
                });
//        }else {
//            Toast.makeText(SelectPictureActivity.this, "您尚未选择图片。音频", Toast.LENGTH_SHORT).show();
////          //  parseAudo();
//            waveProgress.setVisibility(View.GONE);
//        }
    }

    List<String> codeList = new ArrayList<>();

    //解析文件上传成功的code值
    private void getFileCode(String response) {

        if (num + 1 == myFile.size()) {
            waveProgress.setProgress(100);
            waveProgress.setVisibility(View.GONE);
            Log.e("total", myFile.size() + "");
        } else {
            waveProgress.setProgress((int) (100 * (num) / myFile.size()));
            Log.e("progress", (100 * (num) / myFile.size()) + "");
        }
        Log.e("TAG", "uploadMethod2:" + response);
        Toast.makeText(SelectPictureActivity.this, "上传成功", Toast.LENGTH_SHORT).show();

        String[] valueTemp1 = response.split(":");
        String valueCode = valueTemp1[1];
        codeList.add(valueCode);
        Log.e(TAG, "getFileCode: codeList " + codeList.toString());

    }


    //解析文件音频上传成功的code值
    private void getAutoFileCode(String response) {
        Log.e("TAG", "uploadMethod2:" + response);
        Toast.makeText(SelectPictureActivity.this, "上传成功", Toast.LENGTH_SHORT).show();

        jump2Activity();
//        List<Integer> codeList = new ArrayList<>();
//        if (response.contains(":")) {
//            String[] value = response.split(",");
//            for (String valueTemp : value) {
//                String[] valueTemp1 = valueTemp.split(":");
//                int valueCode = Integer.valueOf(valueTemp1[1]);
//                codeList.add(valueCode);
//            }
//            Log.e("TAG", "文件上传codeList:" + codeList.toString());
//            int leg = codeList.size();
//            if (leg > 0) {
//                for (int i = 0; i < leg; i++) {
//                    if (i == (leg - 1)) {
//                        codeListStr = codeListStr + codeList.get(i);
//                    } else {
//                        codeListStr = codeListStr + codeList.get(i) + ",";
//                    }
//                }
//            }
//
//        } else {
//            Toast.makeText(SelectPictureActivity.this, "文件值解析出现问题", Toast.LENGTH_SHORT).show();
//        }
        Log.e("TAG", "文件上传码codeListStr:" + codeListStr);
    }
//
//    private void parseAudo() {
//        if (mDatas != null && mDatas.size() > 0) {
//            uploadAudioFirst();
//        } else {
//            Toast.makeText(SelectPictureActivity.this, "您尚未录制音频", Toast.LENGTH_SHORT).show();
//        }
//    }


    private void jump2Activity() {


        codeListStr = DataProcess.listToString(codeList);


        Intent intentTree = new Intent();

        intentTree.setClass(SelectPictureActivity.this, OperateDataActivity.class);

        Bundle bundle = new Bundle();

        bundle.putString("position", position);
        bundle.putString("codeListStr", codeListStr);
        intentTree.putExtra("bundle", bundle);
        setResult(101, intentTree);
        this.finish();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        MediaManager.pause();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MediaManager.resume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MediaManager.release();
    }
   ImageView soundFile;
    private void showListView(final String path, String fileName, String voiceSecond){
        soundFile=(ImageView)findViewById(R.id.soundFile);


        final File file=new File(path+fileName);
        if(file.exists()) {

            soundFile.setVisibility(View.VISIBLE);
            String soundName = file.getName();

            soundFile.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        soundFile.setBackgroundColor(getResources().getColor(R.color.blue));
                        MediaPlayer player = new MediaPlayer();
                       try {
                            player.setDataSource(path);
                            player.prepare();
                            player.start();
                       } catch (IllegalArgumentException e) {
                           // TODO Auto-generated catch block
                            e.printStackTrace();
                           } catch (SecurityException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            } catch (IllegalStateException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            }
                        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                        soundFile.setBackgroundColor(getResources().getColor(R.color.white));
                      }
                    return true;
                }
            });



        }



    }

}
