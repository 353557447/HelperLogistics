package com.shuiwangzhijia.wuliu.uiwarehouse;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.adapterwarehouse.EditHandleAdapter;
import com.shuiwangzhijia.wuliu.adapterwarehouse.EditHandleTuiShuiAdapter;
import com.shuiwangzhijia.wuliu.adapterwarehouse.HandleOrderAdapter;
import com.shuiwangzhijia.wuliu.base.BaseAct;
import com.shuiwangzhijia.wuliu.beanwarehouse.BucketBean;
import com.shuiwangzhijia.wuliu.beanwarehouse.BucketRecordBean;
import com.shuiwangzhijia.wuliu.beanwarehouse.WarehouseGoodsBean;
import com.shuiwangzhijia.wuliu.beanwarehouse.ShopBean;
import com.shuiwangzhijia.wuliu.dialogwarehouse.TuishuiBucketDialog;
import com.shuiwangzhijia.wuliu.eventwarehouse.HuitongEvent;
import com.shuiwangzhijia.wuliu.eventwarehouse.TuishuiEvent;
import com.shuiwangzhijia.wuliu.eventwarehouse.ZiyingEvent;
import com.shuiwangzhijia.wuliu.http.EntityObject;
import com.shuiwangzhijia.wuliu.http.RetrofitUtils;

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
 */
public class EditHandleActivity extends BaseAct {
    private static final String TAG = "EditHandleActivity";
    @BindView(R.id.addBucketBtn)
    TextView addBucketBtn;
    @BindView(R.id.bucketRecyclerView)
    RecyclerView bucketRecyclerView;
    @BindView(R.id.sureBtn)
    TextView sureBtn;
    @BindView(R.id.titleTv)
    TextView titleTv;
    @BindView(R.id.root)
    LinearLayout mRoot;
    private HandleOrderAdapter bucketHandleOrderAdapter;
    private int count = 1;
    private ShopBean mShopBean;
    private int type;
    private double price = 10;
    private int payType = 0;
    private BucketBean quitBucket;
    private BucketRecordBean mBucketRecordBean;
    private String mOutOrder;
    private ArrayList<WarehouseGoodsBean> huiTongResult;
    private EditHandleAdapter mAdapter;
    private EditHandleTuiShuiAdapter mTuiShuiAdapter;
    private ArrayList<WarehouseGoodsBean> tuiShuiResult;
    private ArrayList<WarehouseGoodsBean> mList;
    private ArrayList<WarehouseGoodsBean> mPassData;
    private ArrayList<WarehouseGoodsBean> mData;
    private String mBname;

    public static void startAct(Context context, int type, String outOrder,ArrayList<WarehouseGoodsBean> data) {
        Intent intent = new Intent(context, EditHandleActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("list", (Serializable) data);
        intent.putExtras(bundle);
        intent.putExtra("type", type);
        intent.putExtra("outorder", outOrder);
        context.startActivity(intent);
    }

    public static void startActb(Context context, int type, ArrayList<WarehouseGoodsBean> zybucket, String outOrder, ArrayList<WarehouseGoodsBean> passData) {
        Intent intent = new Intent(context, EditHandleActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("outorder", outOrder);
        Bundle bundle = new Bundle();
        bundle.putSerializable("list", (Serializable) zybucket);
        bundle.putSerializable("pass_data", (Serializable) passData);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouse_eidt_handle);
        ButterKnife.bind(this);
        type = getIntent().getIntExtra("type", -1);
        mOutOrder = getIntent().getStringExtra("outorder");
        mList = (ArrayList<WarehouseGoodsBean>) getIntent().getSerializableExtra("list");
        mPassData = (ArrayList<WarehouseGoodsBean>) getIntent().getSerializableExtra("pass_data");
        switch (type) {
            case 0:
                setTitle("回桶自营桶操作");
                titleTv.setText("回桶自营桶数量确认");
                if (mList == null){
                    getZiYingList();
                    initBucketRecyclerView(huiTongResult);
                }else {
                    initBucketRecyclerView(mList);
                }
                break;
            case 1:
                setTitle("烂水操作");
                titleTv.setText("退水数量确认");
                if (mList == null){
                    getTuiShuiList();
                    initTuiShuiRec(tuiShuiResult);
                }else {
                    initTuiShuiRec(mList);
                }
                break;
            case 2:
                setTitle("回收杂桶");
                titleTv.setText("回收杂桶数量确认");
                if (mPassData == null){
                    initZiyingRv(mList);
                }else {
                    initZiyingRv(mPassData);
                }
                break;
        }
        getList();
    }

    private void getList() {
        RetrofitUtils.getInstances().create().warehouseAddCouldOutGoodsList(mOutOrder).enqueue(new Callback<EntityObject<ArrayList<WarehouseGoodsBean>>>() {
            @Override
            public void onResponse(Call<EntityObject<ArrayList<WarehouseGoodsBean>>> call, Response<EntityObject<ArrayList<WarehouseGoodsBean>>> response) {
                EntityObject<ArrayList<WarehouseGoodsBean>> body = response.body();
                if (body.getCode() == 200){
                    mData = body.getResult();
                }
            }

            @Override
            public void onFailure(Call<EntityObject<ArrayList<WarehouseGoodsBean>>> call, Throwable t) {

            }
        });
    }

    private void initZiyingRv(ArrayList<WarehouseGoodsBean> data) {
        bucketRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        bucketRecyclerView.setHasFixedSize(true);
        mTuiShuiAdapter = new EditHandleTuiShuiAdapter(this, data);
        mTuiShuiAdapter.setOnViewItemClickListener(new EditHandleTuiShuiAdapter.OnViewItemClickListener() {
            @Override
            public void doEditClick(int position, int count) {
                WarehouseGoodsBean item = mTuiShuiAdapter.getItem(position);
//                if (count <= 0) {
//                    return;
//                }
                item.setNum(count);
                mTuiShuiAdapter.notifyDataSetChanged();
            }
        });
        bucketRecyclerView.setAdapter(mTuiShuiAdapter);
    }

    private void getTuiShuiList() {
        showLoad();
        RetrofitUtils.getInstances().create().warehouseGetBucketListOther(mOutOrder, type).enqueue(new Callback<EntityObject<ArrayList<WarehouseGoodsBean>>>() {
            @Override
            public void onResponse(Call<EntityObject<ArrayList<WarehouseGoodsBean>>> call, Response<EntityObject<ArrayList<WarehouseGoodsBean>>> response) {
                hintLoad();
                EntityObject<ArrayList<WarehouseGoodsBean>> body = response.body();
                if (body.getCode() == 200) {
                    tuiShuiResult = body.getResult();
                    mTuiShuiAdapter.setData(tuiShuiResult);
                }
            }

            @Override
            public void onFailure(Call<EntityObject<ArrayList<WarehouseGoodsBean>>> call, Throwable t) {

            }
        });
    }

    private void initTuiShuiRec(ArrayList<WarehouseGoodsBean> result) {
        bucketRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        bucketRecyclerView.setHasFixedSize(true);
        mTuiShuiAdapter = new EditHandleTuiShuiAdapter(this, result);
        mTuiShuiAdapter.setOnViewItemClickListener(new EditHandleTuiShuiAdapter.OnViewItemClickListener() {
            @Override
            public void doEditClick(int position, int count) {
                WarehouseGoodsBean item = mTuiShuiAdapter.getItem(position);
//                if (count <= 0) {
//                    return;
//                }
                item.setNum(count);
                mTuiShuiAdapter.notifyDataSetChanged();
            }
        });
        bucketRecyclerView.setAdapter(mTuiShuiAdapter);
    }

    private void getZiYingList() {
        showLoad();
        RetrofitUtils.getInstances().create().warehouseGetBucketListOther(mOutOrder,type).enqueue(new Callback<EntityObject<ArrayList<WarehouseGoodsBean>>>() {
            @Override
            public void onResponse(Call<EntityObject<ArrayList<WarehouseGoodsBean>>> call, Response<EntityObject<ArrayList<WarehouseGoodsBean>>> response) {
                hintLoad();
                EntityObject<ArrayList<WarehouseGoodsBean>> body = response.body();
                if (body.getCode() == 200) {
                    huiTongResult = body.getResult();
                    mAdapter.setData(huiTongResult);
                }
            }

            @Override
            public void onFailure(Call<EntityObject<ArrayList<WarehouseGoodsBean>>> call, Throwable t) {

            }
        });

    }

    private void initBucketRecyclerView(ArrayList<WarehouseGoodsBean> data) {
        bucketRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        bucketRecyclerView.setHasFixedSize(true);
        mAdapter = new EditHandleAdapter(this, data);
        mAdapter.setOnViewItemClickListener(new EditHandleAdapter.OnViewItemClickListener() {
            @Override
            public void doEditClick(int position, int count) {
                WarehouseGoodsBean item = mAdapter.getItem(position);
//                if (count <= 0) {
//                    return;
//                }
                item.setNum(count);
                mAdapter.notifyDataSetChanged();
            }
        });
        bucketRecyclerView.setAdapter(mAdapter);
    }

    private int bucketCount = 0;

    @OnClick({R.id.addBucketBtn, R.id.sureBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.addBucketBtn:
                if (type == 0) {
                    TuishuiBucketDialog dialog = new TuishuiBucketDialog(this, mData, new TuishuiBucketDialog.OnConfirmClickListener() {
                        @Override
                        public void onConfirm(Dialog dialog, List<WarehouseGoodsBean> data) {
                            for (int i = 0; i < data.size(); i++) {
                                data.get(i).setBname(data.get(i).getGname());
                                data.get(i).setBid(data.get(i).getGid()+"");
                            }
                            dialog.dismiss();
                            mAdapter.setData((ArrayList<WarehouseGoodsBean>) data);
                        }
                    });
                    dialog.show();
                } else if (type == 1){
                    TuishuiBucketDialog dialog = new TuishuiBucketDialog(this, mData, new TuishuiBucketDialog.OnConfirmClickListener() {
                        @Override
                        public void onConfirm(Dialog dialog, List<WarehouseGoodsBean> data) {
                            dialog.dismiss();
                            mTuiShuiAdapter.setData(data);
                        }
                    });
                    dialog.show();
                }else if (type == 2){
                    TuishuiBucketDialog dialog = new TuishuiBucketDialog(this, mData, new TuishuiBucketDialog.OnConfirmClickListener() {
                        @Override
                        public void onConfirm(Dialog dialog, List<WarehouseGoodsBean> data) {
                            dialog.dismiss();
                            mTuiShuiAdapter.setData(data);
                        }
                    });
                    dialog.show();
                }
                break;
            case R.id.sureBtn:
                switch (type) {
                    case 0:
                        ArrayList<WarehouseGoodsBean> data = mAdapter.getData();
                        EventBus.getDefault().post(new HuitongEvent(data));
                        break;
                    case 1:
                        ArrayList<WarehouseGoodsBean> mTuiShuiAdapterData = mTuiShuiAdapter.getData();
                        EventBus.getDefault().post(new TuishuiEvent(mTuiShuiAdapterData));
                        break;
                    case 2:
                        ArrayList<WarehouseGoodsBean> ziyingData = mTuiShuiAdapter.getData();
                        EventBus.getDefault().post(new ZiyingEvent(ziyingData));
                }
                finish();
                break;
        }
    }


}
