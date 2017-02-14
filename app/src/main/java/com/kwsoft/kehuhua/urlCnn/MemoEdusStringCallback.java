package com.kwsoft.kehuhua.urlCnn;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.kwsoft.version.StuLoginActivity;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Response;

/**
 * Created by Administrator on 2017/2/10 0010.
 */

public abstract class MemoEdusStringCallback extends Callback<String> {
    public Context mContext;


    public MemoEdusStringCallback(Context mContext) {
        this.mContext = mContext;
        SwitchStatueCode.netToast((Activity) this.mContext);

    }

    private static final String TAG = "MemoEdusStringCallback";

    @Override
    public void onResponse(String response, int id) {
        Log.e(TAG, "onResponse: responsestr" + response);
        if (!response.contains("!DOCTYPE html")) {
//            Pattern p = Pattern.compile("[0-9]*");
//            Matcher m = p.matcher(response);
//            if ((response.length() <= 2) && (m.matches())) {
//                edusOnResponse(re]
//
// sponse, id);
//            } else {
//                Map<String, Object> menuMap = JSON.parseObject(response,
//                        new TypeReference<Map<String, Object>>() {
//                        });
//                Log.e(TAG, "onResponse: menuMap" + menuMap.toString());
//                if (menuMap.containsKey("promptMessage")) {
            if (response.contains("promptMessage")) {
                sentLogin();
            } else {
                edusOnResponse(response, id);
            }
//            }
        } else {
            sentLogin();
        }


    }

    private void sentLogin() {
        Toast.makeText(mContext, "已在另一手机登陆，已退出登陆！", Toast.LENGTH_SHORT).show();
        Log.e(TAG, "onResponse: promptMessage" + "promptMessage");
        Intent intent = new Intent(mContext, StuLoginActivity.class);
        mContext.startActivity(intent);
    }

    @Override
    public String parseNetworkResponse(Response response, int id) throws IOException {
        return response.body().string();
    }

    public abstract void edusOnResponse(String response, int id);
}
