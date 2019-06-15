package com.shuiwangzhijia.wuliu.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.adapter.BucketAdapter;
import com.shuiwangzhijia.wuliu.adapter.OrderItemAdapterNew;
import com.shuiwangzhijia.wuliu.adapter.ZiyingAdapter;
import com.shuiwangzhijia.wuliu.base.BaseAct;
import com.shuiwangzhijia.wuliu.bean.AppendOrderBean;
import com.shuiwangzhijia.wuliu.bean.BucketBean;
import com.shuiwangzhijia.wuliu.bean.ChangeBucketBean;
import com.shuiwangzhijia.wuliu.bean.ChangeModeBean;
import com.shuiwangzhijia.wuliu.bean.GoodsBean;
import com.shuiwangzhijia.wuliu.bean.OrderBean;
import com.shuiwangzhijia.wuliu.bean.PayOrderDetailBean;
import com.shuiwangzhijia.wuliu.bean.WaterTicketBean;
import com.shuiwangzhijia.wuliu.http.EntityObject;
import com.shuiwangzhijia.wuliu.http.RetrofitUtils;
import com.shuiwangzhijia.wuliu.utils.DateUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 订单详情页面
 * created by wangsuli on 2018/8/30.
 */
public class OrderDetailActivity extends BaseAct {
    @BindView(R.id.shopName)
    TextView shopName;
    @BindView(R.id.shopAddress)
    TextView shopAddress;
    @BindView(R.id.callBtn)
    TextView callBtn;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.totalMoney)
    TextView totalMoney;
    @BindView(R.id.count)
    TextView count;
    @BindView(R.id.bucketRecyclerView)
    RecyclerView bucketRecyclerView;
    @BindView(R.id.llBucket)
    LinearLayout llBucket;
    @BindView(R.id.orderId)
    TextView mOrderId;
    @BindView(R.id.orderDate)
    TextView mOrderDate;
    @BindView(R.id.tuishui_status)
    TextView mTuishuiStatus;
    @BindView(R.id.tuishui)
    RecyclerView mTuishui;
    @BindView(R.id.lanshui_status)
    TextView mLanshuiStatus;
    @BindView(R.id.lanshui)
    RecyclerView mLanshui;
    @BindView(R.id.zatong_status)
    TextView mZatongStatus;
    @BindView(R.id.zatong_number)
    TextView mZatongNumber;
    @BindView(R.id.potong_number)
    TextView mPotongNumber;
    @BindView(R.id.ziyingtong)
    RecyclerView mZiyingtong;
    @BindView(R.id.way)
    TextView mWay;
    @BindView(R.id.money)
    TextView mMoney;
    @BindView(R.id.status)
    TextView mStatus;
    @BindView(R.id.shuipiao_bottom)
    RecyclerView mShuipiaoBottom;
    @BindView(R.id.root_zatong)
    LinearLayout mRootZatong;
    @BindView(R.id.way_trade)
    TextView mWayTrade;
    private String orderNo;
    private OrderBean orderBean;
    private OrderItemAdapterNew mOrderAdapter;
    private BucketAdapter mBucketAdapter;
    private String phone;
    private int fromType;       //1操作订单页面 和 配送、赊账、已完成页面
    private int mType;

    public static void startAct(Context context, int fromType, String orderNo) {
        Intent in = new Intent(context, OrderDetailActivity.class);
        in.putExtra("orderNo", orderNo);
        in.putExtra("fromType", fromType);
        context.startActivity(in);
    }

    public static void startActb(Context context, int fromType, String orderNo, int type) {
        Intent in = new Intent(context, OrderDetailActivity.class);
        in.putExtra("orderNo", orderNo);
        in.putExtra("fromType", fromType);
        in.putExtra("type", type);
        context.startActivity(in);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);
        setTitle("订单详情");
        orderNo = getIntent().getStringExtra("orderNo");
        fromType = getIntent().getIntExtra("fromType", -1);
        mType = getIntent().getIntExtra("type", -1);
        if (mType == 1) {
            llBucket.setVisibility(View.GONE);
        }
        initGoodsRecyclerView();
        switch (fromType) {
            case 1:
                //订单详情
                getDetailInfo();
                break;
            case 2:
                getPayDetailInfo();
                break;
            case 3:
                //追加订单详情
                appendOrderDetail();
                break;
        }
    }

    private void initLanshuiRecycleView(List<BucketBean> data) {
        mLanshui.setLayoutManager(new LinearLayoutManager(this));
        mLanshui.setHasFixedSize(true);
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(getResources().getDrawable(R.drawable.divider_bg_white));
        mLanshui.addItemDecoration(divider);
        mBucketAdapter = new BucketAdapter(this, 2,data);
        mLanshui.setAdapter(mBucketAdapter);
    }

    private void initTuishuiRecycleView(List<BucketBean> data) {
        mTuishui.setLayoutManager(new LinearLayoutManager(this));
        mTuishui.setHasFixedSize(true);
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(getResources().getDrawable(R.drawable.divider_bg_white));
        mTuishui.addItemDecoration(divider);
        mBucketAdapter = new BucketAdapter(this, 3,data);
        mTuishui.setAdapter(mBucketAdapter);
    }

    private void appendOrderDetail() {
        RetrofitUtils.getInstances().create().appendOrderDetail(orderNo).enqueue(new Callback<EntityObject<AppendOrderBean>>() {
            @Override
            public void onResponse(Call<EntityObject<AppendOrderBean>> call, Response<EntityObject<AppendOrderBean>> response) {
                EntityObject<AppendOrderBean> body = response.body();
                if (body.getCode() == 200) {
                    AppendOrderBean orderBean = body.getResult();
                    if (orderBean == null) {
                        return;
                    }
                    phone = orderBean.getPhone();
                    shopName.setText(orderBean.getSname());
                    shopAddress.setText("水店地址:" + orderBean.getAddress());
                    setNumStyle(totalMoney, "合计:", orderBean.getTotal_price(), "元");
                    setNumStyle(count, "共:", orderBean.getSum() + "", "件");
                    mOrderId.setText("订单号:" + orderBean.getOrder_sns());
                    mOrderDate.setText("下单时间:" + DateUtils.getFormatDateStr(orderBean.getUpdate_time() * 1000L));
                    List<GoodsBean> goods = orderBean.getGoods();
                    mOrderAdapter.setData(goods);
                    llBucket.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<EntityObject<AppendOrderBean>> call, Throwable t) {

            }
        });
    }

    private void getPayDetailInfo() {
        RetrofitUtils.getInstances().create().getPayOrderDetail(orderNo).enqueue(new Callback<EntityObject<PayOrderDetailBean>>() {
            @Override
            public void onResponse(Call<EntityObject<PayOrderDetailBean>> call, Response<EntityObject<PayOrderDetailBean>> response) {
                EntityObject<PayOrderDetailBean> body = response.body();
                if (body.getCode() == 200) {
                    PayOrderDetailBean orderBean = body.getResult();
                    if (orderBean == null) {
                        return;
                    }
                    phone = orderBean.getPhone();
                    shopName.setText(orderBean.getName());
                    shopAddress.setText("水店地址:" + orderBean.getAddress());
                    setNumStyle(totalMoney, "合计:", orderBean.getAmount(), "元");
                    setNumStyle(count, "共:", orderBean.getSum() + "", "件");
                    mOrderId.setText("订单号:" + orderBean.getOrder_no());
                    mOrderDate.setText("下单时间:" + DateUtils.getFormatDateStr(orderBean.getCreate_time() * 1000L));
                    List<GoodsBean> goods = orderBean.getList().get(0).getGoods();
                    mOrderAdapter.setData(goods);
                    llBucket.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<EntityObject<PayOrderDetailBean>> call, Throwable t) {

            }
        });
    }

    private void initGoodsRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(getResources().getDrawable(R.drawable.divider_line));
        recyclerView.addItemDecoration(divider);
        mOrderAdapter = new OrderItemAdapterNew(this, true);
        mOrderAdapter.setDetail(true);
        recyclerView.setAdapter(mOrderAdapter);
    }

    private void initBucketRecyclerView(List<BucketBean> data) {
        bucketRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        bucketRecyclerView.setHasFixedSize(true);
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(getResources().getDrawable(R.drawable.divider_bg_white));
        bucketRecyclerView.addItemDecoration(divider);
        mBucketAdapter = new BucketAdapter(this, 1,data);
        bucketRecyclerView.setAdapter(mBucketAdapter);
    }

    private void getDetailInfo() {
        RetrofitUtils.getInstances().create().getOrderDetail(orderNo).enqueue(new Callback<EntityObject<OrderBean>>() {
            @Override
            public void onResponse(Call<EntityObject<OrderBean>> call, Response<EntityObject<OrderBean>> response) {
                EntityObject<OrderBean> body = response.body();
                if (body.getCode() == 200) {
                    orderBean = body.getResult();
                    if (orderBean == null) {
                        return;
                    }
                    phone = orderBean.getPhone();
                    shopName.setText(orderBean.getSname());
                    shopAddress.setText("水店地址:" + orderBean.getAddress());
                    setNumStyle(totalMoney, "合计:", orderBean.getTotal_price(), "元");
                    setNumStyle(count, "共:", orderBean.getSnum() + "", "件");
                    mOrderId.setText("订单号:" + orderBean.getOrder_sn());
                    mOrderDate.setText("下单时间:" + DateUtils.getFormatDateStr(orderBean.getCreate_time() * 1000L));
                    List<GoodsBean> goods = orderBean.getGoods();
                    mOrderAdapter.setData(goods);
                    List<BucketBean> recycler = orderBean.getRecycler();
                    List<BucketBean> refundwt_water = orderBean.getRefundwt_water();//烂水
                    List<BucketBean> return_water = orderBean.getReturn_water();//退水
                    if (recycler == null) {
                        llBucket.setVisibility(View.GONE);
                        return;
                    }
                    initBucketRecyclerView(recycler);
                    initLanshuiRecycleView(refundwt_water);
                    initTuishuiRecycleView(return_water);


                    //换杂桶情况
                    ChangeBucketBean change_bucket = orderBean.getChange_bucket();
                    if (change_bucket != null) {
                        ChangeBucketBean.BucketBean bucket = change_bucket.getBucket();
                        mZatongNumber.setText(bucket.getMix_num() + "");
                        mPotongNumber.setText(bucket.getPo_num() + "");
                        List<WaterTicketBean> goods1 = change_bucket.getGoods();
//                        List<WaterTicketBean> waterTicket = change_bucket.getWater_ticket();
                        initZiyingRv(goods1);
//                        initShuiPiaoBottom(waterTicket);
                    } else {
                        mRootZatong.setVisibility(View.GONE);
                        mZatongStatus.setVisibility(View.VISIBLE);
                    }

                    //交易方式：
                    ChangeModeBean change_mode = orderBean.getChange_mode();
                    if (change_mode != null) {
                        int change_way = change_mode.getChange_way();
                        int pay_status = change_mode.getPay_status();
                        if (change_way == 0) {
                            mWay.setText("支付金额");
                            mMoney.setText("补金额： " + change_mode.getTotal_price());
                            mShuipiaoBottom.setVisibility(View.GONE);
                        } else {
                            mWay.setText("送水票");
                            mMoney.setText("赠水票");
                            mShuipiaoBottom.setVisibility(View.VISIBLE);
                        }
                        if (pay_status == 0) {
                            mStatus.setText("未交易");
                        } else {
                            mStatus.setText("已交易");
                        }
                    } else {
                        mWayTrade.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<EntityObject<OrderBean>> call, Throwable t) {

            }
        });
    }

    private void initZiyingRv(List<WaterTicketBean> good) {
        mZiyingtong.setLayoutManager(new LinearLayoutManager(this));
        mZiyingtong.setHasFixedSize(true);
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(getResources().getDrawable(R.drawable.divider_bg_white));
        mZiyingtong.addItemDecoration(divider);
        ZiyingAdapter adapter= new ZiyingAdapter(this,good);
        mZiyingtong.setAdapter(adapter);
    }


    private void setNumStyle(TextView text, String first, String content, String second) {
        SpannableString spanString = new SpannableString(first + content + second);
        spanString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_ff3939)), first.length(), (content + first).length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);//颜色
        spanString.setSpan(new AbsoluteSizeSpan(16, true), first.length(), (content + first).length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);//字体大小
        text.setText(spanString);
    }

    @OnClick({R.id.callBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.callBtn:
                if (TextUtils.isEmpty(phone)) {
                    return;
                }
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + phone);
                intent.setData(data);
                startActivity(intent);
                break;
        }
    }


}
