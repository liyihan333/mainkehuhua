package com.kwsoft.kehuhua.hampson.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.kwsoft.kehuhua.adcustom.R;
import com.kwsoft.kehuhua.adcustom.base.BaseActivity;
import com.kwsoft.kehuhua.widget.CommonToolbar;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.util.Locale;

import me.relex.photodraweeview.PhotoDraweeView;
import okhttp3.Call;

import static com.kwsoft.kehuhua.config.Constant.topBarColor;

/**
 * Created by Administrator on 2016/11/14 0014.
 *
 *
 */

public class ZoomImageActivity extends BaseActivity{
    String imgPaths;
    private CommonToolbar mToolbar;
    PhotoDraweeView my_image_view;
    String fileName;
    String prefix;
    private static final String TAG = "ZoomImageActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_image_layout);
        getInfoData();
        initView();
    }

    private void getInfoData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            imgPaths = bundle.getString("imgPaths");
            fileName= bundle.getString("fileName");
            prefix= bundle.getString("fileEnd");
            Log.e(TAG, "getInfoData: imgPaths "+imgPaths);

        }

    }

    @Override
    public void initView() {
        mToolbar = (CommonToolbar) findViewById(R.id.common_toolbar);
        mToolbar.setTitle("查看大图");
        mToolbar.setBackgroundColor(getResources().getColor(topBarColor));
        mToolbar.setLeftButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

//        my_image_view=(PhotoDraweeView)findViewById(R.id.photo_drawee_view);
        showProgressiveJPEGs(imgPaths);


    }
    /**
     * 演示：逐渐加载的图片，即，从模糊逐渐清晰。需要图片本身也支持这种方式
     */
    private void showProgressiveJPEGs(String url) {
        final String path=Environment.getExternalStorageDirectory().getPath();
        OkHttpUtils.get()//
                .url(url)
                .tag(this)//
                .build()
                .execute(new FileCallBack(path, fileName) {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(File response, int id) {
                        //Activity mActivity,final String filePath
                        Log.e(TAG, "onResponse: 下载成功"+response.getName());
                        Log.e(TAG, "onResponse: path "+path+fileName+prefix);
                        openFile(path+"/"+response.getName());
                    }

                    @Override
                    public void inProgress(float progress, long total, int id) {
                        super.inProgress(progress, total, id);
                    }
                });
    }

    /**

     */
    public void openFile(final String filePath)
    {
        String ext = filePath.substring(filePath.lastIndexOf('.')).toLowerCase(Locale.US);
        try
        {
            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
            String temp = ext.substring(1);
            String mime = mimeTypeMap.getMimeTypeFromExtension(temp);

            Intent intent = new Intent();
            intent.setAction(android.content.Intent.ACTION_VIEW);
            File file = new File(filePath);
            intent.setDataAndType(Uri.fromFile(file), mime);
            startActivity(intent);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "无法打开后缀名为." + ext + "的文件！",
                    Toast.LENGTH_LONG).show();
        }
    }
}
