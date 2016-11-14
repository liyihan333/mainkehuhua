package com.kwsoft.kehuhua.hampson.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.kwsoft.kehuhua.adcustom.R;
import com.kwsoft.kehuhua.config.Constant;
import com.kwsoft.kehuhua.hampson.activity.PlayAudioActivity;
import com.kwsoft.kehuhua.hampson.activity.ZoomImageActivity;

import java.util.List;



/**
 * Created by Administrator on 2016/11/14 0014.
 */

public class ZuoYeGridViewAdapter extends BaseAdapter {
    Context context;
    List<String> datas;
    List<String> fileNames;

    public ZuoYeGridViewAdapter(Context context, List<String> datas,List<String> fileNames) {
        this.context=context;
        this.datas=datas;
        this.fileNames=fileNames;


    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = LayoutInflater.from(context).inflate(R.layout.hampson_zuoye_item_image_layout, null);


        TextView  voice_file=(TextView)view.findViewById(R.id.voice_file);
        SimpleDraweeView my_image_view=(SimpleDraweeView)view.findViewById(R.id.my_image_view);
        final String url= Constant.sysUrl+Constant.downLoadFileStr+datas.get(i);
        final String filename=fileNames.get(i);
        if (fileNames.get(i).endsWith(".JPG")||fileNames.get(i).endsWith(".JPEG")
        ||fileNames.get(i).endsWith(".GIF")||fileNames.get(i).endsWith(".PNG")
                ||fileNames.get(i).endsWith(".BMP")||fileNames.get(i).endsWith(".WBMP")
        ||fileNames.get(i).endsWith(".jpg")||fileNames.get(i).endsWith(".jpeg")
                ||fileNames.get(i).endsWith(".gif")||fileNames.get(i).endsWith(".png")
                ||fileNames.get(i).endsWith(".bmp")||fileNames.get(i).endsWith(".wbmp")
                ) {
            my_image_view.setVisibility(View.VISIBLE);

            Uri uri = Uri.parse(url);
            my_image_view.setImageURI(uri);
            my_image_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString("imgPaths", url);
                    goToActivity(context, ZoomImageActivity.class, bundle);
                }
            });
        }else if(fileNames.get(i).endsWith(".MP3")||fileNames.get(i).endsWith(".M4A")
                ||fileNames.get(i).endsWith(".WAV")||fileNames.get(i).endsWith(".AMR")
                ||fileNames.get(i).endsWith(".AWB")||fileNames.get(i).endsWith(".WMA")
                ||fileNames.get(i).endsWith(".OGG")||fileNames.get(i).endsWith(".3GPP")
             ||fileNames.get(i).endsWith(".mp3")||fileNames.get(i).endsWith(".m4a")
                ||fileNames.get(i).endsWith(".wav")||fileNames.get(i).endsWith(".amr")
                ||fileNames.get(i).endsWith(".awb")||fileNames.get(i).endsWith(".wma")
                ||fileNames.get(i).endsWith(".ogg")||fileNames.get(i).endsWith(".3gpp")
        ){

            voice_file.setVisibility(View.VISIBLE);
            voice_file.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString("imgPaths", url);
                    bundle.putString("fileName", filename);
                    goToActivity(context, PlayAudioActivity.class, bundle);
                }
            });


        }

        return view;
    }


    public void goToActivity(Context context, Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(context, cls);
        if (bundle == null) {
            bundle = new Bundle();
        }
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
