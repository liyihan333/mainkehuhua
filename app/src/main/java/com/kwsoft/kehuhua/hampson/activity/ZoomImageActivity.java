package com.kwsoft.kehuhua.hampson.activity;

import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.imagepipeline.image.ImageInfo;
import com.kwsoft.kehuhua.adcustom.R;
import com.kwsoft.kehuhua.adcustom.base.BaseActivity;
import com.kwsoft.kehuhua.widget.CommonToolbar;

import me.relex.photodraweeview.PhotoDraweeView;

import static com.kwsoft.kehuhua.config.Constant.topBarColor;

/**
 * Created by Administrator on 2016/11/14 0014.
 *
 *
 */

public class ZoomImageActivity extends BaseActivity {
    String imgPaths;
    private CommonToolbar mToolbar;

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
            Log.e(TAG, "getInfoData: imgPaths "+imgPaths);

        }

    }

    @Override
    public void initView() {
        //mTextViewTitle.setText(listMap.get(0).get("fieldCnName2")+"");
        mToolbar = (CommonToolbar) findViewById(R.id.common_toolbar);
        mToolbar.setTitle("大图模式");
        mToolbar.setBackgroundColor(getResources().getColor(topBarColor));
        //左侧返回按钮
        mToolbar.setRightButtonIcon(getResources().getDrawable(R.mipmap.often_more));
        mToolbar.setLeftButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final PhotoDraweeView my_image_view=(PhotoDraweeView)findViewById(R.id.my_image_view);
        Uri uri = Uri.parse(imgPaths);
        PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder();
        controller.setUri(uri);
        controller.setOldController(my_image_view.getController());
        controller.setControllerListener(new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                super.onFinalImageSet(id, imageInfo, animatable);
                if (imageInfo == null || my_image_view == null) {
                    return;
                }
                my_image_view.update(imageInfo.getWidth(), imageInfo.getHeight());
            }
        });
        my_image_view.setController(controller.build());





    }



}
