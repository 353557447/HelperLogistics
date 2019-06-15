package com.shuiwangzhijia.wuliu.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.adapter.OperationGoodsAdapter;
import com.shuiwangzhijia.wuliu.adapter.OperationHuiShouAdapter;
import com.shuiwangzhijia.wuliu.adapter.OperationTuishuiAdapter;
import com.shuiwangzhijia.wuliu.adapter.OperationZiyingAdapter;
import com.shuiwangzhijia.wuliu.base.BaseAct;
import com.shuiwangzhijia.wuliu.bean.OrderShowBean;
import com.shuiwangzhijia.wuliu.event.HuitongEvent;
import com.shuiwangzhijia.wuliu.event.OperationEvent;
import com.shuiwangzhijia.wuliu.event.PayFinishEvent;
import com.shuiwangzhijia.wuliu.event.RecyclingEvent;
import com.shuiwangzhijia.wuliu.event.TuishuiEvent;
import com.shuiwangzhijia.wuliu.http.EntityObject;
import com.shuiwangzhijia.wuliu.http.RetrofitUtils;
import com.shuiwangzhijia.wuliu.utils.CalculateUtils;
import com.shuiwangzhijia.wuliu.utils.ToastUitl;
import com.socks.library.KLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 操作订单界面  create by xxc
 */
public class OperationOrderActivity extends BaseAct {
    private static final String TAG = "OperationOrderActivity";
    @BindView(R.id.shopName)
    TextView mShopName;
    @BindView(R.id.orderId)
    TextView mOrderId;
    //    @BindView(R.id.orderDate)
//    TextView mOrderDate;
    @BindView(R.id.money)
    TextView mMoney;
    @BindView(R.id.numRecyclerView)
    RecyclerView mNumRecyclerView;
    @BindView(R.id.addBucketBtn)
    TextView mAddBucketBtn;
    @BindView(R.id.huiShouRecyclerView)
    RecyclerView mHuiShouRecyclerView;
    @BindView(R.id.huiShouBucketBtn)
    TextView mHuiShouBucketBtn;
    @BindView(R.id.tuiShuiRecyclerView)
    RecyclerView mTuiShuiRecyclerView;
    @BindView(R.id.tuiShuiBucketBtn)
    TextView mTuiShuiBucketBtn;
    @BindView(R.id.zaTongNumber)
    TextView mZaTongNumber;
    @BindView(R.id.ziYingRecyclerView)
    RecyclerView mZiYingRecyclerView;
    @BindView(R.id.buTieWay)
    TextView mBuTieWay;
    @BindView(R.id.shuiPiaoName)
    TextView mShuiPiaoName;
    @BindView(R.id.shuiPiaoNumber)
    TextView mShuiPiaoNumber;
    @BindView(R.id.shuiPiaoStatus)
    TextView mShuiPiaoStatus;
    @BindView(R.id.zaTongBucketBtn)
    TextView mZaTongBucketBtn;
    @BindView(R.id.unPayBtn)
    TextView mUnPayBtn;
    @BindView(R.id.sureBtn)
    TextView mSureBtn;
    @BindView(R.id.detailBtn)
    TextView mDetailBtn;
    @BindView(R.id.zatong_ll)
    LinearLayout mZatongLl;
    @BindView(R.id.delete_huitong)
    ImageView mDeleteHuitong;
    @BindView(R.id.delete_linear)
    LinearLayout mDeleteLinear;
    @BindView(R.id.huitong_linear)
    LinearLayout mHuitongLinear;
    private String mOrderData;
    private Double total;
    private Double price;
    private Double tuishuiTotal;

    private List<OrderShowBean.GoodsBean> mGoods;
    private List<OrderShowBean.RecyclerBean> mRecycler;
    private List<OrderShowBean.RecyclerBean> mRefund_water;
    private OperationGoodsAdapter mPeisongAdapter;
    private OperationHuiShouAdapter mHuitongAdapter;
    private OperationTuishuiAdapter mTuishuiAdapter;
    private int mPay_style;
    private OrderShowBean mResult;
    private List<OrderShowBean.GoodsBean> mPeisongData;
    private List<OrderShowBean.RecyclerBean> mHuiTongData;
    private List<OrderShowBean.RecyclerBean> mTuiShuiData;
    private double mSub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation_order);
        ButterKnife.bind(this);
        setTitle("操作订单");
        mOrderData = getIntent().getStringExtra("orderData");
        showLoad();
        getList();
    }

    private void getList() {
        //操作订单显示接口
        RetrofitUtils.getInstances().create().orderShowNew(mOrderData).enqueue(new Callback<EntityObject<OrderShowBean>>() {
            @Override
            public void onResponse(Call<EntityObject<OrderShowBean>> call, Response<EntityObject<OrderShowBean>> response) {
                hintLoad();
                EntityObject<OrderShowBean> body = response.body();
                if (body != null) {
                    if (body.getCode() == 200) {
                        mResult = body.getResult();
                        mShopName.setText(mResult.getSname());
                        mOrderId.setText("订单号：" + mResult.getOrder_sn());
                        mMoney.setText("￥" + mResult.getTprice());

                        //是在线支付还是货到付款
                        mPay_style = mResult.getPay_style();
                        if (mPay_style == 0) {
                            mUnPayBtn.setVisibility(View.GONE);
                            mSureBtn.setText("确认送达");
                        } else {
                            mUnPayBtn.setVisibility(View.VISIBLE);
                            mSureBtn.setText("立即收款");
                        }

                        //是否有换桶数据
                        OrderShowBean.ChangeBucketBean change_bucket = mResult.getChange_bucket();
                        if (change_bucket != null) {
                            mDeleteLinear.setVisibility(View.VISIBLE);
                            //杂桶数量
                            mZaTongNumber.setText(change_bucket.getMix_num() + "");
//                            int barrel_way = change_bucket.getBarrel_way();
//                            if (barrel_way == 0) {
//                                mBuTieWay.setText("支付金额");
//                            } else {
//                                mBuTieWay.setText("送水票");
//                            }
                            mShuiPiaoName.setText(change_bucket.getTotal_price() + "");
//                            mShuiPiaoNumber.setText("x" + change_bucket.getS_sum());
                            int pay_status = change_bucket.getPay_status();
                            if (pay_status == 0) {
                                mShuiPiaoStatus.setText("未支付");
                                mZaTongBucketBtn.setVisibility(View.VISIBLE);
                                mDeleteHuitong.setVisibility(View.VISIBLE);
                            } else {
                                mShuiPiaoStatus.setText("已支付");
                                mZaTongBucketBtn.setVisibility(View.GONE);
                                mDeleteHuitong.setVisibility(View.GONE);
                            }
                            List<OrderShowBean.RecyclerBean> goods = mResult.getChange_bucket().getGoods();
                            if (goods != null) {
                                initZiyingRecycle(goods);
                            }
                        } else {
                            mDeleteLinear.setVisibility(View.GONE);
                        }

                        mGoods = mResult.getGoods();
                        mRecycler = mResult.getRecycler();
//                        mRefund_water = mResult.getRefund_water();
                        //这里加判断是防止添加杂桶的时候进入到操作订单页面重新请求数据导致配送、回桶、退水的数量不正确
                        if(mPeisongData == null){
                            initGoodsRecycle(mGoods);//已配送
                        }
                        price = 0.0;
                        for (OrderShowBean.GoodsBean bean : mGoods) {
                            price = CalculateUtils.add(CalculateUtils.mul(Double.valueOf(bean.getPrice()), bean.getSnum()), price);
                        }
                        //水票的价格
                        mSub = CalculateUtils.sub(price, Double.parseDouble(mResult.getTprice()));
                        if (mRecycler != null) {
                            mHuitongLinear.setVisibility(View.VISIBLE);
                        } else {
                            mHuitongLinear.setVisibility(View.GONE);
                        }
                        if (mHuiTongData == null){
                            initRecycler(mRecycler);//回桶自营桶
                        }
                        if (mTuiShuiData == null){
                            initRefundWater(mRefund_water);//退水数量
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<EntityObject<OrderShowBean>> call, Throwable t) {

            }
        });

    }

    private void initZiyingRecycle(List<OrderShowBean.RecyclerBean> goods) {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mZiYingRecyclerView.setLayoutManager(manager);
        OperationZiyingAdapter ziyingAdapter = new OperationZiyingAdapter(this, goods,1);
        mZiYingRecyclerView.setAdapter(ziyingAdapter);
    }

    private void initRefundWater(List<OrderShowBean.RecyclerBean> refund_water) {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mTuiShuiRecyclerView.setLayoutManager(manager);
//        mTuishuiAdapter = new OperationTuishuiAdapter(this, refund_water);
        mTuiShuiRecyclerView.setAdapter(mTuishuiAdapter);
    }

    private void initRecycler(List<OrderShowBean.RecyclerBean> recycler) {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mHuiShouRecyclerView.setLayoutManager(manager);
        mHuitongAdapter = new OperationHuiShouAdapter(this, recycler);
        mHuiShouRecyclerView.setAdapter(mHuitongAdapter);
    }

    private void initGoodsRecycle(List<OrderShowBean.GoodsBean> goods) {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mNumRecyclerView.setLayoutManager(manager);
        mPeisongAdapter = new OperationGoodsAdapter(this, goods);
        mNumRecyclerView.setAdapter(mPeisongAdapter);
    }

    @OnClick({R.id.detailBtn, R.id.addBucketBtn, R.id.huiShouBucketBtn, R.id.tuiShuiBucketBtn, R.id.zaTongBucketBtn, R.id.unPayBtn, R.id.sureBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.detailBtn:
                OrderDetailActivity.startAct(OperationOrderActivity.this, 1, mOrderData);
                break;
            case R.id.addBucketBtn:
                //               if (mPeisongData == null){
                EditHandleActivity.startAct(this, 1, mGoods);
//                }else {
//                    EditHandleActivity.startAct(this, 1, mPeisongData);
//                }
                break;
            case R.id.huiShouBucketBtn:
                if (mHuiTongData == null) {
                    EditHandleActivity.startActa(this, 2, mRecycler);
                } else {
                    EditHandleActivity.startActa(this, 2, mHuiTongData);
                }
                break;
            case R.id.tuiShuiBucketBtn:
                if (mTuiShuiData == null) {
                    EditHandleActivity.startActb(this, 3, mRefund_water, mGoods);
                } else {
                    EditHandleActivity.startActb(this, 3, mTuiShuiData, mGoods);
                }
                break;
            case R.id.zaTongBucketBtn:
                Intent intent = new Intent(OperationOrderActivity.this, OtherBucketHandleActivity.class);
                intent.putExtra("order_sn", mOrderData);
                startActivity(intent);
                break;
            //赊账
            case R.id.unPayBtn:
//                post(2);
                break;
            //确认送达
            case R.id.sureBtn:
                if (mPay_style == 0) {
                    //普通支付（在线支付）
//                    post(1);
                } else {
//                    RetrofitUtils.getInstances().create().sureOrder(CommonUtils.getToken(), 1, mOrderData,
//                            packageGoodsData(mPeisongAdapter.getData()), packageHuitongData(mHuitongAdapter.getData()),
//                            packageTuishuiData(mTuishuiAdapter.getData()),packageGoodsData(mPeisongAdapter.getData())).enqueue(new Callback<EntityObject<OperationSureBean>>() {
//                        @Override
//                        public void onResponse(Call<EntityObject<OperationSureBean>> call, Response<EntityObject<OperationSureBean>> response) {
//                            EntityObject<OperationSureBean> body = response.body();
//                            if (body.getCode() == 200) {
//                                int jumpType = body.getResult().getJump_type();
//                                if (jumpType == 0) {
//                                    //跳完成
//                                    EventBus.getDefault().post(new MainEvent(3));
//                                    finish();
//                                } else if (jumpType == 1) {
//                                    //跳支付
//
//                                    //货到付款
//                                    String money = mMoney.getText().toString().trim();
//                                    String actualMoney = money.substring(1, money.length());
//                                    PreparePayBean bean = new PreparePayBean();
//                                    bean.setSname(mResult.getSname());
//                                    bean.setTamount(actualMoney);
//                                    bean.setOrder_no(mResult.getOrder_sn());
//                                    bean.setCreate_time(mResult.getCreate_time() + "");
////                                  bean.setCreate_time(DateUtils.getFormatDateStr(mResult.getTime() * 1000));
//                                    //赊账中firstBtn 生成的二维码
//                                    bean.setPayFrom(4);
//                                    ReceivePayActivity.startAtc(OperationOrderActivity.this, bean, 4);
//                                } else if (jumpType == 2) {
//                                    //跳赊账
//                                    EventBus.getDefault().post(new MainEvent(2));
//                                    finish();
//                                }
//
//                            } else {
//                                ToastUitl.showToastCustom(body.getMsg());
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<EntityObject<OperationSureBean>> call, Throwable t) {
//
//                        }
//                    });
                }
                break;
        }
    }

    //确认订单
//    private void post(final int status) {
//        KLog.d("\n" + CommonUtils.getToken() + "\n" + status + "\n" + mOrderData + "\n" + packageGoodsData(mPeisongAdapter.getData()) + "\n" + packageHuitongData(mHuitongAdapter.getData())
//                + "\n" + packageTuishuiData(mTuishuiAdapter.getData()));
//        RetrofitUtils.getInstances().create().sureOrder(CommonUtils.getToken(), status, mOrderData,
//                packageGoodsData(mPeisongAdapter.getData()), packageHuitongData(mHuitongAdapter.getData()),
//                packageTuishuiData(mTuishuiAdapter.getData()),packageGoodsData(mPeisongAdapter.getData())).enqueue(new Callback<EntityObject<OperationSureBean>>() {
//            @Override
//            public void onResponse(Call<EntityObject<OperationSureBean>> call, Response<EntityObject<OperationSureBean>> response) {
//                EntityObject<OperationSureBean> body = response.body();
//                if (body.getCode() == 200) {
//                    ToastUitl.showToastCustom("操作成功");
////                    if (status == 1) {
////                        EventBus.getDefault().post(new MainEvent(3));
////                    } else {
//                    int jumpType = body.getResult().getJump_type();
//                    if (jumpType == 0) {
//                        EventBus.getDefault().post(new MainEvent(3));
//                    } else {
//                        EventBus.getDefault().post(new MainEvent(2));
//                    }
////                    }
//                    finish();
//                } else {
//                    ToastUitl.showToastCustom(body.getMsg());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<EntityObject<OperationSureBean>> call, Throwable t) {
//
//            }
//        });
//    }

    private String packageGoodsData(List<OrderShowBean.GoodsBean> data) {
        if (data == null || data.size() == 0) {
            return "";
        }
        JSONArray array = new JSONArray();
        JSONObject item = null;
        for (OrderShowBean.GoodsBean bean : data) {
            item = new JSONObject();
            try {
                item.put("gid", bean.getGid());
                item.put("num", bean.getSnum());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            array.put(item);
        }
        Log.i("retrofit", array.toString());
        return array.toString();
    }

    private String packageHuitongData(List<OrderShowBean.RecyclerBean> data) {
        if (data == null || data.size() == 0) {
            return "";
        }
        JSONArray array = new JSONArray();
        JSONObject item = null;
        for (OrderShowBean.RecyclerBean bean : data) {
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

    private String packageTuishuiData(List<OrderShowBean.RecyclerBean> data) {
        if (data == null || data.size() == 0) {
            return "";
        }
        JSONArray array = new JSONArray();
        JSONObject item = null;
        for (OrderShowBean.RecyclerBean bean : data) {
            item = new JSONObject();
            try {
                item.put("gid", bean.getGid());
                item.put("num", bean.getSnum());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            array.put(item);
        }
        Log.i("retrofit", array.toString());
        return array.toString();
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onMessageEventMainThread(PayFinishEvent event) {
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onMessageEventMainThread(OperationEvent event) {
        mPeisongData = event.getPeisongData();
        mGoods = mPeisongData;
        initGoodsRecycle(mPeisongData);
        total = 0.0;
        for (OrderShowBean.GoodsBean bean : mPeisongData) {
            total = CalculateUtils.add(CalculateUtils.mul(Double.valueOf(bean.getPrice()), bean.getSnum()), total);
        }
        KLog.d(total + "\n" + mSub);
        double sub = CalculateUtils.sub(total, mSub);
        if (sub <= 0) {
            sub = 0.00;
        }
        mResult.setTprice(sub + "");
        mMoney.setText("￥" + sub);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onMessageEventMainThread(HuitongEvent event) {
        mHuiShouRecyclerView.setVisibility(View.VISIBLE);
        mHuiTongData = event.getData();
        initRecycler(mHuiTongData);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onMessageEventMainThread(TuishuiEvent event) {
        mTuiShuiRecyclerView.setVisibility(View.VISIBLE);
        mTuiShuiData = event.getData();
        initRefundWater(mTuiShuiData);
//        tuishuiTotal = 0.0;
//        for(OrderShowBean.RefundWaterBean bean : mTuiShuiData){
//            tuishuiTotal = CalculateUtils.add(CalculateUtils.mul(Double.valueOf(bean.getPrice()),bean.getSnum()),tuishuiTotal);
//        }
//        total = 0.0;
//        List<OrderShowBean.GoodsBean> peisongData = mPeisongAdapter.getData();
//        for(OrderShowBean.GoodsBean bean : peisongData){
//            total = CalculateUtils.add(CalculateUtils.mul(Double.valueOf(bean.getPrice()), bean.getSnum()), total);
//        }
//
//        KLog.d("\n"+tuishuiTotal+"\n"+total);
//        double realMoney = CalculateUtils.sub(total, tuishuiTotal);
//        if (realMoney < 0){
//            return;
//        }
//        mResult.setTprice(realMoney+"");
//        mMoney.setText("￥"+realMoney);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onMessageEventMainThread(RecyclingEvent event) {
        showLoad();
        getList();
    }

    private void hint(String text) {
        ToastUitl.showToastCustom(text);
    }

    @OnClick(R.id.delete_huitong)
    public void onViewClicked() {
        RetrofitUtils.getInstances().create().clearChangeBucket(mOrderData).enqueue(new Callback<EntityObject<String>>() {
            @Override
            public void onResponse(Call<EntityObject<String>> call, Response<EntityObject<String>> response) {
                EntityObject<String> body = response.body();
                if (body.getCode() == 200) {
                    hint("删除成功");
                    mDeleteLinear.setVisibility(View.GONE);
                } else {
                    hint(body.getMsg());
                }
            }

            @Override
            public void onFailure(Call<EntityObject<String>> call, Throwable t) {
            }
        });

    }
}
