package com.shuiwangzhijia.wuliu.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.adapter.OrderAdapter;
import com.shuiwangzhijia.wuliu.base.BaseFragment;
import com.shuiwangzhijia.wuliu.bean.OrderBean;
import com.shuiwangzhijia.wuliu.bean.PreparePayBean;
import com.shuiwangzhijia.wuliu.dialog.DriverDialog;
import com.shuiwangzhijia.wuliu.dialog.HintDialog;
import com.shuiwangzhijia.wuliu.event.CashDeliveryEvent;
import com.shuiwangzhijia.wuliu.event.LoginOutEvent;
import com.shuiwangzhijia.wuliu.event.OrderListFlashEvent;
import com.shuiwangzhijia.wuliu.event.PayFinishEvent;
import com.shuiwangzhijia.wuliu.http.EntityObject;
import com.shuiwangzhijia.wuliu.http.RetrofitUtils;
import com.shuiwangzhijia.wuliu.ui.OperationOrderActivityNew;
import com.shuiwangzhijia.wuliu.ui.OrderDetailActivity;
import com.shuiwangzhijia.wuliu.ui.PurchaseActivity;
import com.shuiwangzhijia.wuliu.ui.ReceivePayActivity;
import com.shuiwangzhijia.wuliu.utils.DateUtils;
import com.shuiwangzhijia.wuliu.utils.ToastUitl;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OrderFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, OrderAdapter.OnViewItemClickListener {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rlEmpty)
    RelativeLayout rlEmpty;
    private Unbinder unbinder;
    private OrderAdapter mOrderAdapter;
    private boolean isInit = false;
    //1,2,3代表配送中、赊账中、已完成 三个界面
    private int type;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x101:
                    if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                        completeSwipeRefresh();
                    }

                    break;
            }
        }
    };
    private int currentPage = -1;
    private LinearLayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        type = getArguments().getInt("type");
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_order_base, container, false);
            unbinder = ButterKnife.bind(this, mRootView);
            initRecyclerView();
        } else {
            // 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (parent != null) {
                parent.removeView(mRootView);
            }
            unbinder = ButterKnife.bind(this, mRootView);
        }
        isInit = true;
        return mRootView;
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        KLog.d(getUserVisibleHint()+"   "+isVisibleToUser);
//        if (getUserVisibleHint()) {
//            onRefresh();
//        }
//    }

    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }

    private void getList() {
        RetrofitUtils.getInstances().create().getOrderList(type,currentPage,pageSize).enqueue(new Callback<EntityObject<ArrayList<OrderBean>>>() {
            @Override
            public void onResponse(Call<EntityObject<ArrayList<OrderBean>>> call, Response<EntityObject<ArrayList<OrderBean>>> response) {
                hintLoad();
                completeSwipeRefresh();
                EntityObject<ArrayList<OrderBean>> body = response.body();
                if (body != null){
                    if (body.getCode() == 200) {
                        ArrayList<OrderBean> result = body.getResult();
                        if (result == null || (currentPage == 0 && result.size() == 0)) {
                            rlEmpty.setVisibility(View.VISIBLE);
                            //列表界面隐藏
                            swipeRefreshLayout.setVisibility(View.GONE);
                            return;
                        } else {
                            rlEmpty.setVisibility(View.GONE);
                            swipeRefreshLayout.setVisibility(View.VISIBLE);
                            if (currentPage == 0){
                                mOrderAdapter.setData(result);
                            }else {
                                mOrderAdapter.addData(result);
                            }
                            if (result.size() < pageSize){
                                isLoading = false;
                            }else {
                                isLoading = true;
                            }
                        }
                    }else {
                        if (currentPage == 0){
                            rlEmpty.setVisibility(View.VISIBLE);
                            swipeRefreshLayout.setVisibility(View.GONE);
                        }
                        if (body.getScode() == -200){
                            EventBus.getDefault().post(new LoginOutEvent());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<EntityObject<ArrayList<OrderBean>>> call, Throwable t) {

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onMessageEventMainThread(CashDeliveryEvent event) {
        onRefresh();
    }

    private void initRecyclerView() {
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(getActivity().getResources().getColor(R.color.color_30adfd));
        layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        DividerItemDecoration divider = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(getActivity().getResources().getDrawable(R.drawable.divider_bg));
        mRecyclerView.addItemDecoration(divider);
        mOrderAdapter = new OrderAdapter(getActivity(), type);
        mOrderAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mOrderAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.d("test", "StateChanged = " + newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d("test", "onScrolled");
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                if (dy > 0 && lastVisibleItemPosition + 1 == layoutManager.getItemCount()) {
                    if (isLoading) {
                        isLoading = false;
                        showLoad();
                        new LoadDataThread().start();
                    }
                }
            }
        });
       //实现SwipeRefreshLayout首次进入自动刷新
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                onRefresh();
            }
        });
    }

    @Override
    public void onRefresh() {
        currentPage = -1;
        if (isInit){
            showLoad();
        }
        new LoadDataThread().start();
    }

    private void completeSwipeRefresh() {
        if (swipeRefreshLayout == null)
            return;
        swipeRefreshLayout.setRefreshing(false);
    }

    //赊账中收款按钮
    @Override
    public void onReceiveMoneyClick(OrderBean data) {
        PreparePayBean bean = new PreparePayBean();
        bean.setSname(data.getSname());
        bean.setTamount(data.getTprice());
        bean.setOrder_no(data.getOrder_sn());
        bean.setCreate_time(DateUtils.getFormatDateStr(data.getTime() * 1000));
        //赊账中firstBtn 生成的二维码
        bean.setPayFrom(4);
        ReceivePayActivity.startAtc(getContext(), bean,1);
    }

    @Override
    public void onTurnDriverClick(final String orderNo) {
        DriverDialog driverDialog = new DriverDialog(getContext(), new DriverDialog.OnConfirmClickListener() {
            @Override
            public void onConfirm(Dialog dialog, int driverId) {
                dialog.dismiss();
                RetrofitUtils.getInstances().create().turnDriver(orderNo, driverId).enqueue(new Callback<EntityObject<Object>>() {
                    @Override
                    public void onResponse(Call<EntityObject<Object>> call, Response<EntityObject<Object>> response) {
                        EntityObject<Object> body = response.body();
                        if (body.getCode() == 200) {
                            hint("转单成功!");
                            onRefresh();
                        } else if (body.getCode() == -700){
                            hint("已出货，无法转单");
                        }else {
                            hint("转单失败!");
                        }
                    }

                    @Override
                    public void onFailure(Call<EntityObject<Object>> call, Throwable t) {

                    }
                });
            }

        });

        driverDialog.show();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isInit = false;
    }

    @Override
    public void onCallClick(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phone);
        intent.setData(data);
        startActivity(intent);
    }

    @Override
    public void onDetailClick(int position) {
        OrderDetailActivity.startActb(getContext(),1, mOrderAdapter.getItem(position).getOrder_sn(),type);
    }

    @Override
    public void onHandleOrderClick(int position) {
//        wsl 操作订单页面
//        Intent intent = new Intent(getContext(), HandleOrderActivity.class);
//        intent.putExtra("orderData", mOrderAdapter.getItem(position));
        Intent intent = new Intent(getContext(),OperationOrderActivityNew.class);
        intent.putExtra("orderData",mOrderAdapter.getItem(position).getOrder_sn());
        startActivity(intent);
    }

    //已完成 追加订单按钮
    @Override
    public void onRePayOrderClick(final int position) {
        HintDialog hintDialog = new HintDialog(getContext(), "确定追加订单？", new HintDialog.OnConfirmClickListener() {
            @Override
            public void onConfirm(Dialog dialog) {
                dialog.dismiss();
                OrderBean item = mOrderAdapter.getItem(position);
                PurchaseActivity.startAtc(getContext(), item.getId(), 3, item);
            }
        });
        hintDialog.show();
    }


    /**
     * 模拟加载数据的线程
     */
    class LoadDataThread extends Thread {
        @Override
        public void run() {
            currentPage = currentPage + 1;
            getList();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            handler.sendEmptyMessage(0x101);//通过handler发送一个更新数据的标记
        }
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onMessageEventMainThread(OrderListFlashEvent event) {
        if (type == event.type) {
            type = event.type;
            swipeRefreshLayout.setRefreshing(true);
            onRefresh();
        }
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onMessageEventMainThread(PayFinishEvent event) {
        onRefresh();
    }

    private void hint(String text) {
        ToastUitl.showToastCustom(text);
    }
}
