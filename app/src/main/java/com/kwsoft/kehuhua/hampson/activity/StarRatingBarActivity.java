package com.kwsoft.kehuhua.hampson.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.kwsoft.kehuhua.adcustom.R;
import com.kwsoft.kehuhua.adcustom.base.BaseActivity;
import com.kwsoft.kehuhua.config.Constant;
import com.kwsoft.kehuhua.urlCnn.EdusStringCallback;
import com.kwsoft.kehuhua.urlCnn.ErrorToast;
import com.kwsoft.kehuhua.widget.CommonToolbar;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.Map;

import okhttp3.Call;

import static com.kwsoft.kehuhua.config.Constant.topBarColor;

/**
 * Created by Administrator on 2016/11/8 0008.
 */

public class StarRatingBarActivity extends BaseActivity implements View.OnClickListener {
    public CheckBox cb_first_rb1, cb_first_rb2, cb_first_rb3, cb_first_rb4, cb_first_rb5;//一颗星
    public CheckBox cb_sec_rb1, cb_sec_rb2, cb_sec_rb3;//两颗星
    public CheckBox cb_third_rb1, cb_third_rb2, cb_third_rb3, cb_third_rb4;//三颗星
    public CheckBox cb_forth_rb1, cb_forth_rb2, cb_forth_rb3, cb_forth_rb4, cb_forth_rb5, cb_forth_rb6;//四颗星
    public CheckBox cb_fifth_rb1, cb_fifth_rb2, cb_fifth_rb3, cb_fifth_rb4, cb_fifth_rb5;//五颗星
    public EditText et_content;
    public CheckBox ratebar1, ratebar2, ratebar3, ratebar4, ratebar5;
    public LinearLayout ll_cb_first, ll_cb_sec, ll_cb_third, ll_cb_forth, ll_cb_fifth;
    private CommonToolbar mToolbar;
    private Button btnAdd;
    private String ratingBar, rbDetailStr, assessStr, contentStr;//星级、星级对应的评价、备注内容
    private TextView tv_assess;//教师点评

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star_rating_bar);
        getDataIntent();
        initView();


    }

    private String mainTableId, mainPageId, tableId, pageId, dataId;


    private void getDataIntent() {
        //获取数据并解析
        Intent intent = getIntent();
        String buttonSetItemStr = intent.getStringExtra("itemSet");
        Map<String, Object> buttonSetItem = JSON.parseObject(buttonSetItemStr);
        Log.e(TAG, "getIntentData: buttonSetItem " + buttonSetItem.toString());

        //获取参数并添加
        //mainTableId
        mainTableId = String.valueOf(buttonSetItem.get("tableIdList"));
        //mainPageId
        mainPageId = String.valueOf(buttonSetItem.get("pageIdList"));
        //tableId
        tableId = String.valueOf(buttonSetItem.get("tableId"));
        //pageId
        pageId = String.valueOf(buttonSetItem.get("startTurnPage"));
        //dataId：在对列表操作的时候是没有的，只有行级操作的时候才有
        dataId = String.valueOf(buttonSetItem.get("dataId"));
    }

    @Override
    public void initView() {
        mToolbar = (CommonToolbar) findViewById(R.id.common_toolbar);
        mToolbar.setTitle("评价");
        mToolbar.setBackgroundColor(getResources().getColor(topBarColor));
        //左侧返回按钮
        mToolbar.setRightButtonIcon(getResources().getDrawable(R.mipmap.edit_commit1));
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
//                popButton();
            }
        });

        ratebar1 = (CheckBox) findViewById(R.id.ratebar1);
        ratebar2 = (CheckBox) findViewById(R.id.ratebar2);
        ratebar3 = (CheckBox) findViewById(R.id.ratebar3);
        ratebar4 = (CheckBox) findViewById(R.id.ratebar4);
        ratebar5 = (CheckBox) findViewById(R.id.ratebar5);

        tv_assess = (TextView) findViewById(R.id.tv_assess);

        ll_cb_first = (LinearLayout) findViewById(R.id.ll_cb_first);
        ll_cb_sec = (LinearLayout) findViewById(R.id.ll_cb_sec);
        ll_cb_third = (LinearLayout) findViewById(R.id.ll_cb_third);
        ll_cb_forth = (LinearLayout) findViewById(R.id.ll_cb_forth);
        ll_cb_fifth = (LinearLayout) findViewById(R.id.ll_cb_fifth);

        cb_first_rb1 = (CheckBox) findViewById(R.id.cb_first_rb1);
        cb_first_rb2 = (CheckBox) findViewById(R.id.cb_first_rb2);
        cb_first_rb3 = (CheckBox) findViewById(R.id.cb_first_rb3);
        cb_first_rb4 = (CheckBox) findViewById(R.id.cb_first_rb4);
        cb_first_rb5 = (CheckBox) findViewById(R.id.cb_first_rb5);

        cb_sec_rb1 = (CheckBox) findViewById(R.id.cb_sec_rb1);
        cb_sec_rb2 = (CheckBox) findViewById(R.id.cb_sec_rb2);
        cb_sec_rb3 = (CheckBox) findViewById(R.id.cb_sec_rb3);

        cb_third_rb1 = (CheckBox) findViewById(R.id.cb_third_rb1);
        cb_third_rb2 = (CheckBox) findViewById(R.id.cb_third_rb2);
        cb_third_rb3 = (CheckBox) findViewById(R.id.cb_third_rb3);
        cb_third_rb4 = (CheckBox) findViewById(R.id.cb_third_rb4);

        cb_forth_rb1 = (CheckBox) findViewById(R.id.cb_forth_rb1);
        cb_forth_rb2 = (CheckBox) findViewById(R.id.cb_forth_rb2);
        cb_forth_rb3 = (CheckBox) findViewById(R.id.cb_forth_rb3);
        cb_forth_rb4 = (CheckBox) findViewById(R.id.cb_forth_rb4);
        cb_forth_rb5 = (CheckBox) findViewById(R.id.cb_forth_rb5);
        cb_forth_rb6 = (CheckBox) findViewById(R.id.cb_forth_rb6);

        cb_fifth_rb1 = (CheckBox) findViewById(R.id.cb_fifth_rb1);
        cb_fifth_rb2 = (CheckBox) findViewById(R.id.cb_fifth_rb2);
        cb_fifth_rb3 = (CheckBox) findViewById(R.id.cb_fifth_rb3);
        cb_fifth_rb4 = (CheckBox) findViewById(R.id.cb_fifth_rb4);
        cb_fifth_rb5 = (CheckBox) findViewById(R.id.cb_fifth_rb5);

        et_content = (EditText) findViewById(R.id.et_content);
        btnAdd = (Button) findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(this);
        initRateBarListener();
    }

    private void initRateBarListener() {
        ratebar1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                ratebar1.setChecked(true);
                if (b) {
                    if (!ratebar5.isChecked() && !ratebar4.isChecked() && !ratebar3.isChecked() && !ratebar2.isChecked()) {
                        ll_cb_first.setVisibility(View.VISIBLE);
                        ll_cb_sec.setVisibility(View.GONE);
                        ll_cb_third.setVisibility(View.GONE);
                        ll_cb_forth.setVisibility(View.GONE);
                        ll_cb_fifth.setVisibility(View.GONE);
                        tv_assess.setText("差");
                    } else {
                        ll_cb_first.setVisibility(View.GONE);
                    }
                }
            }
        });

        ratebar2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    ratebar1.setChecked(true);
                    if (!ratebar5.isChecked() && !ratebar4.isChecked() && !ratebar3.isChecked()) {
                        ll_cb_first.setVisibility(View.GONE);
                        ll_cb_sec.setVisibility(View.VISIBLE);
                        ll_cb_third.setVisibility(View.GONE);
                        ll_cb_forth.setVisibility(View.GONE);
                        ll_cb_fifth.setVisibility(View.GONE);
                        tv_assess.setText("不满意");
                    }
                } else {
                    ratebar3.setChecked(false);
                    ratebar4.setChecked(false);
                    ratebar5.setChecked(false);
                    ll_cb_sec.setVisibility(View.GONE);
                    //if (ratebar1.isChecked()) {
                    ll_cb_first.setVisibility(View.VISIBLE);
                    tv_assess.setText("差");
                    // }
                }
            }
        });
        ratebar3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (!ratebar5.isChecked() && !ratebar4.isChecked()) {
                        ll_cb_first.setVisibility(View.GONE);
                        ll_cb_sec.setVisibility(View.GONE);
                        ll_cb_third.setVisibility(View.VISIBLE);
                        ll_cb_forth.setVisibility(View.GONE);
                        ll_cb_fifth.setVisibility(View.GONE);
                        tv_assess.setText("一般，需进步");
                    }
                    ratebar1.setChecked(true);
                    ratebar2.setChecked(true);
                } else {
                    ratebar4.setChecked(false);
                    ratebar5.setChecked(false);
                    ll_cb_third.setVisibility(View.GONE);
                    if (ratebar2.isChecked()) {
                        ll_cb_sec.setVisibility(View.VISIBLE);
                        tv_assess.setText("不满意");
                    }
                }
            }
        });
        ratebar4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (!ratebar5.isChecked()) {
                        ll_cb_first.setVisibility(View.GONE);
                        ll_cb_sec.setVisibility(View.GONE);
                        ll_cb_third.setVisibility(View.GONE);
                        ll_cb_forth.setVisibility(View.VISIBLE);
                        ll_cb_fifth.setVisibility(View.GONE);
                        tv_assess.setText("满意，仍需进步");
                    }
                    ratebar1.setChecked(true);
                    ratebar2.setChecked(true);
                    ratebar3.setChecked(true);
                } else {
                    ratebar5.setChecked(false);
                    ll_cb_forth.setVisibility(View.GONE);
                    if (ratebar3.isChecked()) {
                        ll_cb_third.setVisibility(View.VISIBLE);
                        tv_assess.setText("一般，需进步");
                    }
                }
            }
        });
        ratebar5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    ratebar1.setChecked(true);
                    ratebar2.setChecked(true);
                    ratebar3.setChecked(true);
                    ratebar4.setChecked(true);
                    ll_cb_first.setVisibility(View.GONE);
                    ll_cb_sec.setVisibility(View.GONE);
                    ll_cb_third.setVisibility(View.GONE);
                    ll_cb_forth.setVisibility(View.GONE);
                    ll_cb_fifth.setVisibility(View.VISIBLE);
                    tv_assess.setText("非常满意，无可挑剔");
                } else {
                    ll_cb_fifth.setVisibility(View.GONE);
                    if (ratebar4.isChecked()) {
                        ll_cb_forth.setVisibility(View.VISIBLE);
                        tv_assess.setText("满意，仍需进步");
                    }
                }
            }
        });
    }

    private static final String TAG = "StarRatingBarActivity";


    private void getCommit() {

        String pingJiaValue = "";
        if (hasInternetConnected()) {
            dialog.show();
            String volleyUrl1 = Constant.sysUrl + Constant.commitAdd + "?" +
                    Constant.tableId + "=" + tableId + "&" + Constant.pageId + "=" + pageId + "&" +
                    pingJiaValue + "&t0_au_" + tableId + "_" + pageId + "_3301=" + dataId;//+ keyRelation

            //请求地址（关联添加和修改）
            String volleyUrl = volleyUrl1.replaceAll(" ", "%20").replaceAll("&&", "&");
            //get请求
            OkHttpUtils
                    .get()
                    .url(volleyUrl)
                    .build()
                    .execute(new EdusStringCallback(StarRatingBarActivity.this) {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            ErrorToast.errorToast(mContext, e);
                            dialog.dismiss();
                            Toast.makeText(StarRatingBarActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Log.e(TAG, "onResponse: " + response);
                            if (response != null && !response.equals("0")) {
                                //返回列表页面并刷新
//                                    backToInfo();
                            } else {
                                Toast.makeText(StarRatingBarActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "无网络", Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                //获取星级
                if (ratebar1.isChecked() && (!ratebar2.isChecked())) {
                    ratingBar = "一星";
                } else if (ratebar2.isChecked() && (!ratebar3.isChecked())) {
                    ratingBar = "两星";
                } else if (ratebar3.isChecked() && (!ratebar4.isChecked())) {
                    ratingBar = "三星";
                } else if (ratebar4.isChecked() && (!ratebar5.isChecked())) {
                    ratingBar = "四星";
                } else if (ratebar5.isChecked()){
                    ratingBar = "五星";
                }
                //获取星级介绍
                rbDetailStr = tv_assess.getText().toString();

                //获取星级对应评价
                if (ll_cb_first.getVisibility() == View.VISIBLE) {
                    Log.e("TAG",cb_first_rb1.isChecked()+"");
                    if (cb_first_rb1.isChecked()) {
                        assessStr = cb_first_rb1.getText().toString();
                    } else if (cb_first_rb2.isChecked()) {
                        assessStr = cb_first_rb2.getText().toString();
                    } else if (cb_first_rb3.isChecked()) {
                        assessStr = cb_first_rb3.getText().toString();
                    } else if (cb_first_rb4.isChecked()) {
                        assessStr = cb_first_rb4.getText().toString();
                    } else if (cb_first_rb5.isChecked()) {
                        assessStr = cb_first_rb5.getText().toString();
                    }

                } else if (ll_cb_sec.getVisibility() == View.VISIBLE) {
                    if (cb_sec_rb1.isChecked()) {
                        assessStr = cb_sec_rb1.getText().toString();
                    } else if (cb_sec_rb2.isChecked()) {
                        assessStr = cb_sec_rb2.getText().toString();
                    } else if (cb_sec_rb3.isChecked()) {
                        assessStr = cb_sec_rb3.getText().toString();
                    }
                } else if (ll_cb_third.getVisibility() == View.VISIBLE) {
                    if (cb_third_rb1.isChecked()) {
                        assessStr = cb_third_rb1.getText().toString();
                    } else if (cb_third_rb2.isChecked()) {
                        assessStr = cb_third_rb2.getText().toString();
                    } else if (cb_third_rb3.isChecked()) {
                        assessStr = cb_third_rb3.getText().toString();
                    } else if (cb_third_rb4.isChecked()) {
                        assessStr = cb_third_rb4.getText().toString();
                    }
                } else if (ll_cb_forth.getVisibility() == View.VISIBLE) {
                    if (cb_forth_rb1.isChecked()) {
                        assessStr = cb_forth_rb1.getText().toString();
                    } else if (cb_forth_rb2.isChecked()) {
                        assessStr = cb_forth_rb2.getText().toString();
                    } else if (cb_forth_rb3.isChecked()) {
                        assessStr = cb_forth_rb3.getText().toString();
                    } else if (cb_forth_rb4.isChecked()) {
                        assessStr = cb_forth_rb4.getText().toString();
                    } else if (cb_forth_rb5.isChecked()) {
                        assessStr = cb_forth_rb5.getText().toString();
                    } else if (cb_forth_rb6.isChecked()) {
                        assessStr = cb_forth_rb6.getText().toString();
                    }
                } else if (ll_cb_fifth.getVisibility() == View.VISIBLE){
                    if (cb_fifth_rb1.isChecked()) {
                        assessStr = cb_fifth_rb1.getText().toString();
                    } else if (cb_fifth_rb2.isChecked()) {
                        assessStr = cb_fifth_rb2.getText().toString();
                    } else if (cb_fifth_rb3.isChecked()) {
                        assessStr = cb_fifth_rb3.getText().toString();
                    } else if (cb_fifth_rb4.isChecked()) {
                        assessStr = cb_fifth_rb4.getText().toString();
                    } else if (cb_fifth_rb5.isChecked()) {
                        assessStr = cb_fifth_rb5.getText().toString();
                    }
                }

                //评论备注
                contentStr = et_content.getText().toString();

                Log.e("con",contentStr+"/"+ratingBar+"/"+rbDetailStr+"/"+assessStr);
                break;
            default:
                break;
        }


    }
}
