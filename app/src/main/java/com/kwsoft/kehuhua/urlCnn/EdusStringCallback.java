package com.kwsoft.kehuhua.urlCnn;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.kwsoft.version.StuLoginActivity;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;
import java.util.Map;

import okhttp3.Response;

/**
 * Created by Administrator on 2016/10/27 0027.
 */

public abstract class EdusStringCallback extends Callback<String> {


    public Context mContext;


    public EdusStringCallback(Context mContext) {
        this.mContext = mContext;
        SwitchStatueCode.netToast((Activity) this.mContext);

    }

    @Override
    public String parseNetworkResponse(Response response, int id) throws IOException {
        return response.body().string();
    }

}
