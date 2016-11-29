package com.kwsoft.kehuhua.hampson.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.kwsoft.kehuhua.adcustom.R;
import com.kwsoft.kehuhua.adcustom.base.BaseActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;


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

            my_image_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((BaseActivity)context).dialog.show();
                    try {
                        showProgressiveJPEGs(datas.get(i),fileNames.get(i));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        return view;
    }

    private static final String TAG = "ZuoYeGridViewAdapter";
    /**
     * 演示：逐渐加载的图片，即，从模糊逐渐清晰。需要图片本身也支持这种方式
     */
    private void showProgressiveJPEGs(String url,String fileName) {
        final String path= Environment.getExternalStorageDirectory().getPath();
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
    private void openFile(final String filePath)
    {
        Log.e(TAG, "openFile: filePath "+filePath);
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
            context.startActivity(intent);
            ((BaseActivity)context).dialog.dismiss();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(context, "无法打开后缀名为." + ext + "的文件！",
                    Toast.LENGTH_LONG).show();
        }
    }
}
