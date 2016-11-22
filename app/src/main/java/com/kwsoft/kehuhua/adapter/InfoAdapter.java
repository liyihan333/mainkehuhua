package com.kwsoft.kehuhua.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.kwsoft.kehuhua.adcustom.R;
import com.kwsoft.kehuhua.hampson.activity.ReadFileActivity;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/14 0014.
 */

public class InfoAdapter extends BaseAdapter {

    private List<Map<String, String>> fieldSet;
    private Context context;
    private LayoutInflater inflater = null;


    public InfoAdapter(Context context, List<Map<String, String>> fieldSet) {
        this.context = context;
        this.fieldSet = fieldSet;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return fieldSet.size();
    }

    @Override
    public Object getItem(int i) {
        return fieldSet.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private static final String TAG = "InfoAdapter";

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {


        view = inflater.inflate(R.layout.activity_info_item, null);

        TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
        TextView tv_entity_name = (TextView) view.findViewById(R.id.tv_entity_name);
//        ZuoYeImageGridView zuoYeImageGridView=(ZuoYeImageGridView)view.findViewById(R.id.zuoYeImageGridView);
        Map<String, String> itemMap = fieldSet.get(i);
        Log.e(TAG, "getView: itemMap " + itemMap.toString());
        String name = itemMap.get("fieldCnName");
        String value = itemMap.get("fieldCnName2");

        tv_name.setText(name);
        if (name.contains("附件")) {

            tv_entity_name.setVisibility(View.VISIBLE);
//            zuoYeImageGridView.setVisibility(View.VISIBLE);
//
//            //获取mongoDB字符串
//          String downLoadId="";
//            for (int k=0;k<fieldSet.size();k++) {
//
//               String fieldCnName= fieldSet.get(k).get("fieldCnName");
//
//                if (fieldCnName.equals("mongodbId")) {
//                    downLoadId=fieldSet.get(k).get("fieldCnName2");
//                }
//
//            }
//
//
//
//            Log.e(TAG, "getView: downLoadId "+downLoadId);
//            List<String> mongoIds=new ArrayList<>();
//            if (!downLoadId.equals("")) {
//             String[] downLoadIdArr=downLoadId.split(",");
//                for (int m=0;m<downLoadIdArr.length;m++) {
//                    mongoIds.add(downLoadIdArr[m]);
//                }
//            }
//            Log.e(TAG, "getView: mongoIds "+mongoIds.toString() );


//            List<String> fileNames=new ArrayList<>();
            String downLoadId="";
            if (name.contains("作业附件")) {
                for (int k = 0; k < fieldSet.size(); k++) {

                    String fieldCnName = fieldSet.get(k).get("fieldCnName");

                    if (fieldCnName.contains("mongodbid")) {
                        downLoadId = fieldSet.get(k).get("fieldCnName2");
                    }

                }

            }else if(name.contains("完成附件")){

                for (int k = 0; k < fieldSet.size(); k++) {

                    String fieldCnName = fieldSet.get(k).get("fieldCnName");

                    if (fieldCnName.contains("完成作业mongodbID")) {
                        downLoadId = fieldSet.get(k).get("fieldCnName2");
                    }

                }

            }
            if (!value.equals("")) {
                String[] valueArr = value.split(",");
//                for (int m=0;m<valueArr.length;m++) {
//                    fileNames.add(valueArr[m]);
//                }
                String fileNum = valueArr.length + "个附件";
                Log.e(TAG, "getView: fileNum "+fileNum);
                tv_entity_name.setText(fileNum);
                final String fieldSetString = JSON.toJSONString(fieldSet);
                final String finalDownLoadId = downLoadId;
                tv_entity_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, ReadFileActivity.class);
                        intent.putExtra("position", i+"");
                        intent.putExtra("fieldSet", fieldSetString);
                        intent.putExtra("downLoadId", finalDownLoadId);

                        context.startActivity(intent);
                    }
                });


            }else {
                tv_entity_name.setText("无附件");

            }
//
//            if (mongoIds.size()>0) {
//                Log.e(TAG, "getView: mongoIds "+mongoIds);
//                ZuoYeGridViewAdapter gridViewAdapter=new ZuoYeGridViewAdapter(context, mongoIds,fileNames);
//                zuoYeImageGridView.setAdapter(gridViewAdapter);
//            }
//        }else {
//
//
//            tv_entity_name.setText(value);
//
//        }


        }else {
            tv_entity_name.setText(value);

        }
        return view;
    }
}
