package com.kwsoft.kehuhua.adcustom;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.kwsoft.kehuhua.widget.CommonToolbar;

import static com.kwsoft.kehuhua.config.Constant.topBarColor;

public class CourseDetailActivity extends AppCompatActivity {

    private TextView startTime,endTime,tvdetail,zhangJieMingCheng,zhangJieRenWu,zhangJieMiaoShu;
    private CommonToolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        startTime = (TextView) findViewById(R.id.start_time);
        endTime = (TextView) findViewById(R.id.end_time);
        tvdetail = (TextView) findViewById(R.id.course_content);
        zhangJieMingCheng= (TextView) findViewById(R.id.CHAPTER_NAME);
        zhangJieRenWu= (TextView) findViewById(R.id.CHAPTER_TASK);
        zhangJieMiaoShu= (TextView) findViewById(R.id.CHAPTER_DESCRIBE);
        mToolbar = (CommonToolbar) findViewById(R.id.common_toolbar);

        mToolbar.setBackgroundColor(getResources().getColor(topBarColor));
        //左侧返回按钮
        mToolbar.setLeftButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent=getIntent();
        if (intent!=null) {
            String sTimeStr=intent.getStringExtra("sTimeStr");
            String eTimeStr=intent.getStringExtra("eTimeStr");
            String content=intent.getStringExtra("content");
            String content1=content.replaceAll(",","\n");
            String content2=content1.replaceAll("_","：");
            String content3=content2.replaceAll(":","：");
            String CHAPTER_DESCRIBE=intent.getStringExtra("CHAPTER_DESCRIBE");
            String CHAPTER_NAME=intent.getStringExtra("CHAPTER_NAME");
            String CHAPTER_TASK=intent.getStringExtra("CHAPTER_TASK");


            startTime.setText(sTimeStr);
            endTime.setText(eTimeStr);
            tvdetail.setText(content3);
            zhangJieMingCheng.setText(!CHAPTER_NAME.equals("null") ? CHAPTER_NAME : "无内容");
            zhangJieRenWu.setText(!CHAPTER_TASK.equals("null") ? CHAPTER_TASK : "无内容");
            zhangJieMiaoShu.setText(!CHAPTER_DESCRIBE.equals("null") ? CHAPTER_DESCRIBE : "无内容");
        }

        mToolbar.setTitle("课程详情");
    }
}
