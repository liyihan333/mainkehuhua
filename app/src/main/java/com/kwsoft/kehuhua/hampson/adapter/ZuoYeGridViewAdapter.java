package com.kwsoft.kehuhua.hampson.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.facebook.drawee.view.SimpleDraweeView;
import com.kwsoft.kehuhua.adcustom.R;

import java.util.List;


/**
 * Created by Administrator on 2016/11/14 0014.
 *
 */

public class ZuoYeGridViewAdapter extends BaseAdapter {
    private Context context;
    private List<String> datas;
    private List<String> fileNames;

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
    public View getView(final int i, View view, ViewGroup viewGroup) {

        view = LayoutInflater.from(context).inflate(R.layout.hampson_zuoye_item_image_layout, null);
        SimpleDraweeView my_image_view=(SimpleDraweeView)view.findViewById(R.id.my_image_view);
            my_image_view.setVisibility(View.VISIBLE);
//          final String prefix=fileNames.get(i).substring(fileNames.get(i).lastIndexOf(".")+1);
            Uri uri = Uri.parse(datas.get(i));
            my_image_view.setImageURI(uri);
        return view;
    }

    private static final String TAG = "ZuoYeGridViewAdapter";

}
