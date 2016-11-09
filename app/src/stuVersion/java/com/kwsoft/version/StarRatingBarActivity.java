package com.kwsoft.version;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import com.kwsoft.kehuhua.adcustom.R;
import com.kwsoft.kehuhua.adcustom.base.BaseActivity;
import com.kwsoft.kehuhua.widget.CommonToolbar;

import static com.kwsoft.kehuhua.config.Constant.topBarColor;

/**
 * Created by Administrator on 2016/11/8 0008.
 */

public class StarRatingBarActivity extends BaseActivity {
    public RatingBar rb_ratingbar;
    public CheckBox cb_first_rb1, cb_first_rb2, cb_first_rb3, cb_first_rb4, cb_first_rb5;//一颗星
    public CheckBox cb_sec_rb1, cb_sec_rb2, cb_sec_rb3, cb_sec_rb4;//两颗星
    public CheckBox cb_third_rb1, cb_third_rb2, cb_third_rb3, cb_third_rb4;//三颗星
    public CheckBox cb_forth_rb1, cb_forth_rb2, cb_forth_rb3, cb_forth_rb4, cb_forth_rb5;//四颗星
    public CheckBox cb_fifth_rb1, cb_fifth_rb2, cb_fifth_rb3, cb_fifth_rb4, cb_fifth_rb5;//五颗星
    public CheckBox cb_six_rb1, cb_six_rb2, cb_six_rb3, cb_six_rb4, cb_six_rb5;//0颗星
    public EditText et_content;
    public CheckBox ratebar1, ratebar2, ratebar3, ratebar4, ratebar5;
    public LinearLayout ll_cb_first, ll_cb_sec, ll_cb_third, ll_cb_forth, ll_cb_fifth, ll_cb_six;
    private CommonToolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star_rating_bar);
        initView();
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

        //rb_ratingbar = (RatingBar) findViewById(R.id.rb_ratingbar);

        ll_cb_first = (LinearLayout) findViewById(R.id.ll_cb_first);
        ll_cb_sec = (LinearLayout) findViewById(R.id.ll_cb_sec);
        ll_cb_third = (LinearLayout) findViewById(R.id.ll_cb_third);
        ll_cb_forth = (LinearLayout) findViewById(R.id.ll_cb_forth);
        ll_cb_fifth = (LinearLayout) findViewById(R.id.ll_cb_fifth);
        ll_cb_six = (LinearLayout) findViewById(R.id.ll_cb_six);

        cb_first_rb1 = (CheckBox) findViewById(R.id.cb_fifth_rb1);
        cb_first_rb2 = (CheckBox) findViewById(R.id.cb_first_rb2);
        cb_first_rb3 = (CheckBox) findViewById(R.id.cb_first_rb3);
        cb_first_rb4 = (CheckBox) findViewById(R.id.cb_first_rb4);
        cb_first_rb5 = (CheckBox) findViewById(R.id.cb_first_rb5);

        cb_sec_rb1 = (CheckBox) findViewById(R.id.cb_sec_rb1);
        cb_sec_rb2 = (CheckBox) findViewById(R.id.cb_sec_rb2);
        cb_sec_rb3 = (CheckBox) findViewById(R.id.cb_sec_rb3);
        cb_sec_rb4 = (CheckBox) findViewById(R.id.cb_sec_rb4);

        cb_third_rb1 = (CheckBox) findViewById(R.id.cb_third_rb1);
        cb_third_rb2 = (CheckBox) findViewById(R.id.cb_third_rb2);
        cb_third_rb3 = (CheckBox) findViewById(R.id.cb_third_rb3);
        cb_third_rb4 = (CheckBox) findViewById(R.id.cb_third_rb4);

        cb_forth_rb1 = (CheckBox) findViewById(R.id.cb_forth_rb1);
        cb_forth_rb2 = (CheckBox) findViewById(R.id.cb_forth_rb2);
        cb_forth_rb3 = (CheckBox) findViewById(R.id.cb_forth_rb3);
        cb_forth_rb4 = (CheckBox) findViewById(R.id.cb_forth_rb4);
        cb_forth_rb5 = (CheckBox) findViewById(R.id.cb_forth_rb5);

        cb_fifth_rb1 = (CheckBox) findViewById(R.id.cb_fifth_rb1);
        cb_fifth_rb2 = (CheckBox) findViewById(R.id.cb_fifth_rb2);
        cb_fifth_rb3 = (CheckBox) findViewById(R.id.cb_fifth_rb3);
        cb_fifth_rb4 = (CheckBox) findViewById(R.id.cb_fifth_rb4);
        cb_fifth_rb5 = (CheckBox) findViewById(R.id.cb_fifth_rb5);

        cb_six_rb1 = (CheckBox) findViewById(R.id.cb_six_rb1);
        cb_six_rb2 = (CheckBox) findViewById(R.id.cb_six_rb2);
        cb_six_rb3 = (CheckBox) findViewById(R.id.cb_six_rb3);
        cb_six_rb4 = (CheckBox) findViewById(R.id.cb_six_rb4);
        cb_six_rb5 = (CheckBox) findViewById(R.id.cb_six_rb5);

        et_content = (EditText) findViewById(R.id.et_content);
        initRateBarListener();
    }

    private void initRateBarListener() {
        ratebar1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (!ratebar5.isChecked() && !ratebar4.isChecked() && !ratebar3.isChecked() && !ratebar2.isChecked()) {
                        ll_cb_first.setVisibility(View.VISIBLE);
                        ll_cb_sec.setVisibility(View.GONE);
                        ll_cb_third.setVisibility(View.GONE);
                        ll_cb_forth.setVisibility(View.GONE);
                        ll_cb_fifth.setVisibility(View.GONE);
                        ll_cb_six.setVisibility(View.GONE);
                    }

                } else {
                    ratebar2.setChecked(false);
                    ratebar3.setChecked(false);
                    ratebar4.setChecked(false);
                    ratebar5.setChecked(false);
                    ll_cb_first.setVisibility(View.GONE);
                    ll_cb_six.setVisibility(View.VISIBLE);
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
                        ll_cb_six.setVisibility(View.GONE);
                    }
                } else {
                    ratebar3.setChecked(false);
                    ratebar4.setChecked(false);
                    ratebar5.setChecked(false);
                    ll_cb_sec.setVisibility(View.GONE);
                    if (!ratebar1.isChecked()) {
                        ll_cb_six.setVisibility(View.VISIBLE);
                    }
                    else {
                        ll_cb_first.setVisibility(View.VISIBLE);
                        ll_cb_six.setVisibility(View.GONE);
                    }
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
                        ll_cb_six.setVisibility(View.GONE);
                    }
                    ratebar1.setChecked(true);
                    ratebar2.setChecked(true);
                } else {
                    ratebar4.setChecked(false);
                    ratebar5.setChecked(false);
                    ll_cb_third.setVisibility(View.GONE);
                    if (ratebar2.isChecked()){
                        ll_cb_sec.setVisibility(View.VISIBLE);
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
                        ll_cb_six.setVisibility(View.GONE);
                    }
                    ratebar1.setChecked(true);
                    ratebar2.setChecked(true);
                    ratebar3.setChecked(true);
                } else {
                    ratebar5.setChecked(false);
                    ll_cb_forth.setVisibility(View.GONE);
                    if (ratebar3.isChecked()){
                        ll_cb_third.setVisibility(View.VISIBLE);
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
                    ll_cb_six.setVisibility(View.GONE);
                } else {
                    ll_cb_fifth.setVisibility(View.GONE);
                    if (ratebar4.isChecked()){
                        ll_cb_forth.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }
}
