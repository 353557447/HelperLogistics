package com.shuiwangzhijia.wuliu.fragmentwarehouse;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.socks.library.KLog;
import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.adapterwarehouse.OrderAdapter;
import com.shuiwangzhijia.wuliu.adapterwarehouse.OrderRealAdapter;
import com.shuiwangzhijia.wuliu.base.BaseFragment;
import com.shuiwangzhijia.wuliu.beanwarehouse.OrderDetailBean;
import com.shuiwangzhijia.wuliu.beanwarehouse.WarehouseOutOrderBean;
import com.shuiwangzhijia.wuliu.event.LoginOutEvent;
import com.shuiwangzhijia.wuliu.event.MainEvent;
import com.shuiwangzhijia.wuliu.eventwarehouse.OrderListFlashEvent;
import com.shuiwangzhijia.wuliu.eventwarehouse.PayFinishEvent;
import com.shuiwangzhijia.wuliu.http.EntityObject;
import com.shuiwangzhijia.wuliu.http.RetrofitUtils;
import com.shuiwangzhijia.wuliu.uiwarehouse.HuiChangConfirmActivity;
import com.shuiwangzhijia.wuliu.uiwarehouse.OrderDetailActivity;
import com.shuiwangzhijia.wuliu.utils.CommonUtils;
import com.shuiwangzhijia.wuliu.utils.SpacesItemDecoration;
import com.shuiwangzhijia.wuliu.utils.ToastUitl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 订单管理基础类
 * created by wangsuli on 2018/8/20.
 */
public class OrderFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, OrderAdapter.OnViewItemClickListener {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rlEmpty)
    RelativeLayout rlEmpty;
    private Unbinder unbinder;
    private OrderAdapter mOrderAdapter;
    private int type;
    private boolean isInit = false;
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
    private String mOut_order;
    private OrderRealAdapter adapter;
    private ArrayList<WarehouseOutOrderBean> mResult;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        type = getArguments().getInt("type");
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_warehouse_order_base, container, false);
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
//        if (getUserVisibleHint()){
//            onRefresh();
//        }
//    }


    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }

    public void getList() {
        RetrofitUtils.getInstances().create().warehouseGetOutOrderList(type, page, pageSize,0).enqueue(new Callback<EntityObject<ArrayList<WarehouseOutOrderBean>>>() {
            @Override
            public void onResponse(Call<EntityObject<ArrayList<WarehouseOutOrderBean>>> call, Response<EntityObject<ArrayList<WarehouseOutOrderBean>>> response) {
                hintLoad();
                completeSwipeRefresh();
                EntityObject<ArrayList<WarehouseOutOrderBean>> body = response.body();
                if (body.getCode() == 200) {
                    mResult = body.getResult();
                    if (mResult == null || mResult.size() == 0) {
                        rlEmpty.setVisibility(View.VISIBLE);
                        swipeRefreshLayout.setVisibility(View.GONE);
                        return;
                    } else {
                        rlEmpty.setVisibility(View.GONE);
                        swipeRefreshLayout.setVisibility(View.VISIBLE);
                        if (page == 0) {
                            mOrderAdapter.setData(mResult);
                        } else {
                            mOrderAdapter.addData(mResult);
                        }
                        if (mResult.size() < pageSize) {
                            isLoading = false;
                        } else {
                            isLoading = true;
                        }
                    }
                } else {
                    if (page == 0) {
                        rlEmpty.setVisibility(View.VISIBLE);
                        swipeRefreshLayout.setVisibility(View.GONE);
                    }
                    if (body.getScode() == -200) {
                        EventBus.getDefault().post(new LoginOutEvent());
                    }
                }
            }

            @Override
            public void onFailure(Call<EntityObject<ArrayList<WarehouseOutOrderBean>>> call, Throwable t) {
                rlEmpty.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setVisibility(View.GONE);
            }
        });

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


    private void initRecyclerView() {
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(getActivity().getResources().getColor(R.color.color_30adfd));
        layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(mContext, 24));
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
                        page++;
                        getList();
                    }
                }
            }
        });
    }


    @Override
    public void onRefresh() {
        page = -1;
        if (isInit){
            showLoad();
        }
        new LoadDataThread().start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isInit = false;
    }

    private void completeSwipeRefresh() {
        if (swipeRefreshLayout == null)
            return;
        swipeRefreshLayout.setRefreshing(false);
    }


    @Override
    public void onCallClick(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phone);
        intent.setData(data);
        startActivity(intent);
    }


    //出货中 已完成  查看详情或者条目的的点击
    @Override
    public void onDetailClick(int position) {
        if (type != 0){
            OrderDetailActivity.startAct(getContext(), type,mOrderAdapter.getItem(position).getOut_order());
        }

//        Intent intent = new Intent(getContext(), HuiChangConfirmActivity.class);
//        intent.putExtra("orderData", mOrderAdapter.getItem(position));
//        startActivity(intent);
    }

    //确认出货
    @Override
    public void onHandleOrderClick(int position, OrderRealAdapter adapter, int order) {
//        Intent intent = new Intent(getContext(), HandleOrderActivityForSure.class);
//        intent.putExtra("orderData", mOrderAdapter.getItem(position));
//        startActivity(intent);
        WarehouseOutOrderBean item = mOrderAdapter.getItem(position);
        this.adapter = adapter;
        mOut_order = item.getOut_order();
        post(position,order);
    }

    //回仓操作
    @Override
    public void onHuichangOprea(int position) {
        Intent intent = new Intent(getContext(), HuiChangConfirmActivity.class);
        intent.putExtra("orderData", mOrderAdapter.getItem(position));
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onMessageEventMainThread(OrderListFlashEvent event) {
        if (type == event.type) {
            type = event.type;
            swipeRefreshLayout.setRefreshing(true);
            onRefresh();
        }

    }

    private void post(int position, final int order) {
        KLog.d(CommonUtils.getToken()+"\n"+mOut_order+"\n"+packageData(mOrderAdapter.getData().get(position).getGoods()));
        RetrofitUtils.getInstances().create().warehouseConfirmShipment(CommonUtils.getToken(),mOut_order,packageData(mOrderAdapter.getData().get(position).getGoods())).enqueue(new Callback<EntityObject<String>>() {
            @Override
            public void onResponse(Call<EntityObject<String>> call, Response<EntityObject<String>> response) {
                EntityObject<String> body = response.body();
                if (body.getCode() == 200){
                    ToastUitl.showToastCustom("出货成功");
                    if (order == 0){
                        EventBus.getDefault().post(new MainEvent(2));
                        //配送
                    }else if (order == 1){
                        //自提
                        EventBus.getDefault().post(new MainEvent(3));
                    }
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

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onMessageEventMainThread(PayFinishEvent event) {
        onRefresh();
    }

    private void hint(String text) {
        ToastUitl.showToastCustom(text);
    }
}
