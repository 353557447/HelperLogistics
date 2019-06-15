package com.shuiwangzhijia.wuliu.uiwarehouse;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.adapterwarehouse.OrderItemActualAdapter;
import com.shuiwangzhijia.wuliu.adapterwarehouse.OrderItemAdapter;
import com.shuiwangzhijia.wuliu.adapterwarehouse.OrderItemNeedAdapter;
import com.shuiwangzhijia.wuliu.adapterwarehouse.TuiShuiAdapter;
import com.shuiwangzhijia.wuliu.annotation.BaseViewInject;
import com.shuiwangzhijia.wuliu.base.BaseActivity;
import com.shuiwangzhijia.wuliu.beanwarehouse.OrderBean;
import com.shuiwangzhijia.wuliu.beanwarehouse.OrderDetailBean;
import com.shuiwangzhijia.wuliu.beanwarehouse.OutDetailsBean;
import com.shuiwangzhijia.wuliu.http.EntityObject;
import com.shuiwangzhijia.wuliu.http.RetrofitUtils;
import com.shuiwangzhijia.wuliu.utils.DateUtils;
import com.shuiwangzhijia.wuliu.view.BaseBar;

import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@BaseViewInject(contentViewId = R.layout.activity_warehouse_order_detail,title = "订单详情")
public class OrderDetailActivity extends BaseActivity {
    private static final String TAG = "OrderDetailActivity";
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.orderId)
    TextView orderId;
    @BindView(R.id.orderDate)
    TextView orderDate;
    @BindView(R.id.orderBackDate)
    TextView orderBackDate;
    @BindView(R.id.chargeName)
    TextView chargeName;
    @BindView(R.id.orderTitleTv)
    TextView orderTitleTv;
    @BindView(R.id.orderRecyclerView)       //需提货
            RecyclerView orderRecyclerView;
    //    @BindView(R.id.bucketRecyclerView)
//    RecyclerView bucketRecyclerView;
    @BindView(R.id.llBucket)
    LinearLayout llBucket;
    @BindView(R.id.llInfo)
    LinearLayout llInfo;
    @BindView(R.id.order_detail_need)
    TextView mOrderDetailNeed;
    @BindView(R.id.order_detail_actual)
    TextView mOrderDetailActual;
    @BindView(R.id.order_detail_actual_rv)      //实际提货
            RecyclerView mOrderDetailActualRv;
    @BindView(R.id.order_detail_ziyingtong)
    TextView mOrderDetailZiyingtong;
    @BindView(R.id.ZiyingTongRv)        //回自营桶
            RecyclerView mZiyingTongRv;
    @BindView(R.id.order_detail_tuishui)
    TextView mOrderDetailTuishui;
    @BindView(R.id.order_detail_tuishui_rv) //退水
            RecyclerView mOrderDetailTuishuiRv;
    @BindView(R.id.order_detail_zatong)
    TextView mOrderDetailZatong;
    @BindView(R.id.order_detail_zatong_number)
    TextView mOrderDetailZatongNumber;
    @BindView(R.id.chuhuo)
    TextView mChuhuo;
    @BindView(R.id.order_detail_potong_number)
    TextView mOrderDetailPotongNumber;
    @BindView(R.id.zyRecyclerView)
    RecyclerView mZyRecyclerView;
    private String orderNo;
    private OrderBean orderBean;
    private OrderItemAdapter mOrderAdapter;
    private TuiShuiAdapter mBucketAdapter;
    private String phone;
    private int fromType;
    private List<OrderDetailBean.NeedBean> mNeedGoods;
    private List<OrderDetailBean.ActualBean> mActualGoods;

    public static void startAct(Context context, int fromType, String orderNo) {
        Intent in = new Intent(context, OrderDetailActivity.class);
        in.putExtra("orderNo", orderNo);
        in.putExtra("fromType", fromType);
        context.startActivity(in);
    }

    @Override
    protected void beforeSetContentView() {

    }

    @Override
    protected void initView() {
        mBaseBar.setBarListener(new BaseBar.BarListener() {
            @Override
            public void onLeftClick() {
                closeActivity();
            }

            @Override
            public void onRightClick() {

            }
        });

        orderNo = getIntent().getStringExtra("orderNo");
        fromType = getIntent().getIntExtra("fromType", -1);
        Log.d(TAG, fromType + "");
        switch (fromType) {
            case 0:
                getShippingList();
                break;
            case 1:
                setTitle("订单详情(出货中)");
                llBucket.setVisibility(View.GONE);
                llInfo.setVisibility(View.GONE);
                orderTitleTv.setVisibility(View.VISIBLE);
                orderTitleTv.setText("共提120桶");
                getChuhuoList();
                break;
            case 2:
                setTitle("订单详情(已完成)");
                llBucket.setVisibility(View.VISIBLE);
                mChuhuo.setVisibility(View.VISIBLE);
                orderTitleTv.setVisibility(View.GONE);
                getShippingList();
                break;
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }

    private void getChuhuoList() {
        RetrofitUtils.getInstances().create().warehouseGetOutDetailsInfo(orderNo).enqueue(new Callback<EntityObject<OutDetailsBean>>() {

            @Override
            public void onResponse(Call<EntityObject<OutDetailsBean>> call, Response<EntityObject<OutDetailsBean>> response) {
                EntityObject<OutDetailsBean> body = response.body();
                if (body.getCode() == 200) {
                    OutDetailsBean result = body.getResult();
                    name.setText("提货人：" + result.getUname());
                    orderId.setText("出货单号：" + result.getOut_order());
                    orderTitleTv.setText("共提" + result.getSum() + "桶");
                    int count = result.getNeed().getCount();
                    mOrderDetailNeed.setText("(" + count + ")");
                    int actualCount = result.getActual().getCount();
                    mOrderDetailActual.setText("(" + actualCount + ")");
                    mNeedGoods = result.getNeed().getGoods();
                    mActualGoods = result.getActual().getGoods();
                    initOrderRecyclerView(mNeedGoods);
                    initOrderActualRv(mActualGoods);

                }
            }

            @Override
            public void onFailure(Call<EntityObject<OutDetailsBean>> call, Throwable t) {

            }
        });
    }

    private void getShippingList() {
        RetrofitUtils.getInstances().create().warehouseGetFinishOrderDetail(orderNo).enqueue(new Callback<EntityObject<OrderDetailBean>>() {
            @Override
            public void onResponse(Call<EntityObject<OrderDetailBean>> call, Response<EntityObject<OrderDetailBean>> response) {
                EntityObject<OrderDetailBean> body = response.body();
                if (body.getCode() == 200) {
                    OrderDetailBean result = body.getResult();
                    name.setText("提货人：" + result.getUname());
                    orderId.setText("出货单号：" + result.getOut_order());
                    orderDate.setText("出货时间：" + DateUtils.getFormatDateStr(result.getShipment_time() * 1000L));
                    orderBackDate.setText("回仓时间：" + DateUtils.getFormatDateStr(result.getBack_time() * 1000L));
                    chargeName.setText("仓库负责人：" + result.getSender());
                    mOrderDetailNeed.setText("(" + result.getNeed_count() + ")");
                    mOrderDetailActual.setText("(" + result.getActual_count() + ")");
                    int need_count = result.getNeed_count();
                    int actual_count = result.getActual_count();
                    int back_count = result.getBack_count();
                    int refund_count = result.getRefund_count();
                    int miscellaneous = result.getMiscellaneous();
                    int zybucket_count = result.getZybucket_count();
                    int pobucket = result.getPobucket();
                    mOrderDetailNeed.setText("(" + need_count + ")");
                    mOrderDetailActual.setText("(" + actual_count + ")");
                    mOrderDetailZiyingtong.setText("(" + back_count + ")");
                    mOrderDetailTuishui.setText("(" + refund_count + ")");
                    mOrderDetailZatong.setText("("+zybucket_count+")");
                    mOrderDetailZatongNumber.setText("(x" + miscellaneous+")");
                    mOrderDetailPotongNumber.setText("(x" + pobucket+")");
                    List<OrderDetailBean.NeedBean> need = result.getNeed();
                    List<OrderDetailBean.ActualBean> actual = result.getActual();
                    List<OrderDetailBean.BackBean> backList = result.getBack();
                    List<OrderDetailBean.RefundBean> refundlist = result.getRefund();
                    List<OrderDetailBean.NeedBean> zybucket = result.getZybucket();

//                    List<OutDetailsBean.NeedBean.GoodsBean> need = result.getNeed();
//                    List<OutDetailsBean.ActualBean.GoodsBeanX> actual = result.getActual();
//                    List<OrderDetailBean.BackBean> backList = result.getBackList();
//                    List<OrderDetailBean.RefundBean> refundlist = result.getRefundlist();
                    initOrderRecyclerView(need);
                    initOrderActualRv(actual);
                    initZyRv(zybucket);
                    if (backList != null) {
                        initOrderDetailNeedRv(backList);
                    }
                    if (refundlist != null) {
                        initBucketRecyclerView(refundlist);
                    }

                }
            }

            @Override
            public void onFailure(Call<EntityObject<OrderDetailBean>> call, Throwable t) {

            }
        });
    }

    private void initZyRv(List<OrderDetailBean.NeedBean> zybucket) {
        mZyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mZyRecyclerView.setHasFixedSize(true);
        mOrderAdapter = new OrderItemAdapter(this, zybucket);
        mZyRecyclerView.setAdapter(mOrderAdapter);
    }

    private void initOrderActualRv(List<OrderDetailBean.ActualBean> actual) {
        mOrderDetailActualRv.setLayoutManager(new LinearLayoutManager(this));
        mOrderDetailActualRv.setHasFixedSize(true);
        OrderItemActualAdapter actualAdapter = new OrderItemActualAdapter(this, actual);
        mOrderDetailActualRv.setAdapter(actualAdapter);
    }

    private void initOrderDetailNeedRv(List<OrderDetailBean.BackBean> need) {
        mZiyingTongRv.setLayoutManager(new LinearLayoutManager(this));
        mZiyingTongRv.setHasFixedSize(true);
        OrderItemNeedAdapter needAdapter = new OrderItemNeedAdapter(this, need);
        mZiyingTongRv.setAdapter(needAdapter);
    }


    private void initOrderRecyclerView(List<OrderDetailBean.NeedBean> result) {
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderRecyclerView.setHasFixedSize(true);
        mOrderAdapter = new OrderItemAdapter(this, result);
        orderRecyclerView.setAdapter(mOrderAdapter);
    }

    private void initBucketRecyclerView(List<OrderDetailBean.RefundBean> data) {
        mOrderDetailTuishuiRv.setLayoutManager(new LinearLayoutManager(this));
        mOrderDetailTuishuiRv.setHasFixedSize(true);
        mBucketAdapter = new TuiShuiAdapter(this, data);
        mOrderDetailTuishuiRv.setAdapter(mBucketAdapter);
    }


    private void setNumStyle(TextView text, String first, String content, String second) {
        SpannableString spanString = new SpannableString(first + content + second);
        spanString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_ff3939)), first.length(), (content + first).length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);//颜色
        spanString.setSpan(new AbsoluteSizeSpan(16, true), first.length(), (content + first).length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);//字体大小
        text.setText(spanString);
    }

}
