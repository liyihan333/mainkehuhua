package com.kwsoft.kehuhua.adapter;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.kwsoft.kehuhua.adcustom.InfoTwoActivity;
import com.kwsoft.kehuhua.adcustom.ListActivity4;
import com.kwsoft.kehuhua.adcustom.OperateDataActivity;
import com.kwsoft.kehuhua.adcustom.OperateDataTwoActivity;
import com.kwsoft.kehuhua.adcustom.R;
import com.kwsoft.kehuhua.adcustom.TabActivity;
import com.kwsoft.kehuhua.config.Constant;
import com.kwsoft.kehuhua.hampson.activity.StarRatingBarActivity;
import com.kwsoft.kehuhua.urlCnn.EdusStringCallback;
import com.kwsoft.kehuhua.urlCnn.ErrorToast;
import com.kwsoft.version.StuPra;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

import static com.kwsoft.kehuhua.config.Constant.tableId;


public class ListAdapter2 extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private List<List<Map<String, String>>> mDatas;
    private List<Map<String, Object>> childTab;
    private Context mContext;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private static final int VIEW_TYPE = 1;
    private List<Map<String, Object>> operaButton;
    private Map<String, String> delMapParams = new HashMap<>();

    /**
     * 获取条目 View填充的类型
     * 默认返回0
     * 将lists为空返回 1
     */
    public int getItemViewType(int position) {
        if (mDatas.size() <= 0) {
            return VIEW_TYPE;
        }
        return super.getItemViewType(position);
    }


    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, String data);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public ListAdapter2(List<List<Map<String, String>>> mDatas, List<Map<String, Object>> childTab) {
        this.mDatas = mDatas;
        this.childTab = childTab;
    }

    public ListAdapter2(List<List<Map<String, String>>> mDatas, List<Map<String, Object>> childTab, List<Map<String, Object>> operaButton) {
        this.mDatas = mDatas;
        this.childTab = childTab;
        this.operaButton = operaButton;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        mContext = parent.getContext();
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        Log.e("TAG", "viewType:" + viewType);
//        Log.e("mdatawyl=",mDatas.get(0).get(0).toString());
        if (VIEW_TYPE == viewType) {
            view = mInflater.inflate(R.layout.empty_view, parent, false);

            return new EmptyViewHolder(view);
        }

        //  if (!StuPra.studentProId.equals(StuPra.teachProId)) {
        view = mInflater.inflate(R.layout.activity_list_item, null);
//        } else {
//            view = mInflater.inflate(R.layout.activity_list_item_teach, null);
//        }

        //将创建的View注册点击事件
        view.setOnClickListener(this);
        return new ListViewHolder(view);
    }

    private static final String TAG = "ListAdapter2";

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder thisHolder, int position) {
        if (thisHolder instanceof ListViewHolder) {
            final ListViewHolder holder = (ListViewHolder) thisHolder;

            List<Map<String, String>> item = getData(position);

            try {
                final String title = item.get(0).get("fieldCnName2");
                holder.studentName.setText(!title.equals("null") ? title : "");
                holder.studentName.setVisibility(View.VISIBLE);
//左1
                String left1Title = item.get(1).get("fieldCnName");
                holder.left1.setText(!left1Title.equals("null") ? left1Title : "");
                holder.left1.setVisibility(View.VISIBLE);
//右1
                String right1Title = item.get(1).get("fieldCnName2");
                holder.right1.setText(!right1Title.equals("null") ? right1Title : "");
                holder.right1.setVisibility(View.VISIBLE);
//左2
                String left2Title = item.get(2).get("fieldCnName");
                holder.left2.setText(!left2Title.equals("null") ? left2Title : "");
                holder.left2.setVisibility(View.VISIBLE);

                String right2Title = item.get(2).get("fieldCnName2");
                holder.right2.setText(!right2Title.equals("null") ? right2Title : "");
                holder.right2.setVisibility(View.VISIBLE);
//左3
                String left3Title = item.get(3).get("fieldCnName");
                holder.left3.setText(!left3Title.equals("null") ? left3Title : "");
                holder.left3.setVisibility(View.VISIBLE);

                String right3Title = item.get(3).get("fieldCnName2");
                holder.right3.setText(!right3Title.equals("null") ? right3Title : "");
                holder.right3.setVisibility(View.VISIBLE);
//左4
                String left4Title = item.get(4).get("fieldCnName");
                holder.left4.setText(!left4Title.equals("null") ? left4Title : "");
                holder.left4.setVisibility(View.VISIBLE);

                String right4Title = item.get(4).get("fieldCnName2");
                holder.right4.setText(!right4Title.equals("null") ? right4Title : "");
                holder.right4.setVisibility(View.VISIBLE);


                //判断显示按钮
                //首先过滤不能显示的按钮，将不显示的按钮删除
                if (operaButton != null && operaButton.size() > 0) {
                    Log.e(TAG, "onBindViewHolder: item.get(0)-" + item.get(0).toString());
                    final List<Map<String, Object>> operaButtonNow = new ArrayList<>();//不能在原来的上面改，需要新建，否则后面的会得到错误的集合
                    String itemDataStr = item.get(0).get("allItemData");
                    Log.e(TAG, "onBindViewHolder: item-" + item.toString());
                    Map<String, Object> itemDataMap = JSON.parseObject(itemDataStr,
                            new TypeReference<Map<String, Object>>() {
                            });

                    for (int i = 0; i < operaButton.size(); i++) {
                        Log.e(TAG, "onBindViewHolder: operaButton" + i + operaButton.get(i).toString());
                        String buttonId = String.valueOf(operaButton.get(i).get("buttonId"));
                        String buttonKey = "BTN_SHOW_" + buttonId;
                        String isShow = String.valueOf(itemDataMap.get(buttonKey));
                        Log.e(TAG, "onBindViewHolder: isshow" + isShow + "//" + itemDataMap.toString());
                        if (isShow.equals("1")) {
                            operaButtonNow.add(operaButton.get(i));
                        }
                    }
                    Log.e(TAG, "onBindViewHolder: operaButtonNow " + operaButtonNow.toString());

                    if (operaButtonNow.size() > 0) {
                        holder.list_opera_layout.setVisibility(View.VISIBLE);
                        try {
                            String mainId = item.get(0).get("mainId");
                            final String pageId = item.get(0).get("pageId");
                            final String tableId = item.get(0).get("tableId");
//                            final String dataIds = item.get(0).get("dataIds");
                            String allItemData = item.get(0).get("allItemData");
                            Map<String, Object> allItemDataMap = JSON.parseObject(allItemData,
                                    new TypeReference<Map<String, Object>>() {
                                    });
                            final String dataIds = String.valueOf(allItemDataMap.get("LMF_ID"));

                            //第1个按钮
                            final String buttonName1 = String.valueOf(operaButtonNow.get(0).get("buttonName"));
                            final String buttonType1 = String.valueOf(operaButtonNow.get(0).get("buttonType"));
                            holder.list_opera0_tv.setText(!buttonName1.equals("null") ? buttonName1 : "");
                            holder.ll_list_opera0.setVisibility(View.VISIBLE);

                            operaButtonNow.get(0).put("dataId", mainId);
                            operaButtonNow.get(0).put("tableIdList", tableId);
                            operaButtonNow.get(0).put("pageIdList", pageId);
                            final String operaButtonSetMapStr = JSON.toJSONString(operaButtonNow.get(0));

                            holder.ll_list_opera0.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    Log.e(TAG, "onItemClick: buttonName " + buttonName1 + "//" + buttonType1);
                                    switch (buttonType1) {
                                        case "12"://修改页面
                                        case "18"://关联添加页面
                                            Log.e(TAG, "onItemClick: operaButtonNow.get(0) " + operaButtonNow.get(0));
                                            if (buttonName1.contains(mContext.getString(R.string.confirm_xia_class)) && StuPra.studentProId.equals(StuPra.stuProId)) {
                                                //学员端
                                                Intent mIntentEdit = new Intent(mContext, StarRatingBarActivity.class);
                                                mIntentEdit.putExtra("itemSet", operaButtonSetMapStr);
                                                mContext.startActivity(mIntentEdit);
                                            } else {
                                                Intent mIntentEdit = new Intent(mContext, OperateDataActivity.class);
                                                mIntentEdit.putExtra("itemSet", operaButtonSetMapStr);
                                                mContext.startActivity(mIntentEdit);
                                            }
                                            break;
                                        case "13"://单项删除操作
                                            delMapParams.put(Constant.tableId, tableId);
                                            delMapParams.put(Constant.pageId, String.valueOf(operaButtonNow.get(0).get("startTurnPage")));
                                            delMapParams.put(Constant.delIds, String.valueOf(operaButtonNow.get(0).get("dataId")));
                                            delMapParams.put("buttonType", String.valueOf(operaButtonNow.get(0).get("buttonType")));
                                            toDelete();
                                            break;
                                        case "21"://直接操作
                                            Toast.makeText(mContext, "直接操作", Toast.LENGTH_SHORT).show();
                                            String directSetIds = String.valueOf(operaButtonNow.get(0).get("directSetIds"));
                                            toDOperation(tableId, pageId, dataIds, directSetIds);
                                            break;
                                        default:
                                            break;
                                    }

//                                    if (buttonName1.contains("确认下课") && StuPra.studentProId.equals(StuPra.stuProId)) {
//                                        //学员端
//                                        Intent mIntentEdit = new Intent(mContext, StarRatingBarActivity.class);
//                                        mIntentEdit.putExtra("itemSet", operaButtonSetMapStr);
//                                        Log.e(TAG, "onClick: operaButtonSetMapStr " + operaButtonSetMapStr);
//                                        mContext.startActivity(mIntentEdit);
//                                    } else if (StuPra.studentProId.equals(StuPra.teachProId) && buttonName1.contains("确认下课") && buttonName1.contains("作业")) {
//                                        //教师端
//                                        Intent mIntentEdit = new Intent(mContext, OperateDataTwoActivity.class);
//                                        Map<String, Object> operaButtonSetMap1 = operaButtonSet.get(1);
//                                        operaButtonSetMap1.put("tableIdList", tableId);
//                                        operaButtonSetMap1.put("dataId", mainId);
//                                        operaButtonSetMap1.put("pageIdList", pageId);
//                                        String operaButtonSetMapStr1 = JSON.toJSONString(operaButtonSetMap1);
//
//                                        mIntentEdit.putExtra("itemSet", operaButtonSetMapStr);
//                                        mIntentEdit.putExtra("itemSet1", operaButtonSetMapStr1);
//                                        startActivityForResult(mIntentEdit, FINISH_ATY);
                                    // } else {
//                                        Intent mIntentEdit = new Intent(mContext, OperateDataActivity.class);
//                                        mIntentEdit.putExtra("itemSet", operaButtonSetMapStr);
//                                        Log.e(TAG, "onClick: operaButtonSetMapStr " + operaButtonSetMapStr);
//                                        mContext.startActivity(mIntentEdit);
                                    //  }
                                }
                            });

                            //第2个按钮
                            final String buttonName2 = String.valueOf(operaButtonNow.get(1).get("buttonName"));
                            final String buttonType2 = String.valueOf(operaButtonNow.get(1).get("buttonType"));
                            holder.list_opera1_tv.setText(!buttonName2.equals("null") ? buttonName2 : "");
                            holder.ll_list_opera1.setVisibility(View.VISIBLE);
                            holder.line_view1.setVisibility(View.VISIBLE);
                            Log.e(TAG, "onBindViewHolder: operaButton.get(1) " + operaButtonNow.get(1).toString());
                            operaButtonNow.get(1).put("dataId", mainId);
                            operaButtonNow.get(1).put("tableIdList", tableId);
                            operaButtonNow.get(1).put("pageIdList", pageId);
                            Log.e(TAG, "onBindViewHolder: operaButton.get(1) " + operaButtonNow.get(1).toString());
                            final String operaButtonSetMapStr1 = JSON.toJSONString(operaButtonNow.get(1));


                            holder.ll_list_opera1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    switch (buttonType2) {
                                        case "12"://修改页面
                                        case "18"://关联添加页面
                                            Log.e(TAG, "onItemClick: operaButtonNow.get(1) " + operaButtonNow.get(1));
                                            if (buttonName1.contains(mContext.getString(R.string.confirm_xia_class)) && StuPra.studentProId.equals(StuPra.stuProId)) {
                                                //学员端
                                                Intent mIntentEdit = new Intent(mContext, StarRatingBarActivity.class);
                                                mIntentEdit.putExtra("itemSet", operaButtonSetMapStr);
                                                mContext.startActivity(mIntentEdit);
                                            } else {
                                            Intent mIntentEdit = new Intent(mContext, OperateDataActivity.class);
                                            mIntentEdit.putExtra("itemSet", operaButtonSetMapStr1);
                                            mContext.startActivity(mIntentEdit);}
                                            break;
                                        case "13"://单项删除操作
                                            delMapParams.put(Constant.tableId, tableId);
                                            delMapParams.put(Constant.pageId, String.valueOf(operaButtonNow.get(1).get("startTurnPage")));
                                            delMapParams.put(Constant.delIds, String.valueOf(operaButtonNow.get(1).get("dataId")));
                                            delMapParams.put("buttonType", String.valueOf(operaButtonNow.get(1).get("buttonType")));
                                            toDelete();
                                            break;
                                        case "21"://直接操作
                                            Toast.makeText(mContext, "直接操作", Toast.LENGTH_SHORT).show();
                                            String directSetIds = String.valueOf(operaButtonNow.get(1).get("directSetIds"));
                                            toDOperation(tableId, pageId, dataIds, directSetIds);
                                            break;
                                        default:
                                            break;
                                    }
//                                    if (buttonName1.contains("确认下课") && StuPra.studentProId.equals(StuPra.stuProId)) {
//                                        //学员端
//                                        Intent mIntentEdit = new Intent(mContext, StarRatingBarActivity.class);
//                                        mIntentEdit.putExtra("itemSet", operaButtonSetMapStr);
//                                        Log.e(TAG, "onClick: operaButtonSetMapStr " + operaButtonSetMapStr);
//                                        mContext.startActivity(mIntentEdit);
//                                    } else if (StuPra.studentProId.equals(StuPra.teachProId) && buttonName1.contains("确认下课") && buttonName1.contains("作业")) {
//                                        //教师端
//                                        Intent mIntentEdit = new Intent(mContext, OperateDataTwoActivity.class);
//                                        Map<String, Object> operaButtonSetMap1 = operaButtonSet.get(1);
//                                        operaButtonSetMap1.put("tableIdList", tableId);
//                                        operaButtonSetMap1.put("dataId", mainId);
//                                        operaButtonSetMap1.put("pageIdList", pageId);
//                                        String operaButtonSetMapStr1 = JSON.toJSONString(operaButtonSetMap1);
//
//                                        mIntentEdit.putExtra("itemSet", operaButtonSetMapStr);
//                                        mIntentEdit.putExtra("itemSet1", operaButtonSetMapStr1);
//                                        startActivityForResult(mIntentEdit, FINISH_ATY);
                                    //  } else {
//                                    Intent mIntentEdit = new Intent(mContext, OperateDataActivity.class);
//                                    mIntentEdit.putExtra("itemSet", operaButtonSetMapStr);
//                                    Log.e(TAG, "onClick: operaButtonSetMapStr " + operaButtonSetMapStr);
//                                    mContext.startActivity(mIntentEdit);
                                    //   }
                                }
                            });
                            //第3个按钮
                            final String buttonName3 = String.valueOf(operaButtonNow.get(2).get("buttonName"));
                            final String buttonType3 = String.valueOf(operaButtonNow.get(2).get("buttonType"));
                            holder.list_opera2_tv.setText(!buttonName3.equals("null") ? buttonName3 : "");
                            holder.ll_list_opera2.setVisibility(View.VISIBLE);
                            holder.line_view2.setVisibility(View.VISIBLE);
                            operaButtonNow.get(2).put("dataId", mainId);
                            operaButtonNow.get(2).put("tableIdList", tableId);
                            operaButtonNow.get(2).put("pageIdList", pageId);
                            final String operaButtonSetMapStr2 = JSON.toJSONString(operaButtonNow.get(2));

                            holder.ll_list_opera2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Log.e(TAG, "onItemClick: buttonName " + buttonName3 + "//" + buttonType3);
                                    switch (buttonType3) {
                                        case "12"://修改页面
                                        case "18"://关联添加页面
                                            Log.e(TAG, "onItemClick: operaButtonNow.get(2) " + operaButtonNow.get(2));
                                            if (buttonName1.contains(mContext.getString(R.string.confirm_xia_class)) && StuPra.studentProId.equals(StuPra.stuProId)) {
                                                //学员端
                                                Intent mIntentEdit = new Intent(mContext, StarRatingBarActivity.class);
                                                mIntentEdit.putExtra("itemSet", operaButtonSetMapStr);
                                                mContext.startActivity(mIntentEdit);
                                            } else {
                                            Intent mIntentEdit = new Intent(mContext, OperateDataActivity.class);
                                            mIntentEdit.putExtra("itemSet", operaButtonSetMapStr2);
                                            mContext.startActivity(mIntentEdit);}
                                            break;
                                        case "13"://单项删除操作
                                            delMapParams.put(Constant.tableId, tableId);
                                            delMapParams.put(Constant.pageId, String.valueOf(operaButtonNow.get(2).get("startTurnPage")));
                                            delMapParams.put(Constant.delIds, String.valueOf(operaButtonNow.get(2).get("dataId")));
                                            delMapParams.put("buttonType", String.valueOf(operaButtonNow.get(2).get("buttonType")));
                                            toDelete();
                                            break;
                                        case "21"://直接操作
                                            Toast.makeText(mContext, "直接操作", Toast.LENGTH_SHORT).show();
                                            String directSetIds = String.valueOf(operaButtonNow.get(2).get("directSetIds"));
                                            toDOperation(tableId, pageId, dataIds, directSetIds);
                                            break;
                                        default:
                                            break;
                                    }
//                                    if (buttonName1.contains("确认下课") && StuPra.studentProId.equals(StuPra.stuProId)) {
//                                        //学员端
//                                        Intent mIntentEdit = new Intent(mContext, StarRatingBarActivity.class);
//                                        mIntentEdit.putExtra("itemSet", operaButtonSetMapStr);
//                                        Log.e(TAG, "onClick: operaButtonSetMapStr " + operaButtonSetMapStr);
//                                        mContext.startActivity(mIntentEdit);
//                                    } else if (StuPra.studentProId.equals(StuPra.teachProId) && buttonName1.contains("确认下课") && buttonName1.contains("作业")) {
//                                        //教师端
//                                        Intent mIntentEdit = new Intent(mContext, OperateDataTwoActivity.class);
//                                        Map<String, Object> operaButtonSetMap1 = operaButtonSet.get(1);
//                                        operaButtonSetMap1.put("tableIdList", tableId);
//                                        operaButtonSetMap1.put("dataId", mainId);
//                                        operaButtonSetMap1.put("pageIdList", pageId);
//                                        String operaButtonSetMapStr1 = JSON.toJSONString(operaButtonSetMap1);
//
//                                        mIntentEdit.putExtra("itemSet", operaButtonSetMapStr);
//                                        mIntentEdit.putExtra("itemSet1", operaButtonSetMapStr1);
//                                        startActivityForResult(mIntentEdit, FINISH_ATY);
                                    //  } else {
//                                    Intent mIntentEdit = new Intent(mContext, OperateDataActivity.class);
//                                    mIntentEdit.putExtra("itemSet", operaButtonSetMapStr);
//                                    Log.e(TAG, "onClick: operaButtonSetMapStr " + operaButtonSetMapStr);
//                                    mContext.startActivity(mIntentEdit);
                                    //  }
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

//判断跳转子表格
            try {
                final String titleName = item.get(0).get("fieldCnName2");
                final String mainId = item.get(0).get("mainId");
                Log.e(TAG, "onBindViewHolder: childTab " + childTab.toString());
                Log.e(TAG, "onBindViewHolder: operaButton "+operaButton.size()+" \\ "+operaButton.get(0).toString());

                if (childTab.size() > 0  && StuPra.studentProId.equals(StuPra.stuProId)) {
//                    holder.dash_ll.setVisibility(View.VISIBLE);
                    holder.click_open.setVisibility(View.VISIBLE);

                    if (operaButton!=null&&operaButton.size()>0){
                        holder.dash_ll.setVisibility(View.VISIBLE);
                    }else {
                        holder.dash_ll.setVisibility(View.GONE);
                    }
                    holder.click_open.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setClass(mContext, TabActivity.class);
                            intent.putExtra("mainId", mainId);
                            intent.putExtra("childTab", JSON.toJSONString(childTab));
                            intent.putExtra("titleName", titleName);
                            mContext.startActivity(intent);

                        }
                    });
                } else {
                    //holder.dash_ll.setVisibility(View.GONE);
                    holder.click_open.setVisibility(View.GONE);
                    if (operaButton!=null&&operaButton.size()>0 && StuPra.studentProId.equals(StuPra.stuProId)){
                        holder.dash_ll.setVisibility(View.VISIBLE);
                    }else {
                        holder.dash_ll.setVisibility(View.GONE);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            holder.itemView.setTag(item);
        }
    }


    private void toDOperation(final String tableId, final String pageId, final String dataIds, final String directSetIds) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(R.string.if_direct_operation);
        builder.setTitle(R.string.direct_operation);
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                directOper(tableId, pageId, dataIds, directSetIds);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void directOper(String tableId, String pageId, String dataId, String directSetIds) {
        // localhost:8081/edus_auto_main/update_interfaceDicrectTrigger.do?directSetIds=&dataIds

        final String volleyUrl = Constant.sysUrl + Constant.directDicreation;
        Log.e("TAG", "获取dataUrl " + volleyUrl);
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("tableId", tableId);
        paramMap.put("pageId", pageId);
        paramMap.put("dataIds", dataId);
        paramMap.put("directSetIds", directSetIds);
        paramMap.put("sessionId", Constant.sessionId);

        Log.e(TAG, "删除参数  " + paramMap.toString());
//请求
        OkHttpUtils
                .post()
                .params(paramMap)
                .url(volleyUrl)
                .build()
                .execute(new EdusStringCallback(mContext) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ErrorToast.errorToast(mContext, e);
                        Log.e(TAG, "onError: Call  " + call + "  id  " + id);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG, "onResponse: " + "  id  " + id);
                        Log.e("TAG", "删除返回数据" + response);
                        if ("1".equals(response)) {
                            Toast.makeText(mContext, "操作成功", Toast.LENGTH_SHORT).show();
                            ((ListActivity4) mContext).finish();
                        } else {
                            Toast.makeText(mContext, "操作失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void toDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(R.string.database_will_be_deleted);
        builder.setTitle(R.string.delete);
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                deleteItems();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();


    }

    private void deleteItems() {
        final String volleyUrl = Constant.sysUrl + Constant.requestDelete;
        Log.e("TAG", "获取dataUrl " + volleyUrl);
        delMapParams.put("sessionId", Constant.sessionId);
        Log.e(TAG, "删除参数  " + delMapParams.toString());
        //请求
        OkHttpUtils
                .post()
                .params(delMapParams)
                .url(volleyUrl)
                .build()
                .execute(new EdusStringCallback(mContext) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ErrorToast.errorToast(mContext, e);
                        Log.e(TAG, "onError: Call  " + call + "  id  " + id);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG, "onResponse: " + "  id  " + id);
                        Log.e("TAG", "删除返回数据" + response);
                        String isSuccess = response.substring(0, 1);
                        if (isSuccess.equals("1")) {
                            Intent intent = new Intent();
                            intent.setClass(mContext, ListActivity4.class);
                            mContext.startActivity(intent);
                        } else {
                            Toast.makeText(mContext, response + mContext.getString(R.string.please_check_table_association), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * 获取单项数据
     */

    private List<Map<String, String>> getData(int position) {

        return mDatas.get(position);
    }

    /**
     * 获取全部数据
     */
    public List<List<Map<String, String>>> getDatas() {

        return mDatas;
    }

    /**
     * 清除数据
     */
    public void clearData() {

        mDatas.clear();
        notifyItemRangeRemoved(0, mDatas.size());
    }


    /**
     * 下拉刷新更新数据
     */
    public void addData(List<List<Map<String, String>>> datas, List<Map<String, Object>> childTab) {

        addData(0, datas, childTab);
    }

    /**
     * 上拉加载添加数据的方法
     */
    public void addData(int position, List<List<Map<String, String>>> datas, List<Map<String, Object>> childTab) {
        this.childTab = childTab;
        if (datas != null && datas.size() > 0) {

            mDatas.addAll(datas);
            notifyItemRangeChanged(position, mDatas.size());
        }

    }

    @Override
    public int getItemCount() {

        return mDatas.size() > 0 ? mDatas.size() : 1;

    }

    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(view, JSON.toJSONString(view.getTag()));
        }
    }
}


//创建新的对象对fieldCnName2进行操作，否则会在下个页面造成显示错误
//            for (int i=0;i<getData(position).size();i++) {
//
//                Map<String, String> map1=getData(position).get(i);
//
//                Map<String, String> map = new HashMap<>();
//                for (String s : map1.keySet()) {
//                    map.put(s, map1.get(s));
//                }
//                item.add(map);
//                Log.e(TAG, "onBindViewHolder: map1 "+map1.getClass());
//                Log.e(TAG, "onBindViewHolder: map "+map.getClass());
//            }
//
//            for (int i=0;i<item.size();i++) {
//               String value1= item.get(i).get("fieldCnName");
//                String value2= item.get(i).get("fieldCnName2");
//                if (value1.contains("附件")&&!value1.contains("mongo")) {
//                    String[] valueArr = value2.split(",");
//                    String fileNum;
//                    if (valueArr.length>0) {
//                        fileNum = valueArr.length + "个附件";
//                    }else{
//                        fileNum = "无附件";
//                    }
//
//                    item.get(i).put("fieldCnName2",fileNum);
//                }
//            }

//多出来的两行

////左5
//                String left5Title = item.get(5).get("fieldCnName");
//                holder.left5.setText(!left5Title.equals("null") ? left5Title : "");
//                holder.left5.setVisibility(View.VISIBLE);
//
//                String right5Title = item.get(5).get("fieldCnName2");
//                holder.right5.setText(!right5Title.equals("null") ? right5Title : "");
//                holder.right5.setVisibility(View.VISIBLE);
////左6
//                String left6Title = item.get(6).get("fieldCnName");
//                holder.left6.setText(!left6Title.equals("null") ? left6Title : "");
//                holder.left6.setVisibility(View.VISIBLE);
//
//                String right6Title = item.get(6).get("fieldCnName2");
//                holder.right6.setText(!right6Title.equals("null") ? right6Title : "");
//                holder.right6.setVisibility(View.VISIBLE);


//判断显示按钮

//首先过滤不能显示的按钮，将不显示的按钮删除


//            if (operaButton!=null&&operaButton.size()>0) {
//                List<Map<String, Object>> operaButtonNow=new ArrayList<>();//不能在原来的上面改，需要新建，否则后面的会得到错误的集合
//                String itemDataStr=item.get(0).get("allItemData");
//                Map<String,Object>  itemDataMap = JSON.parseObject(itemDataStr,
//                        new TypeReference<Map<String, Object>>() {
//                        });
//
//                for (int i=0;i<operaButton.size();i++) {
//
//                   String buttonId= String.valueOf(operaButton.get(i).get("buttonId"));
//                   String buttonKey="BTN_SHOW_"+buttonId;
//                    String isShow=String.valueOf(itemDataMap.get(buttonKey));
//
//                    if (isShow.equals("1")) {
//                        operaButtonNow.add(operaButton.get(i));
//                    }
//                }
//                Log.e(TAG, "onBindViewHolder: operaButtonNow "+operaButtonNow.toString());
//
//                if (operaButtonNow.size()>0) {
//                    holder.list_opera_layout.setVisibility(View.VISIBLE);
//                    try {
//                        String mainId = item.get(0).get("mainId");
//
//
//
//                        //第1个按钮
//                        final String buttonName1 = String.valueOf(operaButtonNow.get(0).get("buttonName"));
//                        holder.list_opera0.setText(!buttonName1.equals("null") ? buttonName1 : "");
//                        holder.list_opera0.setVisibility(View.VISIBLE);
//
//                        operaButtonNow.get(0).put("dataId", mainId);
//                        final String operaButtonSetMapStr=JSON.toJSONString(operaButton.get(0));
//                        holder.list_opera0.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//
//                                Log.e(TAG, "onItemClick: buttonName "+buttonName1);
//                                if (buttonName1.contains("确认下课")&& StuPra.studentProId.equals("57159822f07e75084cb8a1fe")) {
//                                    Intent mIntentEdit = new Intent(mContext, StarRatingBarActivity.class);
//                                    mIntentEdit.putExtra("itemSet", operaButtonSetMapStr);
//                                    Log.e(TAG, "onClick: operaButtonSetMapStr "+operaButtonSetMapStr);
//                                    mContext.startActivity(mIntentEdit);
//                                }else{
//                                    Intent mIntentEdit = new Intent(mContext, OperateDataActivity.class);
//                                    mIntentEdit.putExtra("itemSet", operaButtonSetMapStr);
//                                    Log.e(TAG, "onClick: operaButtonSetMapStr "+operaButtonSetMapStr);
//                                    mContext.startActivity(mIntentEdit);
//                                }
//                            }
//                        });
//
//                        //第2个按钮
//                        final String buttonName2 = String.valueOf(operaButtonNow.get(1).get("buttonName"));
//                        holder.list_opera1.setText(!buttonName2.equals("null") ? buttonName2 : "");
//                        holder.list_opera1.setVisibility(View.VISIBLE);
//                        Log.e(TAG, "onBindViewHolder: operaButton.get(1) "+operaButtonNow.get(1).toString());
//                        operaButtonNow.get(1).put("dataId", mainId);
//                        Log.e(TAG, "onBindViewHolder: operaButton.get(1) "+operaButtonNow.get(1).toString());
//                        final String operaButtonSetMapStr1=JSON.toJSONString(operaButtonNow.get(1));
//
//
//                        holder.list_opera1.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                if (buttonName2.contains("确认下课")&& StuPra.studentProId.equals("57159822f07e75084cb8a1fe")) {
//                                    Intent mIntentEdit = new Intent(mContext, StarRatingBarActivity.class);
//                                    mIntentEdit.putExtra("itemSet", operaButtonSetMapStr1);
//                                    mContext.startActivity(mIntentEdit);
//                                }else{
//                                    Intent mIntentEdit = new Intent(mContext, OperateDataActivity.class);
//                                    mIntentEdit.putExtra("itemSet", operaButtonSetMapStr1);
//                                    mContext.startActivity(mIntentEdit);
//                                }
//                            }
//                        });
//                        //第3个按钮
//                        final String buttonName3 = String.valueOf(operaButtonNow.get(2).get("buttonName"));
//                        holder.list_opera2.setText(!buttonName3.equals("null") ? buttonName3 : "");
//                        holder.list_opera2.setVisibility(View.VISIBLE);
//
//                        operaButtonNow.get(2).put("dataId", mainId);
//                        final String operaButtonSetMapStr2=JSON.toJSONString(operaButtonNow.get(2));
//                        holder.list_opera2.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                if (buttonName3.contains("确认下课")&& StuPra.studentProId.equals("57159822f07e75084cb8a1fe")) {
//                                    Intent mIntentEdit = new Intent(mContext, StarRatingBarActivity.class);
//                                    mIntentEdit.putExtra("itemSet", operaButtonSetMapStr2);
//                                    mContext.startActivity(mIntentEdit);
//                                }else{
//                                    Intent mIntentEdit = new Intent(mContext, OperateDataActivity.class);
//                                    mIntentEdit.putExtra("itemSet", operaButtonSetMapStr2);
//                                    mContext.startActivity(mIntentEdit);
//                                }
//                            }
//                        });
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//            }