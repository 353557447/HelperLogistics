package com.shuiwangzhijia.wuliu.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.adapter.HandleOrderAdapter;
import com.shuiwangzhijia.wuliu.adapter.OperationHuitongAdapter;
import com.shuiwangzhijia.wuliu.adapter.PeisongSureAdapter;
import com.shuiwangzhijia.wuliu.adapter.TuishuiAdapter;
import com.shuiwangzhijia.wuliu.base.BaseAct;
import com.shuiwangzhijia.wuliu.bean.BucketBean;
import com.shuiwangzhijia.wuliu.bean.OrderShowBean;
import com.shuiwangzhijia.wuliu.dialog.AddhuitongDialog;
import com.shuiwangzhijia.wuliu.event.HuitongEvent;
import com.shuiwangzhijia.wuliu.event.OperationEvent;
import com.shuiwangzhijia.wuliu.event.TuishuiEvent;
import com.shuiwangzhijia.wuliu.http.EntityObject;
import com.shuiwangzhijia.wuliu.http.RetrofitUtils;
import com.shuiwangzhijia.wuliu.utils.ToastUitl;
import com.socks.library.KLog;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * created by wangsuli on 2018/9/5.
 * 配送确认操作界面
 */
public class EditHandleActivity extends BaseAct{
    private static final String TAG = "EditHandleActivity";
    @BindView(R.id.addBucketBtn)
    TextView addBucketBtn;
    @BindView(R.id.bucketRecyclerView)
    RecyclerView bucketRecyclerView;
    @BindView(R.id.sureBtn)
    TextView sureBtn;
    @BindView(R.id.titleTv)
    TextView titleTv;
    private HandleOrderAdapter bucketHandleOrderAdapter;
    private ArrayList<BucketBean> recycler;
    private int type;
    private List<OrderShowBean.GoodsBean> mGoodsList;
    private List<OrderShowBean.RecyclerBean> mRecyclerBeanList;
    private List<OrderShowBean.RecyclerBean> mRefundWaterBeanList;
    private PeisongSureAdapter mPeisongAdapter;
    private ArrayList<OrderShowBean.RecyclerBean> recyclerTongDialogData;
    private OperationHuitongAdapter mHuitongAdapter;
    private TuishuiAdapter mTuishuiAdapter;
    private int goodsCount;

    public static void startAct(Context context, int type, List<OrderShowBean.GoodsBean> goods) {
        Intent intent = new Intent(context,EditHandleActivity.class);
        intent.putExtra("type", type);
        //传递从操作订单界面传递过来的数据
        Bundle bundle = new Bundle();
        bundle.putSerializable("list",(Serializable)goods);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void startActa(Context context, int type, List<OrderShowBean.RecyclerBean> recycles) {
        Intent intent = new Intent(context,EditHandleActivity.class);
        intent.putExtra("type", type);
        //传递从操作订单界面传递过来的数据
        Bundle bundle = new Bundle();
        bundle.putSerializable("list",(Serializable)recycles);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void startActb(Context context, int type, List<OrderShowBean.RecyclerBean> refunds,List<OrderShowBean.GoodsBean> goods) {
        Intent intent = new Intent(context,EditHandleActivity.class);
        intent.putExtra("type", type);
        //传递从操作订单界面传递过来的数据
        Bundle bundle = new Bundle();
        bundle.putSerializable("list",(Serializable)refunds);
        bundle.putSerializable("peisongList",(Serializable)goods);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eidt_handle);
        ButterKnife.bind(this);
        type = getIntent().getIntExtra("type", -1);
        switch (type) {
            case 1:
                setTitle("配送确认操作");
                titleTv.setText("已配送数量确认");
                addBucketBtn.setVisibility(View.GONE);
                Intent intent = getIntent();
                mGoodsList = (List<OrderShowBean.GoodsBean>) intent.getSerializableExtra("list");
                initPeisongSureRv(mGoodsList);
                break;
            case 2:
                setTitle("回桶确认操作");
                titleTv.setText("回桶数量确认");
                Intent intent1 = getIntent();
                mRecyclerBeanList = (List<OrderShowBean.RecyclerBean>) intent1.getSerializableExtra("list");
                initHuiRv(mRecyclerBeanList);
                break;
            case 3:
                setTitle("退水确认操作");
                titleTv.setText("退水数量确认");
                //addBucketBtn.setVisibility(View.GONE);
                Intent intent2 = getIntent();
                mRefundWaterBeanList = (List<OrderShowBean.RecyclerBean>) intent2.getSerializableExtra("list");
                mGoodsList = (List<OrderShowBean.GoodsBean>) intent2.getSerializableExtra("peisongList");
                initRefundRv(mRefundWaterBeanList,mGoodsList);
                break;
        }
        getList();
    }

    private void getList() {
        RetrofitUtils.getInstances().create().canHuiTonglist().enqueue(new Callback<EntityObject<ArrayList<OrderShowBean.RecyclerBean>>>() {
            @Override
            public void onResponse(Call<EntityObject<ArrayList<OrderShowBean.RecyclerBean>>> call, Response<EntityObject<ArrayList<OrderShowBean.RecyclerBean>>> response) {
                EntityObject<ArrayList<OrderShowBean.RecyclerBean>> body = response.body();
                if (body.getCode() == 200){
                    ArrayList<OrderShowBean.RecyclerBean> result = body.getResult();
                    if (result == null || result.size() == 0){
                        return;
                    }
                    recyclerTongDialogData = result;
                }
            }

            @Override
            public void onFailure(Call<EntityObject<ArrayList<OrderShowBean.RecyclerBean>>> call, Throwable t) {

            }
        });
    }

    private void initRefundRv(List<OrderShowBean.RecyclerBean> data,List<OrderShowBean.GoodsBean> peisongData) {
        bucketRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        bucketRecyclerView.setHasFixedSize(true);
        mTuishuiAdapter = new TuishuiAdapter(this,data,peisongData);
        mTuishuiAdapter.setOnViewItemClickListener(new TuishuiAdapter.OnViewItemClickListener() {
            @Override
            public void doEditClick(int positon, int count) {
                OrderShowBean.RecyclerBean item = mTuishuiAdapter.getItem(positon);
                if (count < 1){
                    hint("购买数量不能低于最低购买量!");
                    return;
                }
//                if (count > mGoodsList.get(positon).getSnum()){
//                    item.setSnum(mGoodsList.get(positon).getSnum());
//                    hint("退水数量不能超过配送数量");
//                }else {
                    item.setSnum(count);
//                }
                mTuishuiAdapter.notifyDataSetChanged();
            }
        });
        bucketRecyclerView.setAdapter(mTuishuiAdapter);
    }

    private void initHuiRv(List<OrderShowBean.RecyclerBean> recyclerBeanList) {
        bucketRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        bucketRecyclerView.setHasFixedSize(true);
        mHuitongAdapter = new OperationHuitongAdapter(this,recyclerBeanList);
        mHuitongAdapter.setOnViewItemClickListener(new OperationHuitongAdapter.OnViewItemClickListener() {
            @Override
            public void doEditClick(int position, int count) {
                OrderShowBean.RecyclerBean item = mHuitongAdapter.getItem(position);
                if (count < 1){
                    hint("购买数量不能低于最低购买量!");
                    return;
                }
                item.setNum(count);
                mHuitongAdapter.notifyDataSetChanged();
            }
        });
        bucketRecyclerView.setAdapter(mHuitongAdapter);
    }

    private void initPeisongSureRv(List<OrderShowBean.GoodsBean> goodsList) {
        bucketRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        bucketRecyclerView.setHasFixedSize(true);
        mPeisongAdapter = new PeisongSureAdapter(this);
        mPeisongAdapter.setPayStyle(0);
        mPeisongAdapter.setOnItemClickListener(new PeisongSureAdapter.OnViewItemClickListener() {
            @Override
            public void doEditCount(int position, int count) {
                OrderShowBean.GoodsBean item = mPeisongAdapter.getItem(position);
                if (count < 1){
                    hint("购买数量不能低于最低购买量!");
                    return;
                }
                KLog.d(count+"\n"+item.getNum());
                if (count > item.getNum()){
                    item.setSnum(item.getNum());
                    hint("不能大于配送数量");
                }else{
                    item.setSnum(count);
                }
                mPeisongAdapter.notifyDataSetChanged();
            }
        });
        mPeisongAdapter.setData(goodsList);
        bucketRecyclerView.setAdapter(mPeisongAdapter);
    }

    private void hint(String text) {
        ToastUitl.showToastCustom(text);
    }

    @OnClick({R.id.addBucketBtn, R.id.sureBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.addBucketBtn:
                AddhuitongDialog dialog = new AddhuitongDialog(this, recyclerTongDialogData, new AddhuitongDialog.OnConfirmClickListener() {
                    @Override
                    public void onConfirm(Dialog dialog, List<OrderShowBean.RecyclerBean> data) {
                        if (type == 2){
                            mHuitongAdapter.setData(data);
                        }else if (type == 3){
                            mTuishuiAdapter.setData(data);
                        }

                    }
                });
                dialog.show();
                break;
            case R.id.sureBtn:
                switch (type){
                    case 1:
                        goodsCount = 0;
                        List<OrderShowBean.GoodsBean> data = mPeisongAdapter.getData();
                        for (OrderShowBean.GoodsBean bean:data){
                            goodsCount = goodsCount + bean.getSnum();
                        }
                        KLog.d(goodsCount);
                        if (goodsCount == 0){
                            hint("配送数量不能为0");
                            return;
                        }
                        EventBus.getDefault().post(new OperationEvent(data));
                        break;
                    case 2:
                        List<OrderShowBean.RecyclerBean> mHuitongAdapterData = mHuitongAdapter.getData();
                        EventBus.getDefault().post(new HuitongEvent(mHuitongAdapterData));
                        break;
                    case 3:
                        List<OrderShowBean.RecyclerBean> mTuishuiAdapterData = mTuishuiAdapter.getData();
                        EventBus.getDefault().post(new TuishuiEvent(mTuishuiAdapterData));
                        break;
                }
                finish();
                break;
        }
    }
}
