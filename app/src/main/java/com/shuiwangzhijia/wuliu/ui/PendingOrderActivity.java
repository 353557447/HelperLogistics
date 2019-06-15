package com.shuiwangzhijia.wuliu.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.adapter.PendingPrderAdapter;
import com.shuiwangzhijia.wuliu.annotation.BaseViewInject;
import com.shuiwangzhijia.wuliu.base.BaseActivity;
import com.shuiwangzhijia.wuliu.bean.SendOrderBean;
import com.shuiwangzhijia.wuliu.event.EditOrderEvent;
import com.shuiwangzhijia.wuliu.event.LoginOutEvent;
import com.shuiwangzhijia.wuliu.event.pendingEvent;
import com.shuiwangzhijia.wuliu.http.EntityObject;
import com.shuiwangzhijia.wuliu.http.RetrofitUtils;
import com.shuiwangzhijia.wuliu.utils.MeasureUtil;
import com.shuiwangzhijia.wuliu.utils.PreferenceUtils;
import com.shuiwangzhijia.wuliu.utils.SpacesItemDecoration;
import com.shuiwangzhijia.wuliu.utils.ToastUitl;
import com.shuiwangzhijia.wuliu.view.BaseBar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 待配送订单页面
 */
@BaseViewInject(contentViewId = R.layout.activity_pending_order,title = "待配送订单")
public class PendingOrderActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, PendingPrderAdapter.OnViewItemClickListener {
    private static final String TAG = "PendingOrderActivity";
    @BindView(R.id.pending_order_rv)
    RecyclerView mPendingOrderRv;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.iv)
    ImageView mIv;
    @BindView(R.id.rlEmpty)
    RelativeLayout mRlEmpty;
    @BindView(R.id.pending_order_total)
    TextView mPendingOrderTotal;
    @BindView(R.id.pending_order_surnbtn)
    TextView mPendingOrderSurnbtn;
    @BindView(R.id.pendding_order_bottom)
    RelativeLayout mPenddingOrderBottom;
    private LinearLayoutManager mLayoutManager;
    private PendingPrderAdapter mPendingPrderAdapter;
    public boolean isLoading;
    private int total = 0;
    private List<SendOrderBean> buyData = new ArrayList<>();
    private String mOrderSn;
    private List<SendOrderBean> mSelectData;
    private boolean isInit;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x101:
                    if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) {
                        completeSwipeRefresh();
                    }

                    break;
            }
        }
    };
    private String mOrderNo;
    private int mFromType;      //1添加订单
    private ArrayList<SendOrderBean> mResult;

    public static void startAct(Context context, int fromType, String orderNo) {
        Intent in = new Intent(context, PendingOrderActivity.class);
        in.putExtra("orderNo", orderNo);
        in.putExtra("fromType", fromType);
        context.startActivity(in);
    }

    public static void startActb(Context context, int fromType, String orderNo, List<SendOrderBean> mdata) {
        Intent in = new Intent(context, PendingOrderActivity.class);
        in.putExtra("orderNo", orderNo);
        in.putExtra("fromType", fromType);
        Bundle bundle = new Bundle();
        bundle.putSerializable("list", (Serializable) mdata);
        in.putExtras(bundle);
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
        mOrderNo = getIntent().getStringExtra("orderNo");
        mFromType = getIntent().getIntExtra("fromType", -1);
        mSelectData = (List<SendOrderBean>) getIntent().getSerializableExtra("list");
        showLoadingDialog();
        if (mFromType == 1) {
            getList();
        } else if (mFromType == 2) {
            getOtherList();
            mSwipeRefreshLayout.setEnabled(false);
        }
        initRecycleView();
        mSwipeRefreshLayout.setEnabled(false);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }

    private void getOtherList() {
        RetrofitUtils.getInstances().create().editOutOrderDetail(mOrderNo).enqueue(new Callback<EntityObject<ArrayList<SendOrderBean>>>() {
            @Override
            public void onResponse(Call<EntityObject<ArrayList<SendOrderBean>>> call, Response<EntityObject<ArrayList<SendOrderBean>>> response) {
                dismissLoadingDialog();
                completeSwipeRefresh();
                EntityObject<ArrayList<SendOrderBean>> body = response.body();
                if (body != null) {
                    if (body.getCode() == 200) {
                        ArrayList<SendOrderBean> result = body.getResult();
                        if (result == null || result.size() == 0) {
                            mRlEmpty.setVisibility(View.VISIBLE);
                            mSwipeRefreshLayout.setVisibility(View.GONE);
                            mPenddingOrderBottom.setVisibility(View.GONE);
                            return;
                        } else {
                            mRlEmpty.setVisibility(View.GONE);
                            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                            mPenddingOrderBottom.setVisibility(View.VISIBLE);
                            mPendingPrderAdapter.setData(result);
                            for (int i = 0; i < result.size(); i++) {
                                if (result.get(i).getFlag() == 1 || result.get(i).getIs_selected() == 1) {
                                    total += result.get(i).getSnum();
                                }
                            }
                            mPendingOrderTotal.setText("合计："+total +  "(箱/桶）");
                           // setTextStyle(mPendingOrderTotal, "共", total + "", "(箱/桶）");
                        }
                    } else {
                        mRlEmpty.setVisibility(View.VISIBLE);
                        mSwipeRefreshLayout.setVisibility(View.GONE);
                        mPenddingOrderBottom.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<EntityObject<ArrayList<SendOrderBean>>> call, Throwable t) {
                mRlEmpty.setVisibility(View.VISIBLE);
                mSwipeRefreshLayout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initRecycleView() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(this.getResources().getColor(R.color.color_30adfd));
        mLayoutManager = new LinearLayoutManager(this);
        mPendingOrderRv.setLayoutManager(mLayoutManager);
        mPendingOrderRv.setHasFixedSize(true);
        mPendingOrderRv.addItemDecoration(new SpacesItemDecoration(this, MeasureUtil.dip2px(this,12)));
        mPendingPrderAdapter = new PendingPrderAdapter(this, mFromType);
        mPendingPrderAdapter.setOnItemClickListener(this);
        mPendingOrderRv.setAdapter(mPendingPrderAdapter);
        mPendingOrderRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItemPosition = mLayoutManager.findLastVisibleItemPosition();
                if (dy > 0 && lastVisibleItemPosition + 1 == mLayoutManager.getItemCount()) {
                    if (isLoading) {
                        isLoading = false;
                        isInit = true;
                        showLoadingDialog();
                        new LoadDataThread().start();
                    }
                }
            }
        });
    }

    private void getList() {
        RetrofitUtils.getInstances().create().getSendOrderList(page, PageSize).enqueue(new Callback<EntityObject<ArrayList<SendOrderBean>>>() {
            @Override
            public void onResponse(Call<EntityObject<ArrayList<SendOrderBean>>> call, Response<EntityObject<ArrayList<SendOrderBean>>> response) {
                dismissLoadingDialog();
                completeSwipeRefresh();
                EntityObject<ArrayList<SendOrderBean>> body = response.body();
                if (body.getCode() == 200) {
                    mResult = body.getResult();
                    if (mResult == null || mResult.size() == 0) {
                        mRlEmpty.setVisibility(View.VISIBLE);
                        mSwipeRefreshLayout.setVisibility(View.GONE);
                        mPenddingOrderBottom.setVisibility(View.GONE);
                        return;
                    } else {
                        initCheckStatus();
                        mRlEmpty.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                        mPenddingOrderBottom.setVisibility(View.VISIBLE);
                        if (page == 0) {
                            mPendingPrderAdapter.setData(mResult);
                        } else {
                            mPendingPrderAdapter.addData(mResult);
                        }
                        if (mResult.size() < PageSize) {
                            isLoading = false;
                        } else {
                            isLoading = true;
                        }
                    }
                } else {
                    if (page == 0) {
                        mRlEmpty.setVisibility(View.VISIBLE);
                        mSwipeRefreshLayout.setVisibility(View.GONE);
                        mPenddingOrderBottom.setVisibility(View.GONE);
                    }
                    if (body.getScode() == -200) {
                        EventBus.getDefault().post(new LoginOutEvent());
                    }
                }
            }

            @Override
            public void onFailure(Call<EntityObject<ArrayList<SendOrderBean>>> call, Throwable t) {
                mRlEmpty.setVisibility(View.VISIBLE);
                mSwipeRefreshLayout.setVisibility(View.GONE);
                mPenddingOrderBottom.setVisibility(View.GONE);
            }
        });
    }

    public void initCheckStatus(){
        if (mSelectData != null) {
            for (SendOrderBean bean : mResult) {
                for (SendOrderBean selectBean : mSelectData) {
                    if (bean.getOrder_sn().equals(selectBean.getOrder_sn())) {
                        bean.setFlag(1);
                    }
                }
            }
            if (!isInit){
                total = 0;
                for (int i = 0; i < mSelectData.size(); i++) {
                    total += mSelectData.get(i).getSnum();
                }
                mPendingOrderTotal.setText("合计："+total +  "(箱/桶）");
               // setTextStyle(mPendingOrderTotal, "共", total + "", "(箱/桶）");
            }
        } else {
            if (!isInit){
                mPendingOrderTotal.setText("合计："+0 +  "(箱/桶）");
               // setTextStyle(mPendingOrderTotal, "共", 0 + "", "(箱/桶）");
            }
        }
    }

    private void completeSwipeRefresh() {
        if (mSwipeRefreshLayout == null)
            return;
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        Log.d(TAG, "onRefresh");
        page = -1;
        showLoadingDialog();
        new LoadDataThread().start();
    }

    @Override
    public void onSelectClick(int position) {
        SendOrderBean item = mPendingPrderAdapter.getItem(position);
        item.setFlag(1);
        total += item.getSnum();
        mPendingPrderAdapter.notifyDataSetChanged();
        mPendingOrderTotal.setText("合计："+total +  "(箱/桶）");
       // setTextStyle(mPendingOrderTotal, "共", total + "", "(箱/桶）");
    }

    @Override
    public void onDeleteClick(int position) {
        SendOrderBean item = mPendingPrderAdapter.getItem(position);
        item.setFlag(0);
        total -= item.getSnum();
        mPendingPrderAdapter.notifyDataSetChanged();
        mPendingOrderTotal.setText("合计："+this.total +  "(箱/桶）");
       // setTextStyle(mPendingOrderTotal, "共", this.total + "", "(箱/桶）");
    }

    @OnClick(R.id.pending_order_surnbtn)
    public void onViewClicked() {
        if (mFromType == 1) {
            finish();
            buyData.clear();
            ArrayList<SendOrderBean> data = mPendingPrderAdapter.getData();
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).getFlag() == 1) {
                    buyData.add(data.get(i));
                }
            }
            EventBus.getDefault().post(new pendingEvent(buyData));
        } else {
            StringBuilder builder = new StringBuilder();
            ArrayList<SendOrderBean> data = mPendingPrderAdapter.getData();
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).getFlag() == 1) {
                    builder.append(data.get(i).getOrder_sn()).append(",");
                }
            }
            if (builder.toString().length() >= 1) {
                mOrderSn = builder.deleteCharAt(builder.length() - 1).toString();
            }
            if (mOrderSn == null) {
                ToastUitl.showToastCustom("请选择订单");
                return;
            }
            String deliver_order = PreferenceUtils.getString("deliver_order");
            RetrofitUtils.getInstances().create().makeOutOrder(mOrderSn, deliver_order + "", "").enqueue(new Callback<EntityObject<String>>() {
                @Override
                public void onResponse(Call<EntityObject<String>> call, Response<EntityObject<String>> response) {
                    EntityObject<String> body = response.body();
                    if (body.getCode() == 700) {
                        ToastUitl.showToastCustom(body.getMsg());
                        return;
                    }
                    if (body.getCode() == 200) {
                        ToastUitl.showToastCustom("编辑订单成功");
                        EventBus.getDefault().post(new EditOrderEvent());
                        finish();
                    } else {
                        ToastUitl.showToastCustom(body.getMsg());
                    }
                }

                @Override
                public void onFailure(Call<EntityObject<String>> call, Throwable t) {

                }
            });
        }
    }

    private void setTextStyle(TextView text, String first, String content, String second) {
        SpannableString spanString = new SpannableString(first + content + second);
        spanString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_ff3939)), first.length(), (content + first).length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);//颜色
        spanString.setSpan(new AbsoluteSizeSpan(24, true), first.length(), (content + first).length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);//字体大小
        text.setText(spanString);
    }

    /**
     * 模拟加载数据的线程
     */
    class LoadDataThread extends Thread {
        @Override
        public void run() {
            page = page + 1;
            getList();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            handler.sendEmptyMessage(0x101);//通过handler发送一个更新数据的标记
        }
    }
}
