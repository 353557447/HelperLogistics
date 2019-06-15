package com.shuiwangzhijia.wuliu.uiwarehouse;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.adapterwarehouse.HandleOrderAdapter;
import com.shuiwangzhijia.wuliu.adapterwarehouse.NumHandleOrderAdapter;
import com.shuiwangzhijia.wuliu.base.BaseAct;
import com.shuiwangzhijia.wuliu.beanwarehouse.BucketBean;
import com.shuiwangzhijia.wuliu.beanwarehouse.OrderBean;
import com.shuiwangzhijia.wuliu.beanwarehouse.OrderDetailBean;
import com.shuiwangzhijia.wuliu.beanwarehouse.OutDetailsBean;
import com.shuiwangzhijia.wuliu.beanwarehouse.WarehouseOutOrderBean;
import com.shuiwangzhijia.wuliu.http.EntityObject;
import com.shuiwangzhijia.wuliu.http.RetrofitUtils;
import com.shuiwangzhijia.wuliu.utils.CommonUtils;
import com.shuiwangzhijia.wuliu.utils.ToastUitl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 操作订单页面
 * created by wangsuli on 2018/9/5.
 */
public class HandleOrderActivity extends BaseAct {
    private static final String TAG = "HandleOrderActivity";
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.orderId)
    TextView orderId;
    @BindView(R.id.numRecyclerView)
    RecyclerView numRecyclerView;
    @BindView(R.id.realTitle)
    TextView realTitle;
    @BindView(R.id.realRecyclerView)
    RecyclerView realRecyclerView;
    @BindView(R.id.sureBtn)
    TextView sureBtn;
    @BindView(R.id.editSelfBucket)
    TextView editSelfBucket;
    @BindView(R.id.selfBucketRecyclerView)
    RecyclerView selfBucketRecyclerView;
    @BindView(R.id.editReturnWater)
    TextView editReturnWater;
    @BindView(R.id.waterRecyclerView)
    RecyclerView waterRecyclerView;
    @BindView(R.id.remove)
    ImageView remove;
    @BindView(R.id.account)
    TextView account;
    @BindView(R.id.add)
    ImageView add;
    @BindView(R.id.llReturnWareHouse)
    LinearLayout llReturnWareHouse;
    @BindView(R.id.needTiHuo)
    TextView mNeedTiHuo;
    private NumHandleOrderAdapter numHandleOrderAdapter;
    private HandleOrderAdapter realHandleOrderAdapter;
    private HandleOrderAdapter selfHandleOrderAdapter;
    private HandleOrderAdapter waterHandleOrderAdapter;
    private String orderNo;
    private WarehouseOutOrderBean mOrderBean;
    private List<BucketBean> recycler;//显示弹窗数据
    private OrderBean resultOrderBean;
    private int payStyle;
    private int leastNum;
    private int goodCount;
    private ArrayList<BucketBean> recyclerDialogData;
    private int type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouse_handle_order);
        ButterKnife.bind(this);
        type = getIntent().getIntExtra("type", -1);
        Log.d(TAG,"type"+type);
        mOrderBean = (WarehouseOutOrderBean) getIntent().getSerializableExtra("orderData");
        orderNo = mOrderBean.getOut_order();
        setTitle("订单操作");
            if (type == 1) {
            //出货操作
            sureBtn.setText("确认提货");
            llReturnWareHouse.setVisibility(View.GONE);
            getList();
        } else {
            //回仓操作
            sureBtn.setText("回仓确认");
            llReturnWareHouse.setVisibility(View.VISIBLE);
            initSelfRecyclerView();
            initWaterRecyclerView();
        }
        name.setText("提货人：" + mOrderBean.getUname());
        orderId.setText("出货单号： " + orderNo);
        //需提货数量
//        initNumRecyclerView();
        //实际提货
//        initBucketRecyclerView();

    }

    private void getList() {
//        RetrofitUtils.getInstances().create().HandleOrder(orderNo).enqueue(new Callback<EntityObject<OrderBean>>() {
//            @Override
//            public void onResponse(Call<EntityObject<OrderBean>> call, Response<EntityObject<OrderBean>> response) {
//                EntityObject<OrderBean> body = response.body();
//                if (body.getCode() == 200) {
//                    resultOrderBean = body.getResult();
//                    recycler = resultOrderBean.getRecycler();
//                    leastNum = resultOrderBean.getLeast_num();
//                    payStyle = resultOrderBean.getPay_style();
//                    if (payStyle == 1) {
//                        sureBtn.setText("去支付");
//                    } else {
//                        sureBtn.setText("确认送达");
//                    }
//                    numHandleOrderAdapter.setPayStyle(payStyle);
//                    for (BucketBean bean : recycler) {
//                        bean.setCount(bean.getNum());
//                    }
////                    realHandleOrderAdapter.setData(recycler);
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<EntityObject<OrderBean>> call, Throwable t) {
//
//            }
//        });
//        RetrofitUtils.getInstances().create().showDialogBucket().enqueue(new Callback<EntityObject<ArrayList<BucketBean>>>() {
//            @Override
//            public void onResponse(Call<EntityObject<ArrayList<BucketBean>>> call, Response<EntityObject<ArrayList<BucketBean>>> response) {
//                EntityObject<ArrayList<BucketBean>> body = response.body();
//                if (body.getCode() == 200) {
//                    ArrayList<BucketBean> result = body.getResult();
//                    if (result == null || result.size() == 0) {
//                        return;
//                    }
//                    recyclerDialogData = result;
//                }
//            }
//
//            @Override
//            public void onFailure(Call<EntityObject<ArrayList<BucketBean>>> call, Throwable t) {
//            }
//        });

        RetrofitUtils.getInstances().create().warehouseGetOutDetailsInfo("1234567891").enqueue(new Callback<EntityObject<OutDetailsBean>>() {

            private List<OrderDetailBean.ActualBean> mActualGoods;

            @Override
            public void onResponse(Call<EntityObject<OutDetailsBean>> call, Response<EntityObject<OutDetailsBean>> response) {
                EntityObject<OutDetailsBean> body = response.body();
                if (body.getCode() == 200) {
                    OutDetailsBean result = body.getResult();
                    OutDetailsBean.NeedBean need = result.getNeed();
                    int count = need.getCount();
                    mNeedTiHuo.setText("需提货" + count + "桶");
                    int actualNumber = result.getActual().getCount();
                    realTitle.setText("实际出货" + "(" + actualNumber + ")");
                    List<OrderDetailBean.NeedBean> goods = result.getNeed().getGoods();
                    mActualGoods = result.getActual().getGoods();
                    initNumRecyclerView(goods);
                    initBucketRecyclerView(mActualGoods);
                }
            }

            @Override
            public void onFailure(Call<EntityObject<OutDetailsBean>> call, Throwable t) {

            }
        });
    }


    /**
     * 需提货数量
     * @param goods
     */
    private void initNumRecyclerView(List<OrderDetailBean.NeedBean> goods) {
        numRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        numRecyclerView.setHasFixedSize(true);
        numHandleOrderAdapter = new NumHandleOrderAdapter(this,goods);
//        numHandleOrderAdapter.setShopCartInterface(new ShopCartInterface() {
//            @Override
//            public void add(View view, int position) {
//                showTotalPrice();
//            }
//
//            @Override
//            public void remove(View view, int position) {
//                showTotalPrice();
//
//            }
//        });
        numRecyclerView.setAdapter(numHandleOrderAdapter);
    }

    /**
     * 实际提货
     * @param actualGoods
     */
    private void initBucketRecyclerView(List<OrderDetailBean.ActualBean> actualGoods) {
        realRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        realRecyclerView.setHasFixedSize(true);
        realHandleOrderAdapter = new HandleOrderAdapter(this,actualGoods);
        realRecyclerView.setAdapter(realHandleOrderAdapter);
    }

    /**
     * 退水数量
     */
    private void initWaterRecyclerView() {
        waterRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        waterRecyclerView.setHasFixedSize(true);
//        waterHandleOrderAdapter = new HandleOrderAdapter(this, false);
        waterRecyclerView.setAdapter(waterHandleOrderAdapter);

    }

    /**
     * 回桶自营桶
     */
    private void initSelfRecyclerView() {
        selfBucketRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        selfBucketRecyclerView.setHasFixedSize(true);
//        selfHandleOrderAdapter = new HandleOrderAdapter(this, false);
        selfBucketRecyclerView.setAdapter(selfHandleOrderAdapter);

    }

    private void showTotalPrice() {
//        List<GoodsBean> data = numHandleOrderAdapter.getData();
//        Double total = 0.0;
//        goodCount = 0;
//        for (GoodsBean bean : data) {
//            total = CalculateUtils.add(CalculateUtils.mul(Double.valueOf(bean.getPrice()), bean.getCount()), total);
//            goodCount = goodCount + bean.getCount();
//        }
//        numHandleOrderAdapter.setLimitCount(goodCount);
    }


    @OnClick({R.id.editSelfBucket, R.id.editReturnWater, R.id.remove, R.id.add, R.id.sureBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.editSelfBucket:
//                EditHandleActivity.startAct(this, 1);
                break;
            case R.id.editReturnWater:
//                EditHandleActivity.startAct(this, 2);
                break;
            case R.id.remove:
                break;
            case R.id.add:
                break;
            case R.id.sureBtn:
                post();
                break;
        }
    }

    private void post() {
        RetrofitUtils.getInstances().create().warehouseConfirmShipment(CommonUtils.getToken(),"1234567891",packageData(realHandleOrderAdapter.getData())).enqueue(new Callback<EntityObject<String>>() {
            @Override
            public void onResponse(Call<EntityObject<String>> call, Response<EntityObject<String>> response) {
                EntityObject<String> body = response.body();
                if (body.getCode() == 200){
                    ToastUitl.showToastCustom("出货成功");
                    finish();
                }else if (body.getCode() == 400){
                    ToastUitl.showToastCustom("已操作订单");
                    finish();
                }else{
                    ToastUitl.showToastCustom(body.getMsg());
                }
            }

            @Override
            public void onFailure(Call<EntityObject<String>> call, Throwable t) {

            }
        });
    }

    private String packageData(List<OrderDetailBean.ActualBean> data) {
        if (data == null || data.size() == 0) {
            return "";
        }
        JSONArray array = new JSONArray();
        JSONObject item = null;
        for (OrderDetailBean.ActualBean bean : data) {
            item = new JSONObject();
            try {
                item.put("gid", bean.getGid());
                item.put("num", bean.getNum());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            array.put(item);
        }
        Log.i("retrofit", array.toString());
        return array.toString();
    }

}
