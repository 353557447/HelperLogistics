package com.shuiwangzhijia.wuliu.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.shuiwangzhijia.wuliu.R;
import com.shuiwangzhijia.wuliu.adapter.ConsumerDetailAdapter;
import com.shuiwangzhijia.wuliu.base.BaseAct;
import com.shuiwangzhijia.wuliu.bean.ConsumerDetailBean;
import com.shuiwangzhijia.wuliu.http.EntityObject;
import com.shuiwangzhijia.wuliu.http.RetrofitUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 消费明细
 * created by wangsuli on 2018/8/20.
 */
public class ConsumerDetailActivity extends BaseAct implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private ConsumerDetailAdapter mConsumerDetailAdapter;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumer_detail);
        ButterKnife.bind(this);
        setTitle("消费明细");
        initRecyclerView();
        getData();
    }

    private void getData() {
        RetrofitUtils.getInstances().create().getConsumerDetailList(page, PageSize).enqueue(new Callback<EntityObject<ArrayList<ConsumerDetailBean>>>() {
            @Override
            public void onResponse(Call<EntityObject<ArrayList<ConsumerDetailBean>>> call, Response<EntityObject<ArrayList<ConsumerDetailBean>>> response) {
                completeSwipeRefresh();
                EntityObject<ArrayList<ConsumerDetailBean>> body = response.body();
                if (body.getCode() == 200) {
                    ArrayList<ConsumerDetailBean> result = body.getResult();
                    if (result == null) {
                        return;
                    }
                    if (page == 0) {
                        mConsumerDetailAdapter.setData(result);
                    } else {
                        if (result.size() < PageSize) {
                            loading = false;
                        } else {
                            loading = true;
                        }
                        mConsumerDetailAdapter.addData(result);
                    }
                }
            }

            @Override
            public void onFailure(Call<EntityObject<ArrayList<ConsumerDetailBean>>> call, Throwable t) {

            }
        });
    }

    private void initRecyclerView() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.color_30adfd));
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(getResources().getDrawable(R.drawable.divider_line));
        mRecyclerView.addItemDecoration(divider);
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
                        getData();
                    }
                }
            }
        });
        mConsumerDetailAdapter = new ConsumerDetailAdapter(this);
        mRecyclerView.setAdapter(mConsumerDetailAdapter);
    }

    @Override
    public void onRefresh() {
        new LoadDataThread().start();
    }

    private void completeSwipeRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    /**
     * 模拟加载数据的线程
     */
    class LoadDataThread extends Thread {
        @Override
        public void run() {
            page = 0;
            getData();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            handler.sendEmptyMessage(0x101);//通过handler发送一个更新数据的标记
        }
    }

    private boolean isLoading = false;
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
}
