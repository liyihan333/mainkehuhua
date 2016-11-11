package com.kwsoft.kehuhua.hampson.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.kwsoft.kehuhua.adcustom.InfoActivity;
import com.kwsoft.kehuhua.adcustom.R;
import com.kwsoft.kehuhua.adcustom.base.BaseActivity;
import com.kwsoft.kehuhua.config.Constant;
import com.kwsoft.kehuhua.hampson.adapter.CourseRatingBarAdapter;
import com.kwsoft.kehuhua.urlCnn.EdusStringCallback;
import com.kwsoft.kehuhua.urlCnn.ErrorToast;
import com.kwsoft.kehuhua.utils.DataProcess;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;



public class CourseRatingBarFragment extends Fragment {
    @Bind(R.id.lv)
    ListView mRecyclerView;

    //    private List<Map<String, String>> list = new ArrayList<>();

    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private String tableId, pageId;


    private int totalNum = 0;
    private int start = 0;
    private final int limit = 20;
    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFREH = 1;
    private static final int STATE_MORE = 2;
    private int state = STATE_NORMAL;


    private String operaButtonSet;
    private List<List<Map<String, String>>> datas;
    private CourseRatingBarAdapter mAdapter;
    private static final String TAG = "CourseRatingBarFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_course_ratingbar_star_list, container, false);
        ButterKnife.bind(this, view);

        ((BaseActivity) getActivity()).dialog.show();
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
//        initRefreshLayout();//初始化空间
        getDataIntent();//获取初始化数据
        getData();
//        Map<String, Object> map = new HashMap<>();
//        map.put("title", "少儿一对一英语基础课程");
//        map.put("teachName", "李明福");
//        map.put("teachContent", "第一阶段第一阶段第一阶段第一阶段第一阶段第一阶段");
//        String[] tags = {"一般ds", "没有新意", "作业太多", "我很认真", "讲课速度太快"};
//        map.put("tags",tags);
//        list.add(map);
//        String[] tags1 = {"没有新意dsf", "作业太多", "我很认真", "讲课速度太快"};
//        map.put("tags",tags1);
//        list.add(map);
//        list.add(map);
//        list.add(map);
//        CourseRatingBarAdapter adapter = new CourseRatingBarAdapter(getActivity(),list);
//        listView.setAdapter(adapter);
        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
    public Bundle listDataBundle;
    private Map<String, String> paramsMap;

    public void getDataIntent() {



        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        //设置下拉刷新监听
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                new LoadDataThread().start();
            }
        });





        listDataBundle = getArguments();
        String paramsStr = listDataBundle.getString("listFragmentData");

        paramsMap = JSON.parseObject(paramsStr,
                new TypeReference<Map<String, String>>() {
                });

        tableId = paramsMap.get(Constant.tableId);
        pageId = paramsMap.get(Constant.pageId);
        Constant.mainTableIdValue = tableId;
        Constant.mainPageIdValue = pageId;
    }
    /**
     * 加载菜单数据的线程
     */
    class LoadDataThread extends Thread {
        @Override
        public void run() {
            //下载数据，重新设定dataList
            getData();
            //防止数据加载过快动画效果差
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.e("TAG", "学员端开始handler通知 ");
            handler.sendEmptyMessage(0x101);//通过handler发送一个更新数据的标记，适配器进行dataSetChange，然后停止刷新动画
        }
    }

    //下拉刷新handler
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x101:
                    Log.e("TAG", "学员端开始handler通知跳转后 ");
                    if (swipeRefreshLayout.isRefreshing()) {
                        mAdapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);//设置不刷新
                        Toast.makeText(getActivity(), "数据已刷新", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };
    /**
     * 获取字段接口数据
     */
    @SuppressWarnings("unchecked")
    public void getData() {
        if (((BaseActivity) getActivity()).hasInternetConnected()) {

            //地址
            String volleyUrl = Constant.sysUrl + Constant.requestListSet;
            Log.e("TAG", "列表请求地址：" + volleyUrl);

            //参数
            paramsMap.put("start", start + "");
            paramsMap.put("limit", limit + "");

            Log.e(TAG, "getData: paramsMap " + paramsMap.toString());
            //请求
            OkHttpUtils
                    .post()
                    .params(paramsMap)
                    .url(volleyUrl)
                    .build()
                    .execute(new EdusStringCallback(getActivity()) {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            ErrorToast.errorToast(mContext, e);
//                            mRefreshLayout.finishRefresh();
                            ((BaseActivity) getActivity()).dialog.dismiss();
                            backStart();
                            Log.e(TAG, "onError: Call  " + call + "  id  " + id);
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Log.e(TAG, "onResponse: " + "  id  " + id);

                            setStore(response);
                        }
                    });
        } else {

            ((BaseActivity) getActivity()).dialog.dismiss();
//            mRefreshLayout.finishRefresh();
            Toast.makeText(getActivity(), "请连接网络", Toast.LENGTH_SHORT).show();
            backStart();
        }
    }

    public void backStart() {

        //下拉失败后需要将加上limit的strat返还给原来的start，否则会获取不到数据
        if (state == STATE_MORE) {
            //start只能是limit的整数倍
            if (start > limit) {
                start -= limit;
            }
//            mRefreshLayout.finishRefreshLoadMore();
        }
    }

    List<Map<String, Object>> operaButtonSetList;

    @SuppressWarnings("unchecked")
    public void setStore(String jsonData) {
        List<Map<String, Object>> dataList = new ArrayList<>();
        List<Map<String, Object>> fieldSet = new ArrayList<>();
        Log.e("TAG", "解析set" + jsonData);
        try {
            Map<String, Object> setMap = JSON.parseObject(jsonData,
                    new TypeReference<Map<String, Object>>() {
                    });
//获取各项总配置pageSet父级
            Map<String, Object> pageSet = (Map<String, Object>) setMap.get("pageSet");
//获取条目总数
            totalNum = Integer.valueOf(String.valueOf(setMap.get("dataCount")));

//获取子项内部按钮
            if (pageSet.get("operaButtonSet") != null) {
                try {
                    operaButtonSetList = (List<Map<String, Object>>) pageSet.get("operaButtonSet");

                    if (operaButtonSetList.size() > 0) {
                        for (int i = 0; i < operaButtonSetList.size(); i++) {
                            operaButtonSetList.get(i).put("tableIdList", tableId);
                            operaButtonSetList.get(i).put("pageIdList", pageId);
                        }
                    }
                    operaButtonSet = JSONArray.toJSONString(operaButtonSetList);
                    Log.e("TAG", "获取operaButtonSet" + operaButtonSet);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

//数据左侧配置数据
            fieldSet = (List<Map<String, Object>>) pageSet.get("fieldSet");
            Log.e("TAG", "获取fieldSet" + fieldSet.toString());
//获取dataList
            dataList = (List<Map<String, Object>>) setMap.get("dataList");
            Log.e("TAG", "获取dataList" + dataList);

        } catch (Exception e) {
            e.printStackTrace();
            ((BaseActivity) getActivity()).dialog.dismiss();
        }
//将dataList与fieldSet合并准备适配数据
        datas = DataProcess.combineSetData(tableId, pageId, fieldSet, dataList);
        showData();


    }

    public int isResume = 0;

    /**
     * 分动作展示数据
     */
    private void showData() {
        Log.e(TAG, "showData: " + state);
        switch (state) {
            case STATE_NORMAL:
                normalRequest();
                break;
            case STATE_REFREH:

//                if (mAdapter != null) {
//
//                    mAdapter.clearData();
//                    mAdapter.addData(datas);
//                    mRecyclerView.scrollToPosition(0);
//                    mRefreshLayout.finishRefresh();
//                    if (datas.size() == 0) {
//                        Snackbar.make(mRecyclerView, "本页无数据", Snackbar.LENGTH_SHORT).show();
//                    } else if(isResume==0){
//                        Snackbar.make(mRecyclerView, "共"+totalNum+"条", Snackbar.LENGTH_SHORT).show();
//                    }
//
//                }
//                isResume=0;
                break;
            case STATE_MORE:
//                if (mAdapter != null) {
//                    mAdapter.addData(mAdapter.getDatas().size(), datas);
//                    mRecyclerView.scrollToPosition(mAdapter.getDatas().size());
//                    mRefreshLayout.finishRefreshLoadMore();
//                    Snackbar.make(mRecyclerView, "更新了" + datas.size() + "条数据", Snackbar.LENGTH_SHORT).show();
//                }

                break;
        }
    }

    /**
     * 下拉刷新方法
     */
    private void refreshData() {
        start = 0;
        state = STATE_REFREH;

        getData();

    }

    /**
     * 上拉加载方法
     */
    private void loadMoreData() {

        start += limit;
        state = STATE_MORE;
        getData();

    }

    public void normalRequest() {
        Log.e(TAG, "normalRequest: ");
        mAdapter = new CourseRatingBarAdapter(getActivity(), datas);
        mRecyclerView.setAdapter(mAdapter);
//        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity()));
//        mRecyclerView.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.VERTICAL));
////        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
////                mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
        ((BaseActivity) getActivity()).dialog.dismiss();
        if (totalNum == 0) {
            Snackbar.make(mRecyclerView, "本页无数据", Snackbar.LENGTH_SHORT).show();

        }

    }

    /**
     * 跳转至子菜单列表
     */
    public void toItem(String itemData) {
        try {
            Intent intent = new Intent();
            intent.setClass(getActivity(), InfoActivity.class);
            intent.putExtra("childData", itemData);
            intent.putExtra("tableId", tableId);
            intent.putExtra("operaButtonSet", operaButtonSet);

            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refreshPage(int position) {
//        titleName = childList.get(position).get("menuName") + "";
        Log.e("TAG", "list子菜单position " + position);
        //重新设置顶部名称
//        mToolbar.setTitle(titleName);
        //重设参数值
        paramsMap.put(Constant.tableId, tableId);
        paramsMap.put(Constant.pageId, pageId);
        Constant.paramsMapSearch = paramsMap;
        Constant.mainTableIdValue = tableId;
        Constant.mainPageIdValue = pageId;
        //重新请求数据


        refreshData();


    }
}
